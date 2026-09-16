package org.examplee.proyecto_intento.block;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.JsonParser;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.examplee.proyecto_intento.entity.DungBeetleEntity;
import org.examplee.proyecto_intento.entity.ModEntities;
import org.examplee.proyecto_intento.item.ModItems;

public final class GrandBeetleNestBlockEntity extends BlockEntity implements SidedInventory {
    public static final int CAPACITY = 360;
    public static final int POPULATION_CAPACITY = 30;

    private final DefaultedList<ItemStack> items = DefaultedList.ofSize(6, ItemStack.EMPTY);
    private final List<NbtCompound> occupants = new ArrayList<>();
    
    private int amountInvested = 0;
    private boolean soundPlayed = false;
    private BlockPos origin;

    // Cache the blueprint presence to avoid reading files over and over
    private static boolean[][] BLUEPRINT_PIECES = null;

    public GrandBeetleNestBlockEntity(BlockPos pos, BlockState state) { 
        super(ModBlocks.GRAND_BEETLE_NEST_ENTITY, pos, state); 
    }

    
    @Override
    public net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket toUpdatePacket() {
        return net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registries) {
        return createNbt(registries);
    }

    public boolean isComplete() { return getCachedState().get(GrandBeetleNestPieceBlock.STAGE) == 4; }
    
    public int getFoodCount() { return items.stream().mapToInt(ItemStack::getCount).sum(); }
    public int getOccupantCount() { return occupants.size(); }
    public int getAmountInvested() { return amountInvested; }
    /** Flat clear footprint, with ten full blocks between the nearest edges of colonies. */
    public static boolean isValidSite(ServerWorld world,BlockPos origin) {
        for(int x=0;x<5;x++) for(int z=0;z<5;z++) {
            BlockPos floor=origin.add(x,-1,z);
            if(!world.isChunkLoaded(floor) || !world.getBlockState(floor).isSideSolidFullSquare(world,floor,Direction.UP)) return false;
            for(int y=0;y<5;y++) if(!world.getBlockState(origin.add(x,y,z)).isAir()) return false;
        }
        BlockPos entrance=origin.add(2,0,-1);
        if(!world.isChunkLoaded(entrance) || !world.getBlockState(entrance).isAir()
                || !world.getBlockState(entrance.down()).isSideSolidFullSquare(world,entrance.down(),Direction.UP)) return false;
        for(int cx=(origin.getX()-20)>>4;cx<=(origin.getX()+20)>>4;cx++) for(int cz=(origin.getZ()-20)>>4;cz<=(origin.getZ()+20)>>4;cz++) {
            BlockPos chunkPos=new BlockPos(cx*16,origin.getY(),cz*16);
            if(!world.isChunkLoaded(chunkPos)) return false;
            for(var be:world.getWorldChunk(chunkPos).getBlockEntities().values()) if(be instanceof GrandBeetleNestBlockEntity other) {
                int gapX=Math.max(0,Math.abs(origin.getX()-other.getOrigin().getX())-5);
                int gapZ=Math.max(0,Math.abs(origin.getZ()-other.getOrigin().getZ())-5);
                if(gapX*gapX+gapZ*gapZ<100) return false;
            }
        }
        return true;
    }
    public void playSoundOnce(World world, BlockPos pos) {
        if (!soundPlayed && world != null && !world.isClient) {
            if (world instanceof net.minecraft.server.world.ServerWorld sw) {
                for (net.minecraft.server.network.ServerPlayerEntity player : sw.getPlayers()) {
                    if (player.squaredDistanceTo(pos.toCenterPos()) < 4096) { // 64 blocks
                        // Stop vanilla ambient music
                        player.networkHandler.sendPacket(new net.minecraft.network.packet.s2c.play.StopSoundS2CPacket(null, net.minecraft.sound.SoundCategory.MUSIC));
                        
                        // Play soundtrack directly to the player at their own location so it acts as standard Soundtrack without positional dropping
                        // We use the player's own position for the sound spawn so they hear it directly in their head
                        player.playSoundToPlayer(org.examplee.proyecto_intento.entity.ModSounds.GRAND_NEST_START, net.minecraft.sound.SoundCategory.RECORDS, 1.0F, 1.0F);
                    }
                }
            }
            soundPlayed = true;
            markDirty();
            world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_ALL);
        }
    }
    public BlockPos getOrigin() { return origin != null ? origin : pos.add(-2, 0, -2); }
    public void setOrigin(BlockPos origin) { this.origin = origin.toImmutable(); markDirty(); }
    public List<NbtCompound> getOccupants() { return occupants; }

    public int deposit(ItemStack offered) {
        if (world == null || world.isClient || !offered.isOf(ModItems.POPO)) return 0;
        
        int stage = getCachedState().get(GrandBeetleNestPieceBlock.STAGE);
        
        int transferred = 0;
        if (stage < 4) {
            if(!world.getGameRules().getBoolean(net.minecraft.world.GameRules.DO_MOB_GRIEFING) || !canUpdateStructure()) return 0;
            // Construction phase
            int amount = Math.min(offered.getCount(), 64 - amountInvested);
            if (amount > 0) {
                amountInvested += amount;
                offered.decrement(amount);
                transferred += amount;
                
                // SPpawn dirt particles simulating construction!
                if (world instanceof ServerWorld sw) {
                    sw.spawnParticles(new net.minecraft.particle.BlockStateParticleEffect(net.minecraft.particle.ParticleTypes.BLOCK, Blocks.GRAVEL.getDefaultState()), pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, 30, 1.5, 1.0, 1.5, 0.15);
                    sw.playSound(null, pos, net.minecraft.sound.SoundEvents.BLOCK_GRAVEL_BREAK, net.minecraft.sound.SoundCategory.BLOCKS, 0.7f, 1.2f);
                }
                
                playSoundOnce(world, pos);
                checkConstructionStage();
            }
        } else {
            // Food storage phase
            for (int slot = 0; slot < size() && !offered.isEmpty(); slot++) {
                var stored = items.get(slot);
                if (!stored.isEmpty() && !ItemStack.areItemsAndComponentsEqual(stored, offered)) continue;
                int amount = Math.min(offered.getCount(), 60 - stored.getCount());
                if (amount <= 0) continue;
                if (stored.isEmpty()) items.set(slot, offered.copyWithCount(amount)); else stored.increment(amount);
                offered.decrement(amount);
                transferred += amount;
            }
        }
        if (transferred > 0) {
            markDirty();
            world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_ALL);
        }
        return transferred;
    }

    private void checkConstructionStage() {
        if (world == null || world.isClient) return;
        int currentStage = getCachedState().get(GrandBeetleNestPieceBlock.STAGE);
        int targetStage = 1;
        if (amountInvested >= 64) targetStage = 4;
        else if (amountInvested >= 48) targetStage = 3;
        else if (amountInvested >= 32) targetStage = 2;

        if (targetStage > currentStage) {
            world.setBlockState(pos, getCachedState().with(GrandBeetleNestPieceBlock.STAGE, targetStage), Block.NOTIFY_ALL);
            updateStructure(targetStage);
            if (targetStage == 4 && world instanceof ServerWorld sw) {
                org.examplee.proyecto_intento.entity.AdvancementHelper.grantNearby(sw, pos, "finish_grand_nest");
                for (net.minecraft.entity.player.PlayerEntity player : sw.getPlayers()) {
                    if (player.squaredDistanceTo(pos.toCenterPos()) < 4096) {
                        ((net.minecraft.server.network.ServerPlayerEntity) player).networkHandler
                            .sendPacket(new net.minecraft.network.packet.s2c.play.StopSoundS2CPacket(
                                net.minecraft.util.Identifier.of("proyecto_intento", "grand_nest_start"), 
                                net.minecraft.sound.SoundCategory.RECORDS));
                    }
                }
            }
        }
    }

    public boolean canUpdateStructure() {
        if(world==null || !getOrigin().add(2,0,2).equals(pos)) return false;
        for(int y=0;y<5;y++) for(int z=0;z<5;z++) for(int x=0;x<5;x++) {
            BlockPos at=getOrigin().add(x,y,z);
            if(!world.isChunkLoaded(at)) return false;
            if(at.equals(pos)) continue;
            var state=world.getBlockState(at);
            if(!state.isAir() && !(state.isOf(ModBlocks.GRAND_BEETLE_NEST_PIECE) && state.get(GrandBeetleNestPieceBlock.PIECE)==x+5*z+25*y)) return false;
        }
        return true;
    }
    public void updateStructure(int stage) {
        if (world == null || world.isClient) return;
        if(stage<0 || stage>4 || (stage>0 && !canUpdateStructure())) return;
        if (BLUEPRINT_PIECES == null) loadBlueprint();
        BlockPos basePos = getOrigin();
        
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                for (int z = 0; z < 5; z++) {
                    int p = x + 5 * z + 25 * y;
                    if (p == 12 && stage > 0) continue; // controller itself
                    BlockPos pPos = basePos.add(x, y, z);
                    
                    boolean shouldHave = (stage > 0) && BLUEPRINT_PIECES[stage - 1][p];
                    BlockState current = world.getBlockState(pPos);
                    
                    if (shouldHave) {
                        if (!current.isOf(ModBlocks.GRAND_BEETLE_NEST_PIECE) || current.get(GrandBeetleNestPieceBlock.STAGE) != stage) {
                            world.setBlockState(pPos, ModBlocks.GRAND_BEETLE_NEST_PIECE.getDefaultState()
                                    .with(GrandBeetleNestPieceBlock.STAGE, stage)
                                    .with(GrandBeetleNestPieceBlock.PIECE, p), Block.NOTIFY_ALL);
                        }
                    } else {
                        // Remove if it's our piece from a previous stage or just hanging around
                        if (current.isOf(ModBlocks.GRAND_BEETLE_NEST_PIECE) && current.get(GrandBeetleNestPieceBlock.PIECE)==p) {
                            world.setBlockState(pPos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL);
                        }
                    }
                }
            }
        }
    }

    private synchronized static void loadBlueprint() {
        if (BLUEPRINT_PIECES != null) return;
        try (var stream = GrandBeetleNestBlockEntity.class.getResourceAsStream("/data/proyecto_intento/grand_nest/blueprint.json")) {
            if (stream == null) throw new IllegalStateException("Missing blueprint");
            var data = JsonParser.parseReader(new InputStreamReader(stream, StandardCharsets.UTF_8)).getAsJsonObject();
            var stagesArray = data.getAsJsonArray("stages");
            BLUEPRINT_PIECES = new boolean[4][125];
            for (int s = 0; s < stagesArray.size(); s++) {
                var piecesArray = stagesArray.get(s).getAsJsonObject().getAsJsonArray("pieces");
                for (var p : piecesArray) {
                    int pieceId = p.getAsJsonObject().get("piece").getAsInt();
                    BLUEPRINT_PIECES[s][pieceId] = true;
                }
            }
        } catch (Exception e) { throw new IllegalStateException("Cannot load blueprint", e); }
    }

    public boolean tryEnter(DungBeetleEntity beetle) {
        if (world == null || world.isClient || occupants.size() >= POPULATION_CAPACITY
                || !beetle.isAlive() || beetle.isRemoved() || beetle.hasPassengers() || beetle.hasVehicle() || beetle.isLeashed()
                || beetle.getCarrying() != 0 || beetle.squaredDistanceTo(pos.toCenterPos()) > 64) return false;
        
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

    public static void tick(World world, BlockPos pos, BlockState state, GrandBeetleNestBlockEntity nest) {
        if (world.isClient) return;
        ServerWorld server = (ServerWorld) world;
        boolean complete = nest.isComplete();
        
        for (NbtCompound occupant : nest.occupants) {
            occupant.putInt("TicksInside", Math.min(24000, occupant.getInt("TicksInside") + 1));
            NbtCompound data = occupant.getCompound("Entity");
            int age = data.getInt("Age");
            data.putInt("Age", age < 0 ? age + 1 : Math.max(0, age - 1));
        }

        if (!complete && nest.soundPlayed && world.getTime() % 5 == 0) {
            double px = nest.getOrigin().getX() + 2.5 + (world.random.nextDouble() - 0.5) * 6.0;
            double py = nest.getOrigin().getY() + 3.0 + world.random.nextDouble() * 2.0;
            double pz = nest.getOrigin().getZ() + 2.5 + (world.random.nextDouble() - 0.5) * 6.0;
            server.spawnParticles(ParticleTypes.NOTE, px, py, pz, 0, world.random.nextDouble(), 0.0, 0.0, 1.0);
        }

        if (world.getTime() % 20 == 0) {
            if (complete) {
                List<NbtCompound> parents = nest.occupants.stream().filter(n -> n.getInt("TicksInside") >= 200
                        && n.getCompound("Entity").getInt("Age") == 0
                        && !n.getCompound("Entity").getBoolean("NurseryResident")).limit(2).toList();
                
                if (nest.getFoodCount() > 0 && parents.size() == 2 && nest.occupants.size() < POPULATION_CAPACITY && nest.localPopulation() < 30) {
                    if (nest.consumeFood(1)) {
                        DungBeetleEntity baby = ModEntities.DUNG_BEETLE.create(server);
                        if (baby != null) {
                            baby.setBreedingAge(-24000);
                            baby.setNest(pos);
                            NbtCompound data = new NbtCompound();
                            baby.saveNbt(data);
                            NbtCompound resident = new NbtCompound();
                            resident.put("Entity", data);
                            nest.occupants.add(resident);
                            for (NbtCompound parent : parents) parent.getCompound("Entity").putInt("Age", 6000);
                            server.spawnParticles(ParticleTypes.HEART, pos.getX() + .5, pos.getY() + 1, pos.getZ() + .5, 4, .2, .15, .2, 0);
                        }
                    }
                }
            }

            for (int i = nest.occupants.size() - 1; i >= 0; i--) {
                NbtCompound resident = nest.occupants.get(i);
                int age = resident.getCompound("Entity").getInt("Age");
                // If not complete, eject quickly after 100 ticks (5 seconds) to keep collecting!
                int stay = !complete ? 100 : (age < 0 ? 600 : age == 0 && nest.getFoodCount() > 0 ? 1200 : 400);
                if (resident.getInt("TicksInside") >= stay && nest.release(server, resident, false)) nest.occupants.remove(i);
            }
            
            // Re-founding small nests if full
            if (complete && world.getGameRules().getBoolean(net.minecraft.world.GameRules.DO_MOB_GRIEFING) && nest.occupants.size() >= POPULATION_CAPACITY && nest.getFoodCount() >= 10 && world.getTime() % 100 == 0) {
                // We should let a beetle out or trigger small nest placement.
                // Wait, logic says "If grand nest is full and has food, use food to place small nests nearby."
                nest.tryFoundSmallNest(server);
            }
        }
        
        if (!nest.occupants.isEmpty()) nest.markDirty();
    }
    
    private void tryFoundSmallNest(ServerWorld server) {
        // Look for empty space for a single nest
        if (consumeFood(6)) {
            for (int x = -10; x <= 10; x++) {
                for (int z = -10; z <= 10; z++) {
                    if (Math.abs(x) < 3 && Math.abs(z) < 3) continue; // Don't build too close to center
                    for (int y = -2; y <= 2; y++) {
                        BlockPos c = pos.add(x, y, z);
                        if (validateSmallNestSpace(server, c)) {
                            server.setBlockState(c, ModBlocks.BEETLE_NEST.getDefaultState().with(BeetleNestBlock.STAGE, 5));
                            if(server.getBlockEntity(c) instanceof BeetleNestBlockEntity small) small.addMaterial(); // sixth unit is food
                            // Also place a beetle to inhabit it
                            if (occupants.size() > 0) {
                                NbtCompound resident = occupants.get(occupants.size() - 1);
                                if(release(server, resident, true, c)) occupants.remove(resident);
                            }
                            return;
                        }
                    }
                }
            }
            // If failed to find space, revert food
            deposit(new ItemStack(ModItems.POPO, 6));
        }
    }
    
    private boolean validateSmallNestSpace(ServerWorld world, BlockPos c) {
        return world.getBlockState(c).isAir() && world.getBlockState(c.down()).isOpaqueFullCube(world, c.down());
    }

    private boolean consumeFood(int amount) {
        if(amount<0 || getFoodCount()<amount) return false;
        int toConsume = amount;
        for (int i = 0; i < size(); i++) {
            ItemStack stack = items.get(i);
            if (!stack.isEmpty()) {
                int take = Math.min(toConsume, stack.getCount());
                stack.decrement(take);
                toConsume -= take;
                if (toConsume <= 0) break;
            }
        }
        if (toConsume <= 0) {
            markDirty();
            return true;
        }
        return false;
    }

    private int localPopulation() {
        int count = world.getEntitiesByClass(DungBeetleEntity.class, new Box(pos).expand(16), e -> e.isAlive()).size();
        return count + occupants.size();
    }

    private boolean release(ServerWorld server, NbtCompound resident, boolean emergency) {
        return release(server, resident, emergency, pos);
    }
    
    private boolean release(ServerWorld server, NbtCompound resident, boolean emergency, BlockPos exitTarget) {
        NbtCompound data = resident.getCompound("Entity");
        if (data.containsUuid("UUID") && server.getEntity(data.getUuid("UUID")) != null) return false;
        
        DungBeetleEntity beetle = ModEntities.DUNG_BEETLE.create(server);
        if (beetle == null) return false;
        beetle.readNbt(data.copy());
        beetle.onNestExit(exitTarget, emergency);
        if(!exitTarget.equals(pos) && server.getBlockState(exitTarget).isOf(ModBlocks.BEETLE_NEST)) beetle.setNest(exitTarget);
        
        // Use origin's exit which is North by default
        BlockPos exit = getOrigin().add(2, 0, -1);
        if (emergency && !exitTarget.equals(pos)) exit = exitTarget;
        
        if (server.isChunkLoaded(exit)) {
            beetle.refreshPositionAndAngles(exit.getX() + .5, exit.getY() + .05, exit.getZ() + .5, 180, 0); // Face North = 180 yaw usually
            if (server.isSpaceEmpty(beetle) && server.spawnEntity(beetle)) {
                markDirty();
                return true;
            }
        }
        if (emergency) {
            beetle.refreshPositionAndAngles(exitTarget.getX() + .5, exitTarget.getY() + .05, exitTarget.getZ() + .5, 0, 0);
            return server.spawnEntity(beetle);
        }
        return false;
    }

    @Override public int size() { return items.size(); }
    public void evacuateAndDismantle() {
        if(!(world instanceof ServerWorld server)) return;
        occupants.removeIf(resident -> release(server,resident,true)
                || (resident.getCompound("Entity").containsUuid("UUID") && server.getEntity(resident.getCompound("Entity").getUuid("UUID"))!=null));
        updateStructure(0);
        if(amountInvested>0) Block.dropStack(world,pos,new ItemStack(ModItems.POPO,amountInvested));
        amountInvested=0;
        markDirty();
    }
    @Override public int getMaxCountPerStack() { return 60; }
    @Override public boolean isEmpty() { return items.stream().allMatch(ItemStack::isEmpty); }
    @Override public ItemStack getStack(int slot) { return items.get(slot); }
    @Override public ItemStack removeStack(int slot, int amount) {
        var removed = Inventories.splitStack(items, slot, amount);
        if (!removed.isEmpty()) markDirty();
        return removed;
    }
    @Override public ItemStack removeStack(int slot) {
        var removed = Inventories.removeStack(items, slot);
        if (!removed.isEmpty()) markDirty();
        return removed;
    }
    @Override public void setStack(int slot, ItemStack stack) {
        if (!stack.isEmpty() && !isValid(slot, stack)) return;
        items.set(slot, stack.isEmpty() ? ItemStack.EMPTY : stack.copyWithCount(Math.min(60, stack.getCount())));
        markDirty();
    }
    @Override public boolean isValid(int slot, ItemStack stack) { return stack.isOf(ModItems.POPO); }
    @Override public boolean canPlayerUse(PlayerEntity player) { return world != null && world.getBlockEntity(pos) == this && player.squaredDistanceTo(pos.getX()+.5, pos.getY()+.5, pos.getZ()+.5) <= 64; }
    @Override public void clear() { items.clear(); markDirty(); }
    @Override public int[] getAvailableSlots(Direction side) { return side == Direction.DOWN && isComplete() ? new int[]{0,1,2,3,4,5} : new int[0]; }
    @Override public boolean canInsert(int slot, ItemStack stack, Direction dir) { return false; }
    @Override public boolean canExtract(int slot, ItemStack stack, Direction dir) { return dir == Direction.DOWN && isComplete() && stack.isOf(ModItems.POPO); }
    
    @Override protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        super.writeNbt(nbt, registries);
        Inventories.writeNbt(nbt, items, registries);
        nbt.putInt("AmountInvested", amountInvested);
        nbt.putBoolean("SoundPlayed", soundPlayed);
        if (origin != null) {
            nbt.putInt("OriginX", origin.getX());
            nbt.putInt("OriginY", origin.getY());
            nbt.putInt("OriginZ", origin.getZ());
        }
        NbtList list = new NbtList();
        occupants.forEach(n -> list.add(n.copy()));
        nbt.put("Occupants", list);
    }
    
    @Override protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        super.readNbt(nbt, registries);
        items.clear();
        Inventories.readNbt(nbt, items, registries);
        for (int i = 0; i < size(); i++) {
            if (!items.get(i).isEmpty() && !isValid(i, items.get(i))) items.set(i, ItemStack.EMPTY);
            else if (items.get(i).getCount() > 60) items.get(i).setCount(60);
        }
        amountInvested = Math.clamp(nbt.getInt("AmountInvested"),0,64);
        origin=null;
        soundPlayed = nbt.getBoolean("SoundPlayed");
        if (nbt.contains("OriginX")) {
            origin = new BlockPos(nbt.getInt("OriginX"), nbt.getInt("OriginY"), nbt.getInt("OriginZ"));
        }
        occupants.clear();
        NbtList list = nbt.getList("Occupants", NbtElement.COMPOUND_TYPE);
        for (int i = 0; i < Math.min(POPULATION_CAPACITY, list.size()); i++) occupants.add(list.getCompound(i).copy());
    }
}
