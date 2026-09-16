import os

f = 'src/client/java/org/examplee/proyecto_intento/client/Proyecto_intentoClient.java'
with open(f, 'w', encoding='utf-8') as file: file.write('''package org.examplee.proyecto_intento.client;
import net.fabricmc.api.ClientModInitializer;
public class Proyecto_intentoClient implements ClientModInitializer {
    public static final net.minecraft.client.render.entity.model.EntityModelLayer DUNG_BEETLE_LAYER = new net.minecraft.client.render.entity.model.EntityModelLayer(net.minecraft.util.Identifier.of("proyecto_intento", "dung_beetle"), "main");
    @Override
    public void onInitializeClient() {
        net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry.registerModelLayer(DUNG_BEETLE_LAYER, DungBeetleModel::getTexturedModelData); 
        net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry.register(org.examplee.proyecto_intento.entity.ModEntities.DUNG_BEETLE, DungBeetleRenderer::new);
    }
}
''')

print("Rewrote Client entrypoint cleanly")
