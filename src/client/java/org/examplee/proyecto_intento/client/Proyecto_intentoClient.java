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
        net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin.register(context->context.addModels(net.minecraft.util.Identifier.of("proyecto_intento","item/pestilence")));
        net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap.INSTANCE.putBlock(org.examplee.proyecto_intento.block.ModBlocks.PESTILENT_TORCH,net.minecraft.client.render.RenderLayer.getCutout());
        EntityModelLayerRegistry.registerModelLayer(FetidSlimeRenderer.LAYER,FetidSlimeModel::createModel);
        EntityRendererRegistry.register(ModEntities.FETID_SLIME,FetidSlimeRenderer::new);
        EntityRendererRegistry.register(ModEntities.TOILET_SEAT, net.minecraft.client.render.entity.EmptyEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.POPO_PROJECTILE, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.INVASION_PROJECTILE, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.FETID_SLIME_ALPHA,FetidSlimeRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(DungBeetleRenderer.LAYER, DungBeetleModel::createModel);
        EntityRendererRegistry.register(ModEntities.DUNG_BEETLE, DungBeetleRenderer::new);
        ParticleFactoryRegistry.getInstance().register(ModParticles.FLY, FlyParticle.Factory::new);
        SmellAmbience.initialize();

        net.minecraft.client.option.KeyBinding poopKeyBinding = net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper.registerKeyBinding(new net.minecraft.client.option.KeyBinding(
                "key.proyecto_intento.poop",
                net.minecraft.client.util.InputUtil.Type.KEYSYM,
                org.lwjgl.glfw.GLFW.GLFW_KEY_G,
                "category.proyecto_intento.keys"
        ));

        net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (poopKeyBinding.wasPressed()) {
                if (net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.canSend(org.examplee.proyecto_intento.network.ToiletPoopPayload.ID)) {
                    net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.send(new org.examplee.proyecto_intento.network.ToiletPoopPayload());
                }
            }
        });
    }
}
