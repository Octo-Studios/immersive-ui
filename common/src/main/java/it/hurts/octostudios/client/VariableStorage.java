package it.hurts.octostudios.client;

public class VariableStorage {
    public static final long TARGET_INTERVAL_MS = 100; // Interval in milliseconds
    public static long lastExecutedTime = System.currentTimeMillis();
    public static long currentTime;
    public static long elapsedTime;
    public static final double SCREEN_ZORDER = 50;

    public static boolean shouldTick;
}
