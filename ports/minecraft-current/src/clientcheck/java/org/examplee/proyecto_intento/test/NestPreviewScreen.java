package org.examplee.proyecto_intento.test;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.util.ScreenshotRecorder;
import net.minecraft.text.Text;
import net.minecraft.util.math.RotationAxis;
import org.examplee.proyecto_intento.block.BeetleNestBlock;
import org.examplee.proyecto_intento.block.ModBlocks;

/** Isolated resource QA screen: renders the actual five baked block models. */
final class NestPreviewScreen extends Screen {
    private int ticks;
    NestPreviewScreen() { super(Text.literal("PopoCraft shelter stages")); }
    @Override public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(0, 0, width, height, 0xFF242B30);
        context.drawCenteredTextWithShadow(textRenderer, "PopoCraft - Refugios de escarabajos", width / 2, 24, 0xFFF0D9A5);
        for (int stage = 1; stage <= 5; stage++) {
            int x = width * stage / 6;
            var matrices = context.getMatrices();
            matrices.push();
            matrices.translate(x, height / 2.0, 100);
            float scale = Math.min(width / 7F, 72F);
            matrices.scale(scale, -scale, scale);
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(25));
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(145));
            matrices.translate(-.5, -.3, -.5);
            client.getBlockRenderManager().renderBlockAsEntity(ModBlocks.BEETLE_NEST.getDefaultState().with(BeetleNestBlock.STAGE, stage),
                    matrices, context.getVertexConsumers(), LightmapTextureManager.MAX_LIGHT_COORDINATE, OverlayTexture.DEFAULT_UV);
            context.draw();
            matrices.pop();
            context.drawCenteredTextWithShadow(textRenderer, "Fase " + stage, x, height / 2 + 65, 0xFFFFFFFF);
        }
        context.drawCenteredTextWithShadow(textRenderer, "5 aportes construyen la casa; el siguiente alimenta a la familia.", width / 2, height - 30, 0xFFBFC9CB);
    }
    @Override public void tick() {
        if (++ticks == 30) {
            ScreenshotRecorder.saveScreenshot(client.runDirectory, "refugios.png", client.getFramebuffer(), message -> client.execute(client::scheduleStop));
        }
    }
}
