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
        ACTIVE.put(n,new ServerBossBar(Text.literal("InvasiÃ³n fÃ©tida â€” Â¡a las armas!"),BossBar.Color.GREEN,BossBar.Style.PROGRESS));
        n.markDirty();return true;
    }
    private static void prepareResident(GrandBeetleNestBlockEntity n,NbtCompound resident) {
        var data=resident.getCompound("Entity");var c=data.getCompound("Invasion");
        if(c.getInt("Role")>0)return;
        c.putUuid("Event",n.invasion.getUuid("Id"));c.putLong("Nest",n.getPos().asLong());
        c.putInt("Role",n.getWorld().random.nextFloat()<.65F?1:2);c.putDouble("OriginalMaxHealth",8);data.put("Invasion",c);
        // Vanilla attribute base is restored by clearFighter; actual equipment happens inside.
        var attributes=data.getList("Attributes",NbtElement.COMPOUND_TYPE);
        for(int i=0;i<attributes.size();i++)if(attributes.getCompound(i).getString("Name").equals("minecraft:generic.max_health")) {
            c.putDouble("OriginalMaxHealth",attributes.getCompound(i).getDouble("Base"));attributes.getCompound(i).putDouble("Base",40);
        }
        data.putFloat("Health",40);resident.putInt("TicksInside",0);
    }
    public static boolean tickNest(ServerWorld w,GrandBeetleNestBlockEntity n) {
        if(!active(n)) {
            if(w.getTime()%20==0)for(var player:w.getPlayers())if(player.hasStatusEffect(ModEffects.FETID_OMEN) && player.squaredDistanceTo(n.getPos().toCenterPos())<=100 && start(w,n)) {player.removeStatusEffect(ModEffects.FETID_OMEN);break;}
            if(!active(n))return false;
        }
        var bar=ACTIVE.computeIfAbsent(n,k->new ServerBossBar(Text.literal("InvasiÃ³n fÃ©tida"),BossBar.Color.GREEN,BossBar.Style.PROGRESS));
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
            if(released<2 && r.getInt("TicksInside")>=40 && n.release(w,r,false)){it.remove();released++;}
        }
        if(ticks%20!=0){n.markDirty();return true;}
        var all=fighters(w,n);var defenders=all.stream().filter(e->e instanceof DungBeetleEntity && ((InvasionParticipant)e).combatRole()>0).toList();
        boolean reserves=n.getOccupants().stream().anyMatch(r->r.getCompound("Entity").getInt("Age")>=0);
        if(ticks>1200 && defenders.isEmpty() && !reserves){finish(w,n,false);return true;}
        if(ticks>12000){finish(w,n,false);return true;}
        if(n.invasion.getInt("Phase")==0) {
            bar.setName(Text.literal("InvasiÃ³n fÃ©tida â€” equipando la colonia"));bar.setPercent(Math.min(1,ticks/200F));
            if(ticks>=200 && !defenders.isEmpty()){n.invasion.putInt("Phase",1);n.invasion.putInt("NextWave",ticks);}
        } else {
            NbtList pending=n.invasion.getList("Enemies",NbtElement.INT_ARRAY_TYPE);
            boolean alive=false,unloaded=false;
            for(var entry:pending) {UUID id=net.minecraft.nbt.NbtHelper.toUuid(entry);Entity e=w.getEntity(id);
                if(e==null){ // loaded arena + missing UUID is treated as unloaded, never awarded a kill
                    var dead=n.invasion.getList("Defeated",NbtElement.INT_ARRAY_TYPE);if(!dead.contains(entry))unloaded=true;
                } else if(e.isAlive())alive=true;
            }
            int wave=n.invasion.getInt("Wave");bar.setName(Text.literal("InvasiÃ³n fÃ©tida â€” oleada "+wave+"/3"));bar.setPercent(Math.clamp((wave-1+(alive?0:.95F))/3F,0,1));
            if(!alive && !unloaded && ticks>=n.invasion.getInt("NextWave")) {
                if(wave>=3){finish(w,n,true);return true;}
                if(spawnWave(w,n,wave+1,defenders.size())) {n.invasion.putInt("Wave",wave+1);n.invasion.putInt("NextWave",ticks+100);}
            }
        }
        n.markDirty();return true;
    }
        private static boolean spawnWave(ServerWorld w,GrandBeetleNestBlockEntity n,int wave,int defenders) {
        int count=Math.clamp(defenders/2+wave*3,3,35);var prepared=new ArrayList<FetidSlimeEntity>();
        float hpMult = defenders >= 25 ? 1.75f : defenders >= 15 ? 1.3f : 1.0f;
        for(int i=0;i<count;i++) {
            var slime=ModEntities.FETID_SLIME.create(w);if(slime==null)return false;
            int role=wave==3 && i<(1+(defenders/10))?3:i%3==2?2:1;
            slime.combat().event=n.invasion.getUuid("Id");slime.combat().nest=n.getPos();slime.combatRole(role);
            slime.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue((role==3?55:role==2?18:12)*hpMult);slime.setHealth(slime.getMaxHealth());
            boolean placed=false;
            for(int trial=0;trial<32;trial++) {
                double angle=(i+trial*.37)*Math.PI*2/count;
                BlockPos p=n.getPos().add((int)(Math.cos(angle)*16),0,(int)(Math.sin(angle)*16));
                for(int dy=3;dy>=-3;dy--) {BlockPos at=p.up(dy);if(!w.isChunkLoaded(at))continue;
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
                for(int i=0;i<attrs.size();i++)if(attrs.getCompound(i).getString("Name").equals("minecraft:generic.max_health"))attrs.getCompound(i).putDouble("Base",max);
                data.putFloat("Health",Math.min(data.getFloat("Health"),(float)max));data.remove("Invasion");}
        }
        for(String key:new HashSet<>(n.invasion.getKeys()))n.invasion.remove(key);
        var bar=ACTIVE.remove(n);if(bar!=null){for(var p:bar.getPlayers())p.sendMessage(Text.literal(won?"Â¡La colonia ha defendido el nido!":"La invasiÃ³n ha terminado."),false);bar.clearPlayers();}
        n.markDirty();
    }
    public static void clearFighter(MobEntity mob) {
        if(!(mob instanceof InvasionParticipant p))return;
        if(mob instanceof DungBeetleEntity && p.combatRole()>0){mob.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(Math.max(1,p.combat().originalHealth));mob.setHealth(Math.min(mob.getHealth(),mob.getMaxHealth()));}
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
            if(mob.squaredDistanceTo(n.getPos().toCenterPos())>22*22){if(--c.repath<=0){mob.getNavigation().startMovingTo(n.getPos().getX()+.5,mob.getY(),n.getPos().getZ()+.5,1.2);c.repath=20;}}
            else {
                var target=mob.getWorld().getEntitiesByClass(MobEntity.class,mob.getBoundingBox().expand(18),e->e.isAlive() && enemies(mob,e) && ((InvasionParticipant)e).combatRole()>0)
                    .stream().min(Comparator.comparingDouble(mob::squaredDistanceTo)).orElse(null);
                if(target!=null) {
                    double d=mob.squaredDistanceTo(target);boolean ranged=p.combatRole()==2;
                    mob.getLookControl().lookAt(target,30,30);
                    if(--c.repath<=0){if(!ranged || d>64)mob.getNavigation().startMovingTo(target,1.3);else mob.getNavigation().stop();c.repath=15;}
                    if(mob.getMoveControl() instanceof SlimeMoveControlAccessor slime){var delta=target.getPos().subtract(mob.getPos());slime.popocraft$look((float)(Math.toDegrees(Math.atan2(delta.z,delta.x))-90),true);slime.popocraft$move(ranged && d<49?0:1.2);}
                    if(--c.cooldown<=0 && mob.canSee(target)) {
                        if(ranged && d<144){InvasionProjectileEntity.fire(mob,target);c.cooldown=35;}
                        else if(d<Math.pow(mob.getWidth()+target.getWidth()+.5,2)){target.damage(mob.getDamageSources().mobAttack(mob),mob instanceof DungBeetleEntity?6:p.combatRole()==3?4:2);c.cooldown=20;}
                    }
                }else mob.getNavigation().stop();
            }
            if(mob instanceof DungBeetleEntity && mob.age%80==0)mob.heal(1);
        }
        mob.getNavigation().tick();mob.getMoveControl().tick();mob.getLookControl().tick();mob.getJumpControl().tick();return true;
    }
}

