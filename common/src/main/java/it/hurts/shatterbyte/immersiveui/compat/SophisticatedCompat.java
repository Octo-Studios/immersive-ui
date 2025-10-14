package it.hurts.shatterbyte.immersiveui.compat;

import lombok.SneakyThrows;
import net.minecraft.client.gui.screens.Screen;

public class SophisticatedCompat implements SophisticatedProxy {
    @SneakyThrows
    @Override
    public boolean isStorageScreenBase(Screen screen) {
        return Class.forName("net.p3pp3rf1y.sophisticatedcore.client.gui.StorageScreenBase").isInstance(screen);
    }
}
