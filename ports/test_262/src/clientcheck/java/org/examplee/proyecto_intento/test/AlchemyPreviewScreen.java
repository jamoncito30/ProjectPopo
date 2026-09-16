package org.examplee.proyecto_intento.test;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ScreenshotRecorder;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import org.examplee.proyecto_intento.item.ModItems;
import org.examplee.proyecto_intento.item.ModPotions;

final class AlchemyPreviewScreen extends Screen {
    private int ticks;
    AlchemyPreviewScreen() { super(Text.literal("PopoCraft alchemy preview")); }
    @Override public void render(DrawContext context,int mouseX,int mouseY,float delta) {
        context.fill(0,0,width,height,0xFF242B30);
        context.drawCenteredTextWithShadow(textRenderer,"PopoCraft - Nueva textura y alquimia",width/2,25,0xFFF0D9A5);
        ItemStack[] stacks={new ItemStack(ModItems.POPO),PotionContentsComponent.createStack(Items.POTION,ModPotions.BOTTLED_FART),
                PotionContentsComponent.createStack(Items.SPLASH_POTION,ModPotions.BOTTLED_FART),PotionContentsComponent.createStack(Items.LINGERING_POTION,ModPotions.BOTTLED_FART)};
        String[] labels={"Popo renovado","Bebible","Arrojadiza","Persistente"};
        for(int i=0;i<stacks.length;i++) {
            int x=width*(i+1)/5;
            var matrices=context.getMatrices(); matrices.push();
            float scale=Math.min(4F,width/100F);
            matrices.translate(x-8*scale,height/2.0-8*scale,0); matrices.scale(scale,scale,1);
            context.drawItem(stacks[i],0,0); context.draw(); matrices.pop();
            context.drawCenteredTextWithShadow(textRenderer,labels[i],x,height/2+44,0xFFFFFFFF);
        }
        context.drawCenteredTextWithShadow(textRenderer,"Pedo en botella: pocion rara + popo",width/2,height-45,0xFFF0D9A5);
        context.drawCenteredTextWithShadow(textRenderer,"Peste: 30 segundos. Polvora y aliento de dragon para sus variantes.",width/2,height-28,0xFFBFC9CB);
    }
    @Override public void tick() {
        if(++ticks==30) ScreenshotRecorder.saveScreenshot(client.runDirectory,"alquimia.png",client.getFramebuffer(),message->client.execute(client::scheduleStop));
    }
}
