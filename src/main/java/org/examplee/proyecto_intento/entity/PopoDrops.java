package org.examplee.proyecto_intento.entity;

import net.minecraft.entity.passive.AnimalEntity;
import org.examplee.proyecto_intento.item.ModItems;

public final class PopoDrops {
    public static void drop(AnimalEntity animal) {
        if (animal.getWorld().isClient || !animal.isAlive()) return;
        if (animal.dropItem(ModItems.POPO) != null) {
            animal.playSound(ModSounds.FART, 0.65F, 0.9F + animal.getRandom().nextFloat() * 0.2F);
        }
    }

    public static void onFed(AnimalEntity animal) {
        if (!animal.getWorld().isClient && animal.isAlive() && animal.getRandom().nextInt(4) == 0) drop(animal);
    }

    private PopoDrops() { }
}
