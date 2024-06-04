package it.hurts.octostudios.mixin;

import it.hurts.octostudios.client.TickManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

import static it.hurts.octostudios.client.TickManager.*;

@Mixin(Gui.class)
public class GooeyMixin {
    @Shadow @Final private Minecraft minecraft;
    @Unique
    Random random = new Random();

    @Inject(method = "tick()V", at = @At("TAIL"))
    public void ö(CallbackInfo ci) {

    }

    @Inject(method = "render", at = @At("TAIL"))
    public void ä(GuiGraphics guiGraphics, float partialTick, CallbackInfo ci) {
        currentTime = System.currentTimeMillis();
        elapsedTime = currentTime - TickManager.lastExecutedTime;
        if (elapsedTime >= TARGET_INTERVAL_MS) {
            lastExecutedTime = currentTime;
        }
    }
}
