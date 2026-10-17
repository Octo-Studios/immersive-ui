package it.hurts.shatterbyte.immersiveui.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import it.hurts.shatterbyte.immersiveui.compat.ExtraScreenData;
import it.hurts.shatterbyte.immersiveui.util.CommonCode;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

@Mixin(Gui.class)
public class GooeyMixin {
    @Shadow @Final private Minecraft minecraft;
    @Unique
    Random random = new Random();

    @Inject(method = "extractRenderState", at = @At("TAIL"))
    public void renderCode(DeltaTracker deltaTracker, boolean shouldRenderLevel, boolean resourcesLoaded, CallbackInfo ci) {
        CommonCode.gooeyRenderCode(deltaTracker.getGameTimeDeltaPartialTick(true));
    }

    @Inject(require = 0, method = "extractRenderState", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;extractRenderStateWithTooltipAndSubtitles(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IIF)V", shift = At.Shift.BEFORE))
    private void beforeScreenRender(DeltaTracker deltaTracker, boolean shouldRenderLevel, boolean resourcesLoaded, CallbackInfo ci, @Local(ordinal = 0) int xMouse, @Local(ordinal = 1) int yMouse) {
        Screen screen = Minecraft.getInstance().gui.screen();
        if (screen instanceof ExtraScreenData screenData) {
            CommonCode.computeMouseDelta(screenData.getMouseInfo(), xMouse, yMouse);
        }
    }
}
