package it.hurts.octostudios.immersiveui.neoforge;

import dev.architectury.platform.Platform;
import it.hurts.octostudios.immersiveui.ImmersiveUI;
import it.hurts.octostudios.immersiveui.compat.SophisticatedCompat;
import it.hurts.octostudios.immersiveui.compat.SophisticatedProxy;
import it.hurts.shatterbyte.shatterlib.module.config.ConfigManager;
import lombok.SneakyThrows;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod(ImmersiveUI.MOD_ID)
@EventBusSubscriber
public final class ImmersiveUINeoForge {
    @SneakyThrows
    public ImmersiveUINeoForge() {
//        // Submit our event bus to let Architectury API register our content on the right time.
//        EventBuses.registerModEventBus(ImmersiveUI.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());

        // Run our common setup.
        ImmersiveUI.init();
        if (Platform.isModLoaded("sophisticatedcore")) {
            ImmersiveUI.SOPHISTICATED_COMPAT = (SophisticatedProxy) Class.forName("it.hurts.octostudios.immersiveui.compat.SophisticatedCompat").getDeclaredConstructor().newInstance();
        }
    }

    @SubscribeEvent
    public static void setup(FMLCommonSetupEvent event) {
        ConfigManager.registerConfig("immersiveui", ImmersiveUI.CONFIG);
    }
}
