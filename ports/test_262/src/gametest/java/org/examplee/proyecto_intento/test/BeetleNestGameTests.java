package org.examplee.proyecto_intento.test;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtElement;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import org.examplee.proyecto_intento.block.*;
import org.examplee.proyecto_intento.entity.*;
import org.examplee.proyecto_intento.item.ModItems;

public final class BeetleNestGameTests implements FabricGameTest {
    private static final String ARENA = "popocraft_tests:arena";
    private static final BlockPos HOME = new BlockPos(8, 2, 8);
    private static void floor(TestContext c) {
        for (int x = 1; x < 31; x++) for (int z = 1; z < 31; z++) c.setBlockState(x, 1, z, Blocks.STONE);
        // Keep wandering adults inside the volume counted by TestContext.getEntities.
        for (int edge = 0; edge <= 31; edge++) for (int y = 2; y <= 4; y++) {
            c.setBlockState(0, y, edge, Blocks.STONE);
            c.setBlockState(31, y, edge, Blocks.STONE);
            c.setBlockState(edge, y, 0, Blocks.STONE);
            c.setBlockState(edge, y, 31, Blocks.STONE);
        }
    }
    private static BeetleNestBlockEntity nest(TestContext c, int stage) {
        floor(c);
        c.setBlockState(HOME, ModBlocks.BEETLE_NEST.getDefaultState().with(BeetleNestBlock.STAGE, stage));
        return (BeetleNestBlockEntity)c.getWorld().getBlockEntity(c.getAbsolutePos(HOME));
    }
    private static DungBeetleEntity resident(TestContext c, BeetleNestBlockEntity nest) {
        DungBeetleEntity beetle = c.spawnEntity(ModEntities.DUNG_BEETLE, 8.5F, 2, 7.5F);
        c.assertTrue(nest.tryEnter(beetle), "Beetle must enter completed home");
        return beetle;
    }
    @GameTest(templateName = ARENA)
    public void fiveStagesAndFoodConserveMaterial(TestContext c) {
        BeetleNestBlockEntity nest = nest(c, 1);
        DungBeetleEntity beetle = c.spawnEntity(ModEntities.DUNG_BEETLE, 8.5F, 2, 7.5F);
        c.assertFalse(nest.tryEnter(beetle), "Unfinished house cannot hold residents");
        for (int stage = 2; stage <= 5; stage++) {
            c.assertTrue(nest.addMaterial(), "Each new material advances construction");
            c.assertEquals(c.getWorld().getBlockState(c.getAbsolutePos(HOME)).get(BeetleNestBlock.STAGE), stage, "Five distinct construction stages");
        }
        for (int i = 0; i < BeetleNestBlockEntity.MAX_FOOD; i++) c.assertTrue(nest.addMaterial(), "Finished house stores food");
        c.assertFalse(nest.addMaterial(), "Full food storage must refuse another item");
        var rule = c.getWorld().getGameRules().get(GameRules.DO_MOB_GRIEFING);
        boolean before = rule.get();
        try {
            rule.set(false, c.getWorld().getServer());
            c.assertFalse(nest.addMaterial(), "Building respects mobGriefing");
        } finally { rule.set(before, c.getWorld().getServer()); }
        c.getWorld().breakBlock(c.getAbsolutePos(HOME), true);
        int loose = c.getEntities(EntityType.ITEM).stream().filter(i -> i.getStack().isOf(ModItems.POPO)).mapToInt(i -> i.getStack().getCount()).sum();
        c.assertEquals(loose, 13, "Breaking returns five building materials plus eight unconsumed food");
        c.complete();
    }
    @GameTest(templateName = ARENA, tickLimit = 900)
    public void familyPersistsAndLeavesHome(TestContext c) {
        BeetleNestBlockEntity nest = nest(c, 5);
        nest.addMaterial();
        Set<UUID> parents = Set.of(resident(c, nest).getUuid(), resident(c, nest).getUuid());
        c.waitAndRun(240, () -> {
            c.assertEquals(nest.getOccupantCount(), 3, "Two adults and one new child must be inside");
            c.assertEquals(nest.getFood(), 0, "One birth consumes exactly one food");
            var saved = nest.createNbt(c.getWorld().getRegistryManager());
            nest.read(saved, c.getWorld().getRegistryManager());
            c.assertEquals(nest.getOccupantCount(), 3, "Whole family survives NBT round trip");
            var occupants = saved.getList("Occupants", NbtElement.COMPOUND_TYPE);
            int babies = 0;
            Set<UUID> identities = new HashSet<>();
            for (int i = 0; i < occupants.size(); i++) {
                var data = occupants.getCompound(i).getCompound("Entity");
                identities.add(data.getUuid("UUID"));
                if (data.getInt("Age") < 0) babies++;
                else c.assertTrue(data.getInt("Age") > 0, "Parents have breeding cooldown");
            }
            c.assertEquals(babies, 1, "New resident is a baby");
            c.assertEquals(identities.size(), 3, "Every resident has its own identity");
            c.assertTrue(identities.containsAll(parents), "Original adults keep UUIDs");
        });
        c.waitAndRun(850, () -> {
            c.assertEquals(nest.getOccupantCount(), 0, "Adults and baby leave after their residence time");
            var visible = c.getEntities(ModEntities.DUNG_BEETLE);
            c.assertEquals(visible.size(), 3, "Release creates precisely the family");
            c.assertEquals((int)visible.stream().filter(DungBeetleEntity::isBaby).count(), 1, "Baby emerges as baby");
            c.assertTrue(visible.stream().allMatch(e -> c.getAbsolutePos(HOME).equals(e.getNestPos())), "Family remembers its home outside");
            c.complete();
        });
    }
    @GameTest(templateName = ARENA, tickLimit = 500)
    public void blockedExitsAndBreakingPreserveResidents(TestContext c) {
        BeetleNestBlockEntity nest = nest(c, 5);
        DungBeetleEntity first = resident(c, nest);
        DungBeetleEntity second = resident(c, nest);
        for (var direction : net.minecraft.util.math.Direction.values()) c.setBlockState(HOME.offset(direction), Blocks.STONE);
        c.waitAndRun(450, () -> {
            c.assertEquals(nest.getOccupantCount(), 2, "Blocked exits keep adults safely stored");
            c.getWorld().breakBlock(c.getAbsolutePos(HOME), true);
            c.assertEquals(c.getEntities(ModEntities.DUNG_BEETLE).size(), 2, "Breaking evacuates both residents even when surrounded");
            c.assertTrue(c.getWorld().getEntity(first.getUuid()) instanceof DungBeetleEntity, "First UUID preserved");
            c.assertTrue(c.getWorld().getEntity(second.getUuid()) instanceof DungBeetleEntity, "Second UUID preserved");
            c.complete();
        });
    }
    @GameTest(templateName = ARENA, tickLimit = 2600)
    public void beetlesBuildAndBreedAutonomously(TestContext c) {
        floor(c);
        c.spawnEntity(ModEntities.DUNG_BEETLE, 8, 2, 8);
        c.spawnEntity(ModEntities.DUNG_BEETLE, 10, 2, 8);
        var material = c.spawnItem(ModItems.POPO, 9, 2, 8);
        material.setStack(new ItemStack(ModItems.POPO, 6));
        c.waitAndRun(2400, () -> {
            int babies = (int)c.getEntities(ModEntities.DUNG_BEETLE).stream().filter(DungBeetleEntity::isBaby).count();
            int complete = 0;
            int population = c.getEntities(ModEntities.DUNG_BEETLE).size();
            for (BlockPos pos : BlockPos.iterate(c.getAbsolutePos(new BlockPos(1, 2, 1)), c.getAbsolutePos(new BlockPos(30, 3, 30)))) {
                if (c.getWorld().getBlockEntity(pos) instanceof BeetleNestBlockEntity nest) {
                    if (nest.isComplete()) complete++;
                    population += nest.getOccupantCount();
                    var occupants = nest.createNbt(c.getWorld().getRegistryManager()).getList("Occupants", NbtElement.COMPOUND_TYPE);
                    for (int i = 0; i < occupants.size(); i++) if (occupants.getCompound(i).getCompound("Entity").getInt("Age") < 0) babies++;
                }
            }
            c.assertEquals(complete, 1, "Nearby pair cooperates on one completed shelter");
            c.assertEquals(babies, 1, "Collected sixth poop enables a birth inside");
            c.assertEquals(population, 3, "Autonomous family contains exactly three beetles");
            c.complete();
        });
    }
    @GameTest(templateName = ARENA, tickLimit = 500)
    public void noPartnerMeansNoBirth(TestContext c) {
        BeetleNestBlockEntity nest = nest(c, 5);
        resident(c, nest);
        nest.addMaterial();
        c.waitAndRun(450, () -> {
            c.assertEquals(nest.getOccupantCount(), 1, "One beetle cannot reproduce alone");
            c.assertEquals(nest.getFood(), 1, "No partner means food is conserved");
            c.complete();
        });
    }
    @GameTest(templateName = ARENA, tickLimit = 350)
    public void noFoodMeansNoBirthAndCapacityIsLimited(TestContext c) {
        BeetleNestBlockEntity nest = nest(c, 5);
        for (int i = 0; i < BeetleNestBlockEntity.CAPACITY; i++) resident(c, nest);
        DungBeetleEntity extra = c.spawnEntity(ModEntities.DUNG_BEETLE, 8.5F, 2, 7.5F);
        extra.setAiDisabled(true);
        c.assertFalse(nest.tryEnter(extra), "Full house refuses another resident");
        c.waitAndRun(300, () -> {
            c.assertEquals(nest.getOccupantCount(), 6, "No food means no birth or missing residents");
            var data = nest.createNbt(c.getWorld().getRegistryManager()).getList("Occupants", NbtElement.COMPOUND_TYPE);
            for (int i = 0; i < data.size(); i++) c.assertTrue(data.getCompound(i).getCompound("Entity").getInt("Age") >= 0, "No unfed babies");
            c.complete();
        });
    }
    @GameTest(templateName = ARENA, tickLimit = 2300)
    public void babiesReturnAndGrownChildrenBuildNearby(TestContext c) {
        BeetleNestBlockEntity nest = nest(c, 5);
        DungBeetleEntity[] grownChild = new DungBeetleEntity[1];
        // Parents remain at home long enough to exercise the two-adult residence rule.
        resident(c, nest);
        resident(c, nest);
        DungBeetleEntity child = c.spawnEntity(ModEntities.DUNG_BEETLE, 8.5F, 2, 7.5F);
        child.setBreedingAge(-24000);
        child.setNest(c.getAbsolutePos(HOME));
        c.waitAndRun(150, () -> {
            c.assertTrue(child.isRemoved(), "Baby walks into the family shelter");
            c.assertEquals(nest.getOccupantCount(), 3, "Parents and baby share the home");
        });
        c.waitAndRun(1000, () -> {
            var returned = (DungBeetleEntity)c.getWorld().getEntity(child.getUuid());
            grownChild[0] = returned;
            c.assertTrue(returned != null && returned.isBaby(), "Same baby leaves home alive");
            returned.setBreedingAge(0); // Accelerate maturation only; construction still runs through normal AI.
            var material = c.spawnItem(ModItems.POPO, (float)(returned.getX() - c.getAbsolutePos(BlockPos.ORIGIN).getX()),
                    2, (float)(returned.getZ() - c.getAbsolutePos(BlockPos.ORIGIN).getZ()));
            material.setStack(new ItemStack(ModItems.POPO, 5));
        });
        c.waitAndRun(2100, () -> {
            int houses = 0;
            for (BlockPos pos : BlockPos.iterate(c.getAbsolutePos(new BlockPos(1, 2, 1)), c.getAbsolutePos(new BlockPos(30, 3, 30)))) {
                if (c.getWorld().getBlockEntity(pos) instanceof BeetleNestBlockEntity) houses++;
            }
            c.assertTrue(houses >= 2, "Grown offspring starts a neighboring home");
            c.assertFalse(c.getAbsolutePos(HOME).equals(grownChild[0].getNestPos()), "Grown child establishes its own home instead of displacing parents");
            c.complete();
        });
    }
    @GameTest(templateName = ARENA)
    public void rareSpawnsCoverDifferentOverworldBiomes(TestContext c) {
        var biomes = c.getWorld().getRegistryManager().get(net.minecraft.registry.RegistryKeys.BIOME);
        for (var key : java.util.List.of(net.minecraft.world.biome.BiomeKeys.DESERT,
                net.minecraft.world.biome.BiomeKeys.SNOWY_PLAINS, net.minecraft.world.biome.BiomeKeys.JUNGLE,
                net.minecraft.world.biome.BiomeKeys.PLAINS, net.minecraft.world.biome.BiomeKeys.BEACH)) {
            var entries = biomes.get(key).getSpawnSettings().getSpawnEntries(net.minecraft.entity.SpawnGroup.CREATURE).getEntries();
            var beetles = entries.stream().filter(e -> e.type == ModEntities.DUNG_BEETLE).toList();
            c.assertEquals(beetles.size(), 1, "Biome has one beetle spawn entry: " + key);
            c.assertEquals(beetles.getFirst().getWeight().getValue(), 2, "Low relative spawn weight");
            c.assertEquals(beetles.getFirst().maxGroupSize, 2, "Small natural groups");
        }
        c.assertFalse(biomes.get(net.minecraft.world.biome.BiomeKeys.NETHER_WASTES).getSpawnSettings()
                .getSpawnEntries(net.minecraft.entity.SpawnGroup.CREATURE).getEntries().stream().anyMatch(e -> e.type == ModEntities.DUNG_BEETLE), "No Nether beetles");
        c.complete();
    }
}
