package org.examplee.proyecto_intento.entity;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public final class ModEffects {
    public static final RegistryEntry<StatusEffect> STINKY = Registry.registerReference(Registries.STATUS_EFFECT,
            Identifier.of("proyecto_intento", "stinky"), new StinkyEffect());
    private static final class StinkyEffect extends StatusEffect {
        private StinkyEffect() { super(StatusEffectCategory.HARMFUL, 0x66512B); }
    }
        public static final RegistryEntry<StatusEffect> FETID_OMEN = Registry.registerReference(Registries.STATUS_EFFECT,
            Identifier.of("proyecto_intento", "fetid_omen"), new FetidOmenEffect());
    private static final class FetidOmenEffect extends StatusEffect {
        private FetidOmenEffect() { super(StatusEffectCategory.HARMFUL, 0x4B6E2C); }
    }
    public static void initialize() { }
    private ModEffects() { }
}

