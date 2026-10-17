package it.hurts.shatterbyte.immersiveui.neoforge;

import it.hurts.shatterbyte.immersiveui.ImmersiveUI;
import it.hurts.shatterbyte.immersiveui.compat.SophisticatedProxy;
import it.hurts.shatterbyte.shatterlib.module.config.ConfigManager;
import it.hurts.shatterbyte.shatterlib.neoforge.NeoForgePlatformHelper;
import it.hurts.shatterbyte.shatterlib.platform.ShatterLibServices;
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
        ImmersiveUI.init();
        if (ModList.get().isLoaded("sophisticatedcore")) {
            ImmersiveUI.SOPHISTICATED_COMPAT = (SophisticatedProxy) Class.forName("it.hurts.shatterbyte.immersiveui.compat.SophisticatedCompat").getDeclaredConstructor().newInstance();
        }
    }
}
