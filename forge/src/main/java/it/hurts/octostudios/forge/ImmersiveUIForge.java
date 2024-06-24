package it.hurts.octostudios.forge;

import it.hurts.octostudios.ImmersiveUI;
import net.minecraftforge.fml.common.Mod;

@Mod(ImmersiveUI.MOD_ID)
public final class ImmersiveUIForge {
    public ImmersiveUIForge() {
//        // Submit our event bus to let Architectury API register our content on the right time.
//        EventBuses.registerModEventBus(ImmersiveUI.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());

        // Run our common setup.
        ImmersiveUI.init();
    }
}
