package org.examplee.proyecto_intento.client;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;
import org.examplee.proyecto_intento.entity.DungBeetleEntity;

public final class DungBeetleRenderer extends MobEntityRenderer<DungBeetleEntity, DungBeetleModel> {
    public static final EntityModelLayer LAYER = new EntityModelLayer(Identifier.of("proyecto_intento", "dung_beetle"), "main");
    private final DungBeetleModel normal,soldier=new DungBeetleModel(InvasionModels.beetle(false)),artillery=new DungBeetleModel(InvasionModels.beetle(true));
    public DungBeetleRenderer(EntityRendererFactory.Context context) { super(context, new DungBeetleModel(context.getPart(LAYER)), 0.35F);normal=model; }
    @Override public void render(DungBeetleEntity entity,float yaw,float delta,net.minecraft.client.util.math.MatrixStack matrices,net.minecraft.client.render.VertexConsumerProvider vertices,int light){
        model=entity.combatRole()==1?soldier:entity.combatRole()==2?artillery:normal;
        super.render(entity,yaw,delta,matrices,vertices,light);
    }
    @Override public Identifier getTexture(DungBeetleEntity entity) { if(entity.combatRole()>0)return Identifier.of("proyecto_intento","textures/entity/invasion/"+(entity.combatRole()==1?"beetle_soldier":"beetle_artillery")+".png"); return Identifier.of("proyecto_intento", entity.isTrader()?"textures/entity/dung_beetle_trader.png":"textures/entity/dung_beetle.png"); }
}
