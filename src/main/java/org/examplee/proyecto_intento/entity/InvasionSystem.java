package org.examplee.proyecto_intento.entity;

import java.util.*;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.*;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.*;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.*;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import org.examplee.proyecto_intento.block.GrandBeetleNestBlockEntity;
import org.examplee.proyecto_intento.mixin.SlimeMoveControlAccessor;

/** Server-authoritative encounter; durable state lives in the nest, not this lookup cache. */
public final class InvasionSystem {
    public static final int RADIUS=24;
    private static final Map<GrandBeetleNestBlockEntity,ServerBossBar> ACTIVE=new WeakHashMap<>();
    private InvasionSystem(){}
    public static void initialize() {
        ServerTickEvents.END_SERVER_TICK.register(server -> ACTIVE.entrySet().removeIf(e -> {
            var nest=e.getKey();var w=nest.getWorld();
            if(nest.isRemoved() || w==null || w.getServer()!=server || !w.isChunkLoaded(nest.getPos()) || !active(nest)) {e.getValue().clearPlayers();return true;}
            return false;
        }));
        AttackEntityCallback.EVENT.register((p,w,h,e,hit)->protectedAt(w,e.getBlockPos())?ActionResult.FAIL:ActionResult.PASS);
        UseEntityCallback.EVENT.register((p,w,h,e,hit)->protectedAt(w,e.getBlockPos())?ActionResult.FAIL:ActionResult.PASS);
        UseBlockCallback.EVENT.register((p,w,h,hit)->protectedAt(w,hit.getBlockPos())?ActionResult.FAIL:ActionResult.PASS);
        AttackBlockCallback.EVENT.register((p,w,h,pos,side)->protectedAt(w,pos)?ActionResult.FAIL:ActionResult.PASS);
        UseItemCallback.EVENT.register((p,w,h)->protectedAt(w,p.getBlockPos())?TypedActionResult.fail(p.getStackInHand(h)):TypedActionResult.pass(p.getStackInHand(h)));
        PlayerBlockBreakEvents.BEFORE.register((w,p,pos,state,be)->!protectedAt(w,pos));
    }
    public static boolean active(GrandBeetleNestBlockEntity n){return n.invasion.containsUuid("Id");}
    public static boolean participant(Entity e){return e instanceof InvasionParticipant p && p.combat().event!=null;}
    public static boolean enemies(Entity a,Entity b) {
        return a instanceof InvasionParticipant x && b instanceof InvasionParticipant y && x.combat().event!=null
            && x.combat().event.equals(y.combat().event) && (a instanceof DungBeetleEntity)!=(b instanceof DungBeetleEntity);
    }
    public static boolean protectedAt(World w,BlockPos p) {
        if(w.isClient)return false;
        for(var n:ACTIVE.keySet())if(n.getWorld()==w && !n.isRemoved() && active(n) && p.isWithinDistance(n.getPos(),RADIUS))return true;
        return false;
    }
    public static GrandBeetleNestBlockEntity nest(MobEntity mob) {
        if(!(mob instanceof InvasionParticipant p) || p.combat().nest==null || !mob.getWorld().isChunkLoaded(p.combat().nest))return null;
        return mob.getWorld().getBlockEntity(p.combat().nest) instanceof GrandBeetleNestBlockEntity n?n:null;
    }
    private static List<MobEntity> fighters(ServerWorld w,GrandBeetleNestBlockEntity n) {
        UUID id=n.invasion.getUuid("Id");
        return w.getEntitiesByClass(MobEntity.class,new Box(n.getPos()).expand(RADIUS+8),e->e.isAlive() && e instanceof InvasionParticipant p && id.equals(p.combat().event));
    }
    public static boolean start(ServerWorld w,GrandBeetleNestBlockEntity n) {
        if(active(n) || !n.isStructureIntact() || w.getDifficulty()==net.minecraft.world.Difficulty.PEACEFUL)return false;
        for(var other:ACTIVE.keySet())if(other.getWorld()==w && active(other) && other.getPos().isWithinDistance(n.getPos(),RADIUS*2+2))return false;
        var beetles=w.getEntitiesByClass(DungBeetleEntity.class,new Box(n.getPos()).expand(20),b->b.isAlive() && !b.isBaby() && b.combat().event==null && n.getPos().equals(b.getNestPos()));
        boolean adult=n.getOccupants().stream().anyMatch(r->r.getCompound("Entity").getInt("Age")>=0);
        if(beetles.isEmpty() && !adult)return false;
        UUID id=UUID.randomUUID();n.invasion.putUuid("Id",id);n.invasion.putInt("Ticks",0);n.invasion.putInt("Wave",0);n.invasion.putInt("Phase",0);
        for(var b:beetles){b.combat().event=id;b.combat().nest=n.getPos();b.combat().originalHealth=b.getAttributeBaseValue(EntityAttributes.GENERIC_MAX_HEALTH);b.getNavigation().stop();}
        for(var r:n.getOccupants())if(r.getCompound("Entity").getInt("Age")>=0)prepareResident(n,r);
        ACTIVE.put(n,new ServerBossBar(Text.literal("Invasión fétida – ¡A las armas!"),BossBar.Color.GREEN,BossBar.Style.PROGRESS));
        n.markDirty();return true;
    }
    private static void prepareResident(GrandBeetleNestBlockEntity n,NbtCompound resident) {
        var data=resident.getCompound("Entity");var c=data.getCompound("Invasion");
        if(c.getInt("Role")>0)return;
        c.putUuid("Event",n.invasion.getUuid("Id"));c.putLong("Nest",n.getPos().asLong());
        boolean melee=n.getWorld().random.nextFloat()<.65F;
        c.putInt("Role",melee?1:2);c.putDouble("OriginalMaxHealth",8);data.put("Invasion",c);
        
        double maxHp = melee ? 65 : 40;
        double speed = melee ? 0.16 : 0.25;

        var attributes=data.getList("Attributes",NbtElement.COMPOUND_TYPE);
        for(int i=0;i<attributes.size();i++) {
            if(attributes.getCompound(i).getString("Name").equals("minecraft:generic.max_health")) {
                c.putDouble("OriginalMaxHealth",attributes.getCompound(i).getDouble("Base"));
                attributes.getCompound(i).putDouble("Base",maxHp);
            }
            if(attributes.getCompound(i).getString("Name").equals("minecraft:generic.movement_speed")) {
                attributes.getCompound(i).putDouble("Base",speed);
            }
        }
        data.putFloat("Health",(float)maxHp);resident.putInt("TicksInside",0);
    }
    public static boolean tickNest(ServerWorld w,GrandBeetleNestBlockEntity n) {
        if(!active(n)) {
            if(w.getTime()%20==0)for(var player:w.getPlayers())if(player.hasStatusEffect(ModEffects.FETID_OMEN) && player.squaredDistanceTo(n.getPos().toCenterPos())<=100 && start(w,n)) {player.removeStatusEffect(ModEffects.FETID_OMEN);break;}
            if(!active(n))return false;
        }
        var bar=ACTIVE.computeIfAbsent(n,k->new ServerBossBar(Text.literal("Invasión fétida"),BossBar.Color.GREEN,BossBar.Style.PROGRESS));
        if(w.getTime()%20==0) {
            for(var p:w.getPlayers())if(p.squaredDistanceTo(n.getPos().toCenterPos())<RADIUS*RADIUS)bar.addPlayer(p);else bar.removePlayer(p);
            // Freeze progress when encounter chunks unload; never count unloaded enemies as defeated.
            for(int x=-RADIUS;x<=RADIUS;x+=8)for(int z=-RADIUS;z<=RADIUS;z+=8)if(!w.isChunkLoaded(n.getPos().add(x,0,z)))return true;
            if(!n.isStructureIntact() || w.getDifficulty()==net.minecraft.world.Difficulty.PEACEFUL){finish(w,n,false);return true;}
        }
        int ticks=n.invasion.getInt("Ticks")+1;n.invasion.putInt("Ticks",ticks);
        // Equip and release a bounded number per tick. Blocked exits retain original UUID/NBT.
        int released=0;
        for(var it=n.getOccupants().iterator();it.hasNext();) {
            var r=it.next();if(r.getCompound("Entity").getInt("Age")<0)continue;
            prepareResident(n,r);r.putInt("TicksInside",r.getInt("TicksInside")+1);
            if(released<4 && r.getInt("TicksInside")>=20 && n.release(w,r,false)){it.remove();released++;} // Sped up exit slightly
        }
        if(ticks%20!=0){n.markDirty();return true;}
        var all=fighters(w,n);var defenders=all.stream().filter(e->e instanceof DungBeetleEntity && ((InvasionParticipant)e).combatRole()>0).toList();
        boolean reserves=n.getOccupants().stream().anyMatch(r->r.getCompound("Entity").getInt("Age")>=0);
        if(ticks>1200 && defenders.isEmpty() && !reserves){finish(w,n,false);return true;}
        if(ticks>18000){finish(w,n,false);return true;}
        if(n.invasion.getInt("Phase")==0) {
            int remaining = Math.max(0, 60 - (ticks/20));
            bar.setName(Text.literal("Invasión fétida – Equipando la colonia (" + remaining + "s)"));
            bar.setPercent(Math.clamp((float)ticks/1200F,0,1));
            
            // Advance when time completes
            if(ticks >= 1200 && (!defenders.isEmpty() || !all.isEmpty())) {
                n.invasion.putInt("Phase",1);
                n.invasion.putInt("NextWave",ticks);
                // Force equip remaining roaming beetles
                for(var b : all) {
                    if (b instanceof DungBeetleEntity db && db.combatRole() == 0) {
                        boolean melee = w.random.nextFloat() < .65F;
                        db.combatRole(melee ? 1 : 2);
                        double maxHp = melee ? 65 : 40;
                        db.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(maxHp);
                        db.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(melee ? 0.16 : 0.25);
                        db.setHealth((float)maxHp);
                    }
                }
            }
        } else {
            NbtList pending=n.invasion.getList("Enemies",NbtElement.INT_ARRAY_TYPE);
            boolean alive=false,unloaded=false;
            for(var entry:pending) {UUID id=net.minecraft.nbt.NbtHelper.toUuid(entry);Entity e=w.getEntity(id);
                if(e==null){ // loaded arena + missing UUID is treated as unloaded, never awarded a kill
                    var dead=n.invasion.getList("Defeated",NbtElement.INT_ARRAY_TYPE);if(!dead.contains(entry))unloaded=true;
                } else if(e.isAlive())alive=true;
            }
            int wave=n.invasion.getInt("Wave");
            bar.setName(Text.literal("Invasión fétida – Oleada "+wave+"/5"));
            bar.setPercent(Math.clamp((wave-1+(alive?0:.95F))/5F,0,1));
            
            if(!alive && !unloaded && ticks>=n.invasion.getInt("NextWave")) {
                if(wave>=5){finish(w,n,true);return true;}
                if(spawnWave(w,n,wave+1,defenders.size())) {n.invasion.putInt("Wave",wave+1);n.invasion.putInt("NextWave",ticks+100);}
            }
        }
        n.markDirty();return true;
    }
    private static boolean spawnWave(ServerWorld w,GrandBeetleNestBlockEntity n,int wave,int defenders) {
        int count=Math.clamp((defenders/2)+wave*5,4,45);var prepared=new ArrayList<FetidSlimeEntity>();
        float hpMult = defenders >= 25 ? 1.75f : defenders >= 12 ? 1.35f : 1.0f;
        if (wave >= 4) hpMult *= 1.15f; 
        
        for(int i=0;i<count;i++) {
            var slime=ModEntities.FETID_SLIME.create(w);if(slime==null)return false;
            int role=wave==5 && i<(2+(defenders/10))?3:i%3==2?2:1;
            slime.combat().event=n.invasion.getUuid("Id");slime.combat().nest=n.getPos();slime.combatRole(role);
            slime.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue((role==3?65:role==2?22:15)*hpMult);slime.setHealth(slime.getMaxHealth());
            boolean placed=false;
            for(int trial=0;trial<32;trial++) {
                double angle=(i+trial*.37)*Math.PI*2/count;
                BlockPos p=n.getPos().add((int)(Math.cos(angle)*18),0,(int)(Math.sin(angle)*18));
                for(int dy=4;dy>=-4;dy--) {BlockPos at=p.up(dy);if(!w.isChunkLoaded(at))continue;
                    slime.refreshPositionAndAngles(at.getX()+.5,at.getY()+.05,at.getZ()+.5,0,0);
                    if(!w.getBlockState(at.down()).getCollisionShape(w,at.down()).isEmpty() && w.getFluidState(at).isEmpty() && w.isSpaceEmpty(slime)
                        && prepared.stream().noneMatch(e->e.getBoundingBox().intersects(slime.getBoundingBox()))){placed=true;break;}
                }if(placed)break;
            }if(!placed)return false;prepared.add(slime);
        }
        NbtList ids=new NbtList();
        for(var e:prepared)if(w.spawnEntity(e))ids.add(NbtHelper.fromUuid(e.getUuid()));
        if(ids.isEmpty())return false;n.invasion.put("Enemies",ids);n.invasion.put("Defeated",new NbtList());return true;
    }
    public static void died(MobEntity mob) {
        var n=nest(mob);if(n==null || !active(n) || !(mob instanceof FetidSlimeEntity))return;
        var list=n.invasion.getList("Defeated",NbtElement.INT_ARRAY_TYPE);var id=NbtHelper.fromUuid(mob.getUuid());if(!list.contains(id))list.add(id);n.invasion.put("Defeated",list);n.markDirty();
    }
    public static void finish(ServerWorld w,GrandBeetleNestBlockEntity n,boolean won) {
        if(!active(n))return;
        for(var e:fighters(w,n))if(e instanceof FetidSlimeEntity)e.discard();else clearFighter(e);
        for(var r:n.getOccupants()) {var data=r.getCompound("Entity");var c=data.getCompound("Invasion");
            if(c.containsUuid("Event")){var attrs=data.getList("Attributes",NbtElement.COMPOUND_TYPE);double max=c.getDouble("OriginalMaxHealth");if(max<=0)max=8;
                for(int i=0;i<attrs.size();i++) {
                    if(attrs.getCompound(i).getString("Name").equals("minecraft:generic.max_health")) attrs.getCompound(i).putDouble("Base",max);
                    if(attrs.getCompound(i).getString("Name").equals("minecraft:generic.movement_speed")) attrs.getCompound(i).putDouble("Base",0.22);
                }
                data.putFloat("Health",Math.min(data.getFloat("Health"),(float)max));data.remove("Invasion");}
        }
        for(String key:new HashSet<>(n.invasion.getKeys()))n.invasion.remove(key);
        var bar=ACTIVE.remove(n);if(bar!=null){for(var p:bar.getPlayers())p.sendMessage(Text.literal(won?"¡La colonia ha defendido el nido!":"La invasión ha terminado."),false);bar.clearPlayers();}
        n.markDirty();
    }
    public static void clearFighter(MobEntity mob) {
        if(!(mob instanceof InvasionParticipant p))return;
        if(mob instanceof DungBeetleEntity && p.combatRole()>0) {
            mob.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(Math.max(1,p.combat().originalHealth));
            mob.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(0.22);
            mob.setHealth(Math.min(mob.getHealth(),mob.getMaxHealth()));
        }
        p.combatRole(0);p.combat().clear();mob.setTarget(null);mob.getNavigation().stop();
    }
    
    /** Called instead of vanilla goals while enrolled. Physics and persistence remain vanilla. */
    public static boolean tickFighter(MobEntity mob) {
        if(!(mob instanceof InvasionParticipant p) || p.combat().event==null)return false;
        var c=p.combat();var n=nest(mob);
        if(n==null || !active(n) || !c.event.equals(n.invasion.getUuid("Id"))) {
            if(mob.getWorld().isChunkLoaded(c.nest)){if(mob instanceof FetidSlimeEntity)mob.discard();else clearFighter(mob);}return true;
        }
        mob.setTarget(null);
        if(mob instanceof DungBeetleEntity b && p.combatRole()==0) {
            if(--c.repath<=0){n.navigateToPerimeter(b,1.6);c.repath=20;}
            if(n.atPerimeter(b))n.tryEnter(b);
        } else {
            if(mob.squaredDistanceTo(n.getPos().toCenterPos())>24*24){
                if(--c.repath<=0){mob.getNavigation().startMovingTo(n.getPos().getX()+.5,mob.getY(),n.getPos().getZ()+.5,1.2);c.repath=20;}
            } else {
                var target=mob.getWorld().getEntitiesByClass(MobEntity.class,mob.getBoundingBox().expand(22),e->e.isAlive() && enemies(mob,e) && ((InvasionParticipant)e).combatRole()>0)
                    .stream().min(Comparator.comparingDouble(mob::squaredDistanceTo)).orElse(null);
                    
                if(target!=null) {
                    double d=mob.squaredDistanceTo(target);
                    // Ranged role applies to artillery beetles and maybe artillery slimes (role 2).
                    boolean ranged = p.combatRole()==2; 
                    mob.getLookControl().lookAt(target,30,30);
                    
                    if(--c.repath<=0) {
                        if(ranged) {
                            if(d < 16.0) { // Too close! ~4 blocks 
                                Vec3d dir = mob.getPos().subtract(target.getPos()).normalize();
                                Vec3d away = mob.getPos().add(dir.multiply(5));
                                mob.getNavigation().startMovingTo(away.x, away.y, away.z, 1.4);
                            } else if(d > 81.0) { // Too far! ~9 blocks
                                mob.getNavigation().startMovingTo(target, 1.15);
                            } else {
                                mob.getNavigation().stop();
                            }
                        } else {
                            // Melee approach
                            mob.getNavigation().startMovingTo(target, 1.25);
                        }
                        c.repath = 15;
                    }
                    
                    if(mob.getMoveControl() instanceof SlimeMoveControlAccessor slime){
                        var delta=target.getPos().subtract(mob.getPos());
                        slime.popocraft$look((float)(Math.toDegrees(Math.atan2(delta.z,delta.x))-90),true);
                        slime.popocraft$move(ranged && d < 16.0 ? 0.0f : 1.2f); // Ranged slime hesitates to bounce if too close, but normally jump towards target.
                    }
                    
                    if(--c.cooldown<=0 && mob.canSee(target)) {
                        if(ranged && d<196) { // 14 blocks range for shooting
                            InvasionProjectileEntity.fire(mob,target);
                            c.cooldown = mob instanceof DungBeetleEntity ? 40 : 50; 
                        }
                        else if(d<Math.pow(mob.getWidth()+target.getWidth()+.9,2)) {
                            target.damage(mob.getDamageSources().mobAttack(mob),mob instanceof DungBeetleEntity ? (p.combatRole()==1? 8 : 4) : p.combatRole()==3? 6 : 3);
                            c.cooldown = 20; 
                        }
                    }
                }else mob.getNavigation().stop();
            }
            if(mob instanceof DungBeetleEntity && mob.age%80==0)mob.heal(1);
        }
        mob.getNavigation().tick();mob.getMoveControl().tick();mob.getLookControl().tick();mob.getJumpControl().tick();return true;
    }
}
