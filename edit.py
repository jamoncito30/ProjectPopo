import sys
import re

with open('src/main/java/org/examplee/proyecto_intento/entity/DungBeetleEntity.java', 'r', encoding='utf-8') as f:
    code = f.read()

imports = '''
import org.examplee.proyecto_intento.block.GrandBeetleNestBlockEntity;
import org.examplee.proyecto_intento.block.GrandBeetleNestControllerBlock;
import org.examplee.proyecto_intento.block.GrandBeetleNestPieceBlock;
import net.minecraft.block.Blocks;
'''

code = code.replace('import org.examplee.proyecto_intento.item.ModItems;', 'import org.examplee.proyecto_intento.item.ModItems;' + imports)

tick_mov = '''
    @Override public void tickMovement() {
        super.tickMovement();
        if (!getWorld().isClient && outsideTicks > 0) outsideTicks--;
        if (!getWorld().isClient && getWorld().getTime() % 100 == 0) tryInitiateGrandNest();
    }
    
    private void tryInitiateGrandNest() {
        if (!getWorld().getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)) return;
        
        // Count fully built nests
        int builtNests = 0;
        BlockPos center = getBlockPos();
        for (BlockPos p : BlockPos.iterate(center.add(-16, -4, -16), center.add(16, 4, 16))) {
            if (getWorld().getBlockEntity(p) instanceof BeetleNestBlockEntity nest && nest.isComplete()) {
                builtNests++;
            }
            if (getWorld().getBlockEntity(p) instanceof GrandBeetleNestBlockEntity) {
                return; // already exists
            }
        }
        
        if (builtNests >= 5) {
            // Find 5x5 flat spot
            for (BlockPos p : BlockPos.iterate(center.add(-12, -2, -12), center.add(12, 4, 12))) {
                if (validGrandNestSpot(p)) {
                    BlockPos controllerPos = p.add(2, 0, 2);
                    getWorld().setBlockState(controllerPos, ModBlocks.GRAND_BEETLE_NEST_CONTROLLER.getDefaultState().with(GrandBeetleNestPieceBlock.STAGE, 1));
                    if (getWorld().getBlockEntity(controllerPos) instanceof GrandBeetleNestBlockEntity grandNest) {
                        grandNest.setOrigin(p);
                        grandNest.updateStructure(1);
                        playSound(ModSounds.BEETLE_CHIRP, 1.0F, 1.0F); // some signal
                    }
                    return;
                }
            }
        }
    }
    
    private boolean validGrandNestSpot(BlockPos origin) {
        for (int x = 0; x < 5; x++) {
            for (int z = 0; z < 5; z++) {
                BlockPos floor = origin.add(x, -1, z);
                if (!getWorld().getBlockState(floor).isOpaqueFullCube(getWorld(), floor)) return false;
                for (int y = 0; y < 5; y++) {
                    if (!getWorld().getBlockState(origin.add(x, y, z)).isAir()) return false;
                }
            }
        }
        return true;
    }
'''

code = code.replace(
    '''    @Override public void tickMovement() {
        super.tickMovement();
        if (!getWorld().isClient && outsideTicks > 0) outsideTicks--;
    }''', tick_mov)

find_home = '''
    private Object findHome() {
        if (home == null) { home = getBlockPos(); setPositionTarget(home, 12); }
        if (nurseryResident && !isBaby()) { nestPos = null; nurseryResident = false; }
        
        // Return current nest if valid
        if (nestPos != null && getWorld().isChunkLoaded(nestPos)) {
            var be = getWorld().getBlockEntity(nestPos);
            if (be instanceof GrandBeetleNestBlockEntity grandNest && grandNest.getOccupantCount() < GrandBeetleNestBlockEntity.POPULATION_CAPACITY) {
                return grandNest;
            }
            else if (be instanceof BeetleNestBlockEntity nest && (isBaby() || otherAdults(nestPos, nest) < 2)) {
                return nest;
            }
            nestPos = null;
        }
        
        // Search for grand nest first
        for (BlockPos candidate : BlockPos.iterate(home.add(-24, -4, -24), home.add(24, 4, 24))) {
            if (getWorld().isChunkLoaded(candidate)) {
                if (getWorld().getBlockEntity(candidate) instanceof GrandBeetleNestBlockEntity grandNest
                        && grandNest.getOccupantCount() < GrandBeetleNestBlockEntity.POPULATION_CAPACITY) {
                    Path path = getNavigation().findPathTo(candidate, 1);
                    if (path != null && path.reachesTarget()) { setNest(candidate); return grandNest; }
                }
            }
        }
        
        // Small nest
        for (BlockPos candidate : BlockPos.iterate(home.add(-8, -2, -8), home.add(8, 2, 8))) {
            if (!getWorld().isChunkLoaded(candidate)) continue;
            if (getWorld().getBlockEntity(candidate) instanceof BeetleNestBlockEntity nest
                    && nest.getOccupantCount() < BeetleNestBlockEntity.CAPACITY && otherAdults(candidate, nest) < 2) {
                Path path = getNavigation().findPathTo(candidate, 1);
                if (path != null && path.reachesTarget()) { setNest(candidate); return nest; }
            }
        }
        return null;
    }
'''

code = code.replace('''
    private BeetleNestBlockEntity findHome() {
        if (home == null) { home = getBlockPos(); setPositionTarget(home, 12); }
        if (nurseryResident && !isBaby()) {
            nestPos = null;
            nurseryResident = false;
        }
        if (nestPos != null) {
            if (!getWorld().isChunkLoaded(nestPos)) return null;
            if (getWorld().getBlockEntity(nestPos) instanceof BeetleNestBlockEntity nest
                    && (isBaby() || otherAdults(nestPos, nest) < 2)) return nest;
            nestPos = null; // Grown children establish a nearby house when their parents already occupy this one.
        }
        for (BlockPos candidate : BlockPos.iterate(home.add(-8, -2, -8), home.add(8, 2, 8))) {
            if (!getWorld().isChunkLoaded(candidate)) continue;
            if (getWorld().getBlockEntity(candidate) instanceof BeetleNestBlockEntity nest
                    && nest.getOccupantCount() < BeetleNestBlockEntity.CAPACITY && otherAdults(candidate, nest) < 2) {
                Path path = getNavigation().findPathTo(candidate, 1);
                if (path != null && path.reachesTarget()) { setNest(candidate); return nest; }
            }
        }
        return null;
    }'''.strip(), find_home.strip())

shelter_goal = '''
    private final class ShelterGoal extends Goal {
        private int cooldown;
        private int travelTicks;
        ShelterGoal() { setControls(EnumSet.of(Control.MOVE)); }
        @Override public boolean canStart() {
            if (--cooldown > 0 || getCarrying() > 0 || hasPassengers() || hasVehicle() || isLeashed()) return false;
            cooldown = 60;
            Object nest = findHome();
            if (nest instanceof GrandBeetleNestBlockEntity grandNest) {
                return grandNest.getOccupantCount() < GrandBeetleNestBlockEntity.POPULATION_CAPACITY
                    && (outsideTicks == 0 || (!isBaby() && getBreedingAge() == 0 && grandNest.getFoodCount() > 0));
            } else if (nest instanceof BeetleNestBlockEntity smallNest) {
                return smallNest.isComplete() && smallNest.getOccupantCount() < BeetleNestBlockEntity.CAPACITY
                    && (outsideTicks == 0 || (!isBaby() && getBreedingAge() == 0 && smallNest.getFood() > 0));
            }
            return false;
        }
        @Override public void start() { travelTicks = 0; }
        @Override public boolean shouldContinue() { return !isRemoved() && nestPos != null && travelTicks < 240; }
        @Override public void tick() {
            travelTicks++;
            var be = getWorld().getBlockEntity(nestPos);
            if (be instanceof GrandBeetleNestBlockEntity grandNest) {
                if (travelTicks % 20 == 1) getNavigation().startMovingTo(nestPos.getX() + .5, nestPos.getY(), nestPos.getZ() + .5, .9);
                if (squaredDistanceTo(nestPos.toCenterPos()) < 16 && canSeeNest(nestPos)) {
                    grandNest.tryEnter(DungBeetleEntity.this);
                    travelTicks = 240;
                }
            } else if (be instanceof BeetleNestBlockEntity nest && nest.isComplete()) {
                if (travelTicks % 20 == 1) getNavigation().startMovingTo(nestPos.getX() + .5, nestPos.getY(), nestPos.getZ() + .5, .9);
                if (squaredDistanceTo(nestPos.toCenterPos()) < 4 && canSeeNest(nestPos)) {
                    nest.tryEnter(DungBeetleEntity.this);
                    travelTicks = 240;
                }
            } else { travelTicks = 240; }
        }
        @Override public void stop() { getNavigation().stop(); cooldown = 60; }
    }
'''

code = code.replace('''
    private final class ShelterGoal extends Goal {
        private int cooldown;
        private int travelTicks;
        ShelterGoal() { setControls(EnumSet.of(Control.MOVE)); }
        @Override public boolean canStart() {
            if (--cooldown > 0 || getCarrying() > 0 || hasPassengers() || hasVehicle() || isLeashed()) return false;
            cooldown = 60;
            BeetleNestBlockEntity nest = findHome();
            return nest != null && nest.isComplete() && nest.getOccupantCount() < BeetleNestBlockEntity.CAPACITY
                    && (outsideTicks == 0 || (!isBaby() && getBreedingAge() == 0 && nest.getFood() > 0));
        }
        @Override public void start() { travelTicks = 0; }
        @Override public boolean shouldContinue() { return !isRemoved() && nestPos != null && travelTicks < 240; }
        @Override public void tick() {
            travelTicks++;
            if (!(getWorld().getBlockEntity(nestPos) instanceof BeetleNestBlockEntity nest) || !nest.isComplete()) { travelTicks = 240; return; }
            if (travelTicks % 20 == 1) getNavigation().startMovingTo(nestPos.getX() + .5, nestPos.getY(), nestPos.getZ() + .5, .9);
            if (squaredDistanceTo(nestPos.toCenterPos()) < 4 && canSeeNest(nestPos)) {
                nest.tryEnter(DungBeetleEntity.this);
                travelTicks = 240;
            }
        }
        @Override public void stop() { getNavigation().stop(); cooldown = 60; }
    }'''.strip(), shelter_goal.strip())


can_deposit = '''
    private boolean canDeposit(BlockPos pos) {
        if (!getWorld().isChunkLoaded(pos)) return false;
        
        BlockState state = getWorld().getBlockState(pos);
        if (state.isOf(ModBlocks.GRAND_BEETLE_NEST_PIECE) || state.isOf(ModBlocks.GRAND_BEETLE_NEST_CONTROLLER)) {
            return true;
        }
        
        if (state.isOf(ModBlocks.BEETLE_NEST)) return pos.equals(nestPos)
                && getWorld().getBlockEntity(pos) instanceof BeetleNestBlockEntity nest
                && (!nest.isComplete() || nest.getFood() < BeetleNestBlockEntity.MAX_FOOD);
                
        if (!state.isAir() || !getWorld().getFluidState(pos).isEmpty()
                || !getWorld().getBlockState(pos.up()).isAir()
                || !getWorld().getBlockState(pos.down()).isSideSolidFullSquare(getWorld(), pos.down(), Direction.UP)) return false;
        // Leave space between houses and at least one ground-level exit.
        for (BlockPos nearby : BlockPos.iterate(pos.add(-2, -1, -2), pos.add(2, 1, 2))) {
            if (getWorld().getBlockState(nearby).isOf(ModBlocks.BEETLE_NEST) || getWorld().getBlockState(nearby).isOf(ModBlocks.GRAND_BEETLE_NEST_PIECE)) return false;
        }
        for (Direction direction : Direction.Type.HORIZONTAL) {
            BlockPos exit = pos.offset(direction);
            if (getWorld().getBlockState(exit).isAir() && getWorld().getBlockState(exit.down())
                    .isSideSolidFullSquare(getWorld(), exit.down(), Direction.UP)) return true;
        }
        return false;
    }
'''

code = code.replace('''
    private boolean canDeposit(BlockPos pos) {
        if (!getWorld().isChunkLoaded(pos)) return false;
        BlockState state = getWorld().getBlockState(pos);
        if (state.isOf(ModBlocks.BEETLE_NEST)) return pos.equals(nestPos)
                && getWorld().getBlockEntity(pos) instanceof BeetleNestBlockEntity nest
                && (!nest.isComplete() || nest.getFood() < BeetleNestBlockEntity.MAX_FOOD);
        if (!state.isAir() || !getWorld().getFluidState(pos).isEmpty()
                || !getWorld().getBlockState(pos.up()).isAir()
                || !getWorld().getBlockState(pos.down()).isSideSolidFullSquare(getWorld(), pos.down(), Direction.UP)) return false;
        // Leave space between houses and at least one ground-level exit.
        for (BlockPos nearby : BlockPos.iterate(pos.add(-2, -1, -2), pos.add(2, 1, 2))) {
            if (getWorld().getBlockState(nearby).isOf(ModBlocks.BEETLE_NEST)) return false;
        }
        for (Direction direction : Direction.Type.HORIZONTAL) {
            BlockPos exit = pos.offset(direction);
            if (getWorld().getBlockState(exit).isAir() && getWorld().getBlockState(exit.down())
                    .isSideSolidFullSquare(getWorld(), exit.down(), Direction.UP)) return true;
        }
        return false;
    }'''.strip(), can_deposit.strip())


collect_goal = '''
    private final class CollectGoal extends Goal {
        private ItemEntity target;
        private BlockPos deposit;
        private BlockPos sourceNest;
        private int cooldown;
        private int repath;
        private int workingTicks;
        CollectGoal() { setControls(EnumSet.of(Control.MOVE, Control.LOOK)); }

        @Override public boolean canStart() {
            if (isBaby() || !getWorld().getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)) return false;
            if (--cooldown > 0) return false;
            cooldown = 40;
            if (home == null) { home = getBlockPos(); setPositionTarget(home, 12); }
            
            Object currentNestObj = findHome();
            if (currentNestObj instanceof BeetleNestBlockEntity nest) {
                if (nest.isComplete() && nest.getFood() > 0 && getBreedingAge() == 0 && getCarrying() == 0) return false;
                if (nest.isComplete() && nest.getFood() >= BeetleNestBlockEntity.MAX_FOOD && getCarrying() == 0) return false;
            } else if (currentNestObj instanceof GrandBeetleNestBlockEntity grandNest) {
                if (getCarrying() == 0) {
                    sourceNest = null;
                    for (BlockPos candidate : BlockPos.iterate(home.add(-24, -4, -24), home.add(24, 4, 24))) {
                        if (getWorld().getBlockEntity(candidate) instanceof BeetleNestBlockEntity small) {
                            if (small.getOccupantCount() == 0 || small.getCachedState().get(BeetleNestBlock.STAGE) > 1) {
                                sourceNest = candidate.toImmutable(); 
                                break;
                            }
                        }
                    }
                    if (sourceNest != null) return true;
                }
            }
            
            if (getCarrying() > 0) {
                deposit = null;
                if (nestPos != null && canDeposit(nestPos)) {
                    deposit = nestPos;
                } else {
                    for (BlockPos candidate : BlockPos.iterate(home.add(-12, -4, -12), home.add(12, 4, 12))) {
                        if (getWorld().getBlockEntity(candidate) instanceof GrandBeetleNestBlockEntity) {
                            deposit = candidate.toImmutable();
                            break;
                        }
                    }
                    if (deposit == null) {
                        for (BlockPos candidate : BlockPos.iterate(home.add(-6, -1, -6), home.add(6, 1, 6))) {
                            if (!canDeposit(candidate)) continue;
                            Path path = getNavigation().findPathTo(candidate, 1);
                            if (path != null && path.reachesTarget()) { deposit = candidate.toImmutable(); break; }
                        }
                    }
                }
                return deposit != null;
            }
            
            target = getWorld().getEntitiesByClass(ItemEntity.class, getBoundingBox().expand(12, 3, 12),
                    item -> item.isAlive() && item.getStack().isOf(ModItems.POPO))
                    .stream().sorted(Comparator.comparingDouble(DungBeetleEntity.this::squaredDistanceTo))
                    .filter(item -> {
                        Path path = getNavigation().findPathTo(item, 0);
                        return path != null && path.reachesTarget();
                    }).findFirst().orElse(null);
            return target != null;
        }
        @Override public void start() { repath = 0; workingTicks = 0; }
        @Override public boolean shouldContinue() {
            return workingTicks < 300 && getWorld().getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)
                    && (getCarrying() > 0 ? deposit != null && canDeposit(deposit)
                    : (sourceNest != null && getWorld().getBlockState(sourceNest).isOf(ModBlocks.BEETLE_NEST)) 
                       || (target != null && target.isAlive() && target.getStack().isOf(ModItems.POPO) && squaredDistanceTo(target) < 400));
        }
        @Override public void tick() {
            if (!getWorld().getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)) return;
            workingTicks++;
            if (getCarrying() > 0) {
                if (deposit == null) return;
                if (--repath <= 0) { getNavigation().startMovingTo(deposit.getX() + 0.5, deposit.getY(), deposit.getZ() + 0.5, 0.85); repath = 20; }
                if (squaredDistanceTo(deposit.toCenterPos()) < 3.5 && canSeeNest(deposit)) {
                    BlockState old = getWorld().getBlockState(deposit);
                    boolean placed = false;
                    var be = getWorld().getBlockEntity(deposit);
                    if (be instanceof GrandBeetleNestBlockEntity grandNest) {
                        grandNest.deposit(new ItemStack(ModItems.POPO, 1));
                        placed = true;
                    } else if (old.isOf(ModBlocks.GRAND_BEETLE_NEST_PIECE) || old.isOf(ModBlocks.GRAND_BEETLE_NEST_CONTROLLER)) {
                        for (BlockPos c : BlockPos.iterate(deposit.add(-4, -4, -4), deposit.add(4, 4, 4))) {
                            if (getWorld().getBlockEntity(c) instanceof GrandBeetleNestBlockEntity gnb) {
                                gnb.deposit(new ItemStack(ModItems.POPO, 1));
                                placed = true;
                                break;
                            }
                        }
                    } else if (old.isOf(ModBlocks.BEETLE_NEST)) {
                        placed = be instanceof BeetleNestBlockEntity nest && nest.addMaterial();
                    } else {
                        placed = getWorld().setBlockState(deposit, ModBlocks.BEETLE_NEST.getDefaultState());
                    }
                        
                    if (placed) {
                        if (getWorld().getBlockEntity(deposit) instanceof GrandBeetleNestBlockEntity) {
                            setNest(deposit);
                        }
                        setCarrying(0);
                        playSound(ModSounds.BEETLE_CHIRP, 0.3F, 1.4F);
                    }
                    deposit = null;
                    target = null;
                }
            } else if (sourceNest != null) {
                if (--repath <= 0) { getNavigation().startMovingTo(sourceNest.getX() + 0.5, sourceNest.getY(), sourceNest.getZ() + 0.5, 0.85); repath = 20; }
                if (squaredDistanceTo(sourceNest.toCenterPos()) < 3.5 && canSeeNest(sourceNest)) {
                    var be = getWorld().getBlockEntity(sourceNest);
                    if (be instanceof BeetleNestBlockEntity nest) {
                        if (nest.getOccupantCount() == 0 || nest.getCachedState().get(BeetleNestBlock.STAGE) > 1) {
                            int stage = nest.getCachedState().get(BeetleNestBlock.STAGE);
                            if (stage == 5 && nest.getFood() > 0) {
                                nest.evacuate();
                                setCarrying(1);
                            } else {
                                if (stage > 1) {
                                    getWorld().setBlockState(sourceNest, nest.getCachedState().with(BeetleNestBlock.STAGE, stage - 1));
                                } else {
                                    nest.evacuate();
                                    getWorld().setBlockState(sourceNest, Blocks.AIR.getDefaultState());
                                }
                                setCarrying(1);
                            }
                        }
                    }
                    sourceNest = null;
                }
            } else if (target != null) {
                if (!target.isAlive() || target.getStack().isEmpty() || !target.getStack().isOf(ModItems.POPO)) {
                    target = null;
                    return;
                }
                getLookControl().lookAt(target, 30, 30);
                if (--repath <= 0) { getNavigation().startMovingTo(target, 1); repath = 20; }
                if (squaredDistanceTo(target) < 1.2 && canSee(target)) {
                    ItemStack rest = target.getStack().copy();
                    rest.decrement(1);
                    if (rest.isEmpty()) target.discard(); else target.setStack(rest);
                    setCarrying(1);
                    target = null;
                    deposit = null;
                }
            }
        }
        @Override public void stop() { getNavigation().stop(); target = null; deposit = null; sourceNest = null; cooldown = 20; }
    }
'''

code = re.sub(r'    private final class CollectGoal extends Goal \{.*?\n    \}', collect_goal.strip(), code, flags=re.DOTALL)

with open('src/main/java/org/examplee/proyecto_intento/entity/DungBeetleEntity.java', 'w', encoding='utf-8') as f:
    f.write(code)

print("Done")
