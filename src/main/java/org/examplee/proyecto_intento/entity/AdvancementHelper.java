package org.examplee.proyecto_intento.entity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class AdvancementHelper {
    public static void grantNearby(ServerWorld world, BlockPos pos, String id) {
        Identifier advId = Identifier.of("proyecto_intento", id);
        for (PlayerEntity player : world.getPlayers()) {
            if (player.squaredDistanceTo(pos.toCenterPos()) < 4096) {
                if (player instanceof ServerPlayerEntity sp) {
                    var entry = sp.server.getAdvancementLoader().get(advId);
                    if (entry != null) {
                        var progress = sp.getAdvancementTracker().getProgress(entry);
                        if (!progress.isDone()) {
                            for (String crit : progress.getUnobtainedCriteria()) {
                                sp.getAdvancementTracker().grantCriterion(entry, crit);
                            }
                        }
                    }
                }
            }
        }
    }
}
