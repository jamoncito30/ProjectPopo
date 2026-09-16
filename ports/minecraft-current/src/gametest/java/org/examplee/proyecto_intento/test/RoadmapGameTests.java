package org.examplee.proyecto_intento.test;

import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.block.*;
import net.minecraft.entity.EntityType;
import net.minecraft.item.*;
import net.minecraft.test.*;
import net.minecraft.util.Hand;
import net.minecraft.util.math.*;
import net.minecraft.world.GameMode;
import org.examplee.proyecto_intento.block.*;
import org.examplee.proyecto_intento.entity.*;
import org.examplee.proyecto_intento.item.*;

public final class RoadmapGameTests implements FabricGameTest {
    @GameTest(templateName="popocraft_tests:arena")
    public void progressionAdvancementsLoad(TestContext c) {
        for(String id:new String[]{"find_beetle","first_small_nest","breed_beetle","small_town","start_grand_nest","finish_grand_nest"})
            c.assertTrue(c.getWorld().getServer().getAdvancementLoader().get(net.minecraft.util.Identifier.of("proyecto_intento",id))!=null,"Progression advancement loads: "+id);
        c.complete();
    }
    @GameTest(templateName="popocraft_tests:arena",tickLimit=50)
    public void toiletSeatSingleOccupancyAndCleanup(TestContext c) {
        BlockPos p=new BlockPos(8,2,8);c.setBlockState(p,ModBlocks.INODORO);
        var abs=c.getAbsolutePos(p);var player=c.createMockPlayer(GameMode.SURVIVAL);player.setPosition(abs.toCenterPos());
        c.assertTrue(ToiletSeatEntity.sit(c.getWorld(),abs,player),"Player can sit");
        c.assertTrue(player.getVehicle() instanceof ToiletSeatEntity,"Uses real passenger seat");
        var other=c.createMockPlayer(GameMode.SURVIVAL);other.setPosition(abs.toCenterPos());
        c.assertFalse(ToiletSeatEntity.sit(c.getWorld(),abs,other),"Second player cannot occupy same toilet");
        player.stopRiding();
        c.waitAndRun(2,()->{
            c.assertTrue(c.getEntities(ModEntities.TOILET_SEAT).isEmpty(),"Empty seat removed");
            player.setPosition(abs.toCenterPos());
            c.assertTrue(ToiletSeatEntity.sit(c.getWorld(),abs,player),"Seat can be reused");
            c.setBlockState(p,Blocks.AIR);
            c.assertFalse(player.hasVehicle(),"Breaking toilet dismounts player");
            c.assertTrue(c.getEntities(ModEntities.TOILET_SEAT).isEmpty(),"Breaking removes mount");
            c.complete();
        });
    }
    @GameTest(templateName="popocraft_tests:arena")
    public void plungerUnclogsAndKnocksBack(TestContext c) {
        BlockPos p=new BlockPos(8,2,8); c.setBlockState(p,ModBlocks.INODORO.getDefaultState().with(ToiletBlock.CLOGGED,true));
        var player=c.createMockPlayer(GameMode.SURVIVAL);var tool=new ItemStack(ModItems.DESATASCADOR);player.setStackInHand(Hand.MAIN_HAND,tool);
        c.assertTrue(PlungerItem.unclog(c.getWorld(),c.getAbsolutePos(p),player,Hand.MAIN_HAND),"Plunger clears blockage");
        c.assertFalse(c.getWorld().getBlockState(c.getAbsolutePos(p)).get(ToiletBlock.CLOGGED),"Clogged state cleared");
        c.assertEquals(tool.getDamage(),1,"Unclog wears tool once");
        c.assertFalse(PlungerItem.unclog(c.getWorld(),c.getAbsolutePos(p),player,Hand.MAIN_HAND),"Clean toilet is not charged again");
        c.assertEquals(tool.getDamage(),1,"No wear when already clear");
        var cow=c.spawnEntity(EntityType.COW,10,2,10);cow.setAiDisabled(true);player.setYaw(0);
        ModItems.DESATASCADOR.postHit(tool,cow,player);
        c.assertTrue(cow.getVelocity().horizontalLength()>.4,"Plunger applies extra horizontal knockback");
        c.complete();
    }
    @GameTest(templateName="popocraft_tests:arena",tickLimit=1250)
    public void fertilizerGradualThreeByThreeAndExpiry(TestContext c) {
        for(int x=6;x<=10;x++) for(int z=6;z<=10;z++) {
            c.setBlockState(x,1,z,Blocks.FARMLAND.getDefaultState().with(FarmlandBlock.MOISTURE,7));
            c.setBlockState(x,2,z,Blocks.WHEAT);c.setBlockState(x,4,z,Blocks.GLOWSTONE);
        }
        BlockPos center=c.getAbsolutePos(new BlockPos(8,1,8));
        c.assertEquals(FertilizerItem.apply(c.getWorld(),center,false),9,"Exactly nine soils treated");
        c.assertEquals(FertilizerItem.apply(c.getWorld(),center,false),0,"No stacking active fertilizer");
        c.assertEquals(c.getWorld().getBlockState(center.up()).get(CropBlock.AGE),0,"No instant crop growth");
        c.assertTrue(c.getWorld().getBlockState(center.add(2,0,0)).isOf(Blocks.FARMLAND),"Outside 3x3 untouched");
        c.waitAndRun(205,()->{
            c.assertTrue(c.getWorld().getBlockState(center.up()).isOf(Blocks.WHEAT),"Crop survives soil conversion");
            c.assertTrue(c.getWorld().getBlockState(center.up()).get(CropBlock.AGE)>=1,"Scheduled fertilization grows crop gradually");
            c.assertEquals(c.getWorld().getBlockState(center).get(FertilizedFarmlandBlock.CHARGES),5,"One dose consumed after ten seconds");
        });
        c.waitAndRun(1210,()->{
            c.assertTrue(c.getWorld().getBlockState(center).isOf(Blocks.FARMLAND),"Returns to vanilla farmland after six doses");
            c.assertTrue(c.getWorld().getBlockState(center.up()).isOf(Blocks.WHEAT),"Crop remains after expiry");c.complete();
        });
    }
    @GameTest(templateName="popocraft_tests:arena")
    public void merchantVisualFlagPersists(TestContext c) {
        var beetle=ModEntities.DUNG_BEETLE.create(c.getWorld());beetle.setTrader(true);
        var nbt=new net.minecraft.nbt.NbtCompound();beetle.writeCustomDataToNbt(nbt);
        var copy=ModEntities.DUNG_BEETLE.create(c.getWorld());copy.readCustomDataFromNbt(nbt);
        c.assertTrue(copy.isTrader(),"Merchant visual flag survives NBT save/load");
        c.complete();
    }
}
