package it.hurts.octostudios.immersiveui.quilt;

import it.hurts.octostudios.immersiveui.ImmersiveUI;
import it.hurts.octostudios.octolib.modules.config.ConfigManager;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;

public final class ImmersiveUIQuilt implements ModInitializer {
    @Override
    public void onInitialize(ModContainer mod) {
        ConfigManager.registerConfig("immersiveui", ImmersiveUI.CONFIG);
    }
}
