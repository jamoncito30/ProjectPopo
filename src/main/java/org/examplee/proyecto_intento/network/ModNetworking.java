package org.examplee.proyecto_intento.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.examplee.proyecto_intento.block.ToiletBlock;
import org.examplee.proyecto_intento.entity.DigestionSystem;
import org.examplee.proyecto_intento.entity.ToiletSeatEntity;
import org.examplee.proyecto_intento.item.ModItems;

public class ModNetworking {
    public static void register() {
        PayloadTypeRegistry.playC2S().register(ToiletPoopPayload.ID, ToiletPoopPayload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(ToiletPoopPayload.ID, (payload, context) -> {
            ServerPlayerEntity player = context.player();
            context.server().execute(() -> {
                if (player.getVehicle() instanceof ToiletSeatEntity seatEntity) {
                    boolean hasEaten = DigestionSystem.hasEaten(player);
                    int ticks = DigestionSystem.getTicks(player);

                    if (ticks >= DigestionSystem.MAX_TICKS && hasEaten) {
                        // Drop poop
                        player.dropItem(new ItemStack(ModItems.POPO), false, true);
                        DigestionSystem.reset(player);
                        
                        // Check for clog
                        if (player.getWorld().random.nextFloat() < 0.25f) {
                            var state = player.getWorld().getBlockState(seatEntity.getBlockPos());
                            if (state.getBlock() instanceof ToiletBlock && !state.get(ToiletBlock.CLOGGED)) {
                                player.getWorld().setBlockState(seatEntity.getBlockPos(), state.with(ToiletBlock.CLOGGED, true));
                            }
                        }
                    } else {
                        // Calculate percentage
                        int percentage = (int) ((ticks / (float) DigestionSystem.MAX_TICKS) * 100);
                        if (!hasEaten) {
                            percentage = 0;
                        }
                        player.sendMessage(Text.literal("Ganas de ir al baño: " + percentage + "%"), true);
                    }
                }
            });
        });
    }
}
