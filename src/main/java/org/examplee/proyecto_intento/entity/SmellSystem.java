package org.examplee.proyecto_intento.entity;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.examplee.proyecto_intento.item.ModArmor;

public final class SmellSystem {
    public static final String CLEAN_UNTIL = "PopoCraftCleanUntil";

    public static boolean isArmorSmelly(LivingEntity entity) {
        if (!ModArmor.hasFullSet(entity)) return false;
        long now = entity.getWorld().getTime();
        for (ItemStack piece : entity.getArmorItems()) {
            if (piece.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT)
                    .copyNbt().getLong(CLEAN_UNTIL) <= now) return true;
        }
        return false;
    }

    public static boolean isSmelly(LivingEntity entity) {
        return entity.hasStatusEffect(ModEffects.STINKY) || isArmorSmelly(entity);
    }

    public static void wash(LivingEntity entity) {
        long until = entity.getWorld().getTime() + 3600 + entity.getRandom().nextInt(2401);
        for (ItemStack piece : entity.getArmorItems()) {
            if (ModArmor.isPopoArmor(piece)) {
                NbtComponent.set(DataComponentTypes.CUSTOM_DATA, piece, nbt -> nbt.putLong(CLEAN_UNTIL, until));
            }
        }
    }

    public static void initialize() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (var player : server.getPlayerManager().getPlayerList()) {
                if (player.isTouchingWater() && (server.getTicks() % 20 == 0 || isArmorSmelly(player))) wash(player);
            }
        });
    }
    private SmellSystem() { }
}
