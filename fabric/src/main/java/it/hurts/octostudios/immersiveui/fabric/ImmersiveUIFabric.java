package it.hurts.octostudios.immersiveui.fabric;

import it.hurts.octostudios.immersiveui.ImmersiveUI;
import it.hurts.octostudios.octolib.module.config.ConfigManager;
import net.fabricmc.api.ModInitializer;

public final class ImmersiveUIFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ConfigManager.registerConfig("immersiveui", ImmersiveUI.CONFIG);
    }
}
