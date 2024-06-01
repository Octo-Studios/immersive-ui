package it.hurts.octostudios.util;

import org.joml.Vector2f;

public class VectorUtils {
    public static Vector2f rotate(Vector2f vec, float rotDegrees) {
        float radians = (float) Math.toRadians(rotDegrees);

        float cos = (float) Math.cos(radians);
        float sin = (float) Math.sin(radians);

        float newX = vec.x * cos - vec.y * sin;
        float newY = vec.x * sin + vec.y * cos;

        return new Vector2f(newX, newY);
    }
}
