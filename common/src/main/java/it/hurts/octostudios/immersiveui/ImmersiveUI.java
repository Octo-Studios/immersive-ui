package it.hurts.octostudios.immersiveui;

import it.hurts.octostudios.immersiveui.compat.DummySophisticatedCompat;
import it.hurts.octostudios.immersiveui.compat.SophisticatedProxy;

public final class ImmersiveUI {
    public static final String MOD_ID = "immersiveui";
    public static Config CONFIG = new Config();
    public static SophisticatedProxy SOPHISTICATED_COMPAT = new DummySophisticatedCompat();

    public static void init() {
        // Write common init code here.
    }
}
