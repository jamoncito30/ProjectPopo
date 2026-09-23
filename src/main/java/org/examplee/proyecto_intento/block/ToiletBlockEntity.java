package org.examplee.proyecto_intento.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.examplee.proyecto_intento.entity.*;

public final class ToiletBlockEntity extends BlockEntity {
    public static final int INFESTATION_TICKS = 72000;
    private int cloggedTicks;
    private int useTicks;
    public ToiletBlockEntity(BlockPos pos,BlockState state) { super(ModBlocks.TOILET_ENTITY,pos,state); }
    public int getCloggedTicks() { return cloggedTicks; }
    public void resetInfestation() { cloggedTicks=0; markDirty(); }
    public static void tick(World world,BlockPos pos,BlockState state,ToiletBlockEntity toilet) {
        if (!state.get(ToiletBlock.CLOGGED)) {
            if(toilet.cloggedTicks!=0) toilet.resetInfestation();
            return;
        }
        if (toilet.cloggedTicks < INFESTATION_TICKS) { toilet.cloggedTicks++; toilet.markDirty(); }
        if (toilet.cloggedTicks < INFESTATION_TICKS || world.getTime()%100!=0
                || world.getDifficulty()==Difficulty.PEACEFUL || !world.getGameRules().getBoolean(GameRules.DO_MOB_SPAWNING)) return;
        if (world.getEntitiesByClass(FetidSlimeEntity.class,new Box(pos).expand(8),e->e.isAlive()).size()>=2) return;
        for (Direction direction:Direction.Type.HORIZONTAL) {
            var destination=pos.offset(direction).toBottomCenterPos();
            if (!world.isChunkLoaded(BlockPos.ofFloored(destination)) || !world.getBlockState(BlockPos.ofFloored(destination).down()).isSolidBlock(world,BlockPos.ofFloored(destination).down())) continue;
            var isAlpha = world.random.nextFloat() < 0.1f;
            var slime = isAlpha ? ModEntities.FETID_SLIME_ALPHA.create(world) : ModEntities.FETID_SLIME.create(world);
            if (slime==null) return;
            if (slime instanceof net.minecraft.entity.mob.SlimeEntity slimeEntity) {
                slimeEntity.setSize(isAlpha ? 4 : 1, true);
            }
            slime.setPosition(destination);
            if (world.isSpaceEmpty(slime) && !world.containsFluid(slime.getBoundingBox()) && world.spawnEntity(slime)) { toilet.resetInfestation(); return; }
        }
    }
    @Override protected void writeNbt(NbtCompound nbt,RegistryWrapper.WrapperLookup registries) { super.writeNbt(nbt,registries); nbt.putInt("CloggedTicks",cloggedTicks); nbt.putInt("UseTicks",useTicks); }
    @Override protected void readNbt(NbtCompound nbt,RegistryWrapper.WrapperLookup registries) { super.readNbt(nbt,registries); cloggedTicks=Math.clamp(nbt.getInt("CloggedTicks"),0,INFESTATION_TICKS); useTicks=Math.clamp(nbt.getInt("UseTicks"),0,17999); }
}
