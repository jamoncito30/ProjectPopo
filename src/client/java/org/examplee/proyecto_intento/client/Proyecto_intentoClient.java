package org.examplee.proyecto_intento.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import org.examplee.proyecto_intento.entity.ModEntities;
import org.examplee.proyecto_intento.entity.ModParticles;

public class Proyecto_intentoClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(ModEntities.TOILET_SEAT, net.minecraft.client.render.entity.EmptyEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.POPO_PROJECTILE, FlyingItemEntityRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(DungBeetleRenderer.LAYER, DungBeetleModel::createModel);
        EntityRendererRegistry.register(ModEntities.DUNG_BEETLE, DungBeetleRenderer::new);
        ParticleFactoryRegistry.getInstance().register(ModParticles.FLY, FlyParticle.Factory::new);
        SmellAmbience.initialize();
    }
}
