package org.examplee.proyecto_intento.entity;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class ModParticles {
    public static final SimpleParticleType FLY = Registry.register(Registries.PARTICLE_TYPE,
            Identifier.of("proyecto_intento", "fly"), FabricParticleTypes.simple());
    public static void initialize() { }
    private ModParticles() { }
}
