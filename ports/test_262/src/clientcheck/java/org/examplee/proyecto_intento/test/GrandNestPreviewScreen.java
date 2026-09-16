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

/** Isolated resource QA screen: renders all four modular grand shelter stages. */
final class GrandNestPreviewScreen extends Screen {
    private int ticks;
    GrandNestPreviewScreen() { super(Text.literal("PopoCraft shelter stages")); }
    @Override public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(0, 0, width, height, 0xFF242B30);
        context.drawCenteredTextWithShadow(textRenderer, "Gran refugio - 5 x 5 x 5", width / 2, 10, 0xFFF0D9A5);
        for (int stage = 1; stage <= 4; stage++) {
            int x = width * (stage % 2 == 1 ? 1 : 3) / 4; int rowY = stage <= 2 ? height / 3 : height * 3 / 4;
            var matrices = context.getMatrices();
            matrices.push();
            matrices.translate(x, rowY, 100);
            float scale = Math.min(width / 30F, 15F);
            matrices.scale(scale, -scale, scale);
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(25));
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(145));
            matrices.translate(-2.5, -1.5, -2.5);
            for (int piece = 0; piece < 125; piece++) { matrices.push(); matrices.translate(piece % 5, piece / 25, (piece / 5) % 5); client.getBlockRenderManager().renderBlockAsEntity(ModBlocks.GRAND_BEETLE_NEST_PIECE.getDefaultState().with(org.examplee.proyecto_intento.block.GrandBeetleNestPieceBlock.STAGE, stage).with(org.examplee.proyecto_intento.block.GrandBeetleNestPieceBlock.PIECE, piece),
                    matrices, context.getVertexConsumers(), LightmapTextureManager.MAX_LIGHT_COORDINATE, OverlayTexture.DEFAULT_UV);
            context.draw(); matrices.pop(); }
            matrices.pop();
            context.drawCenteredTextWithShadow(textRenderer, "Fase " + stage, x, rowY + 36, 0xFFFFFFFF);
        }
        context.drawCenteredTextWithShadow(textRenderer, "Plataforma / muros / boveda parcial / refugio completo", width / 2, height - 12, 0xFFBFC9CB);
    }
    @Override public void tick() {
        if (++ticks == 30) {
            ScreenshotRecorder.saveScreenshot(client.runDirectory, "gran-refugio.png", client.getFramebuffer(), message -> client.execute(client::scheduleStop));
        }
    }
}
