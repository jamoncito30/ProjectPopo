package org.examplee.proyecto_intento.block;

import com.mojang.serialization.MapCodec;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import org.examplee.proyecto_intento.item.ModItems;

public final class PopoPileBlock extends Block {
    public static final IntProperty AMOUNT = IntProperty.of("amount", 1, 8);
    public static final MapCodec<PopoPileBlock> CODEC = createCodec(PopoPileBlock::new);
    public PopoPileBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(AMOUNT, 1));
    }
    @Override protected MapCodec<PopoPileBlock> getCodec() { return CODEC; }
    @Override protected void appendProperties(StateManager.Builder<Block, BlockState> builder) { builder.add(AMOUNT); }
    @Override protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return createCuboidShape(2, 0, 2, 14, 2 + state.get(AMOUNT), 14);
    }
    @Override protected List<ItemStack> getDroppedStacks(BlockState state, LootContextParameterSet.Builder builder) {
        return List.of(new ItemStack(ModItems.POPO, state.get(AMOUNT)));
    }
}
