package it.hurts.shatterbyte.immersiveui;

import it.hurts.shatterbyte.immersiveui.compat.DummySophisticatedCompat;
import it.hurts.shatterbyte.immersiveui.compat.SophisticatedProxy;
import it.hurts.shatterbyte.shatterlib.module.config.ConfigManager;
import it.hurts.shatterbyte.shatterlib.platform.ShatterLibServices;

public final class ImmersiveUI {
    public static final String MOD_ID = "immersiveui";
    public static Config CONFIG = new Config();
    public static SophisticatedProxy SOPHISTICATED_COMPAT = new DummySophisticatedCompat();

    public static void init() {
        ConfigManager.register(MOD_ID, CONFIG);
        CONFIG.load(ShatterLibServices.platform().getConfigDirectory());
    }
}
