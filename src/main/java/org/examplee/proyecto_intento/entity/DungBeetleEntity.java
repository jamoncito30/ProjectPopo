package org.examplee.proyecto_intento.entity;

import java.util.Comparator;
import java.util.EnumSet;
import net.minecraft.village.Merchant;
import net.minecraft.village.TradeOfferList;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradedItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.jetbrains.annotations.Nullable;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.AnimalMateGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.examplee.proyecto_intento.block.ModBlocks;
import org.examplee.proyecto_intento.block.PopoPileBlock;
import org.examplee.proyecto_intento.block.BeetleNestBlock;
import org.examplee.proyecto_intento.block.BeetleNestBlockEntity;
import org.examplee.proyecto_intento.item.ModItems;
import org.examplee.proyecto_intento.block.GrandBeetleNestBlockEntity;
import org.examplee.proyecto_intento.block.GrandBeetleNestControllerBlock;
import org.examplee.proyecto_intento.block.GrandBeetleNestPieceBlock;
import net.minecraft.block.Blocks;


public final class DungBeetleEntity extends AnimalEntity implements Merchant, InvasionParticipant {
    private final CombatState combatState = new CombatState();
    private static final TrackedData<Integer> COMBAT_ROLE = DataTracker.registerData(DungBeetleEntity.class, TrackedDataHandlerRegistry.INTEGER);
    public CombatState combat(){return combatState;}
    public int combatRole(){return dataTracker.get(COMBAT_ROLE);}
    public void combatRole(int role){dataTracker.set(COMBAT_ROLE,role);}
    private static final TrackedData<Integer> CARRYING = DataTracker.registerData(DungBeetleEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Boolean> TRADER = DataTracker.registerData(DungBeetleEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private boolean hasDroppedShell;
    private BlockPos home;
    private BlockPos nestPos;
    private int outsideTicks;
    private boolean nurseryResident;

    @Nullable
    private PlayerEntity customer;
    @Nullable
    private TradeOfferList offers;

    public DungBeetleEntity(EntityType<? extends AnimalEntity> type, World world) { super(type, world); }
    public static DefaultAttributeContainer.Builder createAttributes() {
        return AnimalEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 8)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.22).add(EntityAttributes.GENERIC_FOLLOW_RANGE, 16);
    }
    @Override protected void initDataTracker(DataTracker.Builder builder) { super.initDataTracker(builder); builder.add(CARRYING, 0); builder.add(TRADER,false); builder.add(COMBAT_ROLE,0); }
    public boolean isTrader() { return dataTracker.get(TRADER); }
    public void setTrader(boolean value) { dataTracker.set(TRADER,value); }
    public int getCarrying() { return dataTracker.get(CARRYING); }
    private void setCarrying(int value) { dataTracker.set(CARRYING, value); }
    @Override protected void initGoals() {
        goalSelector.add(0, new SwimGoal(this));
        goalSelector.add(1, new AnimalMateGoal(this, 1));
        goalSelector.add(2, new CollectGoal());
        goalSelector.add(3, new ShelterGoal());
        goalSelector.add(6, new WanderAroundFarGoal(this, 0.7));
        goalSelector.add(7, new LookAroundGoal(this));
    }
    @Override public boolean isBreedingItem(ItemStack stack) { return stack.isOf(Items.WHEAT_SEEDS); }
    @Override public PassiveEntity createChild(ServerWorld world, PassiveEntity other) { return ModEntities.DUNG_BEETLE.create(world); }
    @Override protected SoundEvent getAmbientSound() { return ModSounds.BEETLE_CHIRP; }
    @Override public int getMinAmbientSoundDelay() { return 240; }
    @Override protected float getSoundVolume() { return 0.22F; }
    @Override protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.ENTITY_SILVERFISH_HURT; }
    @Override protected SoundEvent getDeathSound() { return SoundEvents.ENTITY_SILVERFISH_DEATH; }
    @Override protected void playStepSound(BlockPos pos, BlockState state) {
        playSound(getCarrying() > 0 ? ModSounds.BEETLE_ROLL : ModSounds.BEETLE_STEP, 0.12F, 1.2F);
    }
    @Override protected void dropInventory() {
        super.dropInventory();
        if (getCarrying() > 0) { dropStack(new ItemStack(ModItems.POPO, getCarrying())); setCarrying(0); }
    }
    @Override public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        combatState.write(nbt,combatRole());
        nbt.putBoolean("IsTrader", isTrader());
        nbt.putBoolean("HasDroppedShell", hasDroppedShell);
        nbt.putInt("CarriedPopo", getCarrying());
        if (home != null) nbt.putLong("PopoHome", home.asLong());
        if (nestPos != null) nbt.putLong("BeetleNest", nestPos.asLong());
        nbt.putInt("OutsideTicks", outsideTicks);
        nbt.putBoolean("NurseryResident", nurseryResident);
    }
    @Override public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        combatRole(combatState.read(nbt));
        setTrader(nbt.getBoolean("IsTrader"));
        hasDroppedShell=nbt.getBoolean("HasDroppedShell");
        
        
        setCarrying(Math.clamp(nbt.getInt("CarriedPopo"), 0, 1));
        home = nbt.contains("PopoHome") ? BlockPos.fromLong(nbt.getLong("PopoHome")) : null;
        if (home != null) setPositionTarget(home, 12);
        nestPos = nbt.contains("BeetleNest") ? BlockPos.fromLong(nbt.getLong("BeetleNest")) : null;
        outsideTicks = Math.clamp(nbt.getInt("OutsideTicks"), 0, 1200);
        nurseryResident = nbt.getBoolean("NurseryResident");
    }

    public BlockPos getNestPos() { return nestPos; }
    public void setNest(BlockPos pos) {
        nestPos = pos.toImmutable();
        nurseryResident = isBaby();
        home = nestPos;
        setPositionTarget(home, 12);
    }
    public void onNestExit(BlockPos pos, boolean destroyed) {
        home = pos.toImmutable();
        nestPos = destroyed ? null : home;
        outsideTicks = 1200;
        setPositionTarget(home, 12);
    }

    @Override public void tickMovement() {
        super.tickMovement();
        if (!getWorld().isClient && outsideTicks > 0) outsideTicks--;
        if (!getWorld().isClient && combatState.event == null && (getWorld().getTime()+getId()) % 200 == 0) {
            org.examplee.proyecto_intento.entity.AdvancementHelper.grantNearby((ServerWorld)getWorld(), getBlockPos(), "find_beetle");
            tryInitiateGrandNest();
        }
    }
    
    private void tryInitiateGrandNest() {
        if(getWorld().isClient || isBaby() || isRemoved()) return;
        if (!getWorld().getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)) return;
        
        // Count fully built nests
        int builtNests = 0;
        BlockPos center = getBlockPos();
        for (BlockPos p : BlockPos.iterate(center.add(-16, -4, -16), center.add(16, 4, 16))) {
            if(!getWorld().isChunkLoaded(p)) continue;
            if (getWorld().getBlockEntity(p) instanceof BeetleNestBlockEntity nest && nest.isComplete()) {
                builtNests++;
            }
            if (getWorld().getBlockEntity(p) instanceof GrandBeetleNestBlockEntity) {
                return; // already exists
            }
        }
        
        if (builtNests >= 4) org.examplee.proyecto_intento.entity.AdvancementHelper.grantNearby((ServerWorld)getWorld(), getBlockPos(), "small_town");
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
                        org.examplee.proyecto_intento.entity.AdvancementHelper.grantNearby((ServerWorld)getWorld(), controllerPos, "start_grand_nest");
                    }
                    return;
                }
            }
        }
    }
    
    private boolean validGrandNestSpot(BlockPos origin) {
        return getWorld() instanceof ServerWorld server && GrandBeetleNestBlockEntity.isValidSite(server,origin);
    }

    private int otherAdults(BlockPos pos, BeetleNestBlockEntity nest) {
        return nest.getAdultCount() + getWorld().getEntitiesByClass(DungBeetleEntity.class,
                new net.minecraft.util.math.Box(pos).expand(24), e -> e != this && e.isAlive() && !e.isBaby()
                        && !e.nurseryResident && pos.equals(e.nestPos)).size();
    }
    private Object findHome() {
        if (home == null) { home = getBlockPos(); setPositionTarget(home, 12); }
        if (nurseryResident && !isBaby()) { nestPos = null; nurseryResident = false; }
        if(nestPos!=null && getWorld().isChunkLoaded(nestPos)
                && getWorld().getBlockEntity(nestPos) instanceof GrandBeetleNestBlockEntity existing
                ) return existing;
        
        // Search for grand nest first EVERY TIME (forces migration)
        for (BlockPos candidate : BlockPos.iterate(home.add(-24, -4, -24), home.add(24, 4, 24))) {
            if (getWorld().isChunkLoaded(candidate)) {
                if (getWorld().getBlockEntity(candidate) instanceof GrandBeetleNestBlockEntity grandNest
                        ) {
                    BlockPos approach = grandNest.approach(this);
                    Path path = approach == null ? null : getNavigation().findPathTo(approach, 0);
                    if (path != null && path.reachesTarget()) { setNest(candidate); return grandNest; }
                }
            }
        }

        // Return current nest if valid
        if (nestPos != null && getWorld().isChunkLoaded(nestPos)) {
            var be = getWorld().getBlockEntity(nestPos);
            if (be instanceof BeetleNestBlockEntity nest && (isBaby() || otherAdults(nestPos, nest) < 2)) {
                return nest;
            }
            nestPos = null;
        }
        
        // Small nest (ONLY IF HOMELESS! PREVENTS CRAMMING/FREEZING)
        if (nestPos == null) {
            for (BlockPos candidate : BlockPos.iterate(home.add(-8, -2, -8), home.add(8, 2, 8))) {
                if (!getWorld().isChunkLoaded(candidate)) continue;
                if (getWorld().getBlockEntity(candidate) instanceof BeetleNestBlockEntity nest
                        && nest.getOccupantCount() < BeetleNestBlockEntity.CAPACITY && otherAdults(candidate, nest) < 2) {
                    Path path = getNavigation().findPathTo(candidate, 1);
                    if (path != null && path.reachesTarget()) { setNest(candidate); return nest; }
                }
            }
        }
        return null;
    }

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

    private final class ShelterGoal extends Goal {
        private int cooldown;
        private int travelTicks;
        ShelterGoal() { setControls(EnumSet.of(Control.MOVE)); }
        @Override public boolean canStart() {
            if (--cooldown > 0 || hasPassengers() || hasVehicle() || isLeashed()) return false;
            cooldown = 60;
            
            // Circadian Rhythm: Sleep at night (unless they just left the nest)
            boolean isNightTime = !getWorld().isDay();
            boolean wantsToSleep = isNightTime && outsideTicks < 1000; 
            
            // If they are carrying poop but the sun sets, they hold onto it and go home to sleep/deposit
            Object nest = findHome();
            
            if (nest instanceof GrandBeetleNestBlockEntity grandNest) {
                return (wantsToSleep || outsideTicks == 0 || (!isBaby() && getBreedingAge() == 0 && grandNest.getFoodCount() > 0));
            } else if (nest instanceof BeetleNestBlockEntity smallNest) {
                return smallNest.isComplete() && smallNest.getOccupantCount() < BeetleNestBlockEntity.CAPACITY
                    && (wantsToSleep || outsideTicks == 0 || (!isBaby() && getBreedingAge() == 0 && smallNest.getFood() > 0));
            }
            return false;
        }
        @Override public void start() { travelTicks = 0; }
        @Override public boolean shouldContinue() { return !isRemoved() && nestPos != null && travelTicks < 240; }
        @Override public void tick() {
            travelTicks++;
            var be = getWorld().getBlockEntity(nestPos);
            if (be instanceof GrandBeetleNestBlockEntity grandNest) {
                if (travelTicks % 20 == 1) grandNest.navigateToPerimeter(DungBeetleEntity.this, 1.1);
                if (grandNest.atPerimeter(DungBeetleEntity.this)) {
                    grandNest.tryEnter(DungBeetleEntity.this);
                    travelTicks = 240;
                }
            } else if (be instanceof BeetleNestBlockEntity nest && nest.isComplete()) {
                if (!getNavigation().isFollowingPath()) getNavigation().startMovingTo(nestPos.getX() + .5, nestPos.getY(), nestPos.getZ() + .5, .9);
                if (squaredDistanceTo(nestPos.toCenterPos()) < 12 && canSeeNest(nestPos)) {
                    nest.tryEnter(DungBeetleEntity.this);
                    travelTicks = 240;
                }
            } else { travelTicks = 240; }
        }
        @Override public void stop() { getNavigation().stop(); cooldown = 60; }
    }

    private boolean canSeeNest(BlockPos pos) {
        var hit = getWorld().raycast(new net.minecraft.world.RaycastContext(getEyePos(), pos.toCenterPos(),
                net.minecraft.world.RaycastContext.ShapeType.COLLIDER, net.minecraft.world.RaycastContext.FluidHandling.NONE, this));
        if (hit.getType() == net.minecraft.util.hit.HitResult.Type.MISS || hit.getBlockPos().equals(pos)) return true;
        BlockState hitState = getWorld().getBlockState(hit.getBlockPos());
        return hitState.isOf(ModBlocks.GRAND_BEETLE_NEST_PIECE) || hitState.isOf(ModBlocks.GRAND_BEETLE_NEST_CONTROLLER) || hitState.isOf(ModBlocks.BEETLE_NEST);
    }

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
            if (!getWorld().isDay()) return false; // Diurnal: only collect during day
            if (--cooldown > 0) return false;
            cooldown = 40;
            if (home == null) { home = getBlockPos(); setPositionTarget(home, 12); }
            
            Object currentNestObj = findHome();
            if (currentNestObj instanceof BeetleNestBlockEntity nest) {
                if (nest.isComplete() && nest.getFood() > 0 && getBreedingAge() == 0 && getCarrying() == 0) return false;
                if (nest.isComplete() && nest.getFood() >= BeetleNestBlockEntity.MAX_FOOD && getCarrying() == 0) return false;
            } else if (currentNestObj instanceof GrandBeetleNestBlockEntity grandNest) {
                if (getCarrying() == 0 && !grandNest.isComplete()) {
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
                boolean isGrand = getWorld().getBlockState(deposit).isOf(ModBlocks.GRAND_BEETLE_NEST_CONTROLLER) || getWorld().getBlockState(deposit).isOf(ModBlocks.GRAND_BEETLE_NEST_PIECE);
                if (squaredDistanceTo(deposit.toCenterPos()) < (isGrand ? 16.0 : 4.0) && canSeeNest(deposit)) {
                    BlockState old = getWorld().getBlockState(deposit);
                    boolean placed = false;
                    var be = getWorld().getBlockEntity(deposit);
                    if (be instanceof GrandBeetleNestBlockEntity grandNest) {
                        placed = grandNest.deposit(new ItemStack(ModItems.POPO, 1)) == 1;
                    } else if (old.isOf(ModBlocks.GRAND_BEETLE_NEST_PIECE) || old.isOf(ModBlocks.GRAND_BEETLE_NEST_CONTROLLER)) {
                        for (BlockPos c : BlockPos.iterate(deposit.add(-4, -4, -4), deposit.add(4, 4, 4))) {
                            if (getWorld().getBlockEntity(c) instanceof GrandBeetleNestBlockEntity gnb) {
                                placed = gnb.deposit(new ItemStack(ModItems.POPO, 1)) == 1;
                                break;
                            }
                        }
                    } else if (old.isOf(ModBlocks.BEETLE_NEST)) {
                        placed = be instanceof BeetleNestBlockEntity nest && nest.addMaterial();
                    } else {
                        placed = getWorld().setBlockState(deposit, ModBlocks.BEETLE_NEST.getDefaultState());
                        if (placed) org.examplee.proyecto_intento.entity.AdvancementHelper.grantNearby((ServerWorld)getWorld(), deposit, "first_small_nest");
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
                        if (nest.takeMaterialForGrandNest()) setCarrying(1);
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
                    
                    // Celestial orientation dance! Real dung beetles look at the sky/sun to orient themselves.
                    getLookControl().lookAt(getX(), getY() + 50, getZ(), 90.0F, 90.0F); 
                    getNavigation().stop();
                    repath = 40; // Freeze for 2 seconds orienting
                }
            }
        }
        @Override public void stop() { getNavigation().stop(); target = null; deposit = null; sourceNest = null; cooldown = 20; }
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        
        if (itemStack.isOf(Items.NAME_TAG) || this.isBreedingItem(itemStack)) {
            ActionResult result = super.interactMob(player, hand);
            if (result.isAccepted()) return result;
            if (this.isBreedingItem(itemStack)) return result;
        }
        
        if (combatState.event != null) return ActionResult.FAIL;
        if (this.isTrader() && this.isAlive() && !this.hasCustomer() && !this.isBaby()) {
            if (!this.getOffers().isEmpty()) {
                if (!this.getWorld().isClient) {
                    this.setCustomer(player);
                    this.sendOffers(player, this.getDisplayName(), 1);
                }
                return ActionResult.success(this.getWorld().isClient);
            }
        }
        
        return super.interactMob(player, hand);
    }
    
    public boolean hasCustomer() {
        return this.customer != null;
    }

    @Override
    public void setCustomer(@Nullable PlayerEntity customer) {
        this.customer = customer;
    }

    @Override
    @Nullable
    public PlayerEntity getCustomer() {
        return this.customer;
    }

    @Override
    public TradeOfferList getOffers() {
        if (this.offers == null) {
            this.offers = new TradeOfferList();
            this.offers.add(new TradeOffer(new TradedItem(Items.EMERALD, 1), new ItemStack(ModItems.POPO, 4), 10, 5, 0.05f));
            this.offers.add(new TradeOffer(new TradedItem(ModItems.POPO, 10), new ItemStack(Items.EMERALD, 1), 10, 5, 0.05f));
            this.offers.add(new TradeOffer(new TradedItem(Items.EMERALD, 3), new ItemStack(ModItems.ESTIERCOL, 1), 10, 5, 0.05f));
        }
        return this.offers;
    }

    @Override
    public void setOffersFromServer(@Nullable TradeOfferList offers) {
        this.offers = offers;
    }

    @Override
    public void trade(TradeOffer offer) {
        offer.use();
        this.ambientSoundChance = -this.getMinAmbientSoundDelay();
        this.onSellingItem(offer.getSellItem());
    }

    @Override
    public void onSellingItem(ItemStack stack) {
        if (!this.getWorld().isClient && this.ambientSoundChance > -this.getMinAmbientSoundDelay() + 20) {
            this.ambientSoundChance = -this.getMinAmbientSoundDelay();
            this.playSound(this.getYesSound(), this.getSoundVolume(), this.getSoundPitch());
        }
    }

    @Override
    public int getExperience() {
        return 0;
    }

    @Override
    public void setExperienceFromServer(int experience) {
    }

    @Override
    public boolean isLeveledMerchant() {
        return false;
    }

    @Override
    public SoundEvent getYesSound() {
        return ModSounds.BEETLE_CHIRP;
    }

    @Override
    public boolean isClient() {
        return this.getWorld().isClient;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        if (source.isOf(net.minecraft.entity.damage.DamageTypes.IN_WALL)) return true;
        return super.isInvulnerableTo(source);
    }
}
