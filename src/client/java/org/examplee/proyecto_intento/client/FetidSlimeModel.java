package org.examplee.proyecto_intento.client;

import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.entity.mob.SlimeEntity;

/** Geometry source: art/fetid-slime-model.json; face rendered as a separate dark material. */
public final class FetidSlimeModel extends SinglePartEntityModel<SlimeEntity> {
    private final ModelPart root;
    public FetidSlimeModel(ModelPart root) { this.root = root; }
    public static TexturedModelData createModel() {
        ModelData data = new ModelData();
        var root = data.getRoot();
        var body = root.addChild("body", ModelPartBuilder.create().uv(0,0).cuboid(-7,-14,-7,14,14,14), ModelTransform.pivot(0,24,0));
        body.addChild("crown_left",ModelPartBuilder.create().uv(0,32).cuboid(-5,-16,-2,4,3,4),ModelTransform.NONE);
        body.addChild("crown_right",ModelPartBuilder.create().uv(16,32).cuboid(2,-15,1,3,2,3),ModelTransform.NONE);
        body.addChild("drip",ModelPartBuilder.create().uv(32,32).cuboid(3,-3,-7.5F,2,3,1),ModelTransform.NONE);
        body.addChild("face",ModelPartBuilder.create().uv(0,0)
                .cuboid(-5,-11,-7.5F,3,3,1).cuboid(2,-11,-7.5F,3,3,1)
                .cuboid(-3,-5,-7.5F,6,2,1),ModelTransform.NONE);
        return TexturedModelData.of(data,64,64);
    }
    @Override public ModelPart getPart() { return root; }
    public ModelPart face() { return root.getChild("body").getChild("face"); }
    @Override public void render(net.minecraft.client.util.math.MatrixStack matrices, net.minecraft.client.render.VertexConsumer vertices, int light, int overlay, int color) {
        face().visible = false;
        root.render(matrices, vertices, light, overlay, color);
        face().visible = true;
        matrices.push();
        root.rotate(matrices);
        root.getChild("body").rotate(matrices);
        face().render(matrices, vertices, light, overlay, 0xFF211B12);
        matrices.pop();
    }
    @Override public void setAngles(SlimeEntity entity,float limbAngle,float limbDistance,float age,float yaw,float pitch) { }
}
