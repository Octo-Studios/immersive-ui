package it.hurts.octostudios.mixin;

import it.hurts.octostudios.system.particles.ParticleStorage;
import it.hurts.octostudios.system.particles.data.ParticleData;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public class ScreenMixin {
    @Inject(method = "render", at = @At("TAIL"))
    public void ä(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        for (ParticleData data : ParticleStorage.getParticlesData()) {
            if (data.getScreen() == null) continue;
            //data.render(data.getPoseStackSnapshot(), partialTick);
        }
    }

    @Inject(method = "onClose", at = @At("HEAD"))
    public void clearContainerParticles(CallbackInfo ci) {
        //List<ParticleData> toRemove = ParticleStorage.getParticlesData().stream().filter(data -> data.getScreen() != null && AbstractContainerScreen.class.isAssignableFrom(data.getScreen())).toList();
        //ParticleStorage.getParticlesData().removeAll(toRemove);
    }

}
