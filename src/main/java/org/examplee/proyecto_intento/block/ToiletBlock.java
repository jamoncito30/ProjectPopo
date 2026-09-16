package org.examplee.proyecto_intento.block;

import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.*;
import net.minecraft.world.*;
import org.examplee.proyecto_intento.entity.ToiletSeatEntity;
import org.examplee.proyecto_intento.item.ModItems;
import org.examplee.proyecto_intento.item.PlungerItem;

public final class ToiletBlock extends Block {
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public static final BooleanProperty CLOGGED = BooleanProperty.of("clogged");
    public ToiletBlock(Settings settings) { super(settings); setDefaultState(getStateManager().getDefaultState().with(FACING,Direction.NORTH).with(CLOGGED,false)); }
    @Override protected void appendProperties(StateManager.Builder<Block,BlockState> b) { b.add(FACING,CLOGGED); }
    @Override public BlockState getPlacementState(ItemPlacementContext c) { return getDefaultState().with(FACING,c.getHorizontalPlayerFacing().getOpposite()); }
    @Override protected BlockState rotate(BlockState s,BlockRotation r) { return s.with(FACING,r.rotate(s.get(FACING))); }
    @Override protected BlockState mirror(BlockState s,BlockMirror m) { return s.rotate(m.getRotation(s.get(FACING))); }
    @Override protected VoxelShape getOutlineShape(BlockState s,BlockView w,BlockPos p,ShapeContext c) {
        VoxelShape bowl=VoxelShapes.union(createCuboidShape(4,0,4,12,5,12),createCuboidShape(2,5,2,14,10,14));
        VoxelShape tank=switch(s.get(FACING)) {
            case NORTH -> createCuboidShape(2,0,12,14,16,16);
            case SOUTH -> createCuboidShape(2,0,0,14,16,4);
            case EAST -> createCuboidShape(0,0,2,4,16,14);
            default -> createCuboidShape(12,0,2,16,16,14);
        };
        return VoxelShapes.union(bowl,tank);
    }
    @Override protected ActionResult onUse(BlockState s,World w,BlockPos p,PlayerEntity player,BlockHitResult hit) {
        if (player.isSneaking()) return ActionResult.PASS;
        if (w.isClient) return ActionResult.SUCCESS;
        return ToiletSeatEntity.sit(w,p,player) ? ActionResult.CONSUME : ActionResult.FAIL;
    }
    @Override protected ItemActionResult onUseWithItem(ItemStack stack,BlockState s,World w,BlockPos p,PlayerEntity player,Hand hand,BlockHitResult hit) {
        if (stack.isOf(ModItems.DESATASCADOR)) {
            PlungerItem.unclog(w,p,player,hand);
            return ItemActionResult.SUCCESS;
        }
        return ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }
    @Override protected void onStateReplaced(BlockState s,World w,BlockPos p,BlockState next,boolean moved) {
        if (!s.isOf(next.getBlock()) && !w.isClient) {
            for(var seat:w.getEntitiesByClass(ToiletSeatEntity.class,new Box(p),e->e.getBlockPos().equals(p))) { seat.removeAllPassengers(); seat.discard(); }
        }
        super.onStateReplaced(s,w,p,next,moved);
    }
}
