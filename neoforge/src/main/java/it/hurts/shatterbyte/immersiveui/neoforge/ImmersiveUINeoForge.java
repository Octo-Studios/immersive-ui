package it.hurts.shatterbyte.immersiveui.neoforge;

import it.hurts.shatterbyte.immersiveui.ImmersiveUI;
import it.hurts.shatterbyte.immersiveui.compat.SophisticatedProxy;
import it.hurts.shatterbyte.shatterlib.module.config.ConfigManager;
import it.hurts.shatterbyte.shatterlib.neoforge.NeoForgePlatformHelper;
import it.hurts.shatterbyte.shatterlib.platform.ShatterLibServices;
import lombok.SneakyThrows;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

import java.lang.reflect.InvocationTargetException;

@Mod(ImmersiveUI.MOD_ID)
@EventBusSubscriber
public final class ImmersiveUINeoForge {
    public ImmersiveUINeoForge(IEventBus modBus, ModContainer container) {
        ShatterLibServices.initialize(new NeoForgePlatformHelper(modBus));
        ImmersiveUI.init();
        if (ModList.get().isLoaded("sophisticatedcore")) {
            try {
                ImmersiveUI.SOPHISTICATED_COMPAT = (SophisticatedProxy) Class.forName("it.hurts.shatterbyte.immersiveui.compat.SophisticatedCompat").getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
