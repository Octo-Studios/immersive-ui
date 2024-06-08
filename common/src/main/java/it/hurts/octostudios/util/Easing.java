package it.hurts.octostudios.util;

import java.util.function.Consumer;
import java.util.function.Function;

public class Easing {
    public enum Type {
        EASE_IN,
        EASE_OUT,
        EASE_IN_OUT,
        EASE_OUT_BOUNCE,
        LINEAR
    }
    public static float lerp(float start_value, float end_value, float pct)
    {
        return (start_value + (end_value - start_value) * pct);
    }
    public static float flip(float x)
    {
        return 1 - x;
    }
    public static float easeIn(float t)
    {
        return t * t;
    }
    public static float easeOut(float t)
    {
        return flip(easeIn(flip(t)));
    }
    public static float easeInOut(float t)
    {
        return lerp(easeIn(t), easeOut(t), t);
    }
    public static float easeOutBounce(float x) {
        final float n1 = 7.5625f;
        final float d1 = 2.75f;

        if (x < 1 / d1) {
            return n1 * x * x;
        } else if (x < 2 / d1) {
            return (float) (n1 * (x -= (float) (1.5 / d1)) * x + 0.75);
        } else if (x < 2.5 / d1) {
            return (float) (n1 * (x -= (float) (2.25 / d1)) * x + 0.9375);
        } else {
            return (float) (n1 * (x -= (float) (2.625 / d1)) * x + 0.984375);
        }
    }

    public static float animate(Type type, float x) {
        switch (type) {
            case EASE_IN -> {
                return easeIn(x);
            }
            case EASE_OUT -> {
                return easeOut(x);
            }
            case EASE_IN_OUT -> {
                return easeInOut(x);
            }
            case EASE_OUT_BOUNCE -> {
                return easeOutBounce(x);
            }
            case LINEAR -> {
                return x;
            }
        }
        return x;
    }
}