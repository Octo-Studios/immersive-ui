package it.hurts.octostudios.client;

import org.spongepowered.asm.mixin.Unique;

public class TickManager {
    public static final long TARGET_INTERVAL_MS = 200; // Interval in milliseconds
    public static long lastExecutedTime = System.currentTimeMillis();
    public static long currentTime;
    public static long elapsedTime;
}
