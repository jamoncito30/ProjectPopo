package org.examplee.proyecto_intento.client;
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
