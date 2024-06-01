package it.hurts.octostudios.mixin;

import com.mojang.math.Axis;
import it.hurts.octostudios.client.TickManager;
import it.hurts.octostudios.system.particles.ParticleStorage;
import it.hurts.octostudios.system.particles.data.ParticleData;
import it.hurts.octostudios.util.VectorUtils;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static it.hurts.octostudios.client.TickManager.*;

@Mixin(Gui.class)
public class GooeyMixin {
    @Shadow @Final private Minecraft minecraft;

    @Inject(method = "tick()V", at = @At("TAIL"))
    public void ö(CallbackInfo ci) {
        List<ParticleData> toRemove = new ArrayList<>();
        for (ParticleData data : ParticleStorage.getParticlesData()) {
            data.tick();
            if (data.lifetime <= 0) toRemove.add(data);
        }
        ParticleStorage.getParticlesData().removeAll(toRemove);
    }

    @Inject(method = "render", at = @At("TAIL"))
    public void ä(GuiGraphics guiGraphics, float partialTick, CallbackInfo ci) {
        currentTime = System.currentTimeMillis();
        elapsedTime = currentTime - TickManager.lastExecutedTime;
        if (elapsedTime >= TARGET_INTERVAL_MS*2) lastExecutedTime = currentTime;

        for (ParticleData data : ParticleStorage.getParticlesData()) {
            //if (data.getScreen() != null) continue;
            data.render(minecraft.screen, guiGraphics, partialTick);
        }
    }
}
