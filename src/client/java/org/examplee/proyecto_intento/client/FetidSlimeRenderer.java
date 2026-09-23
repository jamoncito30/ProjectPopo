package org.examplee.proyecto_intento.client;

import net.minecraft.client.render.entity.*;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public final class FetidSlimeRenderer extends MobEntityRenderer<SlimeEntity,FetidSlimeModel> {
    public static final EntityModelLayer LAYER = new EntityModelLayer(Identifier.of("proyecto_intento","fetid_slime"),"main");
    private final FetidSlimeModel normal;
    private final FetidSlimeModel[] combatModels={new FetidSlimeModel(InvasionModels.slime(0)),new FetidSlimeModel(InvasionModels.slime(1)),new FetidSlimeModel(InvasionModels.slime(2))};
    public FetidSlimeRenderer(EntityRendererFactory.Context context) { super(context,new FetidSlimeModel(context.getPart(LAYER)),.45F);normal=model; }
    @Override public void render(SlimeEntity entity,float yaw,float delta,MatrixStack matrices,net.minecraft.client.render.VertexConsumerProvider vertices,int light){
        int role=entity instanceof org.examplee.proyecto_intento.entity.InvasionParticipant p?p.combatRole():0;
        model=role>0?combatModels[role-1]:normal;super.render(entity,yaw,delta,matrices,vertices,light);
    }
    @Override public Identifier getTexture(SlimeEntity entity) { int role=entity instanceof org.examplee.proyecto_intento.entity.InvasionParticipant p?p.combatRole():0; return Identifier.of("proyecto_intento",role==0?"textures/entity/fetid_slime.png":"textures/entity/invasion/"+new String[]{"slime_runner","slime_artillery","slime_colossus"}[role-1]+".png"); }
    @Override protected void scale(SlimeEntity entity, MatrixStack matrices, float delta) {
        float stretch = MathHelper.lerp(delta,entity.lastStretch,entity.stretch)/2;
        float scale = 1/(stretch+1);
        matrices.scale(scale,1/scale,scale);
        if(entity instanceof org.examplee.proyecto_intento.entity.FetidSlimeAlphaEntity)matrices.scale(1.8F,1.8F,1.8F);
        else if(entity instanceof org.examplee.proyecto_intento.entity.InvasionParticipant p && p.combatRole()==3)matrices.scale(1.35F,1.35F,1.35F);
    }
}
