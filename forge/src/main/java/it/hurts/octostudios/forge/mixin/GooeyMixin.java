package it.hurts.octostudios.forge.mixin;

import it.hurts.octostudios.util.CommonCode;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GooeyMixin {
    @Inject(method = "renderHotbar", at = @At("TAIL"))
    public void ää(float partialTick, GuiGraphics guiGraphics, CallbackInfo ci) {
        CommonCode.gooeyRenderCode(guiGraphics, partialTick);
    }
}
