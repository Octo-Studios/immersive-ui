package it.hurts.octostudios.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import org.joml.Matrix4f;

public class RenderUtils {
    public static void renderTextureFromCenter(PoseStack matrix, float centerX, float centerY, float width, float height, float scale) {
        renderTextureFromCenter(matrix, centerX, centerY, 0, 0, width, height, width, height, scale);
    }

    public static void renderTextureFromCenter(PoseStack matrix, float centerX, float centerY, float texOffX, float texOffY, float texWidth, float texHeight, float width, float height, float scale) {
        BufferBuilder builder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

        RenderSystem.setShader(GameRenderer::getPositionTexShader);

        matrix.pushPose();

        matrix.translate(centerX, centerY, 0);
        matrix.scale(scale, scale, scale);

        Matrix4f m = matrix.last().pose();

        float u1 = texOffX / texWidth;
        float u2 = (texOffX + width) / texWidth;
        float v1 = texOffY / texHeight;
        float v2 = (texOffY + height) / texHeight;

        float w2 = width / 2F;
        float h2 = height / 2F;

        builder.addVertex(m, -w2, +h2, 0).setUv(u1, v2);
        builder.addVertex(m, +w2, +h2, 0).setUv(u2, v2);
        builder.addVertex(m, +w2, -h2, 0).setUv(u2, v1);
        builder.addVertex(m, -w2, -h2, 0).setUv(u1, v1);

        matrix.popPose();

        BufferUploader.drawWithShader(builder.buildOrThrow());
    }

    public static int lerpColor(int colorStart, int colorEnd, float t) {
        // Ensure t is between 0.0 and 1.0
        if (t < 0.0f) t = 0.0f;
        if (t > 1.0f) t = 1.0f;

        // Extract ARGB components from the start color
        int aStart = (colorStart >> 24) & 0xFF;
        int rStart = (colorStart >> 16) & 0xFF;
        int gStart = (colorStart >> 8) & 0xFF;
        int bStart = colorStart & 0xFF;

        // Extract ARGB components from the end color
        int aEnd = (colorEnd >> 24) & 0xFF;
        int rEnd = (colorEnd >> 16) & 0xFF;
        int gEnd = (colorEnd >> 8) & 0xFF;
        int bEnd = colorEnd & 0xFF;

        // Linearly interpolate each component
        int a = (int)(aStart + t * (aEnd - aStart));
        int r = (int)(rStart + t * (rEnd - rStart));
        int g = (int)(gStart + t * (gEnd - gStart));
        int b = (int)(bStart + t * (bEnd - bStart));

        // Combine the components back into an ARGB color
        int color = (a << 24) | (r << 16) | (g << 8) | b;

        return color;
    }
}
