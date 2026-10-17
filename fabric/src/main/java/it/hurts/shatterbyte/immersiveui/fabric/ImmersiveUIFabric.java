package it.hurts.shatterbyte.immersiveui.fabric;

import it.hurts.shatterbyte.immersiveui.ImmersiveUI;
import it.hurts.shatterbyte.shatterlib.fabric.FabricPlatformHelper;
import it.hurts.shatterbyte.shatterlib.module.config.ConfigManager;
import it.hurts.shatterbyte.shatterlib.platform.ShatterLibServices;
import net.fabricmc.api.ModInitializer;

import static it.hurts.shatterbyte.immersiveui.ImmersiveUI.CONFIG;
import static it.hurts.shatterbyte.immersiveui.ImmersiveUI.MOD_ID;

public final class ImmersiveUIFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ShatterLibServices.initialize(FabricPlatformHelper.INSTANCE);
        ImmersiveUI.init();
    }
}
