package it.hurts.shatterbyte.immersiveui.fabric;

import it.hurts.shatterbyte.immersiveui.ImmersiveUI;
import it.hurts.shatterbyte.shatterlib.module.config.ConfigManager;
import net.fabricmc.api.ModInitializer;

public final class ImmersiveUIFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ConfigManager.registerConfig("immersiveui", ImmersiveUI.CONFIG);
    }
}
