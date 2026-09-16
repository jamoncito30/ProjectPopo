package org.examplee.proyecto_intento.block;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import org.examplee.proyecto_intento.entity.DungBeetleEntity;
import org.examplee.proyecto_intento.entity.ModEntities;
import org.examplee.proyecto_intento.item.ModItems;

/** Occupants are serialized entities, not invisible entities left ticking in the world. */
public final class BeetleNestBlockEntity extends BlockEntity {
    public static final int CAPACITY = 6;
    public static final int MAX_FOOD = 8;
    private final List<NbtCompound> occupants = new ArrayList<>();
    private int food;
    public BeetleNestBlockEntity(BlockPos pos, BlockState state) { super(ModBlocks.BEETLE_NEST_ENTITY, pos, state); }
    public int getOccupantCount() { return occupants.size(); }
    public int getFood() { return food; }
    /** Recover one stored item, then one construction layer; never manufacture an extra drop. */
    public boolean takeMaterialForGrandNest() {
        if(world==null || world.isClient || !world.getGameRules().getBoolean(net.minecraft.world.GameRules.DO_MOB_GRIEFING)) return false;
        if(food>0) { food--;markDirty();return true; }
        if(!occupants.isEmpty()) evacuate();
        if(!occupants.isEmpty()) return false;
        int stage=getCachedState().get(BeetleNestBlock.STAGE);
        return world.setBlockState(pos,stage>1?getCachedState().with(BeetleNestBlock.STAGE,stage-1):net.minecraft.block.Blocks.AIR.getDefaultState(),Block.NOTIFY_ALL);
    }
    public int getAdultCount() { return (int) occupants.stream().filter(n -> n.getCompound("Entity").getInt("Age") >= 0
            && !n.getCompound("Entity").getBoolean("NurseryResident")).count(); }
    public boolean isComplete() { return getCachedState().get(BeetleNestBlock.STAGE) == 5; }

    public boolean addMaterial() {
        if (world == null || world.isClient || !world.getGameRules().getBoolean(net.minecraft.world.GameRules.DO_MOB_GRIEFING)) return false;
        int stage = getCachedState().get(BeetleNestBlock.STAGE);
        if (stage < 5) {
            return world.setBlockState(pos, getCachedState().with(BeetleNestBlock.STAGE, stage + 1), Block.NOTIFY_ALL);
        }
        if (food >= MAX_FOOD) return false;
        food++;
        markDirty();
        return true;
    }

    public boolean tryEnter(DungBeetleEntity beetle) {
        if (world == null || world.isClient || !isComplete() || occupants.size() >= CAPACITY
                || !beetle.isAlive() || beetle.isRemoved() || beetle.hasPassengers() || beetle.hasVehicle() || beetle.isLeashed()
                || beetle.getCarrying() != 0 || beetle.squaredDistanceTo(pos.toCenterPos()) > 4) return false;
        if (occupants.stream().anyMatch(n -> n.getCompound("Entity").containsUuid("UUID")
                && n.getCompound("Entity").getUuid("UUID").equals(beetle.getUuid()))) return false;
        beetle.setNest(pos);
        NbtCompound data = new NbtCompound();
        beetle.saveNbt(data);
        NbtCompound resident = new NbtCompound();
        resident.put("Entity", data);
        resident.putInt("TicksInside", 0);
        occupants.add(resident);
        markDirty();
        beetle.discard();
        return true;
    }

    public static void tick(World world, BlockPos pos, BlockState state, BeetleNestBlockEntity nest) {
        if (!(world instanceof ServerWorld server) || !nest.isComplete()) return;
        for (NbtCompound occupant : nest.occupants) {
            occupant.putInt("TicksInside", Math.min(24000, occupant.getInt("TicksInside") + 1));
            NbtCompound data = occupant.getCompound("Entity");
            int age = data.getInt("Age");
            data.putInt("Age", age < 0 ? age + 1 : Math.max(0, age - 1));
        }
        // Local crowd limit counts both visible neighbors and residents of nearby houses.
        if (world.getTime() % 20 == 0) {
            List<NbtCompound> parents = nest.occupants.stream().filter(n -> n.getInt("TicksInside") >= 200
                    && n.getCompound("Entity").getInt("Age") == 0
                    && !n.getCompound("Entity").getBoolean("NurseryResident")).limit(2).toList();
            if (nest.food > 0 && parents.size() == 2 && nest.occupants.size() < CAPACITY && nest.localPopulation() < 16) {
                DungBeetleEntity baby = ModEntities.DUNG_BEETLE.create(server);
                if (baby != null) {
                    baby.setBreedingAge(-24000);
                    baby.setNest(pos);
                    NbtCompound data = new NbtCompound();
                    baby.saveNbt(data);
                    NbtCompound resident = new NbtCompound();
                    resident.put("Entity", data);
                    nest.occupants.add(resident);
                    nest.food--;
                    for (NbtCompound parent : parents) parent.getCompound("Entity").putInt("Age", 6000);
                    server.spawnParticles(ParticleTypes.HEART, pos.getX() + .5, pos.getY() + 1, pos.getZ() + .5, 4, .2, .15, .2, 0);
                }
            }
            for (int i = nest.occupants.size() - 1; i >= 0; i--) {
                NbtCompound resident = nest.occupants.get(i);
                int age = resident.getCompound("Entity").getInt("Age");
                // Ready adults wait longer when food is stocked, giving a partner time to arrive.
                int stay = age < 0 ? 600 : age == 0 && nest.food > 0 ? 1200 : 400;
                if (resident.getInt("TicksInside") >= stay && nest.release(server, resident, false)) nest.occupants.remove(i);
            }
        }
        // Age and residence time must survive a save even between the periodic actions.
        if (!nest.occupants.isEmpty()) nest.markDirty();
    }

    private int localPopulation() {
        int count = world.getEntitiesByClass(DungBeetleEntity.class, new Box(pos).expand(16), e -> e.isAlive()).size();
        for (BlockPos candidate : BlockPos.iterate(pos.add(-16, -2, -16), pos.add(16, 2, 16))) {
            if (world.isChunkLoaded(candidate) && world.getBlockEntity(candidate) instanceof BeetleNestBlockEntity nest) count += nest.getOccupantCount();
        }
        return count;
    }

    private boolean release(ServerWorld server, NbtCompound resident, boolean emergency) {
        NbtCompound data = resident.getCompound("Entity");
        // If an entity with this UUID is already loaded, never create a duplicate.
        if (data.containsUuid("UUID") && server.getEntity(data.getUuid("UUID")) != null) return false;
        DungBeetleEntity beetle = ModEntities.DUNG_BEETLE.create(server);
        if (beetle == null) return false;
        beetle.readNbt(data.copy());
        beetle.onNestExit(pos, emergency);
        for (Direction direction : new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.UP}) {
            BlockPos exit = pos.offset(direction);
            if (!server.isChunkLoaded(exit)) continue;
            beetle.refreshPositionAndAngles(exit.getX() + .5, exit.getY() + .05, exit.getZ() + .5, direction == Direction.UP ? 0 : direction.asRotation(), 0);
            if (server.isSpaceEmpty(beetle) && server.getFluidState(exit).isEmpty() && server.spawnEntity(beetle)) {
                markDirty();
                return true;
            }
        }
        if (emergency) {
            beetle.refreshPositionAndAngles(pos.getX() + .5, pos.getY() + .05, pos.getZ() + .5, 0, 0);
            return server.spawnEntity(beetle);
        }
        return false;
    }

    public void evacuate() {
        if (!(world instanceof ServerWorld server)) return;
        occupants.removeIf(resident -> release(server, resident, true));
        if (food > 0) Block.dropStack(world, pos, new ItemStack(ModItems.POPO, food));
        food = 0;
        markDirty();
    }
    @Override protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        super.writeNbt(nbt, registries);
        nbt.putInt("Food", food);
        NbtList list = new NbtList();
        occupants.forEach(n -> list.add(n.copy()));
        nbt.put("Occupants", list);
    }
    @Override protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        super.readNbt(nbt, registries);
        food = Math.clamp(nbt.getInt("Food"), 0, MAX_FOOD);
        occupants.clear();
        NbtList list = nbt.getList("Occupants", NbtElement.COMPOUND_TYPE);
        for (int i = 0; i < Math.min(CAPACITY, list.size()); i++) occupants.add(list.getCompound(i).copy());
    }
}
