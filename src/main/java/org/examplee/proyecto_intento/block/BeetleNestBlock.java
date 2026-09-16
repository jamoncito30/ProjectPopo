package org.examplee.proyecto_intento.block;

import com.mojang.serialization.MapCodec;
import java.util.List;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.*;
import org.examplee.proyecto_intento.item.ModItems;

public final class BeetleNestBlock extends BlockWithEntity {
    public static final IntProperty STAGE = IntProperty.of("stage", 1, 5);
    public static final MapCodec<BeetleNestBlock> CODEC = createCodec(BeetleNestBlock::new);
    public BeetleNestBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(STAGE, 1));
    }
    @Override protected MapCodec<BeetleNestBlock> getCodec() { return CODEC; }
    @Override protected void appendProperties(StateManager.Builder<Block, BlockState> builder) { builder.add(STAGE); }
    @Override protected BlockRenderType getRenderType(BlockState state) { return BlockRenderType.MODEL; }
    @Override public BlockEntity createBlockEntity(BlockPos pos, BlockState state) { return new BeetleNestBlockEntity(pos, state); }
    @Override public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? null : validateTicker(type, ModBlocks.BEETLE_NEST_ENTITY, BeetleNestBlockEntity::tick);
    }
    @Override protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return createCuboidShape(1, 0, 1, 15, 2 + state.get(STAGE) * 2, 15);
    }
    @Override protected List<ItemStack> getDroppedStacks(BlockState state, LootContextParameterSet.Builder builder) {
        return List.of(new ItemStack(ModItems.POPO, state.get(STAGE)));
    }
    @Override protected void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState next, boolean moved) {
        if (!state.isOf(next.getBlock()) && world.getBlockEntity(pos) instanceof BeetleNestBlockEntity nest) {
            nest.evacuate();
        }
        super.onStateReplaced(state, world, pos, next, moved);
    }
}
