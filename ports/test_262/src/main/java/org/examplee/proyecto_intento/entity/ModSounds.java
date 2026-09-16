package org.examplee.proyecto_intento.entity;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public final class ModSounds {
    public static final SoundEvent FLIES = register("flies");
    public static final SoundEvent FART = register("fart");
    public static final SoundEvent BEETLE_CHIRP = register("beetle_chirp");
    public static final SoundEvent BEETLE_STEP = register("beetle_step");
    public static final SoundEvent BEETLE_ROLL = register("beetle_roll");
    public static final SoundEvent GRAND_NEST_START = register("grand_nest_start");
    private static SoundEvent register(String name) {
        Identifier id = Identifier.of("proyecto_intento", name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }
    public static void initialize() { }
    private ModSounds() { }
}
