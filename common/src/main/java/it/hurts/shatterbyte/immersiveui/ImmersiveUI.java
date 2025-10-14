package it.hurts.shatterbyte.immersiveui;

import it.hurts.shatterbyte.immersiveui.compat.DummySophisticatedCompat;
import it.hurts.shatterbyte.immersiveui.compat.SophisticatedProxy;

public final class ImmersiveUI {
    public static final String MOD_ID = "immersiveui";
    public static Config CONFIG = new Config();
    public static SophisticatedProxy SOPHISTICATED_COMPAT = new DummySophisticatedCompat();

    public static void init() {
        // Write common init code here.
    }
}
