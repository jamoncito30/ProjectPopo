package org.examplee.proyecto_intento.client;

import net.minecraft.client.model.*;

/** Standalone visual factories; role selection is owned by the future server event. */
public final class InvasionModels {
    private InvasionModels() {}

    public static ModelPart beetle(boolean artillery) {
        ModelData data = new ModelData();
        var root = data.getRoot();
        root.addChild("shell_left", ModelPartBuilder.create().uv(0,0).cuboid(-4.5F,-3,-4,4.25F,5,8), ModelTransform.pivot(0,21,1));
        root.addChild("shell_right", ModelPartBuilder.create().uv(0,0).mirrored().cuboid(.25F,-3,-4,4.25F,5,8), ModelTransform.pivot(0,21,1));
        var head = root.addChild("head",ModelPartBuilder.create().uv(0,18).cuboid(-3,-1.5F,-3,6,3,3),ModelTransform.pivot(0,21.5F,-3));
        head.addChild("antenna_left",ModelPartBuilder.create().uv(40,0).cuboid(-.5F,-2,-2.5F,1,1,3),ModelTransform.of(-2,-1,-2,-.3F,.4F,0));
        head.addChild("antenna_right",ModelPartBuilder.create().uv(40,0).cuboid(-.5F,-2,-2.5F,1,1,3),ModelTransform.of(2,-1,-2,-.3F,-.4F,0));
        for(int i=0;i<6;i++) {
            boolean left=i<3;
            root.addChild("leg"+i,ModelPartBuilder.create().uv(40,8).cuboid(left?-5:0,0,-.5F,5,1,1),ModelTransform.of(left?-3:3,22,(i%3)*3-3,0,0,left?-.28F:.28F));
        }
        // Preserve the base animation contract. Combat equipment replaces these parts.
        root.addChild("ball",ModelPartBuilder.create(),ModelTransform.NONE);
        root.addChild("backpack",ModelPartBuilder.create(),ModelTransform.NONE);
        if(artillery) {
            root.addChild("ammo_basket",ModelPartBuilder.create().uv(0,32).cuboid(-3,-4,-1,6,4,6),ModelTransform.pivot(0,18,1));
            root.addChild("sling_left",ModelPartBuilder.create().uv(40,8).cuboid(-4,-8,0,1,8,1),ModelTransform.of(0,19,2,0,0,-.2F));
            root.addChild("sling_right",ModelPartBuilder.create().uv(40,8).cuboid(3,-8,0,1,8,1),ModelTransform.of(0,19,2,0,0,.2F));
            root.addChild("sling_pouch",ModelPartBuilder.create().uv(0,40).cuboid(-3,-1,-1,6,2,3),ModelTransform.pivot(0,11,2));
        } else {
            head.addChild("helmet",ModelPartBuilder.create().uv(24,16).cuboid(-3.5F,-2.5F,-3.5F,7,2,4),ModelTransform.NONE);
            head.addChild("ram",ModelPartBuilder.create().uv(40,8).cuboid(-1,-2,-5.5F,2,2,3),ModelTransform.NONE);
            root.addChild("armor",ModelPartBuilder.create().uv(0,32).cuboid(-5,-1,-4,10,2,8),ModelTransform.pivot(0,17,1));
        }
        return TexturedModelData.of(data,64,64).createModel();
    }

    public static ModelPart slime(int role) {
        if(role<0 || role>2) throw new IllegalArgumentException("Slime role must be 0..2");
        ModelData data=new ModelData();var root=data.getRoot();
        float w=role==0?12:14, h=role==0?9:14;
        var body=root.addChild("body",ModelPartBuilder.create().uv(0,0).cuboid(-w/2,-h,-w/2,w,h,w),ModelTransform.pivot(0,24,0));
        body.addChild("face",ModelPartBuilder.create().uv(0,0).cuboid(-4,-h+3,-w/2-.5F,2,2,1).cuboid(2,-h+3,-w/2-.5F,2,2,1).cuboid(-2,-3,-w/2-.5F,4,1,1),ModelTransform.NONE);
        if(role==1) {
            body.addChild("pressure_sac",ModelPartBuilder.create().uv(0,32).cuboid(-4,-20,-3,8,7,8),ModelTransform.NONE);
            body.addChild("nozzle",ModelPartBuilder.create().uv(32,32).cuboid(-2,-8,-10,4,4,4),ModelTransform.NONE);
        }
        if(role==2) {
            body.addChild("shoulder_left",ModelPartBuilder.create().uv(0,32).cuboid(-10,-12,-3,4,8,7),ModelTransform.NONE);
            body.addChild("shoulder_right",ModelPartBuilder.create().uv(0,32).cuboid(6,-12,-3,4,8,7),ModelTransform.NONE);
            body.addChild("crown",ModelPartBuilder.create().uv(32,32).cuboid(-4,-17,-3,8,3,6),ModelTransform.NONE);
        }
        return TexturedModelData.of(data,64,64).createModel();
    }
}
