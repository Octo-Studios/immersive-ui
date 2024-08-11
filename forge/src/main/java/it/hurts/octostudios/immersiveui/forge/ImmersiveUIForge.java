package it.hurts.octostudios.immersiveui.forge;

import it.hurts.octostudios.immersiveui.ImmersiveUI;
import net.minecraftforge.fml.common.Mod;

@Mod(ImmersiveUI.MOD_ID)
//@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public final class ImmersiveUIForge {
    public ImmersiveUIForge() {
//        // Submit our event bus to let Architectury API register our content on the right time.
//        EventBuses.registerModEventBus(ImmersiveUI.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());

        // Run our common setup.
        ImmersiveUI.init();
    }
//
//    @SubscribeEvent
//    public static void setup(FMLCommonSetupEvent event) {
//        ConfigManager.registerConfig("immersiveui", ImmersiveUI.CONFIG);
//    }
}
