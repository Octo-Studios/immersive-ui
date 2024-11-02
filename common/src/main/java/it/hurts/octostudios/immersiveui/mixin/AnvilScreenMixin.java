package it.hurts.octostudios.immersiveui.mixin;

import it.hurts.octostudios.immersiveui.client.VariableStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(AnvilScreen.class)
public class AnvilScreenMixin {
    @Unique
    float timer;

    @Inject(method = "renderBg", at = @At("HEAD"))
    public void renderBg(GuiGraphics guiGraphics, float f, int i, int j, CallbackInfo ci) {
        if (VariableStorage.anvilParticleSpawn) {
            VariableStorage.anvilParticleSpawn = false;
            timer = 8;
        }

        if (timer > 0) {
            Random rand = new Random();
            timer = Mth.clamp(timer-Minecraft.getInstance().getTimer().getRealtimeDeltaTicks(), 0, 10);
            guiGraphics.pose().translate(rand.nextInt(-1, 1)*(timer/10f)*1.5f, rand.nextInt(-1, 1)*(timer/10f)*1.5f, 0);
        }
    }
}
