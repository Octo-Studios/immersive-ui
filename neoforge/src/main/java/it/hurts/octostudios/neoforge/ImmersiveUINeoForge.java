package it.hurts.octostudios.neoforge;

import it.hurts.octostudios.ImmersiveUI;
import net.neoforged.fml.common.Mod;

@Mod(ImmersiveUI.MOD_ID)
public final class ImmersiveUINeoForge {
    public ImmersiveUINeoForge() {
//        // Submit our event bus to let Architectury API register our content on the right time.
//        EventBuses.registerModEventBus(ImmersiveUI.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());

        // Run our common setup.
        ImmersiveUI.init();
    }
}
