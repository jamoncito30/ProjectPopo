package org.examplee.proyecto_intento.client;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;
import org.examplee.proyecto_intento.entity.DungBeetleEntity;

public final class DungBeetleRenderer extends MobEntityRenderer<DungBeetleEntity, DungBeetleModel> {
    public static final EntityModelLayer LAYER = new EntityModelLayer(Identifier.of("proyecto_intento", "dung_beetle"), "main");
    public DungBeetleRenderer(EntityRendererFactory.Context context) { super(context, new DungBeetleModel(context.getPart(LAYER)), 0.35F); }
    @Override public Identifier getTexture(DungBeetleEntity entity) { return Identifier.of("proyecto_intento", entity.isTrader()?"textures/entity/dung_beetle_trader.png":"textures/entity/dung_beetle.png"); }
}
