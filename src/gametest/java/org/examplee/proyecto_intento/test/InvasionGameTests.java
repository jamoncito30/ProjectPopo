package org.examplee.proyecto_intento.test;

import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.block.Blocks;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.test.*;
import net.minecraft.util.math.*;
import org.examplee.proyecto_intento.block.*;
import org.examplee.proyecto_intento.entity.*;

public final class InvasionGameTests implements FabricGameTest {
    private GrandBeetleNestBlockEntity nest(TestContext c) {
        c.getWorld().getServer().setDifficulty(net.minecraft.world.Difficulty.NORMAL, true);
        for(int x=0;x<32;x++)for(int z=0;z<32;z++)c.setBlockState(x,1,z,Blocks.STONE);
        var p=c.getAbsolutePos(new BlockPos(16,2,16));
        c.getWorld().setBlockState(p,ModBlocks.GRAND_BEETLE_NEST_CONTROLLER.getDefaultState().with(GrandBeetleNestPieceBlock.STAGE,4));
        var n=(GrandBeetleNestBlockEntity)c.getWorld().getBlockEntity(p);n.updateStructure(4);return n;
    }
    private DungBeetleEntity beetle(TestContext c,GrandBeetleNestBlockEntity n,int side) {
        var b=ModEntities.DUNG_BEETLE.create(c.getWorld());b.setNest(n.getPos());
        BlockPos p=n.perimeter().get(side);b.setPosition(p.getX()+.5,p.getY()+.05,p.getZ()+.5);c.getWorld().spawnEntity(b);return b;
    }
    @GameTest(templateName="popocraft_tests:arena",batchId="invasion_admission")
    public void unlimitedAdmissionAllSidesAndNbt(TestContext c) {
        var n=nest(c);
        for(int i=0;i<45;i++){var b=beetle(c,n,(i%4)*5+2);c.assertTrue(n.tryEnter(b),"Every side accepts residents beyond thirty");}
        var copy=new GrandBeetleNestBlockEntity(n.getPos(),n.getCachedState());copy.read(n.createNbt(c.getWorld().getRegistryManager()),c.getWorld().getRegistryManager());
        c.assertEquals(copy.getOccupantCount(),45,"NBT preserves all forty-five residents");
        c.assertEquals(copy.getOccupants().stream().map(r->r.getCompound("Entity").getUuid("UUID")).distinct().count(),45L,"No duplicate UUID");
        var far=beetle(c,n,2);far.setPosition(n.getPos().toCenterPos().add(10,0,0));c.assertFalse(n.tryEnter(far),"Cannot enter from a distance");c.complete();
    }
    @GameTest(templateName="popocraft_tests:arena",batchId="invasion_equipment",tickLimit=180)
    public void musterEquipmentPersistenceAndFinish(TestContext c) {
        var n=nest(c);var b=beetle(c,n,2);var id=b.getUuid();
        c.assertTrue(InvasionSystem.start(c.getWorld(),n),"Intact colony starts encounter");
        c.assertEquals(b.combatRole(),0,"Outside beetle has no armor yet");
        c.waitAndRun(90,()->{
            var returned=(DungBeetleEntity)c.getWorld().getEntity(id);
            c.assertTrue(returned!=null && returned.combatRole()>0,"Same UUID enters and exits equipped");
            c.assertEquals(returned.getMaxHealth(),40F,"Combat equipment grants defender health");
            var saved=new NbtCompound();returned.writeCustomDataToNbt(saved);var loaded=ModEntities.DUNG_BEETLE.create(c.getWorld());loaded.readCustomDataFromNbt(saved);
            c.assertEquals(loaded.combatRole(),returned.combatRole(),"Equipment survives NBT");
            c.assertEquals(loaded.combat().event,returned.combat().event,"Event identity survives NBT");
            InvasionSystem.finish(c.getWorld(),n,true);
            c.assertEquals(returned.combatRole(),0,"Normal skin restored");c.assertEquals(returned.getMaxHealth(),8F,"Normal health restored");
            c.assertFalse(InvasionSystem.protectedAt(c.getWorld(),n.getPos()),"Terrain protection removed");c.complete();
        });
    }
    @GameTest(templateName="popocraft_tests:arena",batchId="invasion_protection")
    public void enemyDamageAndSpectatorProtection(TestContext c) {
        var n=nest(c);var b=beetle(c,n,2);c.assertTrue(InvasionSystem.start(c.getWorld(),n),"Starts");b.combatRole(1);
        var slime=ModEntities.FETID_SLIME.create(c.getWorld());slime.combat().event=b.combat().event;slime.combat().nest=n.getPos();slime.combatRole(1);slime.setPosition(b.getPos().add(2,0,0));c.getWorld().spawnEntity(slime);
        var player=c.createMockPlayer(net.minecraft.world.GameMode.SURVIVAL);
        c.assertFalse(slime.damage(c.getWorld().getDamageSources().playerAttack(player),5),"Human damage blocked");
        c.assertFalse(b.damage(c.getWorld().getDamageSources().generic(),5),"Indirect environmental damage blocked");
        c.assertTrue(slime.damage(c.getWorld().getDamageSources().mobAttack(b),5),"Opposing faction damage accepted");
        c.assertFalse(c.getWorld().setBlockState(n.getPos().up(5),Blocks.DIAMOND_BLOCK.getDefaultState()),"Terrain frozen during encounter");
        InvasionSystem.finish(c.getWorld(),n,false);c.assertTrue(c.getWorld().setBlockState(n.getPos().up(5),Blocks.DIAMOND_BLOCK.getDefaultState()),"Terrain restored after encounter");c.complete();
    }
    @GameTest(templateName="popocraft_tests:arena",batchId="invasion_combat",tickLimit=300)
    public void combatAiAttacksOnlyOpponents(TestContext c) {
        var n=nest(c);var b=beetle(c,n,2);InvasionSystem.start(c.getWorld(),n);b.combatRole(1);
        b.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(40);b.setHealth(40);
        var slime=ModEntities.FETID_SLIME.create(c.getWorld());slime.combat().event=b.combat().event;slime.combat().nest=n.getPos();slime.combatRole(1);slime.setPosition(b.getPos().add(1,0,0));c.getWorld().spawnEntity(slime);
        c.waitAndRun(80,()->{c.assertTrue(slime.getHealth()<16 || !slime.isAlive(),"Autonomous melee causes real enemy damage");c.assertTrue(b.isAlive(),"Defender survives basic slime");InvasionSystem.finish(c.getWorld(),n,true);c.complete();});
    }
    @GameTest(templateName="popocraft_tests:arena",batchId="invasion_ground")
    public void beetlesStandOnOrganicTerrain(TestContext c) {
        var n=nest(c);var b=beetle(c,n,2);
        for(var block:new net.minecraft.block.Block[]{ModBlocks.WET_POPO,ModBlocks.SEWAGE}) {
            var state=block.getDefaultState();c.assertFalse(state.getCollisionShape(c.getWorld(),b.getBlockPos(),net.minecraft.block.ShapeContext.of(b)).isEmpty(),"Organic terrain supports beetle");
        }c.complete();
    }
    @GameTest(templateName="popocraft_tests:arena",tickLimit=280)
    public void victorySpawnsExactlyOneDiscountedTrader(TestContext c) {
        var n=nest(c);beetle(c,n,2);
        c.assertTrue(InvasionSystem.start(c.getWorld(),n),"Colony starts the raid");
        InvasionSystem.finish(c.getWorld(),n,true);
        c.waitAndRun(2,()->{
            var traders=c.getWorld().getEntitiesByClass(DungBeetleEntity.class,new net.minecraft.util.math.Box(n.getPos()).expand(16),DungBeetleEntity::isTrader);
            c.assertEquals(traders.size(),1,"Winning raid guarantees one trader");
            c.assertTrue(traders.get(0).isDiscountedTrader(),"Raid reward receives its persistent discount");
            var ids=new java.util.HashSet<java.util.UUID>();
            var shapes=new java.util.HashSet<String>();
            var palettes=new java.util.HashSet<String>();
            observeCelebration(c,new net.minecraft.util.math.Box(n.getPos()).expand(40),ids,shapes,palettes,250,()->{
                c.assertEquals(ids.size(),24,"Victory launches all twenty-four rockets in the timed celebration");
                c.assertEquals(shapes.size(),5,"The show cycles through all five vanilla explosion shapes");
                c.assertTrue(palettes.size()>=6,"Firework colors visibly vary through the show");
                c.complete();
            });
        });
    }
    @GameTest(templateName="popocraft_tests:arena",tickLimit=280)
    public void forceCelebrationCommandUsesNearestColony(TestContext c) {
        var n=nest(c);
        var manager=c.getWorld().getServer().getCommandManager();
        var source=c.getWorld().getServer().getCommandSource().withWorld(c.getWorld()).withPosition(n.getPos().toCenterPos()).withLevel(4);
        manager.executeWithPrefix(source,"force_beetle_celebration");
        c.assertTrue(c.getWorld().getEntitiesByClass(FireworkRocketEntity.class,new net.minecraft.util.math.Box(n.getPos()).expand(40),e->true).size()==1,"Administrator command launches the first rocket at the nearby colony");
        c.assertFalse(InvasionSystem.active(n),"Forcing a celebration does not start or finish a raid");
        c.assertTrue(c.getWorld().getEntitiesByClass(DungBeetleEntity.class,new net.minecraft.util.math.Box(n.getPos()).expand(16),DungBeetleEntity::isTrader).isEmpty(),"Manual celebration grants no trader");
        var ids=new java.util.HashSet<java.util.UUID>();
        var shapes=new java.util.HashSet<String>();
        var palettes=new java.util.HashSet<String>();
        observeCelebration(c,new net.minecraft.util.math.Box(n.getPos()).expand(40),ids,shapes,palettes,250,()->{
            c.assertEquals(ids.size(),24,"Manual command runs the complete twenty-four-rocket show");
            c.complete();
        });
    }
    @GameTest(templateName="popocraft_tests:arena")
    public void forceCelebrationCommandRequiresAdminAndHandlesNoColony(TestContext c) {
        var manager=c.getWorld().getServer().getCommandManager();
        var farPosition=new net.minecraft.util.math.Vec3d(100000,0,100000);
        var admin=c.getWorld().getServer().getCommandSource().withWorld(c.getWorld()).withPosition(farPosition).withLevel(4);
        c.assertFalse(InvasionSystem.forceCelebration(c.getWorld(),BlockPos.ofFloored(farPosition)),"No nearby colony is found");
        manager.executeWithPrefix(admin,"force_beetle_celebration");
        var command=manager.getDispatcher().getRoot().getChild("force_beetle_celebration");
        c.assertTrue(command.canUse(admin),"Operator permission level can run the command");
        c.assertFalse(command.canUse(admin.withLevel(0)),"Players below permission level two cannot run the command");
        c.complete();
    }
    @GameTest(templateName="popocraft_tests:arena")
    public void defeatDoesNotSpawnRewardTrader(TestContext c) {
        var n=nest(c);beetle(c,n,2);
        c.assertTrue(InvasionSystem.start(c.getWorld(),n),"Colony starts the raid");
        InvasionSystem.finish(c.getWorld(),n,false);
        c.assertTrue(c.getWorld().getEntitiesByClass(DungBeetleEntity.class,new net.minecraft.util.math.Box(n.getPos()).expand(16),DungBeetleEntity::isTrader).isEmpty(),"Losing raid grants no trader");
        c.assertTrue(c.getWorld().getEntitiesByClass(net.minecraft.entity.projectile.FireworkRocketEntity.class,new net.minecraft.util.math.Box(n.getPos()).expand(12),rocket->true).isEmpty(),"A defeat has no fireworks");
        c.complete();
    }

    @GameTest(templateName="popocraft_tests:arena")
    public void abortingRaidDoesNotCelebrate(TestContext c) {
        var n=nest(c);beetle(c,n,2);
        c.assertTrue(InvasionSystem.start(c.getWorld(),n),"Colony starts the raid");
        c.assertTrue(InvasionSystem.stopNearestInvasion(c.getWorld(),n.getPos()),"Administrative stop aborts the raid");
        c.assertTrue(c.getWorld().getEntitiesByClass(net.minecraft.entity.projectile.FireworkRocketEntity.class,new net.minecraft.util.math.Box(n.getPos()).expand(12),rocket->true).isEmpty(),"An aborted raid has no fireworks");
        c.complete();
    }

    @GameTest(templateName="popocraft_tests:arena",tickLimit=300)
    public void celebrationFireworksDoNotDamageNearbyEntities(TestContext c) {
        var n=nest(c);beetle(c,n,2);
        var cow=c.spawnEntity(net.minecraft.entity.EntityType.COW,16,2,16);cow.setNoGravity(true);
        cow.refreshPositionAndAngles(n.getPos().getX()+.5,n.getOrigin().getY()+8.0,n.getPos().getZ()+.5,0,0);
        float health=cow.getHealth();
        c.assertTrue(InvasionSystem.start(c.getWorld(),n),"Colony starts the raid");
        InvasionSystem.finish(c.getWorld(),n,true);
        c.waitAndRun(270,()->{
            c.assertEquals(cow.getHealth(),health,"Ownerless celebration fireworks do not damage nearby mobs");
            c.complete();
        });
    }

    private void observeCelebration(TestContext c,net.minecraft.util.math.Box area,java.util.Set<java.util.UUID> ids,
                                   java.util.Set<String> shapes,java.util.Set<String> palettes,int ticks,Runnable finished) {
        for(FireworkRocketEntity rocket:c.getWorld().getEntitiesByClass(FireworkRocketEntity.class,area,e->true)) {
            if(ids.add(rocket.getUuid())) {
                var component=rocket.getStack().get(DataComponentTypes.FIREWORKS);
                if(component!=null && !component.explosions().isEmpty()) {
                    var explosion=component.explosions().get(0);
                    shapes.add(explosion.shape().asString());
                    palettes.add(explosion.colors().toString()+"/"+explosion.fadeColors());
                }
            }
        }
        if(ticks<=1)finished.run();
        else c.waitAndRun(1,()->observeCelebration(c,area,ids,shapes,palettes,ticks-1,finished));
    }

    @GameTest(templateName="popocraft_tests:arena",batchId="invasion_music")
    public void raidThemeUsesBossBarAudienceRadius(TestContext c) {
        var n=nest(c);var nests=java.util.List.of(n);var center=n.getPos().toCenterPos();
        c.assertTrue(InvasionSystem.isRaidMusicWitnessPosition(center,nests),"Player at the raid center is a witness");
        c.assertTrue(InvasionSystem.isRaidMusicWitnessPosition(center.add(20,0,0),nests),"Player inside the boss-bar radius hears the track");
        c.assertFalse(InvasionSystem.isRaidMusicWitnessPosition(center.add(40,0,0),nests),"Player outside the boss-bar radius is excluded");
        c.complete();
    }

    @GameTest(templateName="popocraft_tests:arena",batchId="invasion_music")
    public void raidThemeLoopIntervalMatchesOggDuration(TestContext c) {
        c.assertEquals(InvasionSystem.RAID_MUSIC_DURATION_TICKS,6246L,"312.28-second soundtrack repeats after 6,246 ticks");
        c.complete();
    }

    @GameTest(templateName="popocraft_tests:arena",batchId="invasion_music")
    public void failedRaidDoesNotCreateMusicSession(TestContext c) {
        var n=nest(c);
        c.assertFalse(InvasionSystem.start(c.getWorld(),n),"An empty colony cannot start a raid");
        c.assertFalse(InvasionSystem.active(n),"A failed start does not create a raid or soundtrack session");
        c.complete();
    }
}
