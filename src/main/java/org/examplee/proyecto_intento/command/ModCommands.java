package org.examplee.proyecto_intento.command;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.block.Blocks;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.examplee.proyecto_intento.block.ModBlocks;
import org.examplee.proyecto_intento.block.GrandBeetleNestBlockEntity;
import org.examplee.proyecto_intento.block.GrandBeetleNestPieceBlock;
import org.examplee.proyecto_intento.entity.DungBeetleEntity;
import org.examplee.proyecto_intento.entity.ModEntities;

public class ModCommands {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("spawn_grand_nest")
                .requires(source -> source.hasPermissionLevel(2))
                .executes(context -> {
                    ServerCommandSource source = context.getSource();
                    ServerPlayerEntity player = source.getPlayer();
                    if (player == null) return 0;
                    
                    ServerWorld world = player.getServerWorld();
                    BlockPos center = player.getBlockPos().add(0, 0, 0); // Spawn exactly where player stands
                    BlockPos origin = center.add(-2, 0, -2);
                    
                    // Force a 5x5 flat foundation
                    for (int x = 0; x < 5; x++) {
                        for (int z = 0; z < 5; z++) {
                            // Floor
                            world.setBlockState(origin.add(x, -1, z), Blocks.GRASS_BLOCK.getDefaultState());
                            // Clear 5x5x5 space
                            for (int y = 0; y < 5; y++) {
                                world.setBlockState(origin.add(x, y, z), Blocks.AIR.getDefaultState());
                            }
                        }
                    }

                    // Set controller piece
                    world.setBlockState(center, ModBlocks.GRAND_BEETLE_NEST_CONTROLLER.getDefaultState().with(GrandBeetleNestPieceBlock.STAGE, 1));
                    if (world.getBlockEntity(center) instanceof GrandBeetleNestBlockEntity grandNest) {
                        grandNest.setOrigin(origin);
                        grandNest.updateStructure(1);
                    }
                    
                    // Spawn some helper beetles
                    for (int i = 0; i < 4; i++) {
                        DungBeetleEntity beetle = ModEntities.DUNG_BEETLE.create(world);
                        if (beetle != null) {
                            beetle.refreshPositionAndAngles(
                                center.getX() + (world.random.nextDouble() - 0.5) * 8, 
                                center.getY(), 
                                center.getZ() + (world.random.nextDouble() - 0.5) * 8, 
                                0, 0
                            );
                            beetle.setNest(center);
                            world.spawnEntity(beetle);
                        }
                    }
                    
                    source.sendMessage(Text.literal("Gran nido y equipo de escarabajos desplegados con xito!"));
                    return 1;
                })
            );
        });
    }
}
