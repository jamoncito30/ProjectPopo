package org.examplee.proyecto_intento.test;

import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.block.Blocks;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import org.examplee.proyecto_intento.block.ModBlocks;
import org.examplee.proyecto_intento.block.PopoPileBlock;
import org.examplee.proyecto_intento.block.BeetleNestBlock;
import org.examplee.proyecto_intento.block.BeetleNestBlockEntity;
import org.examplee.proyecto_intento.entity.DungBeetleEntity;
import org.examplee.proyecto_intento.entity.ModEffects;
import org.examplee.proyecto_intento.entity.ModEntities;
import org.examplee.proyecto_intento.entity.PopoProjectileEntity;
import org.examplee.proyecto_intento.entity.SmellSystem;
import org.examplee.proyecto_intento.item.ModArmor;
import org.examplee.proyecto_intento.item.ModItems;

public final class PopoExpansionGameTests implements FabricGameTest {
    private static final String ARENA = "popocraft_tests:arena";
    private static void floor(TestContext c) {
        for (int x = 1; x < 31; x++) for (int z = 1; z < 31; z++) c.setBlockState(x, 1, z, Blocks.STONE);
    }
    @GameTest(templateName = ARENA)
    public void washedArmorPersistsAndExpires(TestContext c) {
        PlayerEntity player = c.createMockPlayer(GameMode.SURVIVAL);
        player.equipStack(EquipmentSlot.HEAD, new ItemStack(ModArmor.HELMET));
        player.equipStack(EquipmentSlot.CHEST, new ItemStack(ModArmor.CHESTPLATE));
        player.equipStack(EquipmentSlot.LEGS, new ItemStack(ModArmor.LEGGINGS));
        player.equipStack(EquipmentSlot.FEET, new ItemStack(ModArmor.BOOTS));
        c.assertTrue(SmellSystem.isArmorSmelly(player), "Fresh full armor must smell");
        SmellSystem.wash(player);
        c.assertFalse(SmellSystem.isArmorSmelly(player), "Washing must disable the repellent");
        for (ItemStack piece : player.getArmorItems()) {
            long remaining = piece.get(DataComponentTypes.CUSTOM_DATA).copyNbt().getLong(SmellSystem.CLEAN_UNTIL) - c.getWorld().getTime();
            c.assertTrue(remaining >= 3600 && remaining <= 6000, "Clean time must be 3-5 minutes");
        }
        ItemStack helmet = player.getEquippedStack(EquipmentSlot.HEAD);
        ItemStack restored = ItemStack.fromNbt(c.getWorld().getRegistryManager(), helmet.encode(c.getWorld().getRegistryManager())).orElseThrow();
        player.equipStack(EquipmentSlot.HEAD, restored);
        c.assertFalse(SmellSystem.isArmorSmelly(player), "Saved and re-equipped clean armor must remain clean");
        NbtComponent.set(DataComponentTypes.CUSTOM_DATA, restored, nbt -> nbt.putLong(SmellSystem.CLEAN_UNTIL, c.getWorld().getTime() - 1));
        c.assertTrue(SmellSystem.isArmorSmelly(player), "Smell must return after expiry");
        c.complete();
    }
    @GameTest(templateName = ARENA, tickLimit = 140)
    public void projectileHitsAndEffectExpires(TestContext c) {
        floor(c);
        CowEntity cow = c.spawnEntity(EntityType.COW, 8.5F, 2, 8.5F);
        cow.setAiDisabled(true);
        PopoProjectileEntity shot = ModEntities.POPO_PROJECTILE.create(c.getWorld());
        Vec3d pos = c.getAbsolute(new Vec3d(3.5, 2.6, 8.5));
        shot.setPosition(pos);
        shot.setVelocity(1, 0.03, 0);
        c.getWorld().spawnEntity(shot);
        c.waitAndRun(10, () -> {
            c.assertTrue(cow.hasStatusEffect(ModEffects.STINKY), "Real projectile collision must apply stink");
            c.assertTrue(shot.isRemoved(), "Projectile must be consumed on impact");
            c.assertTrue(cow.getStatusEffect(ModEffects.STINKY).getDuration() <= 100, "Stink cannot last more than five seconds");
        });
        c.waitAndRun(115, () -> {
            c.assertFalse(cow.hasStatusEffect(ModEffects.STINKY), "Stink must expire");
            c.complete();
        });
    }
    @GameTest(templateName = ARENA, tickLimit = 150)
    public void infectedMobPanicsAndRepelsNeighbors(TestContext c) {
        floor(c);
        CowEntity infected = c.spawnEntity(EntityType.COW, 16, 2, 16);
        CowEntity neighbor = c.spawnEntity(EntityType.COW, 19, 2, 16);
        Vec3d start = infected.getPos();
        infected.addStatusEffect(new StatusEffectInstance(ModEffects.STINKY, 100));
        c.waitAndRun(65, () -> {
            c.assertTrue(infected.getPos().squaredDistanceTo(start) > 0.3, "Infected mob must run around");
            c.assertTrue(neighbor.squaredDistanceTo(infected) > 25, "Neighbor must flee the infected mob");
        });
        c.waitAndRun(110, () -> {
            c.assertFalse(SmellSystem.isSmelly(infected), "Mob must stop emitting smell after five seconds");
            c.complete();
        });
    }
    @GameTest(templateName = ARENA, tickLimit = 700)
    public void beetleCollectsWithoutDuplicating(TestContext c) {
        floor(c);
        DungBeetleEntity beetle = c.spawnEntity(ModEntities.DUNG_BEETLE, 8, 2, 8);
        ItemEntity dropped = c.spawnItem(ModItems.POPO, 10, 2, 8);
        dropped.setStack(new ItemStack(ModItems.POPO, 2));
        c.waitAndRun(600, () -> {
            int piled = 0;
            for (BlockPos pos : BlockPos.iterate(c.getAbsolutePos(new BlockPos(1, 2, 1)), c.getAbsolutePos(new BlockPos(30, 3, 30)))) {
                var state = c.getWorld().getBlockState(pos);
                if (state.isOf(ModBlocks.POPO_PILE)) piled += state.get(PopoPileBlock.AMOUNT);
                if (state.isOf(ModBlocks.BEETLE_NEST)) piled += state.get(BeetleNestBlock.STAGE)
                        + ((BeetleNestBlockEntity)c.getWorld().getBlockEntity(pos)).getFood();
            }
            int loose = c.getEntities(EntityType.ITEM).stream().filter(i -> i.getStack().isOf(ModItems.POPO)).mapToInt(i -> i.getStack().getCount()).sum();
            c.assertTrue(piled > 0, "Beetle must deposit collected poop into a shelter");
            c.assertEquals(piled + loose + beetle.getCarrying(), 2, "Collection must conserve item count");
            c.complete();
        });
    }
    @GameTest(templateName = ARENA)
    public void beetleCargoAndHomePersist(TestContext c) {
        DungBeetleEntity beetle = ModEntities.DUNG_BEETLE.create(c.getWorld());
        NbtCompound nbt = new NbtCompound();
        beetle.writeCustomDataToNbt(nbt);
        nbt.putInt("CarriedPopo", 1);
        nbt.putLong("PopoHome", new BlockPos(3, 2, 3).asLong());
        beetle.readCustomDataFromNbt(nbt);
        NbtCompound saved = new NbtCompound();
        beetle.writeCustomDataToNbt(saved);
        c.assertEquals(beetle.getCarrying(), 1, "Cargo must survive reload");
        c.assertEquals(saved.getLong("PopoHome"), nbt.getLong("PopoHome"), "Home must survive reload");
        c.assertTrue(Registries.ITEM_GROUP.containsId(Identifier.of("proyecto_intento", "popocraft")), "Creative tab must register");
        c.complete();
    }

    @GameTest(templateName = ARENA, tickLimit = 700)
    public void competingBeetlesCannotCollectTheSameItemTwice(TestContext c) {
        floor(c);
        DungBeetleEntity first = c.spawnEntity(ModEntities.DUNG_BEETLE, 8, 2, 8);
        DungBeetleEntity second = c.spawnEntity(ModEntities.DUNG_BEETLE, 10, 2, 8);
        c.spawnItem(ModItems.POPO, 9, 2, 8);
        c.waitAndRun(600, () -> {
            int total = first.getCarrying() + second.getCarrying();
            for (BlockPos pos : BlockPos.iterate(c.getAbsolutePos(new BlockPos(1, 2, 1)), c.getAbsolutePos(new BlockPos(30, 3, 30)))) {
                var state = c.getWorld().getBlockState(pos);
                if (state.isOf(ModBlocks.POPO_PILE)) total += state.get(PopoPileBlock.AMOUNT);
                if (state.isOf(ModBlocks.BEETLE_NEST)) total += state.get(BeetleNestBlock.STAGE)
                        + ((BeetleNestBlockEntity)c.getWorld().getBlockEntity(pos)).getFood();
            }
            total += c.getEntities(EntityType.ITEM).stream().filter(i -> i.getStack().isOf(ModItems.POPO)).mapToInt(i -> i.getStack().getCount()).sum();
            c.assertEquals(total, 1, "Two beetles competing for one item must never duplicate it");
            c.complete();
        });
    }
}
