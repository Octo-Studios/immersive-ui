package it.hurts.octostudios.immersiveui.mixin;

import it.hurts.octostudios.immersiveui.system.particles.ParticleStorage;
import it.hurts.octostudios.immersiveui.system.particles.data.ParticleData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public class ScreenMixin {
    @Inject(method = "onClose", at = @At("TAIL"))
    public void clearScreenParticles(CallbackInfo ci) {
        ParticleStorage.EMITTERS.clear();
    }
}
