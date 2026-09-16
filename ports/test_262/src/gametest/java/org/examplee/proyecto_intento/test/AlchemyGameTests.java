package org.examplee.proyecto_intento.test;

import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BrewingStandBlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potions;
import net.minecraft.sound.SoundEvent;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.examplee.proyecto_intento.entity.*;
import org.examplee.proyecto_intento.item.*;

public final class AlchemyGameTests implements FabricGameTest {
    private static final String ARENA = "popocraft_tests:arena";
    private static void floor(TestContext c) {
        for (int x=1;x<31;x++) for (int z=1;z<31;z++) c.setBlockState(x,1,z,Blocks.STONE);
    }
    @GameTest(templateName = ARENA)
    public void effectCommandAndSuggestionsWork(TestContext c) throws Exception {
        var player = c.createMockPlayer(GameMode.SURVIVAL);
        var manager = c.getWorld().getServer().getCommandManager();
        var source = c.getWorld().getServer().getCommandSource().withWorld(c.getWorld()).withEntity(player).withLevel(4);
        var dispatcher = manager.getDispatcher();
        var suggestions = dispatcher.getCompletionSuggestions(dispatcher.parse("effect give @s proyecto_intento:", source)).get();
        c.assertTrue(suggestions.getList().stream().anyMatch(s -> s.getText().equals("proyecto_intento:stinky")), "Registered effect appears in command completion");
        manager.executeWithPrefix(source, "effect give @s proyecto_intento:stinky 30 0");
        c.assertTrue(player.hasStatusEffect(ModEffects.STINKY), "Command grants the effect to a player");
        c.assertEquals(player.getStatusEffect(ModEffects.STINKY).getDuration(), 600, "Command duration is honored");
        c.assertTrue(SmellSystem.isSmelly(player), "Effect alone makes an unarmored player smell");
        manager.executeWithPrefix(source, "effect clear @s proyecto_intento:stinky");
        c.assertFalse(SmellSystem.isSmelly(player), "Command can remove the effect");
        c.complete();
    }
    @GameTest(templateName = ARENA, tickLimit = 450)
    public void realBrewingAndVanillaConversions(TestContext c) {
        c.setBlockState(5,1,5,Blocks.STONE);
        c.setBlockState(5,2,5,Blocks.BREWING_STAND);
        var stand = (BrewingStandBlockEntity)c.getWorld().getBlockEntity(c.getAbsolutePos(new BlockPos(5,2,5)));
        for(int slot=0;slot<3;slot++) stand.setStack(slot,PotionContentsComponent.createStack(Items.POTION,Potions.AWKWARD));
        stand.setStack(3,new ItemStack(ModItems.POPO));
        stand.setStack(4,new ItemStack(Items.BLAZE_POWDER));
        c.waitAndRun(410, () -> {
            for(int slot=0;slot<3;slot++) c.assertTrue(stand.getStack(slot).get(DataComponentTypes.POTION_CONTENTS).matches(ModPotions.BOTTLED_FART), "Real brewing stand produces three bottles");
            c.assertTrue(stand.getStack(3).isEmpty(), "Brewing consumes one poop");
            var recipes=c.getWorld().getBrewingRecipeRegistry();
            var splash=recipes.craft(new ItemStack(Items.GUNPOWDER),stand.getStack(0));
            c.assertTrue(splash.isOf(Items.SPLASH_POTION) && splash.get(DataComponentTypes.POTION_CONTENTS).matches(ModPotions.BOTTLED_FART), "Gunpowder makes a splash fart bottle");
            var lingering=recipes.craft(new ItemStack(Items.DRAGON_BREATH),splash);
            c.assertTrue(lingering.isOf(Items.LINGERING_POTION) && lingering.get(DataComponentTypes.POTION_CONTENTS).matches(ModPotions.BOTTLED_FART), "Dragon breath makes lingering fart");
            c.complete();
        });
    }
    @GameTest(templateName = ARENA)
    public void drinkingPotionGrantsPesteAndReturnsGlass(TestContext c) {
        var player=c.createMockPlayer(GameMode.SURVIVAL);
        var bottle=PotionContentsComponent.createStack(Items.POTION,ModPotions.BOTTLED_FART);
        var result=bottle.finishUsing(c.getWorld(),player);
        c.assertTrue(result.isOf(Items.GLASS_BOTTLE), "Survival drinking returns glass bottle");
        c.assertTrue(player.hasStatusEffect(ModEffects.STINKY), "Drinking grants registered stink");
        c.assertEquals(player.getStatusEffect(ModEffects.STINKY).getDuration(),600, "Peste lasts thirty seconds");
        c.assertTrue(SmellSystem.isSmelly(player), "Player becomes a smell source without armor");
        c.complete();
    }
    @GameTest(templateName = ARENA, tickLimit = 100)
    public void splashAndLingeringApplyActualEffects(TestContext c) {
        floor(c);
        CowEntity splashTarget=c.spawnEntity(EntityType.COW,8.5F,2,8.5F);
        CowEntity cloudTarget=c.spawnEntity(EntityType.COW,22.5F,2,22.5F);
        splashTarget.setAiDisabled(true); cloudTarget.setAiDisabled(true);
        PotionEntity splash=new PotionEntity(c.getWorld(),0,0,0);
        splash.setPosition(c.getAbsolute(new Vec3d(8.5,4,8.5)));
        splash.setItem(PotionContentsComponent.createStack(Items.SPLASH_POTION,ModPotions.BOTTLED_FART));
        splash.setVelocity(0,-.5,0); c.getWorld().spawnEntity(splash);
        PotionEntity lingering=new PotionEntity(c.getWorld(),0,0,0);
        // Hit the floor beside the cow: a direct head impact leaves vanilla's cloud above it.
        lingering.setPosition(c.getAbsolute(new Vec3d(24.5,4,22.5)));
        lingering.setItem(PotionContentsComponent.createStack(Items.LINGERING_POTION,ModPotions.BOTTLED_FART));
        lingering.setVelocity(0,-.5,0); c.getWorld().spawnEntity(lingering);
        c.waitAndRun(45, () -> {
            c.assertTrue(splashTarget.hasStatusEffect(ModEffects.STINKY), "Actual splash collision applies peste");
            c.assertTrue(cloudTarget.hasStatusEffect(ModEffects.STINKY), "Actual lingering cloud applies peste");
            c.assertTrue(splash.isRemoved() && lingering.isRemoved(), "Thrown bottles are consumed");
            c.complete();
        });
    }
    private static final class AudibleCow extends CowEntity {
        int farts;
        AudibleCow(World world) { super(EntityType.COW,world); }
        @Override public void playSound(SoundEvent sound,float volume,float pitch) {
            if(sound==ModSounds.FART) farts++;
            super.playSound(sound,volume,pitch);
        }
    }
    @GameTest(templateName = ARENA)
    public void everySuccessfulDropHasOneFart(TestContext c) {
        AudibleCow cow=new AudibleCow(c.getWorld());
        cow.setPosition(c.getAbsolute(new Vec3d(8,2,8)));
        for(int i=0;i<3;i++) PopoDrops.drop(cow);
        c.assertEquals(cow.farts,3,"Exactly one fart per produced poop");
        int count=c.getEntities(EntityType.ITEM).stream().filter(i->i.getStack().isOf(ModItems.POPO)).mapToInt(i->i.getStack().getCount()).sum();
        c.assertEquals(count,3,"Sound does not change item production");
        cow.setHealth(0);
        PopoDrops.drop(cow);
        c.assertEquals(cow.farts,3,"Dead animals produce neither poop nor fart");
        c.complete();
    }
}
