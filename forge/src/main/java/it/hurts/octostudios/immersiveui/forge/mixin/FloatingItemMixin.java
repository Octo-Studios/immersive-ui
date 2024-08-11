package it.hurts.octostudios.immersiveui.forge.mixin;

import it.hurts.octostudios.immersiveui.util.CommonCode;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

@Mixin(AbstractContainerScreen.class)
public abstract class FloatingItemMixin {
    @Unique
    Map<Slot, Float> expandingProgress = new HashMap<>();

    @Shadow
    @Nullable
    protected Slot hoveredSlot;

    @Inject(method = "renderSlotHighlight(Lnet/minecraft/client/gui/GuiGraphics;IIII)V", at = @At(value = "HEAD"), cancellable = true, remap = false)
    private static void disableSlotHighlight(GuiGraphics arg, int i, int j, int k, int color, CallbackInfo ci) {
        ci.cancel();
    }
}
