package it.hurts.shatterbyte.immersiveui.compat;

import lombok.SneakyThrows;
import net.minecraft.client.gui.screens.Screen;

public class SophisticatedCompat implements SophisticatedProxy {
    @Override
    public boolean isStorageScreenBase(Screen screen) {
        try {
            return Class.forName("net.p3pp3rf1y.sophisticatedcore.client.gui.StorageScreenBase").isInstance(screen);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
