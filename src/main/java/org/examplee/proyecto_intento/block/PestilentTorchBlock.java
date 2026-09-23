package org.examplee.proyecto_intento.block;

import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.entity.mob.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.*;
import com.mojang.serialization.MapCodec;

public final class PestilentTorchBlock extends BlockWithEntity {
    public static final MapCodec<PestilentTorchBlock> CODEC=createCodec(PestilentTorchBlock::new);
    public PestilentTorchBlock(Settings settings) { super(settings); }
    @Override protected MapCodec<PestilentTorchBlock> getCodec() { return CODEC; }
    @Override protected BlockRenderType getRenderType(BlockState state) { return BlockRenderType.MODEL; }
    @Override protected VoxelShape getOutlineShape(BlockState s,BlockView w,BlockPos p,ShapeContext c) { return createCuboidShape(6,0,6,10,14,10); }
    @Override public BlockEntity createBlockEntity(BlockPos p,BlockState s) { return new PestilentTorchBlockEntity(p,s); }
    @Override public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World w,BlockState s,BlockEntityType<T> t) { return w.isClient?null:validateTicker(t,ModBlocks.PESTILENT_TORCH_ENTITY,PestilentTorchBlockEntity::tick); }
    @Override protected void onBlockAdded(BlockState s,World w,BlockPos p,BlockState old,boolean notify) {
        if(!w.isClient) org.examplee.proyecto_intento.entity.TorchRepulsion.add(w,p);
    }
    @Override protected void onStateReplaced(BlockState s,World w,BlockPos p,BlockState next,boolean moved) {
        if(!s.isOf(next.getBlock())) org.examplee.proyecto_intento.entity.TorchRepulsion.remove(w,p);
        super.onStateReplaced(s,w,p,next,moved);
    }
}
