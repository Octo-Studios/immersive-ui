package it.hurts.octostudios.immersiveui.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import it.hurts.octostudios.immersiveui.compat.ExtraScreenData;
import it.hurts.octostudios.immersiveui.util.CommonCode;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Inject(require = 0, method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;renderWithTooltipAndSubtitles(Lnet/minecraft/client/gui/GuiGraphics;IIF)V", shift = At.Shift.BEFORE))
    private void beforeScreenRender(DeltaTracker deltaTracker, boolean renderLevel, CallbackInfo ci, @Local(ordinal = 0) int i, @Local(ordinal = 1) int j) {
        Screen screen = Minecraft.getInstance().screen;
        if (screen instanceof ExtraScreenData screenData) {
            CommonCode.computeMouseDelta(screenData.getMouseInfo(), i, j);
        }
    }
}
