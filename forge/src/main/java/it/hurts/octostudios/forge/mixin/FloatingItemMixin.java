package it.hurts.octostudios.forge.mixin;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerScreen.class)
public class FloatingItemMixin {
    @Inject(method = "renderSlotHighlight(Lnet/minecraft/client/gui/GuiGraphics;IIII)V", at = @At(value = "HEAD"), cancellable = true, remap = false)
    private static void vpizdu(GuiGraphics arg, int i, int j, int k, int color, @NotNull CallbackInfo ci) {
        ci.cancel();
    }
}
