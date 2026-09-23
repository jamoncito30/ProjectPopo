package org.examplee.proyecto_intento.test;

import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.examplee.proyecto_intento.block.*;
import org.examplee.proyecto_intento.item.ModItems;

public final class GrandNestGameTests implements FabricGameTest {
    @GameTest(templateName="popocraft_tests:arena")
    public void deconstructionConservesFoodAndLayers(TestContext c) {
        BlockPos p=new BlockPos(12,3,12);
        c.setBlockState(p,ModBlocks.BEETLE_NEST.getDefaultState().with(BeetleNestBlock.STAGE,5));
        var small=(BeetleNestBlockEntity)c.getWorld().getBlockEntity(c.getAbsolutePos(p));
        for(int i=0;i<8;i++) small.addMaterial();
        for(int i=0;i<8;i++) c.assertTrue(small.takeMaterialForGrandNest(),"Stored food recovered once");
        c.assertEquals(small.getFood(),0,"Store emptied");
        c.assertEquals(small.getCachedState().get(BeetleNestBlock.STAGE),5,"Food withdrawn before structural layers");
        for(int i=0;i<5;i++) c.assertTrue(small.takeMaterialForGrandNest(),"One layer recovered per trip");
        c.assertTrue(c.getWorld().getBlockState(c.getAbsolutePos(p)).isAir(),"Last layer removes shelter");
        c.assertTrue(c.getEntities(EntityType.ITEM).isEmpty(),"No duplicated loose food while recovering materials");c.complete();
    }
    @GameTest(templateName="popocraft_tests:arena")
    public void obstructedConstructionDoesNotDestroyPlayerBlocks(TestContext c) {
        var nest=place(c,1);var origin=nest.getOrigin();
        c.getWorld().setBlockState(origin.up(2),Blocks.DIAMOND_BLOCK.getDefaultState());
        var offered=new ItemStack(ModItems.POPO,64);
        c.assertEquals(nest.deposit(offered),0,"Obstruction refuses construction material");
        nest.updateStructure(4);
        c.assertTrue(c.getWorld().getBlockState(origin.up(2)).isOf(Blocks.DIAMOND_BLOCK),"Player block preserved");
        c.assertEquals(offered.getCount(),64,"Unspent material stays with carrier");c.complete();
    }
    @GameTest(templateName="popocraft_tests:arena")
    public void communalCapacityAndBreakingPreserveIdentities(TestContext c) {
        var nest=place(c,4);var abs=c.getAbsolutePos(POS);
        java.util.List<java.util.UUID> ids=new java.util.ArrayList<>();
        for(int i=0;i<30;i++) {
            var beetle=org.examplee.proyecto_intento.entity.ModEntities.DUNG_BEETLE.create(c.getWorld());
            beetle.setPosition(abs.toCenterPos());beetle.setAiDisabled(true);c.getWorld().spawnEntity(beetle);ids.add(beetle.getUuid());
            c.assertTrue(nest.tryEnter(beetle),"Thirty residents enter");
        }
        var extra=org.examplee.proyecto_intento.entity.ModEntities.DUNG_BEETLE.create(c.getWorld());extra.setPosition(abs.toCenterPos());
        c.getWorld().spawnEntity(extra);ids.add(extra.getUuid());
        c.assertTrue(nest.tryEnter(extra),"Admission no longer capped at thirty residents");
        c.waitAndRun(2,()->{
            c.setBlockState(POS,Blocks.AIR);
            c.waitAndRun(2,()->{
                for(var id:ids) c.assertTrue(c.getWorld().getEntity(id)!=null && c.getWorld().getEntity(id).isAlive(),"Every original UUID survives controller break and subsequent ticks");
                c.assertEquals(nest.getOccupantCount(),0,"No serialized duplicate residents left");c.complete();
            });
        });
    }
    @GameTest(templateName="popocraft_tests:arena")
    public void flatSiteAndTenBlockSpacing(TestContext c) {
        for(int x=1;x<31;x++) for(int z=1;z<31;z++) c.setBlockState(x,1,z,Blocks.STONE);
        BlockPos first=c.getAbsolutePos(new BlockPos(2,2,2));
        c.getWorld().setBlockState(first.add(2,0,2),ModBlocks.GRAND_BEETLE_NEST_CONTROLLER.getDefaultState());
        var nest=(GrandBeetleNestBlockEntity)c.getWorld().getBlockEntity(first.add(2,0,2));nest.setOrigin(first);
        c.assertFalse(GrandBeetleNestBlockEntity.isValidSite(c.getWorld(),first.add(14,0,0)),"Nine empty blocks between edges is insufficient");
        c.assertTrue(GrandBeetleNestBlockEntity.isValidSite(c.getWorld(),first.add(15,0,0)),"Ten empty blocks between edges is valid");
        c.getWorld().setBlockState(first.add(15,-1,0),Blocks.AIR.getDefaultState());
        c.assertFalse(GrandBeetleNestBlockEntity.isValidSite(c.getWorld(),first.add(15,0,0)),"Hole in foundation rejects terrain");c.complete();
    }
    private static final BlockPos POS = new BlockPos(8,3,8);
    private static GrandBeetleNestBlockEntity place(TestContext c, int stage) {
        c.setBlockState(POS, ModBlocks.GRAND_BEETLE_NEST_CONTROLLER.getDefaultState().with(GrandBeetleNestPieceBlock.STAGE,stage));
        return (GrandBeetleNestBlockEntity)c.getWorld().getBlockEntity(c.getAbsolutePos(POS));
    }
    @GameTest(templateName="popocraft_tests:arena")
    public void capacityPersistenceAndTransactionalDeposit(TestContext c) {
        var nest=place(c,4);
        int accepted=0, remaining=0;
        for(int i=0;i<6;i++) {
            var stack=new ItemStack(ModItems.POPO,64);
            accepted+=nest.deposit(stack); remaining+=stack.getCount();
        }
        c.assertEquals(accepted,360,"Accepts exactly 360");
        c.assertEquals(remaining,24,"Overflow stays with carrier");
        var overflow=new ItemStack(ModItems.POPO,1);
        c.assertEquals(nest.deposit(overflow),0,"Full store rejects further food");
        c.assertEquals(overflow.getCount(),1,"Rejected item is conserved");
        var stone=new ItemStack(Items.STONE,3);
        c.assertEquals(nest.deposit(stone),0,"Rejects non-poop");
        c.assertEquals(stone.getCount(),3,"Invalid input is untouched");
        var saved=nest.createNbt(c.getWorld().getRegistryManager());
        var loaded=new GrandBeetleNestBlockEntity(c.getAbsolutePos(POS),nest.getCachedState());
        loaded.read(saved,c.getWorld().getRegistryManager());
        c.assertEquals(loaded.getFoodCount(),360,"NBT round trip preserves food");
        c.assertFalse(nest.canInsert(0,overflow,Direction.UP),"Hoppers cannot feed store");
        c.complete();
    }
    @GameTest(templateName="popocraft_tests:arena",tickLimit=100)
    public void realHopperExtractsOnlyWhenComplete(TestContext c) {
        c.setBlockState(POS.down(),Blocks.HOPPER);
        var nest=place(c,3);
        nest.setStack(0,new ItemStack(ModItems.POPO,12));
        var hopper=(HopperBlockEntity)c.getWorld().getBlockEntity(c.getAbsolutePos(POS.down()));
        var offered=new ItemStack(ModItems.POPO);
        c.assertEquals(nest.deposit(offered), 1, "Construction receives popo for budget");
        c.waitAndRun(20,()->{
            c.assertTrue(hopper.isEmpty(),"Incomplete shelter blocks hopper extraction");
            var completed=place(c,4);
            c.assertEquals(completed.getFoodCount(),12,"Stage change preserves block entity inventory");
            c.waitAndRun(20,()->{
                int extracted=0;
                for(int i=0;i<hopper.size();i++) extracted+=hopper.getStack(i).getCount();
                c.assertTrue(extracted>0,"Actual lower hopper extracts poop");
                c.assertEquals(extracted+completed.getFoodCount(),12,"Hopper conserves total items");
                c.complete();
            });
        });
    }
    @GameTest(templateName="popocraft_tests:arena")
    public void breakingControllerReturnsAllStoredFood(TestContext c) {
        var nest=place(c,4);
        for(int i=0;i<6;i++) nest.deposit(new ItemStack(ModItems.POPO,64));
        c.setBlockState(POS,Blocks.AIR);
        int dropped=c.getEntities(EntityType.ITEM).stream().filter(e->e.getStack().isOf(ModItems.POPO)).mapToInt(e->e.getStack().getCount()).sum();
        c.assertEquals(dropped,360,"Breaking controller drops all stored food");
        c.assertEquals(nest.getFoodCount(),0,"Detached inventory is empty to prevent duplicate drops");
        c.complete();
    }
}
