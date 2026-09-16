package org.examplee.proyecto_intento.test;

import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import org.examplee.proyecto_intento.block.ModBlocks;
import org.examplee.proyecto_intento.block.WetPopoBlock;
import org.examplee.proyecto_intento.item.ModArmor;
import org.examplee.proyecto_intento.item.ModItems;

public final class PopoCraftGameTests implements FabricGameTest {
    private static final String ARENA = "popocraft_tests:arena";

    private static int drops(TestContext context, Vec3d center) {
        return context.getWorld().getEntitiesByClass(ItemEntity.class, new Box(center, center).expand(6),
                item -> item.getStack().isOf(ModItems.POPO)).stream().mapToInt(item -> item.getStack().getCount()).sum();
    }

    @GameTest(templateName = ARENA)
    public void periodicDropAndPersistence(TestContext context) {
        CowEntity cow = context.spawnEntity(EntityType.COW, 3, 2, 3);
        NbtCompound data = new NbtCompound();
        cow.writeCustomDataToNbt(data);
        data.putInt("PopoCraftDropTicks", 1);
        cow.readCustomDataFromNbt(data);
        cow.tickMovement();
        context.assertEquals(drops(context, cow.getPos()), 1, "An adult must drop exactly one poop at expiry");
        cow.writeCustomDataToNbt(data);
        int reset = data.getInt("PopoCraftDropTicks");
        context.assertTrue(reset >= 6000 && reset <= 12000, "Timer must reset to 5-10 minutes");
        CowEntity restored = EntityType.COW.create(context.getWorld());
        restored.readCustomDataFromNbt(data);
        NbtCompound restoredData = new NbtCompound();
        restored.writeCustomDataToNbt(restoredData);
        context.assertEquals(restoredData.getInt("PopoCraftDropTicks"), reset, "Timer must survive save/load");
        cow.setBreedingAge(-1000);
        data.putInt("Age", -1000);
        data.putInt("PopoCraftDropTicks", 1);
        cow.readCustomDataFromNbt(data);
        cow.tickMovement();
        context.assertEquals(drops(context, cow.getPos()), 1, "Babies must not produce periodic drops");
        context.complete();
    }

    @GameTest(templateName = ARENA)
    public void acceptedFoodChanceAndRejectedClicks(TestContext context) {
        CowEntity cow = context.spawnEntity(EntityType.COW, 3, 2, 3);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        cow.getRandom().setSeed(49231L);
        for (int i = 0; i < 400; i++) {
            cow.setBreedingAge(0);
            cow.resetLoveTicks();
            player.setStackInHand(Hand.MAIN_HAND, new ItemStack(Items.WHEAT));
            cow.interactMob(player, Hand.MAIN_HAND);
            context.assertTrue(player.getMainHandStack().isEmpty(), "Accepted food must be consumed");
        }
        int accepted = drops(context, cow.getPos());
        context.assertTrue(accepted >= 70 && accepted <= 130, "400 accepted feeds should yield approximately 25%, got " + accepted);
        player.setStackInHand(Hand.MAIN_HAND, new ItemStack(Items.STICK));
        for (int i = 0; i < 100; i++) cow.interactMob(player, Hand.MAIN_HAND);
        context.assertEquals(drops(context, cow.getPos()), accepted, "Rejected food must not roll drops");
        context.complete();
    }

    @GameTest(templateName = ARENA, tickLimit = 300)
    public void recipesAndFurnace(TestContext context) {
        for (String recipe : new String[]{"wet_popo", "dry_popo", "popo_helmet", "popo_chestplate", "popo_leggings", "popo_boots"}) {
            context.assertTrue(context.getWorld().getRecipeManager().get(Identifier.of("proyecto_intento", recipe)).isPresent(), "Recipe must load: " + recipe);
        }
        BlockPos furnacePos = new BlockPos(2, 2, 2);
        context.setBlockState(furnacePos, Blocks.FURNACE);
        AbstractFurnaceBlockEntity furnace = (AbstractFurnaceBlockEntity) context.getBlockEntity(furnacePos);
        furnace.setStack(0, new ItemStack(ModBlocks.WET_POPO));
        furnace.setStack(1, new ItemStack(Items.COAL));
        context.waitAndRun(220, () -> {
            context.assertTrue(furnace.getStack(2).isOf(ModBlocks.DRY_POPO.asItem()), "Furnace must produce dry poop");
            context.assertEquals(furnace.getStack(2).getCount(), 1, "Furnace output count");
            context.complete();
        });
    }

    @GameTest(templateName = ARENA)
    public void wetBlockAirAndEffects(TestContext context) {
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        Vec3d pos = context.getAbsolute(new Vec3d(3.5, 2, 3.5));
        player.refreshPositionAndAngles(pos.x, pos.y, pos.z, 0, 0);
        BlockPos feet = player.getBlockPos();
        context.getWorld().setBlockState(feet, ModBlocks.WET_POPO.getDefaultState());
        context.assertFalse(WetPopoBlock.coversEyes(player), "A single block must not cover standing eyes");
        ModBlocks.WET_POPO.getDefaultState().onEntityCollision(context.getWorld(), feet, player);
        context.assertTrue(player.hasStatusEffect(StatusEffects.NAUSEA), "Contact must cause nausea");
        context.assertTrue(player.hasStatusEffect(StatusEffects.MINING_FATIGUE), "Contact must slow mining");
        context.getWorld().setBlockState(feet.up(), ModBlocks.WET_POPO.getDefaultState());
        context.assertTrue(WetPopoBlock.coversEyes(player), "Two blocks must cover eyes");
        player.setAir(10);
        float health = player.getHealth();
        for (int i = 0; i < 30; i++) player.baseTick();
        context.assertTrue(player.getHealth() < health, "Full submersion must cause vanilla drowning damage");
        context.getWorld().setBlockState(feet.up(), Blocks.AIR.getDefaultState());
        for (int i = 0; i < 10; i++) player.baseTick();
        context.assertTrue(player.getAir() > 0, "Air must recover after emerging");
        context.complete();
    }

    private static void equip(PlayerEntity player) {
        player.equipStack(EquipmentSlot.HEAD, new ItemStack(ModArmor.HELMET));
        player.equipStack(EquipmentSlot.CHEST, new ItemStack(ModArmor.CHESTPLATE));
        player.equipStack(EquipmentSlot.LEGS, new ItemStack(ModArmor.LEGGINGS));
        player.equipStack(EquipmentSlot.FEET, new ItemStack(ModArmor.BOOTS));
    }

    @GameTest(templateName = ARENA)
    public void specialAnimalFeeding(TestContext context) {
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        HorseEntity horse = context.spawnEntity(EntityType.HORSE, 3, 2, 3);
        WolfEntity wolf = context.spawnEntity(EntityType.WOLF, 20, 2, 20);
        wolf.setTamed(true, true);
        horse.getRandom().setSeed(561L);
        wolf.getRandom().setSeed(817L);
        for (int i = 0; i < 200; i++) {
            horse.setHealth(1);
            horse.interactHorse(player, new ItemStack(Items.WHEAT));
            wolf.setHealth(1);
            player.setStackInHand(Hand.MAIN_HAND, new ItemStack(Items.BEEF));
            wolf.interactMob(player, Hand.MAIN_HAND);
        }
        int horseDrops = drops(context, horse.getPos());
        int wolfDrops = drops(context, wolf.getPos());
        context.assertTrue(horseDrops >= 30 && horseDrops <= 70, "Horse accepted feeds should roll 25%: " + horseDrops);
        context.assertTrue(wolfDrops >= 30 && wolfDrops <= 70, "Wolf healing food should roll 25%: " + wolfDrops);
        context.complete();
    }

    @GameTest(templateName = ARENA)
    public void gradualSinking(TestContext context) {
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        Vec3d pos = context.getAbsolute(new Vec3d(3.5, 3.01, 3.5));
        player.refreshPositionAndAngles(pos.x, pos.y, pos.z, 0, 0);
        context.setBlockState(3, 1, 3, Blocks.STONE);
        context.setBlockState(3, 2, 3, ModBlocks.WET_POPO);
        double startY = player.getY();
        for (int i = 0; i < 60; i++) player.tickMovement();
        double sunk = startY - player.getY();
        context.assertTrue(sunk > 0.1 && sunk < 1.0, "Player should sink gradually, not drop through instantly: " + sunk);
        context.complete();
    }

    @GameTest(templateName = ARENA, tickLimit = 160)
    public void fullArmorRepelsPassiveHostileAndBrainMobs(TestContext context) {
        for (int x = 1; x <= 30; x++) for (int z = 1; z <= 30; z++) context.setBlockState(x, 1, z, Blocks.STONE);
        PlayerEntity player = context.createMockCreativeServerPlayerInWorld();
        Vec3d pos = context.getAbsolute(new Vec3d(16, 2, 16));
        player.refreshPositionAndAngles(pos.x, pos.y, pos.z, 0, 0);
        equip(player);
        context.assertTrue(ModArmor.hasFullSet(player), "Full armor must enable smell");
        MobEntity[] mobs = { context.spawnEntity(EntityType.COW, 19, 2, 16),
                context.spawnEntity(EntityType.HUSK, 13, 2, 16), context.spawnEntity(EntityType.VILLAGER, 16, 2, 19),
                context.spawnEntity(EntityType.SLIME, 16, 2, 13) };
        context.waitAndRun(80, () -> {
            for (MobEntity mob : mobs) context.assertTrue(mob.squaredDistanceTo(player) > 36,
                    "Mob must run away from armor: " + mob.getType());
            player.equipStack(EquipmentSlot.HEAD, ItemStack.EMPTY);
            context.assertFalse(ModArmor.hasFullSet(player), "Removing one piece must disable smell");
            player.discard();
            context.complete();
        });
    }
}
