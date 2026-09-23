package org.examplee.proyecto_intento.entity;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DigestionSystem {
    private static final Map<UUID, Integer> digestionTicks = new HashMap<>();
    private static final Map<UUID, Boolean> hasEaten = new HashMap<>();
    
    public static final int MAX_TICKS = 12000;
    
    public static void initialize() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                UUID id = player.getUuid();
                if (hasEaten.getOrDefault(id, false)) {
                    int ticks = digestionTicks.getOrDefault(id, 0);
                    if (ticks < MAX_TICKS) {
                        digestionTicks.put(id, ticks + 1);
                    }
                }
            }
        });
    }
    
    public static void onPlayerEat(PlayerEntity player) {
        if (!player.getWorld().isClient()) {
            hasEaten.put(player.getUuid(), true);
        }
    }
    
    public static int getTicks(PlayerEntity player) {
        return digestionTicks.getOrDefault(player.getUuid(), 0);
    }
    
    public static boolean hasEaten(PlayerEntity player) {
        return hasEaten.getOrDefault(player.getUuid(), false);
    }
    
    public static void reset(PlayerEntity player) {
        UUID id = player.getUuid();
        digestionTicks.put(id, 0);
        hasEaten.put(id, false);
    }
}
