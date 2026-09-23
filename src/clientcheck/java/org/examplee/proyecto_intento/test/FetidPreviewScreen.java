package org.examplee.proyecto_intento.test;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.*;
import net.minecraft.client.util.ScreenshotRecorder;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.item.*;
import org.examplee.proyecto_intento.block.ModBlocks;
import org.examplee.proyecto_intento.client.FetidSlimeModel;
import org.examplee.proyecto_intento.item.*;

final class FetidPreviewScreen extends Screen {
    private int ticks;
    FetidPreviewScreen() { super(Text.literal("Slime fétido")); }
    @Override public void render(DrawContext c,int mx,int my,float delta) {
        c.fill(0,0,width,height,0xFF202720);
        c.drawCenteredTextWithShadow(textRenderer,"ProjectPopo - Slime fetido",width/2,12,0xFFE0EBA0);
        var matrices=c.getMatrices();
        matrices.push();matrices.translate(width*.3,154,150);matrices.scale(85,85,85);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-18));matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(150));matrices.translate(0,-1.25,0);
        var model=new FetidSlimeModel(FetidSlimeModel.createModel().createModel());
        model.render(matrices,c.getVertexConsumers().getBuffer(RenderLayer.getEntityCutout(Identifier.of("proyecto_intento","textures/entity/fetid_slime.png"))),LightmapTextureManager.MAX_LIGHT_COORDINATE,OverlayTexture.DEFAULT_UV,0xFFFFFFFF);c.draw();matrices.pop();
        var blocks=new net.minecraft.block.Block[]{ModBlocks.INODORO,ModBlocks.PESTILENT_TORCH,ModBlocks.SEWAGE};
        for(int i=0;i<3;i++) {
            matrices.push();matrices.translate(width*.7+(i-1)*55,102,150);matrices.scale(32,-32,32);
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(22));matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(140));matrices.translate(-.5,-.5,-.5);
            var state=blocks[i].getDefaultState();if(i==0)state=state.with(org.examplee.proyecto_intento.block.ToiletBlock.CLOGGED,true);
            client.getBlockRenderManager().renderBlockAsEntity(state,matrices,c.getVertexConsumers(),LightmapTextureManager.MAX_LIGHT_COORDINATE,OverlayTexture.DEFAULT_UV);c.draw();matrices.pop();
        }
        ItemStack[] items={new ItemStack(ModItems.VISCOUS_BIOMASS),new ItemStack(ModItems.TOXIC_PLUNGER),new ItemStack(ModItems.SEWAGE_BUCKET),net.minecraft.component.type.PotionContentsComponent.createStack(Items.POTION,ModPotions.PESTILENCE),new ItemStack(ModBlocks.PESTILENT_TORCH)};
        for(int i=0;i<items.length;i++){matrices.push();matrices.translate(width/2-95+i*40,height-60,200);matrices.scale(2,2,2);c.drawItem(items[i],0,0);matrices.pop();}
        c.drawCenteredTextWithShadow(textRenderer,"Biomasa - Desatascador - Aguas residuales - Pestilencia - Antorcha",width/2,height-20,0xFFFFFFFF);
    }
    @Override public void tick(){if(++ticks==30)ScreenshotRecorder.saveScreenshot(client.runDirectory,"fetid-slime.png",client.getFramebuffer(),message->client.execute(client::scheduleStop));}
}
