package it.hurts.octostudios.immersiveui.mixin;

import it.hurts.octostudios.immersiveui.client.MouseInfo;
import it.hurts.octostudios.immersiveui.client.RenderInfo;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractContainerScreen.class)
public interface AbstractContainerScreenAccessor {
    @Accessor
    Slot getHoveredSlot();

    @Accessor("leftPos")
    int getLeftPos();

    @Accessor("topPos")
    int getTopPos();
}
