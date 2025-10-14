package it.hurts.shatterbyte.immersiveui.compat;

import net.minecraft.client.gui.screens.Screen;

public class DummySophisticatedCompat implements SophisticatedProxy{
    @Override
    public boolean isStorageScreenBase(Screen screen) {
        return false;
    }
}
