import os, re

base = 'src/client/java/org/examplee/proyecto_intento/client/'

# DungBeetleModel
with open(base + 'DungBeetleModel.java', 'w', encoding='utf-8') as f:
    f.write('''package org.examplee.proyecto_intento.client;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.EntityModel;
public final class DungBeetleModel extends EntityModel<DungBeetleRenderState> {
    private final ModelPart root;
    public DungBeetleModel(ModelPart root) {
        super(root);
        this.root = root;
    }
    public static TexturedModelData getTexturedModelData() { return TexturedModelData.of(ModelData(), 64, 64); }
    private static ModelData ModelData() { return new ModelData(); } // Extremely stubbed
    @Override public void setAngles(DungBeetleRenderState state) {}
}
''')

# DungBeetleRenderer
with open(base + 'DungBeetleRenderer.java', 'w', encoding='utf-8') as f:
    f.write('''package org.examplee.proyecto_intento.client;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import org.examplee.proyecto_intento.entity.DungBeetleEntity;

public final class DungBeetleRenderer extends MobEntityRenderer<DungBeetleEntity, DungBeetleRenderState, DungBeetleModel> {
    public DungBeetleRenderer(EntityRendererFactory.Context context) { 
        super(context, new DungBeetleModel(context.getPart(Proyecto_intentoClient.DUNG_BEETLE_LAYER)), 0.5f); 
    }
    @Override public DungBeetleRenderState createRenderState() { return new DungBeetleRenderState(); }
    @Override public Identifier getTexture(DungBeetleRenderState state) { return Identifier.of("proyecto_intento", state.isTrader ? "textures/entity/dung_beetle_trader.png" : "textures/entity/dung_beetle.png"); }
    @Override public void updateRenderState(DungBeetleEntity entity, DungBeetleRenderState state, float tickDelta) {
        super.updateRenderState(entity, state, tickDelta);
        state.isTrader = entity.isTrader();
    }
}
''')

# Proyecto_intentoClient
with open(base + 'Proyecto_intentoClient.java', 'r', encoding='utf-8') as f: c = f.read()
# Replace DUNG_BEETLE_LAYER logic with a static definition if missing
if 'DUNG_BEETLE_LAYER' not in c:
    c = c.replace('public class Proyecto_intentoClient implements ClientModInitializer {', 
                  'public class Proyecto_intentoClient implements ClientModInitializer {\n    public static final net.minecraft.client.render.entity.model.EntityModelLayer DUNG_BEETLE_LAYER = new net.minecraft.client.render.entity.model.EntityModelLayer(net.minecraft.util.Identifier.of("proyecto_intento", "dung_beetle"), "main");')
c = c.replace('EntityRendererRegistry.register(ModEntities.DUNG_BEETLE, DungBeetleRenderer::new);', 'net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry.registerModelLayer(DUNG_BEETLE_LAYER, DungBeetleModel::getTexturedModelData); net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry.register(org.examplee.proyecto_intento.entity.ModEntities.DUNG_BEETLE, DungBeetleRenderer::new);')
with open(base + 'Proyecto_intentoClient.java', 'w', encoding='utf-8') as f: f.write(c)

print("Client stubs generated")
