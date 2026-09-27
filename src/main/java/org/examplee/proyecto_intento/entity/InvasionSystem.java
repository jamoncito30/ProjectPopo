package org.examplee.proyecto_intento.entity;

import java.util.*;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.*;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.*;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.*;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKey;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FireworkExplosionComponent;
import net.minecraft.component.type.FireworksComponent;
import net.minecraft.network.packet.s2c.play.StopSoundS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import it.unimi.dsi.fastutil.ints.IntList;
import org.examplee.proyecto_intento.block.GrandBeetleNestBlockEntity;
import org.examplee.proyecto_intento.block.ModBlocks;
import org.examplee.proyecto_intento.mixin.SlimeMoveControlAccessor;

/** Server-authoritative encounter; durable state lives in the nest, not this lookup cache. */
public final class InvasionSystem {
    public static final int RADIUS=24;
    /** The user's final battle OGG is 312.28 seconds at 20 ticks/second. */
    public static final long RAID_MUSIC_DURATION_TICKS=6246L;
    private static final Identifier RAID_MUSIC_ID=Identifier.of("proyecto_intento","raid_final_theme");
    private static final Map<GrandBeetleNestBlockEntity,ServerBossBar> ACTIVE=new WeakHashMap<>();
    /** Event UUID -> listeners and their next restart time, measured in world ticks. */
    private static final Map<UUID,Map<UUID,Long>> RAID_MUSIC_LISTENERS=new HashMap<>();
    private record CelebrationRocket(ItemStack stack,double x,double y,double z) { }
    private static final class VictoryCelebration {
        final RegistryKey<World> dimension;
        final List<CelebrationRocket> rockets;
        int nextRocket=1;
        int nextTick;
        final int deadline;
        VictoryCelebration(RegistryKey<World> dimension,List<CelebrationRocket> rockets,int nextTick,int deadline) {
            this.dimension=dimension;this.rockets=rockets;this.nextTick=nextTick;this.deadline=deadline;
        }
    }
    private static final List<VictoryCelebration> CELEBRATIONS=new ArrayList<>();
    private InvasionSystem(){}
    public static void initialize() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            int now=server.getTicks();
            for(var it=CELEBRATIONS.iterator();it.hasNext();) {
                var celebration=it.next();
                ServerWorld world=server.getWorld(celebration.dimension);
                if(world==null || now>=celebration.deadline || celebration.nextRocket>=celebration.rockets.size()) {
                    it.remove();
                    continue;
                }
                if(now>=celebration.nextTick) {
                    var rocket=celebration.rockets.get(celebration.nextRocket++);
                    launchCelebrationRocket(world,rocket.stack(),rocket.x(),rocket.y(),rocket.z());
                    celebration.nextTick+=10;
                    if(celebration.nextRocket>=celebration.rockets.size())it.remove();
                }
            }
        });
        ServerTickEvents.END_SERVER_TICK.register(server -> ACTIVE.entrySet().removeIf(e -> {
            var nest=e.getKey();var w=nest.getWorld();
            if(nest.isRemoved() || w==null || w.getServer()!=server || !w.isChunkLoaded(nest.getPos()) || !active(nest)) {
                if(!nest.invasion.contains("Coordinator")) {
                    e.getValue().clearPlayers(); // Protect shared bar
                    if(w instanceof ServerWorld sw && nest.invasion.containsUuid("Id")) stopRaidMusic(sw,nest.invasion.getUuid("Id"));
                }
                return true;
            }
            return false;
        }));
        AttackEntityCallback.EVENT.register((p,w,h,e,hit)->protectedAt(w,e.getBlockPos())?ActionResult.FAIL:ActionResult.PASS);
        UseEntityCallback.EVENT.register((p,w,h,e,hit)->protectedAt(w,e.getBlockPos())?ActionResult.FAIL:ActionResult.PASS);
        UseBlockCallback.EVENT.register((p,w,h,hit)->protectedAt(w,hit.getBlockPos())?ActionResult.FAIL:ActionResult.PASS);
        AttackBlockCallback.EVENT.register((p,w,h,pos,side)->protectedAt(w,pos)?ActionResult.FAIL:ActionResult.PASS);
        UseItemCallback.EVENT.register((p,w,h)->protectedAt(w,p.getBlockPos())?TypedActionResult.fail(p.getStackInHand(h)):TypedActionResult.pass(p.getStackInHand(h)));
        PlayerBlockBreakEvents.BEFORE.register((w,p,pos,state,be)->!protectedAt(w,pos));
    }
    public static boolean stopNearestInvasion(ServerWorld w, BlockPos p) {
        GrandBeetleNestBlockEntity nearest = null;
        double minD = Double.MAX_VALUE;
        for (var n : ACTIVE.keySet()) {
            if (active(n) && n.getWorld() == w) {
                double d = n.getPos().getSquaredDistance(p);
                if (d < minD) { minD = d; nearest = n; }
            }
        }
        if (nearest != null) { finish(w, nearest, false); return true; }
        return false;
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
    private static List<GrandBeetleNestBlockEntity> cluster(ServerWorld w, GrandBeetleNestBlockEntity coordinator) {
        List<GrandBeetleNestBlockEntity> list = new ArrayList<>();
        list.add(coordinator);
        if(coordinator.invasion.contains("Cluster")) {
            var arr = coordinator.invasion.getList("Cluster", NbtElement.LONG_TYPE);
            for(int i=0; i<arr.size(); i++) {
                BlockPos p = BlockPos.fromLong(((NbtLong)arr.get(i)).longValue());
                if(p.equals(coordinator.getPos())) continue;
                if(w.isChunkLoaded(p) && w.getBlockEntity(p) instanceof GrandBeetleNestBlockEntity n && active(n)) list.add(n);
            }
        }
        return list;
    }
    /** Starts the normal celebration at the nearest loaded colony without resolving or rewarding a raid. */
    public static boolean forceCelebration(ServerWorld world,BlockPos searchOrigin) {
        GrandBeetleNestBlockEntity nearest=null;
        double nearestDistance=128.0*128.0;
        for(var nest:completeNestsNear(world,searchOrigin,128,128)) {
            double distance=nest.getPos().getSquaredDistance(searchOrigin);
            if(distance<=nearestDistance) {nearest=nest;nearestDistance=distance;}
        }
        if(nearest==null)return false;

        var colony=completeNestsNear(world,nearest.getPos(),72,24);
        if(colony.isEmpty())colony=List.of(nearest);
        var beetles=world.getEntitiesByClass(DungBeetleEntity.class,clusterBox(colony),DungBeetleEntity::isAlive);
        spawnVictoryCelebration(world,colony,beetles);
        return true;
    }
    private static List<GrandBeetleNestBlockEntity> completeNestsNear(ServerWorld world,BlockPos center,int horizontalRange,int verticalRange) {
        List<GrandBeetleNestBlockEntity> nests=new ArrayList<>();
        int minChunkX=(center.getX()-horizontalRange)>>4,maxChunkX=(center.getX()+horizontalRange)>>4;
        int minChunkZ=(center.getZ()-horizontalRange)>>4,maxChunkZ=(center.getZ()+horizontalRange)>>4;
        for(int chunkX=minChunkX;chunkX<=maxChunkX;chunkX++)for(int chunkZ=minChunkZ;chunkZ<=maxChunkZ;chunkZ++) {
            BlockPos chunkPos=new BlockPos(chunkX<<4,center.getY(),chunkZ<<4);
            if(!world.isChunkLoaded(chunkPos))continue;
            for(var blockEntity:world.getWorldChunk(chunkPos).getBlockEntities().values()) {
                if(blockEntity instanceof GrandBeetleNestBlockEntity nest
                    && Math.abs(nest.getPos().getX()-center.getX())<=horizontalRange
                    && Math.abs(nest.getPos().getY()-center.getY())<=verticalRange
                    && Math.abs(nest.getPos().getZ()-center.getZ())<=horizontalRange
                    && nest.isStructureIntact())nests.add(nest);
            }
        }
        return nests;
    }
    private static Box clusterBox(List<GrandBeetleNestBlockEntity> cluster) {
        Box b = new Box(cluster.get(0).getPos());
        for(var n : cluster) b = b.union(new Box(n.getPos()));
        return b.expand(RADIUS+12);
    }
    private static List<MobEntity> fighters(ServerWorld w,UUID id,Box bounds) {
        return w.getEntitiesByClass(MobEntity.class,bounds,e->e.isAlive() && e instanceof InvasionParticipant p && id.equals(p.combat().event));
    }
    public static boolean start(ServerWorld w,GrandBeetleNestBlockEntity coordinator) {
        if(active(coordinator) || !coordinator.isStructureIntact() || w.getDifficulty()==net.minecraft.world.Difficulty.PEACEFUL)return false;
        
        List<GrandBeetleNestBlockEntity> joinedNests = new ArrayList<>();
        BlockPos.stream(coordinator.getPos().add(-72, -24, -72), coordinator.getPos().add(72, 24, 72)).forEach(pos -> {
            if(w.getBlockState(pos).isOf(ModBlocks.GRAND_BEETLE_NEST_CONTROLLER)) {
                if(w.getBlockEntity(pos) instanceof GrandBeetleNestBlockEntity gn && gn.isStructureIntact() && !active(gn)) {
                    joinedNests.add(gn);
                }
            }
        });
        boolean adult = joinedNests.stream().anyMatch(gn -> gn.getOccupants().stream().anyMatch(r->r.getCompound("Entity").getInt("Age")>=0));
        List<DungBeetleEntity> initialBeetles = new ArrayList<>();
        for(var gn : joinedNests) {
            initialBeetles.addAll(w.getEntitiesByClass(DungBeetleEntity.class, new Box(gn.getPos()).expand(30), b->b.isAlive() && !b.isBaby() && b.combat().event==null && gn.getPos().equals(b.getNestPos())));
        }
        if(!adult && initialBeetles.isEmpty()) return false;
        
        UUID id = UUID.randomUUID();
        NbtList clusterNbt = new NbtList();
        ServerBossBar sharedBar = new ServerBossBar(Text.literal("Invasión fétida – ¡A las armas!"), BossBar.Color.GREEN, BossBar.Style.PROGRESS);
        
        for(var gn : joinedNests) {
            gn.invasion.putUuid("Id", id);
            if(gn != coordinator) gn.invasion.putLong("Coordinator", coordinator.getPos().asLong());
            clusterNbt.add(NbtLong.of(gn.getPos().asLong()));
            ACTIVE.put(gn, sharedBar);
            gn.markDirty();
        }
        
        coordinator.invasion.put("Cluster", clusterNbt);
        coordinator.invasion.putInt("Ticks", 0);
        coordinator.invasion.putInt("Wave", 0);
        coordinator.invasion.putInt("Phase", 0);
        
        for(var b:initialBeetles){
            b.combat().event=id;
            b.combat().nest=b.getNestPos(); 
            b.combat().originalHealth=b.getAttributeBaseValue(EntityAttributes.GENERIC_MAX_HEALTH);
            b.getNavigation().stop();
        }
        for(var gn : joinedNests) {
            for(var r:gn.getOccupants()) if(r.getCompound("Entity").getInt("Age")>=0) prepareResident(w,gn,r,id);
        }
        
        coordinator.markDirty();
        // Begin the soundtrack as soon as the raid enters its muster phase.
        syncRaidMusic(w,id,joinedNests);
        return true;
    }
    private static void prepareResident(World w,GrandBeetleNestBlockEntity n,NbtCompound resident,UUID eventId) {
        var data=resident.getCompound("Entity");var c=data.getCompound("Invasion");
        if(c.getInt("Role")>0)return;
        c.putUuid("Event",eventId);c.putLong("Nest",n.getPos().asLong());
        boolean melee=w.random.nextFloat()<.65F;
        c.putInt("Role",melee?1:2);c.putDouble("OriginalMaxHealth",8);data.put("Invasion",c);
        
        double maxHp = melee ? 120 : 65;
        double speed = melee ? 0.17 : 0.25;

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
        if(n.invasion.contains("Coordinator")) return true; // Minions don't advance the siege logic
        
        var clusterNests = cluster(w, n);
        tickRaidMusic(w,n.invasion.getUuid("Id"),clusterNests);
        Box bounds = clusterBox(clusterNests);
        var bar=ACTIVE.computeIfAbsent(n,k->new ServerBossBar(Text.literal("Invasión fétida"),BossBar.Color.GREEN,BossBar.Style.PROGRESS));
        
        if(w.getTime()%20==0) {
            for(var p:playersInWorld(w)) {
                boolean near = clusterNests.stream().anyMatch(gn -> p.squaredDistanceTo(gn.getPos().toCenterPos()) < RADIUS*RADIUS*2);
                if(near) bar.addPlayer(p); else bar.removePlayer(p);
            }
            for(var gn : clusterNests) {
                for(int x=-RADIUS;x<=RADIUS;x+=8)for(int z=-RADIUS;z<=RADIUS;z+=8)if(!w.isChunkLoaded(gn.getPos().add(x,0,z)))return true; // Wait for chunks
            }
            if(!n.isStructureIntact() && clusterNests.size() <= 1){finish(w,n,clusterNests,false);return true;}
        }
        int ticks=n.invasion.getInt("Ticks")+1;n.invasion.putInt("Ticks",ticks);
        
        for(var gn : clusterNests) {
            int released=0;
            for(var it=gn.getOccupants().iterator();it.hasNext();) {
                var r=it.next();if(r.getCompound("Entity").getInt("Age")<0)continue;
                prepareResident(w,gn,r,n.invasion.getUuid("Id"));r.putInt("TicksInside",r.getInt("TicksInside")+1);
                if(released<4 && r.getInt("TicksInside")>=20 && gn.release(w,r,false)){it.remove();released++;} 
            }
            if(released>0 && gn != n) gn.markDirty();
        }
        if(ticks%20!=0){n.markDirty();return true;}
        
        var all=fighters(w,n.invasion.getUuid("Id"),bounds);
        var defenders=all.stream().filter(e->e instanceof DungBeetleEntity && ((InvasionParticipant)e).combatRole()>0).toList();
        boolean reserves=clusterNests.stream().anyMatch(gn -> gn.getOccupants().stream().anyMatch(r->r.getCompound("Entity").getInt("Age")>=0));
        
        if(ticks>1200 && defenders.isEmpty() && !reserves){finish(w,n,clusterNests,false);return true;}
        if(ticks>24000){finish(w,n,clusterNests,false);return true;}
        
        if(n.invasion.getInt("Phase")==0) {
            int remaining = Math.max(0, 60 - (ticks/20));
            bar.setName(Text.literal("Invasión fétida – Fuerzas conjuntas (" + remaining + "s)"));
            bar.setPercent(Math.clamp((float)ticks/1200F,0,1));
            if(ticks >= 1200 && (!defenders.isEmpty() || !all.isEmpty())) {
                n.invasion.putInt("Phase",1);
                n.invasion.putInt("NextWave",ticks);
                for(var b : all) {
                    if (b instanceof DungBeetleEntity db && db.combatRole() == 0) {
                        boolean melee = w.random.nextFloat() < .65F;
                        db.combatRole(melee ? 1 : 2);
                        double maxHp = melee ? 120 : 65;
                        db.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(maxHp);
                        db.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(melee ? 0.17 : 0.25);
                        db.setHealth((float)maxHp);
                    }
                }
            }
        } else {
            NbtList pending=n.invasion.getList("Enemies",NbtElement.INT_ARRAY_TYPE);
            int wave=n.invasion.getInt("Wave");
            int waveTicks = ticks - (n.invasion.getInt("NextWave") - 100);
            boolean alive=false;
            boolean spawning = n.invasion.contains("Spawning");
            
            if (spawning) {
                var s = n.invasion.getCompound("Spawning");
                int total = s.getInt("Total");
                int spawned = s.getInt("Spawned");
                if (spawned < total) {
                    alive = true; // Always alive while there are spawns left!
                    if (ticks % 25 == 0) {
                        int squadSize = Math.min(3 + w.random.nextInt(2), total - spawned);
                        for (int k = 0; k < squadSize; k++) spawnOneSlime(w, n, clusterNests, wave, s.getInt("Defenders"), spawned + k, total, all);
                        s.putInt("Spawned", spawned + squadSize);
                    }
                } else {
                    n.invasion.remove("Spawning");
                    spawning = false;
                }
            }
            
            for(int i = 0; i < pending.size(); i++) {
                UUID id = net.minecraft.nbt.NbtHelper.toUuid(pending.get(i));
                Entity e = w.getEntity(id);
                if (e != null && e.isAlive()) {
                    alive = true;
                    if (e instanceof FetidSlimeEntity slime && !spawning) {
                        if (waveTicks > 900) slime.addStatusEffect(new net.minecraft.entity.effect.StatusEffectInstance(net.minecraft.entity.effect.StatusEffects.GLOWING, 40, 0, false, false));
                        if (waveTicks > 1200 && ticks % 20 == 0) slime.damage(w.getDamageSources().magic(), 5.0F); // auto kill hard cap
                    }
                }
            }
            
            bar.setName(Text.literal("Invasión fétida – Oleada combinada "+wave+"/5"));
            bar.setPercent(Math.clamp((wave-1+(alive?0:.95F))/5F,0,1));
            
            if(!alive && ticks>=n.invasion.getInt("NextWave")) {
                if(wave>=5){finish(w,n,clusterNests,true);return true;}
                beginSpawnWave(n, clusterNests, wave+1, defenders.size());
                n.invasion.putInt("NextWave",ticks+100);
            }
        }
        n.markDirty();return true;
    }
    
    private static void beginSpawnWave(GrandBeetleNestBlockEntity coordinator, List<GrandBeetleNestBlockEntity> clusterNests, int wave, int defenders) {
        int count=Math.clamp((defenders/2)+wave*(3+clusterNests.size()*2),4*clusterNests.size(),60*clusterNests.size());
        coordinator.invasion.putInt("Wave", wave); 
        coordinator.invasion.put("Enemies", new NbtList());
        coordinator.invasion.put("Defeated", new NbtList());
        
        NbtCompound s = new NbtCompound();
        s.putInt("Total", count);
        s.putInt("Spawned", 0);
        s.putInt("Defenders", defenders);
        coordinator.invasion.put("Spawning", s);
    }
    
    private static void spawnOneSlime(ServerWorld w,GrandBeetleNestBlockEntity coordinator,List<GrandBeetleNestBlockEntity> clusterNests,int wave,int defenders,int i,int count,List<MobEntity> all) {
        float hpMult = defenders >= (15*clusterNests.size()) ? 1.75f : defenders >= (8*clusterNests.size()) ? 1.35f : 1.0f;
        if (wave >= 4) hpMult *= 1.15f; 
        var slime=ModEntities.FETID_SLIME.create(w);if(slime==null)return;
        GrandBeetleNestBlockEntity targetNest = clusterNests.get(i % clusterNests.size());
        int role = wave==5 && i<(2+(defenders/10))?3:i%3==2?2:1;
        
        slime.combat().event=coordinator.invasion.getUuid("Id");slime.combat().nest=targetNest.getPos();slime.combatRole(role);
        slime.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue((role==3?65:role==2?22:15)*hpMult);slime.setHealth(slime.getMaxHealth());
        boolean placed=false;
        for(int trial=0;trial<32;trial++) {
            double angle=(i+trial*.37)*Math.PI*2/Math.max(1, count/clusterNests.size());
            BlockPos p=targetNest.getPos().add((int)(Math.cos(angle)*19),0,(int)(Math.sin(angle)*19));
            for(int dy=4;dy>=-4;dy--) {BlockPos at=p.up(dy);if(!w.isChunkLoaded(at))continue;
                slime.refreshPositionAndAngles(at.getX()+.5,at.getY()+.05,at.getZ()+.5,0,0);
                if(!w.getBlockState(at.down()).getCollisionShape(w,at.down()).isEmpty() && w.getFluidState(at).isEmpty() && w.isSpaceEmpty(slime)
                     && all.stream().noneMatch(t -> t.getBoundingBox().intersects(slime.getBoundingBox()))){placed=true;break;}
            }if(placed)break;
        }
        if(placed && w.spawnEntity(slime)) {
            NbtList ids = coordinator.invasion.getList("Enemies", NbtElement.INT_ARRAY_TYPE);
            ids.add(NbtHelper.fromUuid(slime.getUuid()));
            all.add(slime);
        }
    }
    public static void died(MobEntity mob) {
        var n=nest(mob);if(n==null || !active(n) || !(mob instanceof FetidSlimeEntity))return;
        if(n.invasion.contains("Coordinator")) {
            BlockPos coordPos = BlockPos.fromLong(n.invasion.getLong("Coordinator"));
            if(mob.getWorld().isChunkLoaded(coordPos)) n = (GrandBeetleNestBlockEntity)mob.getWorld().getBlockEntity(coordPos);
            if(n==null || !active(n)) return;
        }
        var list=n.invasion.getList("Defeated",NbtElement.INT_ARRAY_TYPE);var id=NbtHelper.fromUuid(mob.getUuid());if(!list.contains(id))list.add(id);n.invasion.put("Defeated",list);n.markDirty();
    }
    public static void finish(ServerWorld w,GrandBeetleNestBlockEntity n,boolean won){if(!active(n))return;if(n.invasion.contains("Coordinator")){BlockPos p=BlockPos.fromLong(n.invasion.getLong("Coordinator"));if(w.isChunkLoaded(p)&&w.getBlockEntity(p) instanceof GrandBeetleNestBlockEntity c){n=c;}}finish(w,n,cluster(w,n),won);}
    private static void finish(ServerWorld w,GrandBeetleNestBlockEntity coordinator,List<GrandBeetleNestBlockEntity> clusterNests,boolean won) {
        if(!active(coordinator))return;
        stopRaidMusic(w,coordinator.invasion.getUuid("Id"));
        var bar = ACTIVE.get(coordinator);
        List<DungBeetleEntity> celebratingBeetles=new ArrayList<>();
        for(var e:fighters(w,coordinator.invasion.getUuid("Id"),clusterBox(clusterNests))) {
            if(e instanceof FetidSlimeEntity)e.discard();
            else {
                if(e instanceof DungBeetleEntity beetle && beetle.isAlive())celebratingBeetles.add(beetle);
                clearFighter(e);
            }
        }
        
        for(var gn : clusterNests) {
            for(var r:gn.getOccupants()) {var data=r.getCompound("Entity");var c=data.getCompound("Invasion");
                if(c.containsUuid("Event")){var attrs=data.getList("Attributes",NbtElement.COMPOUND_TYPE);double max=c.getDouble("OriginalMaxHealth");if(max<=0)max=8;
                    for(int i=0;i<attrs.size();i++) {
                        if(attrs.getCompound(i).getString("Name").equals("minecraft:generic.max_health")) attrs.getCompound(i).putDouble("Base",max);
                        if(attrs.getCompound(i).getString("Name").equals("minecraft:generic.movement_speed")) attrs.getCompound(i).putDouble("Base",0.22);
                    }
                    data.putFloat("Health",Math.min(data.getFloat("Health"),(float)max));data.remove("Invasion");}
            }
            for(String key:new HashSet<>(gn.invasion.getKeys()))gn.invasion.remove(key);
            ACTIVE.remove(gn);
            gn.markDirty();
        }
        if (won) {
            spawnVictoryCelebration(w,clusterNests,celebratingBeetles);
            spawnRewardTrader(w, coordinator);
        }
        if(bar!=null){for(var p:bar.getPlayers())p.sendMessage(Text.literal(won?"¡La colonia unida ha defendido su territorio!":"La invasión fétida ha extinguido la resistencia."),false);bar.clearPlayers();}
    }

    /** A bounded, varied vanilla firework show celebrates a successful defense. */
    private static void spawnVictoryCelebration(ServerWorld world,List<GrandBeetleNestBlockEntity> nests,List<DungBeetleEntity> beetles) {
        for(var nest:nests) {
            BlockPos pos=nest.getPos();
            world.spawnParticles(ParticleTypes.HAPPY_VILLAGER,pos.getX()+.5,pos.getY()+2.0,pos.getZ()+.5,20,.8,.7,.8,.08);
        }
        for(var beetle:beetles)if(beetle.isAlive()) {
            world.spawnParticles(ParticleTypes.HAPPY_VILLAGER,beetle.getX(),beetle.getY()+.6,beetle.getZ(),12,.45,.35,.45,.06);
        }

        List<CelebrationRocket> rockets=new ArrayList<>(24);
        var shapes=FireworkExplosionComponent.Type.values();
        int[][] palettes={
            {0xF9FFFE,0xF9801D,0xFED83D}, {0x3AB3DA,0x8932B8,0xF9FFFE},
            {0xB02E26,0x80C71F,0xF9801D}, {0x3C44AA,0xC74EBD,0x3AB3DA},
            {0xFED83D,0xB02E26,0x8932B8}, {0x80C71F,0xF9FFFE,0x3C44AA}
        };
        for(int i=0;i<24;i++) {
            var nest=nests.get(i%nests.size());
            BlockPos origin=nest.getOrigin();
            int side=i%4;
            double x=nest.getPos().getX()+.5+(side==1?2.5:side==3?-2.5:0);
            double y=origin.getY()+18.0+(i%3==0?1.5:0);
            double z=nest.getPos().getZ()+.5+(side==0?2.5:side==2?-2.5:0);
            ItemStack rocketStack=new ItemStack(Items.FIREWORK_ROCKET);
            int[] colors=palettes[i%palettes.length];
            var explosion=new FireworkExplosionComponent(shapes[i%shapes.length],IntList.of(colors[0],colors[1]),IntList.of(colors[2]),true,i%3==0);
            rocketStack.set(DataComponentTypes.FIREWORKS,new FireworksComponent(1,List.of(explosion)));
            rockets.add(new CelebrationRocket(rocketStack,x,y,z));
        }
        // Launch one now, then one every 10 ticks. The transient show completes in 11.5 seconds.
        var first=rockets.get(0);
        launchCelebrationRocket(world,first.stack(),first.x(),first.y(),first.z());
        int now=world.getServer().getTicks();
        CELEBRATIONS.add(new VictoryCelebration(world.getRegistryKey(),rockets,now+10,now+300));
    }

    private static void launchCelebrationRocket(ServerWorld world,ItemStack stack,double x,double y,double z) {
        BlockPos pos=BlockPos.ofFloored(x,y,z);
        if(!world.isChunkLoaded(pos))return;
        var rocket=new FireworkRocketEntity(world,stack,x,y,z,false);
        if(world.spawnEntity(rocket))world.spawnParticles(ParticleTypes.FIREWORK,x,y,z,8,.2,.2,.2,.02);
    }

    /** Keeps the soundtrack audience aligned with the raid boss-bar audience. */
    private static void tickRaidMusic(ServerWorld world,UUID eventId,List<GrandBeetleNestBlockEntity> nests) {
        if(world.getTime()%20==0) syncRaidMusic(world,eventId,nests);
        Map<UUID,Long> listeners=RAID_MUSIC_LISTENERS.computeIfAbsent(eventId,ignored->new HashMap<>());
        long now=world.getTime();
        for(ServerPlayerEntity player:playersInWorld(world)) {
            Long next=listeners.get(player.getUuid());
            if(next!=null && now>=next) {
                stopRaidMusicForPlayer(player);
                playRaidMusicForPlayer(player);
                listeners.put(player.getUuid(),now+RAID_MUSIC_DURATION_TICKS);
            }
        }
    }

    private static void syncRaidMusic(ServerWorld world,UUID eventId,List<GrandBeetleNestBlockEntity> nests) {
        Map<UUID,Long> listeners=RAID_MUSIC_LISTENERS.computeIfAbsent(eventId,ignored->new HashMap<>());
        long now=world.getTime();
        for(ServerPlayerEntity player:playersInWorld(world)) {
            boolean witnessing=isRaidMusicWitnessPosition(player.getPos(),nests);
            boolean listening=listeners.containsKey(player.getUuid());
            if(witnessing && !listening) {
                playRaidMusicForPlayer(player);
                listeners.put(player.getUuid(),now+RAID_MUSIC_DURATION_TICKS);
            } else if(!witnessing && listening) {
                stopRaidMusicForPlayer(player);
                listeners.remove(player.getUuid());
            }
        }
        // A disconnected client should restart the track if it later rejoins the audience.
        listeners.keySet().removeIf(uuid->world.getServer().getPlayerManager().getPlayer(uuid)==null);
    }

    public static boolean isRaidMusicWitnessPosition(Vec3d position,List<GrandBeetleNestBlockEntity> nests) {
        return nests.stream().anyMatch(nest->position.squaredDistanceTo(nest.getPos().toCenterPos())<RADIUS*RADIUS*2);
    }

    private static List<ServerPlayerEntity> playersInWorld(ServerWorld world) {
        return world.getServer().getPlayerManager().getPlayerList().stream().filter(player->player.getServerWorld()==world).toList();
    }

    private static void playRaidMusicForPlayer(ServerPlayerEntity player) {
        // Direct-to-player playback avoids positional attenuation across the raid arena.
        player.networkHandler.sendPacket(new StopSoundS2CPacket(null,SoundCategory.MUSIC));
        player.playSoundToPlayer(ModSounds.RAID_FINAL_THEME,SoundCategory.MUSIC,1.0F,1.0F);
    }

    private static void stopRaidMusicForPlayer(ServerPlayerEntity player) {
        player.networkHandler.sendPacket(new StopSoundS2CPacket(RAID_MUSIC_ID,SoundCategory.MUSIC));
    }

    private static void stopRaidMusic(ServerWorld world,UUID eventId) {
        Map<UUID,Long> listeners=RAID_MUSIC_LISTENERS.remove(eventId);
        if(listeners==null)return;
        for(UUID uuid:listeners.keySet()) {
            ServerPlayerEntity player=world.getServer().getPlayerManager().getPlayer(uuid);
            if(player!=null)stopRaidMusicForPlayer(player);
        }
    }

    private static void spawnRewardTrader(ServerWorld world, GrandBeetleNestBlockEntity nest) {
        var trader = ModEntities.DUNG_BEETLE.create(world);
        if (trader == null) return;
        trader.setTrader(true);
        trader.setDiscountedTrader(true);
        trader.setNest(nest.getPos());
        BlockPos origin = nest.getOrigin();
        for (int y = 5; y <= 9; y++) {
            for (int x = -2; x <= 6; x++) {
                for (int z = -2; z <= 6; z++) {
                    BlockPos spawn = origin.add(x, y, z);
                    if (!world.isChunkLoaded(spawn)) continue;
                    trader.refreshPositionAndAngles(spawn.getX() + .5, spawn.getY(), spawn.getZ() + .5, 0, 0);
                    if (world.getFluidState(spawn).isEmpty() && world.isSpaceEmpty(trader)
                            && world.getOtherEntities(trader, trader.getBoundingBox()).isEmpty()
                            && world.spawnEntity(trader)) return;
                }
            }
        }
        BlockPos fallback = origin.add(2, 10, 2);
        trader.refreshPositionAndAngles(fallback.getX() + .5, fallback.getY(), fallback.getZ() + .5, 0, 0);
        world.spawnEntity(trader);
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
        var c=p.combat();var n=nest(mob); // Individual local base nest
        if(n==null || !active(n)) {
            if(mob.getWorld().isChunkLoaded(c.nest)){if(mob instanceof FetidSlimeEntity)mob.discard();else clearFighter(mob);}return true;
        }
        mob.setTarget(null);
        if(mob instanceof DungBeetleEntity b && p.combatRole()==0) {
            if(--c.repath<=0){n.navigateToPerimeter(b,1.6);c.repath=20;}
            if(n.atPerimeter(b))n.tryEnter(b);
        } else {
            if(mob.squaredDistanceTo(n.getPos().toCenterPos())>32*32){
                if(--c.repath<=0){mob.getNavigation().startMovingTo(n.getPos().getX()+.5,mob.getY(),n.getPos().getZ()+.5,1.2);c.repath=20;}
            } else {
                var target=mob.getWorld().getEntitiesByClass(MobEntity.class,mob.getBoundingBox().expand(22),e->e.isAlive() && enemies(mob,e) && ((InvasionParticipant)e).combatRole()>0)
                    .stream().min(Comparator.comparingDouble(mob::squaredDistanceTo)).orElse(null);
                    
                if(target!=null) {
                    double d=mob.squaredDistanceTo(target);
                    boolean ranged = p.combatRole()==2; 
                    mob.getLookControl().lookAt(target,30,30);
                    
                    if(--c.repath<=0) {
                        if(ranged) {
                            if(d < 20.0) { 
                                Vec3d dir = mob.getPos().subtract(target.getPos()).normalize();
                                Vec3d away = mob.getPos().add(dir.multiply(5));
                                mob.getNavigation().startMovingTo(away.x, away.y, away.z, 1.4);
                            } else if(d > 81.0) {
                                mob.getNavigation().startMovingTo(target, 1.15);
                            } else {
                                mob.getNavigation().stop();
                            }
                        } else {
                            mob.getNavigation().startMovingTo(target, 1.25);
                        }
                        c.repath = 15;
                    }
                    
                    if(mob.getMoveControl() instanceof SlimeMoveControlAccessor slime){
                        var delta=target.getPos().subtract(mob.getPos());
                        slime.popocraft$look((float)(Math.toDegrees(Math.atan2(delta.z,delta.x))-90),true);
                        slime.popocraft$move(ranged && d < 20.0 ? 0.0f : 1.2f);
                    }
                    
                    if(--c.cooldown<=0 && mob.canSee(target)) {
                        if(ranged && d<196) { 
                            InvasionProjectileEntity.fire(mob,target);
                            c.cooldown = mob instanceof DungBeetleEntity ? 40 : 50; 
                        }
                        else if(d<Math.pow(mob.getWidth()+target.getWidth()+.9,2)) {
                            target.damage(mob.getDamageSources().mobAttack(mob),mob instanceof DungBeetleEntity ? (p.combatRole()==1? 14 : 7) : p.combatRole()==3? 6 : 3);
                            c.cooldown = 20; 
                        }
                    }
                }else mob.getNavigation().stop();
                
                if(mob.isTouchingWater() || mob.isInLava()) {
                    if (mob instanceof DungBeetleEntity) {
                        if (target == null || target.getY() >= mob.getY() - 0.5) mob.getJumpControl().setActive();
                    }
                }
            }
            if(mob instanceof DungBeetleEntity && mob.age%30==0)mob.heal(4);
        }
        mob.getNavigation().tick();mob.getMoveControl().tick();mob.getLookControl().tick();mob.getJumpControl().tick();return true;
    }
}



