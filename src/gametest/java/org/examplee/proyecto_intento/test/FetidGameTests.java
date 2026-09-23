package org.examplee.proyecto_intento.test;

import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.block.*;
import net.minecraft.block.entity.BrewingStandBlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.potion.Potions;
import net.minecraft.test.*;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.*;
import net.minecraft.world.GameMode;
import org.examplee.proyecto_intento.block.*;
import org.examplee.proyecto_intento.entity.*;
import org.examplee.proyecto_intento.item.*;

public final class FetidGameTests implements FabricGameTest {
    private static final String ARENA="popocraft_tests:arena";
    private static void floor(TestContext c) { for(int x=1;x<31;x++)for(int z=1;z<31;z++)c.setBlockState(x,1,z,Blocks.STONE); }
    @GameTest(templateName=ARENA)
    public void sewageBucketPlacementAndCollectionConserveContainer(TestContext c) {
        floor(c);var player=c.createMockPlayer(GameMode.SURVIVAL);player.setPosition(c.getAbsolute(new Vec3d(8,2,7)));
        player.setStackInHand(Hand.MAIN_HAND,new ItemStack(ModItems.SEWAGE_BUCKET));
        var floor=c.getAbsolutePos(new BlockPos(8,1,8));var hit=new BlockHitResult(floor.toCenterPos().add(0,.5,0),Direction.UP,floor,false);
        var result=ModItems.SEWAGE_BUCKET.useOnBlock(new ItemUsageContext(player,Hand.MAIN_HAND,hit));
        c.assertTrue(result.isAccepted(),"Sewage bucket places contents");
        c.assertTrue(player.getMainHandStack().isOf(Items.BUCKET),"Placement returns exactly one empty bucket");
        var pos=floor.up();var state=c.getWorld().getBlockState(pos);c.assertTrue(state.isOf(ModBlocks.SEWAGE),"Sewage block placed");
        state.onUseWithItem(player.getMainHandStack(),c.getWorld(),player,Hand.MAIN_HAND,new BlockHitResult(pos.toCenterPos(),Direction.UP,pos,false));
        c.assertTrue(c.getWorld().getBlockState(pos).isAir(),"Collection removes sewage");
        c.assertTrue(player.getMainHandStack().isOf(ModItems.SEWAGE_BUCKET) && player.getMainHandStack().getCount()==1,"Collection returns one filled bucket");c.complete();
    }
    @GameTest(templateName=ARENA,tickLimit=20)
    public void occupiedToiletProducesAtPersistedThreshold(TestContext c) {
        floor(c);var p=new BlockPos(8,2,8);c.setBlockState(p,ModBlocks.INODORO);var abs=c.getAbsolutePos(p);
        var toilet=(ToiletBlockEntity)c.getWorld().getBlockEntity(abs);var nbt=toilet.createNbt(c.getWorld().getRegistryManager());nbt.putInt("UseTicks",17999);toilet.read(nbt,c.getWorld().getRegistryManager()); c.getWorld().getServer().setDifficulty(net.minecraft.world.Difficulty.NORMAL, true); c.getWorld().getGameRules().get(net.minecraft.world.GameRules.DO_MOB_SPAWNING).set(true, c.getWorld().getServer());
        var player=c.createMockPlayer(GameMode.SURVIVAL);player.setPosition(abs.toCenterPos());c.assertTrue(ToiletSeatEntity.sit(c.getWorld(),abs,player),"Occupied seat");
        c.waitAndRun(2,()->{int count=c.getEntities(EntityType.ITEM).stream().filter(e->e.getStack().isOf(ModItems.POPO)).mapToInt(e->e.getStack().getCount()).sum();
            c.assertEquals(count,8,"Exactly eight poop at accumulated fifteen minute threshold");player.stopRiding();c.complete();});
    }
    @GameTest(templateName=ARENA)
    public void slimeSizeHealthAndAiSurviveReload(TestContext c) {
        var slime=c.spawnEntity(ModEntities.FETID_SLIME,8,2,8);slime.setHealth(7);
        var nbt=new NbtCompound();slime.writeNbt(nbt);var loaded=ModEntities.FETID_SLIME.create(c.getWorld());loaded.readNbt(nbt);
        c.assertEquals(loaded.getSize(),2,"Fixed medium size persists");c.assertTrue(loaded.getMaxHealth()==16 && loaded.getHealth()==7,"Saved health is not healed on reload");
        c.assertFalse(loaded.isAiDisabled(),"Hostile slime AI stays enabled");c.complete();
    }
    @GameTest(templateName=ARENA,tickLimit=240)
    public void slimeDropsBiomassWithoutSplittingAndCloudExpires(TestContext c) {
        floor(c);
        var slime=c.spawnEntity(ModEntities.FETID_SLIME,8,2,8);slime.setAiDisabled(true);
        var cow=c.spawnEntity(EntityType.COW,9,2,8);cow.addStatusEffect(new net.minecraft.entity.effect.StatusEffectInstance(net.minecraft.entity.effect.StatusEffects.SLOWNESS, 100, 10, false, false));
        slime.damage(c.getWorld().getDamageSources().genericKill(),100);
        c.waitAndRun(30,()->{
            c.assertTrue(c.getEntities(ModEntities.FETID_SLIME).isEmpty(),"Dead slime does not split");
            int count=c.getEntities(EntityType.ITEM).stream().filter(e->e.getStack().isOf(ModItems.VISCOUS_BIOMASS)).mapToInt(e->e.getStack().getCount()).sum();
            c.assertTrue(count>=1 && count<=3,"One biomass loot roll, 1 to 3 units");
            c.assertEquals(c.getEntities(EntityType.AREA_EFFECT_CLOUD).size(),1,"One toxic cloud");
            c.assertTrue(cow.hasStatusEffect(StatusEffects.POISON),"Cloud poisons nearby living target");
            c.assertTrue(c.getBlockState(new BlockPos(8,1,8)).isOf(Blocks.STONE),"Burst does not destroy terrain");
        });
        c.waitAndRun(215,()->{c.assertTrue(c.getEntities(EntityType.AREA_EFFECT_CLOUD).isEmpty(),"Toxic cloud expires");c.complete();});
    }
    @GameTest(templateName=ARENA,tickLimit=140)
    public void toiletInfestationPersistsSpawnsAndResets(TestContext c) {
        floor(c);var p=new BlockPos(15,2,15);c.setBlockState(p,ModBlocks.INODORO.getDefaultState().with(ToiletBlock.CLOGGED,true));
        var toilet=(ToiletBlockEntity)c.getWorld().getBlockEntity(c.getAbsolutePos(p));
        var nbt=toilet.createNbt(c.getWorld().getRegistryManager());nbt.putInt("CloggedTicks",ToiletBlockEntity.INFESTATION_TICKS-1);
        toilet.read(nbt,c.getWorld().getRegistryManager()); c.getWorld().getServer().setDifficulty(net.minecraft.world.Difficulty.NORMAL, true); c.getWorld().getGameRules().get(net.minecraft.world.GameRules.DO_MOB_SPAWNING).set(true, c.getWorld().getServer());
        var copy=new ToiletBlockEntity(c.getAbsolutePos(p),toilet.getCachedState());copy.read(toilet.createNbt(c.getWorld().getRegistryManager()),c.getWorld().getRegistryManager());
        c.assertEquals(copy.getCloggedTicks(),71999,"Clog timer survives NBT roundtrip");
        c.waitAndRun(120,()->{
            c.assertEquals(c.getEntities(ModEntities.FETID_SLIME).size(),1,"Neglected toilet spawns one slime");
            c.assertTrue(toilet.getCloggedTicks()<120,"Successful spawn resets timer");
            var player=c.createMockPlayer(GameMode.SURVIVAL);player.setStackInHand(Hand.MAIN_HAND,new ItemStack(ModItems.TOXIC_PLUNGER));
            c.assertTrue(PlungerItem.unclog(c.getWorld(),c.getAbsolutePos(p),player,Hand.MAIN_HAND),"Toxic plunger also cleans");
            c.assertEquals(toilet.getCloggedTicks(),0,"Cleaning clears persisted infestation immediately");
            c.complete();
        });
    }
    @GameTest(templateName=ARENA,tickLimit=120)
    public void blockedToiletDoesNotConsumeSpawnTimer(TestContext c) {
        floor(c);var p=new BlockPos(15,2,15);c.setBlockState(p,ModBlocks.INODORO.getDefaultState().with(ToiletBlock.CLOGGED,true));
        for(var d:Direction.Type.HORIZONTAL){c.setBlockState(p.offset(d),Blocks.STONE);c.setBlockState(p.offset(d).up(),Blocks.STONE);}
        var toilet=(ToiletBlockEntity)c.getWorld().getBlockEntity(c.getAbsolutePos(p));
        var nbt=toilet.createNbt(c.getWorld().getRegistryManager());nbt.putInt("CloggedTicks",72000);toilet.read(nbt,c.getWorld().getRegistryManager()); c.getWorld().getServer().setDifficulty(net.minecraft.world.Difficulty.NORMAL, true); c.getWorld().getGameRules().get(net.minecraft.world.GameRules.DO_MOB_SPAWNING).set(true, c.getWorld().getServer());
        c.waitAndRun(105,()->{c.assertTrue(c.getEntities(ModEntities.FETID_SLIME).isEmpty(),"Blocked exits do not spawn inside blocks");c.assertEquals(toilet.getCloggedTicks(),72000,"Retry retains earned infestation");c.complete();});
    }
    @GameTest(templateName=ARENA)
    public void toxicWeaponSlowsForThreeSeconds(TestContext c) {
        var cow=c.spawnEntity(EntityType.COW,8,2,8);cow.addStatusEffect(new net.minecraft.entity.effect.StatusEffectInstance(net.minecraft.entity.effect.StatusEffects.SLOWNESS, 100, 10, false, false));var player=c.createMockPlayer(GameMode.SURVIVAL);
        ModItems.TOXIC_PLUNGER.postHit(new ItemStack(ModItems.TOXIC_PLUNGER),cow,player);
        var effect=cow.getStatusEffect(StatusEffects.SLOWNESS);
        c.assertTrue(effect!=null && effect.getAmplifier()==3 && effect.getDuration()==60,"Slowness IV for exactly 60 ticks");c.complete();
    }
    @GameTest(templateName=ARENA,tickLimit=450)
    public void pestilenceBrewsDrinksAndConverts(TestContext c) {
        c.setBlockState(5,1,5,Blocks.STONE);c.setBlockState(5,2,5,Blocks.BREWING_STAND);
        var stand=(BrewingStandBlockEntity)c.getWorld().getBlockEntity(c.getAbsolutePos(new BlockPos(5,2,5)));
        stand.setStack(0,PotionContentsComponent.createStack(Items.POTION,Potions.AWKWARD));stand.setStack(3,new ItemStack(ModItems.VISCOUS_BIOMASS));stand.setStack(4,new ItemStack(Items.BLAZE_POWDER));
        c.waitAndRun(410,()->{
            var bottle=stand.getStack(0);c.assertTrue(bottle.get(DataComponentTypes.POTION_CONTENTS).matches(ModPotions.PESTILENCE),"Biomass brews pestilence");
            var recipes=c.getWorld().getBrewingRecipeRegistry();var splash=recipes.craft(new ItemStack(Items.GUNPOWDER),bottle);
            c.assertTrue(splash.isOf(Items.SPLASH_POTION) && splash.get(DataComponentTypes.POTION_CONTENTS).matches(ModPotions.PESTILENCE),"Vanilla splash conversion");
            var player=c.createMockPlayer(GameMode.SURVIVAL);var returned=bottle.copy().finishUsing(c.getWorld(),player);
            c.assertTrue(returned.isOf(Items.GLASS_BOTTLE) && player.hasStatusEffect(StatusEffects.NAUSEA),"Drink grants nausea and returns glass");c.complete();
        });
    }
    @GameTest(templateName=ARENA,tickLimit=40)
    public void sewagePoisonsAndPreventsJump(TestContext c) {
        floor(c);c.setBlockState(8,2,8,ModBlocks.SEWAGE);var cow=c.spawnEntity(EntityType.COW,8.5F,2.5F,8.5F);cow.addStatusEffect(new net.minecraft.entity.effect.StatusEffectInstance(net.minecraft.entity.effect.StatusEffects.SLOWNESS, 100, 10, false, false));
        c.waitAndRun(20,()->{
            c.assertTrue(cow.hasStatusEffect(StatusEffects.POISON),"Sewage collision applies poison");
            cow.setVelocity(Vec3d.ZERO);cow.jump();c.assertTrue(cow.getVelocity().y<=0,"Sewage cancels jumping");c.complete();
        });
    }
    @GameTest(templateName=ARENA)
    public void torchStopsEntryAllowsExitAndRemovalRestoresMovement(TestContext c) {
        floor(c);var p=new BlockPos(15,2,15);c.setBlockState(p,ModBlocks.PESTILENT_TORCH);
        var center=c.getAbsolutePos(p).toCenterPos();var zombie=c.spawnEntity(EntityType.HUSK,26,2,15);zombie.setAiDisabled(true);
        zombie.setPosition(center.add(11,0,0));zombie.move(MovementType.SELF,new Vec3d(-3,0,0));
        c.assertTrue(zombie.getX()>=center.x+10,"Actual hostile movement cannot enter radius ten");
        zombie.setPosition(center.add(5,0,0));zombie.move(MovementType.SELF,new Vec3d(1,0,0));c.assertTrue(zombie.getX()>center.x+5,"Hostiles already inside can exit");
        c.setBlockState(p,Blocks.AIR);zombie.setPosition(center.add(11,0,0));zombie.move(MovementType.SELF,new Vec3d(-3,0,0));
        c.assertTrue(zombie.getX()<center.x+10,"Removing torch removes barrier immediately");c.complete();
    }
}
