package org.examplee.proyecto_intento.test;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.*;
import net.minecraft.client.util.ScreenshotRecorder;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import org.examplee.proyecto_intento.client.*;

/** Renders actual game geometry and textures without spawning or changing a world. */
final class InvasionPreviewScreen extends Screen {
    private int ticks;
    InvasionPreviewScreen(){super(Text.literal("Invasion art"));}
    @Override public void render(DrawContext c,int mx,int my,float delta) {
        c.fill(0,0,width,height,0xFF263039);
        c.drawCenteredTextWithShadow(textRenderer,"ProjectPopo - Invasion / modelos de combate",width/2,8,0xFFEADCA0);
        String[] names={"beetle_soldier","beetle_artillery","slime_runner","slime_artillery","slime_colossus"};
        String[] labels={"Soldado","Artillero","Corredor","Escupidor","Coloso"};
        for(int i=0;i<5;i++) {
            float x=width*(i+.5F)/5;
            var m=c.getMatrices();m.push();m.translate(x,height*.57,150);
            float scale=Math.min(width/8F,height/3F)*(i==4?1.1F:1F);
            m.scale(scale,scale,scale);
            m.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-20));
            m.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(150));m.translate(0,-1.3,0);
            var layer=RenderLayer.getEntityCutout(Identifier.of("proyecto_intento","textures/entity/invasion/"+names[i]+".png"));
            if(i<2) new DungBeetleModel(InvasionModels.beetle(i==1)).render(m,c.getVertexConsumers().getBuffer(layer),LightmapTextureManager.MAX_LIGHT_COORDINATE,OverlayTexture.DEFAULT_UV,0xFFFFFFFF);
            else new FetidSlimeModel(InvasionModels.slime(i-2)).render(m,c.getVertexConsumers().getBuffer(layer),LightmapTextureManager.MAX_LIGHT_COORDINATE,OverlayTexture.DEFAULT_UV,0xFFFFFFFF);
            c.draw();m.pop();c.drawCenteredTextWithShadow(textRenderer,labels[i],(int)x,(int)(height*.65),0xFFFFFFFF);
        }
        c.drawTexture(Identifier.of("proyecto_intento","textures/item/invasion/dung_shot.png"),width/2-60,height-60,0,0,32,32,32,32);
        c.drawTexture(Identifier.of("proyecto_intento","textures/item/invasion/acid_spit.png"),width/2+28,height-60,0,0,32,32,32,32);
        c.drawCenteredTextWithShadow(textRenderer,"Arte preparado; evento y musica pendientes de integrar",width/2,height-14,0xFFBBC9CF);
    }
    @Override public void tick(){if(++ticks==30)ScreenshotRecorder.saveScreenshot(client.runDirectory,"invasion.png",client.getFramebuffer(),message->client.execute(client::scheduleStop));}
}
