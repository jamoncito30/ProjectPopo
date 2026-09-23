package org.examplee.proyecto_intento.test;

import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.block.Blocks;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.test.*;
import net.minecraft.util.math.*;
import org.examplee.proyecto_intento.block.*;
import org.examplee.proyecto_intento.entity.*;

public final class InvasionGameTests implements FabricGameTest {
    private GrandBeetleNestBlockEntity nest(TestContext c) {
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
}
