package org.examplee.proyecto_intento.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.examplee.proyecto_intento.item.ModItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;

public class ExtractorBlockEntity extends BlockEntity implements SidedInventory {
    private final DefaultedList<ItemStack> items = DefaultedList.ofSize(1, ItemStack.EMPTY);
    private int extractDelay = 0;

    public ExtractorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.EXTRACTOR_ENTITY, pos, state);
    }

    public static void tick(World world, BlockPos pos, BlockState state, ExtractorBlockEntity entity) {
        if (world.isClient) return;
        entity.extractDelay++;
        
        // Every 30 seconds (600 ticks) attempt to extract
        if (entity.extractDelay >= 600) {
            entity.extractDelay = 0;
            
            ItemStack currentOut = entity.items.get(0);
            if (currentOut.getCount() >= 64) return;
            
            GrandBeetleNestBlockEntity targetNest = null;
            search:
            for (int x = -5; x <= 5; x++) {
                for (int y = -2; y <= 5; y++) {
                    for (int z = -5; z <= 5; z++) {
                        BlockPos check = pos.add(x, y, z);
                        if (world.getBlockState(check).isOf(ModBlocks.GRAND_BEETLE_NEST_CONTROLLER)) {
                            if (world.getBlockEntity(check) instanceof GrandBeetleNestBlockEntity nest) {
                                if (nest.isComplete()) {
                                    targetNest = nest;
                                    break search;
                                }
                            }
                        }
                    }
                }
            }
            
            if (targetNest != null && targetNest.getFoodCount() >= 1 && targetNest.getOccupantCount() >= 5) {
                boolean consumed = false;
                for (int i = 0; i < targetNest.size(); i++) {
                    ItemStack st = targetNest.getStack(i);
                    if (!st.isEmpty() && st.isOf(ModItems.POPO)) {
                        targetNest.removeStack(i, 1);
                        consumed = true;
                        break;
                    }
                }
                
                if (consumed) {
                    if (currentOut.isEmpty()) {
                        entity.items.set(0, new ItemStack(ModItems.ESTIERCOL, 1));
                    } else {
                        currentOut.increment(1);
                    }
                    entity.markDirty();
                    
                    ((ServerWorld)world).spawnParticles(ParticleTypes.COMPOSTER, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, 5, 0.2, 0.2, 0.2, 0.1);
                    world.playSound(null, pos, net.minecraft.sound.SoundEvents.BLOCK_COMPOSTER_READY, net.minecraft.sound.SoundCategory.BLOCKS, 0.5f, 1.0f);
                }
            }
        }
    }

    @Override public int size() { return items.size(); }
    @Override public boolean isEmpty() { return items.get(0).isEmpty(); }
    @Override public ItemStack getStack(int slot) { return items.get(slot); }
    @Override public ItemStack removeStack(int slot, int amount) {
        var res = Inventories.splitStack(items, slot, amount);
        if (!res.isEmpty()) markDirty();
        return res;
    }
    @Override public ItemStack removeStack(int slot) {
        var res = Inventories.removeStack(items, slot);
        if (!res.isEmpty()) markDirty();
        return res;
    }
    @Override public void setStack(int slot, ItemStack stack) {
        items.set(slot, stack);
        markDirty();
    }
    @Override public boolean canPlayerUse(PlayerEntity player) { return true; }
    @Override public void clear() { items.clear(); markDirty(); }
    @Override public int[] getAvailableSlots(Direction side) { return new int[]{0}; }
    @Override public boolean canInsert(int slot, ItemStack stack, Direction dir) { return false; }
    @Override public boolean canExtract(int slot, ItemStack stack, Direction dir) { return true; }
    
    @Override protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        super.writeNbt(nbt, registries);
        Inventories.writeNbt(nbt, items, registries);
        nbt.putInt("ExtractDelay", extractDelay);
    }
    
    @Override protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        super.readNbt(nbt, registries);
        items.clear();
        Inventories.readNbt(nbt, items, registries);
        extractDelay = nbt.getInt("ExtractDelay");
    }
}
