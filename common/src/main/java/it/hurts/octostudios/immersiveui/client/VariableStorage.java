package it.hurts.octostudios.immersiveui.client;

import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.List;

public class VariableStorage {
    public static final long TARGET_INTERVAL_MS = 350; // Interval in milliseconds
    public static long lastExecutedTime = System.currentTimeMillis();
    public static long currentTime;
    public static long elapsedTime;
    public static final double SCREEN_ZORDER = 50;

    public static List<Screen> shakeScreen = new ArrayList<>();
}
