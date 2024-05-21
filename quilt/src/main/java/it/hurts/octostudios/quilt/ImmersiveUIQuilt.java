package it.hurts.octostudios.quilt;

import it.hurts.octostudios.fabriclike.ImmersiveUIFabricLike;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;

public final class ImmersiveUIQuilt implements ModInitializer {
    @Override
    public void onInitialize(ModContainer mod) {
        // Run the Fabric-like setup.
        ImmersiveUIFabricLike.init();
    }
}
