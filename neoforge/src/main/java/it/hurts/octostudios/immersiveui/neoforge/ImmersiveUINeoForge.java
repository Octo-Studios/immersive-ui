package it.hurts.octostudios.immersiveui.neoforge;

import it.hurts.octostudios.immersiveui.ImmersiveUI;
import it.hurts.octostudios.octolib.module.config.ConfigManager;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod(ImmersiveUI.MOD_ID)
@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public final class ImmersiveUINeoForge {
    public ImmersiveUINeoForge() {
//        // Submit our event bus to let Architectury API register our content on the right time.
//        EventBuses.registerModEventBus(ImmersiveUI.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());

        // Run our common setup.
        ImmersiveUI.init();
    }

    @SubscribeEvent
    public static void setup(FMLCommonSetupEvent event) {
        ConfigManager.registerConfig("immersiveui", ImmersiveUI.CONFIG);
    }
}
