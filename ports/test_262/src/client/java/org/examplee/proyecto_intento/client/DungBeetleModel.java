package org.examplee.proyecto_intento.client;
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
