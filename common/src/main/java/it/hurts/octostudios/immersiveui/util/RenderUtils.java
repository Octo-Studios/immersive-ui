package it.hurts.octostudios.immersiveui.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.CoreShaders;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderProgram;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import org.joml.Matrix4f;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RenderUtils {
    public static void renderTextureFromCenter(PoseStack matrix, float centerX, float centerY, float width, float height, float scale) {
        renderTextureFromCenter(matrix, centerX, centerY, 0, 0, width, height, width, height, scale);
    }

    public static void renderTextureFromCenter(PoseStack matrix, float centerX, float centerY, float texOffX, float texOffY, float texWidth, float texHeight, float width, float height, float scale) {
        BufferBuilder builder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

//        RenderSystem.setShader(GameRenderer::getPositionTexShader);
//        RenderSystem.setSha
        RenderSystem.setShader(CoreShaders.POSITION_TEX);

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

    public static final ResourceLocation ALT_FONT = ResourceLocation.withDefaultNamespace("alt");
    public static final ResourceLocation ILLAGER_ALT_FONT = ResourceLocation.withDefaultNamespace("illageralt");

    public static MutableComponent illageriate(MutableComponent input, double percentage, long seed) {
        return stylize(input, percentage, Style.EMPTY.withFont(ILLAGER_ALT_FONT), seed);
    }

    public static MutableComponent galactizate(MutableComponent input, double percentage, long seed) {
        return stylize(input, percentage, Style.EMPTY.withFont(ALT_FONT), seed);
    }

    public static MutableComponent obfuscate(MutableComponent input, double percentage, long seed) {
        return stylize(input, percentage, Style.EMPTY.withObfuscated(true).withColor(ChatFormatting.DARK_RED), seed);
    }

    public static MutableComponent stylize(MutableComponent input, double percentage, Style style, long seed) {
        RandomSource random = RandomSource.create(seed);

        String text = input.getString();
        int length = text.length();

        var indices = IntStream.generate(() -> random.nextInt(length))
                .distinct()
                .limit((int) (length * percentage))
                .boxed()
                .collect(Collectors.toSet());

        return IntStream.range(0, length)
                .mapToObj(index -> {
                    MutableComponent component = Component.literal(String.valueOf(text.charAt(index))).setStyle(input.getStyle());

                    if (indices.contains(index))
                        component.setStyle(style.applyTo(component.getStyle()));

                    return component;
                })
                .collect(Component::empty, MutableComponent::append, MutableComponent::append);
    }
}
