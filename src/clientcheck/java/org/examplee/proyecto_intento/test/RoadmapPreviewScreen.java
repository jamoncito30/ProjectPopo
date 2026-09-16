package org.examplee.proyecto_intento.test;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.*;
import net.minecraft.client.util.ScreenshotRecorder;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import org.examplee.proyecto_intento.block.*;
import org.examplee.proyecto_intento.client.DungBeetleModel;
import org.examplee.proyecto_intento.item.ModItems;

final class RoadmapPreviewScreen extends Screen {
    private int ticks;
    RoadmapPreviewScreen() { super(Text.literal("PopoCraft expansion")); }
    @Override public void render(DrawContext c,int mouseX,int mouseY,float delta) {
        c.fill(0,0,width,height,0xFF242B30);
        c.drawCenteredTextWithShadow(textRenderer,"PopoCraft - Vida y cultivos",width/2,10,0xFFF0D9A5);
        var blocks=new net.minecraft.block.Block[]{ModBlocks.INODORO,ModBlocks.EXTRACTOR_ESTIERCOL};
        for(int i=0;i<2;i++) {
            int x=width*(i==0?1:3)/4;var m=c.getMatrices();m.push();m.translate(x,78,150);m.scale(48,-48,48);
            m.multiply(RotationAxis.POSITIVE_X.rotationDegrees(25));m.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(145));m.translate(-.5,-.4,-.5);
            client.getBlockRenderManager().renderBlockAsEntity(blocks[i].getDefaultState(),m,c.getVertexConsumers(),LightmapTextureManager.MAX_LIGHT_COORDINATE,OverlayTexture.DEFAULT_UV);c.draw();m.pop();
            c.drawCenteredTextWithShadow(textRenderer,i==0?"Inodoro":"Extractor",x,112,0xFFFFFFFF);
        }
        for(int i=0;i<2;i++) {
            int x=width*(i==0?1:3)/4;var m=c.getMatrices();m.push();m.translate(x,164,150);m.scale(65,65,65);
            m.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-25));m.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(145));m.translate(0,-1.35,0);
            var root=DungBeetleModel.createModel().createModel();root.getChild("ball").visible=false;root.getChild("backpack").visible=i==1;
            var texture=Identifier.of("proyecto_intento",i==0?"textures/entity/dung_beetle.png":"textures/entity/dung_beetle_trader.png");
            root.render(m,c.getVertexConsumers().getBuffer(RenderLayer.getEntityCutout(texture)),LightmapTextureManager.MAX_LIGHT_COORDINATE,OverlayTexture.DEFAULT_UV);c.draw();m.pop();
            c.drawCenteredTextWithShadow(textRenderer,i==0?"Pelotero":"Comerciante",x,195,0xFFFFFFFF);
        }
        var m=c.getMatrices();m.push();m.translate(width/2-26,210,200);m.scale(2,2,2);c.drawItem(new net.minecraft.item.ItemStack(ModItems.DESATASCADOR),0,0);c.drawItem(new net.minecraft.item.ItemStack(ModItems.ESTIERCOL),18,0);m.pop();
    }
    @Override public void tick() { if(++ticks==30) ScreenshotRecorder.saveScreenshot(client.runDirectory,"expansion.png",client.getFramebuffer(),message->client.execute(client::scheduleStop)); }
}
