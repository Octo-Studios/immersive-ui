package it.hurts.shatterbyte.immersiveui.util;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FontDescription;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RenderUtils {
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

    public static final Identifier ALT_FONT = Identifier.withDefaultNamespace("alt");
    public static final Identifier ILLAGER_ALT_FONT = Identifier.withDefaultNamespace("illageralt");

    public static MutableComponent illageriate(MutableComponent input, double percentage, long seed) {
        return stylize(input, percentage, Style.EMPTY.withFont(new FontDescription.Resource(ILLAGER_ALT_FONT)), seed);
    }

    public static MutableComponent galactizate(MutableComponent input, double percentage, long seed) {
        return stylize(input, percentage, Style.EMPTY.withFont(new FontDescription.Resource(ALT_FONT)), seed);
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
