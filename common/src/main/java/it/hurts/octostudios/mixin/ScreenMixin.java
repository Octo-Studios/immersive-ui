package it.hurts.octostudios.mixin;

import it.hurts.octostudios.system.particles.ParticleStorage;
import it.hurts.octostudios.system.particles.data.ParticleData;
import it.hurts.octostudios.system.particles.data.ParticleEmitter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Set;

import static it.hurts.octostudios.client.VariableStorage.SCREEN_ZORDER;


@Mixin(Screen.class)
public class ScreenMixin {
    @Inject(method = "render", at = @At("TAIL"))
    public void renderParticlesIfTheScreenIsPresentAndTheZOrderIsHighEnough(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        for (ParticleData data : ParticleStorage.getParticlesData().stream().filter(data -> data.getPoseStackSnapshot().last().pose().getRowColumn(2,3) > SCREEN_ZORDER).toList()) {
            data.render(data.getPoseStackSnapshot(), guiGraphics, Minecraft.getInstance().getFrameTime());
        }
    }

    @Inject(method = "onClose", at = @At("TAIL"))
    public void clearScreenParticles(CallbackInfo ci) {
        Set<ParticleEmitter> toRemove = new HashSet<>();
        for (ParticleEmitter emitter : ParticleStorage.EMITTERS.keySet()) {
            if (emitter.pose().getRowColumn(2,3) > SCREEN_ZORDER) toRemove.add(emitter);
        }
        for (ParticleEmitter emitter : toRemove) ParticleStorage.EMITTERS.remove(emitter);
    }
}
