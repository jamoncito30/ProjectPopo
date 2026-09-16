package org.examplee.proyecto_intento.block;

import com.google.gson.JsonParser;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import net.minecraft.block.*;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

/** Visual building pieces only. Colony ownership and inventory are not implemented yet. */
public final class GrandBeetleNestPieceBlock extends Block {
    public static final IntProperty STAGE = IntProperty.of("stage", 1, 4);
    public static final IntProperty PIECE = IntProperty.of("piece", 0, 124);
    private static final VoxelShape[] SHAPES = loadShapes();

    public GrandBeetleNestPieceBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(STAGE, 1).with(PIECE, 0));
    }

    private static VoxelShape[] loadShapes() {
        try (var stream = GrandBeetleNestPieceBlock.class.getResourceAsStream("/assets/proyecto_intento/grand_nest_shapes.json")) {
            if (stream == null) throw new IllegalStateException("Missing grand nest geometry");
            var data = JsonParser.parseReader(new InputStreamReader(stream, StandardCharsets.UTF_8)).getAsJsonArray();
            if (data.size() != 500) throw new IllegalStateException("Expected 500 grand nest states");
            var shapes = new VoxelShape[500];
            for (int i = 0; i < shapes.length; i++) {
                VoxelShape shape = VoxelShapes.empty();
                for (var entry : data.get(i).getAsJsonArray()) {
                    var b = entry.getAsJsonArray();
                    shape = VoxelShapes.union(shape, createCuboidShape(b.get(0).getAsDouble(), b.get(1).getAsDouble(), b.get(2).getAsDouble(), b.get(3).getAsDouble(), b.get(4).getAsDouble(), b.get(5).getAsDouble()));
                }
                shapes[i] = shape.simplify();
            }
            return shapes;
        } catch (Exception e) { throw new IllegalStateException("Cannot load grand nest geometry", e); }
    }

    @Override protected void appendProperties(StateManager.Builder<Block, BlockState> builder) { builder.add(STAGE, PIECE); }
    @Override protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPES[(state.get(STAGE)-1)*125 + state.get(PIECE)];
    }
}
