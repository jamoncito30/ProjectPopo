package org.examplee.proyecto_intento.client;

import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.util.math.MathHelper;
import org.examplee.proyecto_intento.entity.DungBeetleEntity;

public final class DungBeetleModel extends SinglePartEntityModel<DungBeetleEntity> {
    private final ModelPart root, head, ball;
    private final ModelPart[] legs = new ModelPart[6];
    public DungBeetleModel(ModelPart root) {
        this.root = root; head = root.getChild("head"); ball = root.getChild("ball");
        for (int i = 0; i < 6; i++) legs[i] = root.getChild("leg" + i);
    }
    public static TexturedModelData createModel() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot();
        root.addChild("shell_left", ModelPartBuilder.create().uv(0, 0).cuboid(-4.5F, -3, -4, 4.25F, 5, 8), ModelTransform.pivot(0, 21, 1));
        root.addChild("shell_right", ModelPartBuilder.create().uv(0, 0).mirrored().cuboid(0.25F, -3, -4, 4.25F, 5, 8), ModelTransform.pivot(0, 21, 1));
        ModelPartData head = root.addChild("head", ModelPartBuilder.create().uv(0, 18).cuboid(-3, -1.5F, -3, 6, 3, 3), ModelTransform.pivot(0, 21.5F, -3));
        head.addChild("antenna_left", ModelPartBuilder.create().uv(40, 0).cuboid(-0.5F, -2, -2.5F, 1, 1, 3), ModelTransform.of(-2, -1, -2, -0.3F, 0.4F, 0));
        head.addChild("antenna_right", ModelPartBuilder.create().uv(40, 0).cuboid(-0.5F, -2, -2.5F, 1, 1, 3), ModelTransform.of(2, -1, -2, -0.3F, -0.4F, 0));
        for (int i = 0; i < 6; i++) {
            boolean left = i < 3;
            root.addChild("leg" + i, ModelPartBuilder.create().uv(40, 8).cuboid(left ? -5 : 0, 0, -0.5F, 5, 1, 1),
                    ModelTransform.of(left ? -3 : 3, 22, (i % 3) * 3 - 3, 0, 0, left ? -0.28F : 0.28F));
        }
        root.addChild("ball", ModelPartBuilder.create().uv(32, 32).cuboid(-2.5F, -2.5F, -2.5F, 5, 5, 5), ModelTransform.pivot(0, 21, -10));
        root.addChild("backpack",ModelPartBuilder.create().uv(0,40).cuboid(-2.5F,-3,-2,5,3,5),ModelTransform.pivot(0,18,2));
        return TexturedModelData.of(data, 64, 64);
    }
    @Override public ModelPart getPart() { return root; }
    @Override public void setAngles(DungBeetleEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        head.yaw = headYaw * MathHelper.RADIANS_PER_DEGREE * 0.35F;
        head.pitch = MathHelper.sin(animationProgress * 0.13F) * 0.05F;
        for (int i = 0; i < legs.length; i++) {
            float side = i < 3 ? -1 : 1;
            float phase = limbAngle * 2.8F + (i % 3) * MathHelper.PI * 0.65F + (i < 3 ? 0 : MathHelper.PI);
            legs[i].yaw = MathHelper.cos(phase) * 0.65F * limbDistance;
            legs[i].roll = side * (0.28F + Math.max(0, MathHelper.sin(phase)) * 0.6F * limbDistance);
        }
        ball.visible = entity.getCarrying() > 0;
        root.getChild("backpack").visible=entity.isTrader();
        ball.pitch = -limbAngle * 1.5F;
        head.getChild("antenna_left").yaw = 0.4F + MathHelper.sin(animationProgress * 0.15F) * 0.12F;
        head.getChild("antenna_right").yaw = -0.4F - MathHelper.sin(animationProgress * 0.15F) * 0.12F;
    }
}
