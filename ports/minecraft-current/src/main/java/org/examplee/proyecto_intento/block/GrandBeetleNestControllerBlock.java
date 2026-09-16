package org.examplee.proyecto_intento.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.state.StateManager;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.*;

/** Replaces central floor piece 12. Does not yet validate a whole colony structure. */
public final class GrandBeetleNestControllerBlock extends BlockWithEntity {
    public static final MapCodec<GrandBeetleNestControllerBlock> CODEC = createCodec(GrandBeetleNestControllerBlock::new);
    public GrandBeetleNestControllerBlock(Settings settings) { super(settings); setDefaultState(getStateManager().getDefaultState().with(GrandBeetleNestPieceBlock.STAGE, 1)); }
    @Override protected MapCodec<GrandBeetleNestControllerBlock> getCodec() { return CODEC; }
    @Override protected void appendProperties(StateManager.Builder<Block, BlockState> builder) { builder.add(GrandBeetleNestPieceBlock.STAGE); }
    @Override protected BlockRenderType getRenderType(BlockState state) { return BlockRenderType.MODEL; }
    @Override public BlockEntity createBlockEntity(BlockPos pos, BlockState state) { return new GrandBeetleNestBlockEntity(pos, state); }
    @Override protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return ModBlocks.GRAND_BEETLE_NEST_PIECE.getDefaultState().with(GrandBeetleNestPieceBlock.PIECE,12).with(GrandBeetleNestPieceBlock.STAGE,state.get(GrandBeetleNestPieceBlock.STAGE)).getOutlineShape(world,pos,context);
    }
    @Override protected void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState next, boolean moved) {
        if (!state.isOf(next.getBlock()) && world.getBlockEntity(pos) instanceof GrandBeetleNestBlockEntity nest) {
            nest.evacuateAndDismantle();
            if (!world.isClient) ItemScatterer.spawn(world,pos,nest);
            nest.clear();
            world.updateComparators(pos,this);
        }
        super.onStateReplaced(state,world,pos,next,moved);
    }
    
    @Override
    public <T extends BlockEntity> net.minecraft.block.entity.BlockEntityTicker<T> getTicker(World world, BlockState state, net.minecraft.block.entity.BlockEntityType<T> type) {
        return world.isClient ? null : validateTicker(type, ModBlocks.GRAND_BEETLE_NEST_ENTITY, GrandBeetleNestBlockEntity::tick);
    }
}
