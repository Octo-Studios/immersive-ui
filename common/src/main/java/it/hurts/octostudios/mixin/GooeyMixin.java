package it.hurts.octostudios.mixin;

import it.hurts.octostudios.client.TickManager;
import it.hurts.octostudios.system.particles.ParticleStorage;
import it.hurts.octostudios.system.particles.data.GenericParticleData;
import it.hurts.octostudios.system.particles.data.ParticleData;
import it.hurts.octostudios.system.particles.data.ParticleEmitter;
import it.hurts.octostudios.util.VectorUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.joml.Vector2f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.struct.InjectorGroupInfo;

import java.util.*;

import static it.hurts.octostudios.client.TickManager.*;

@Mixin(Gui.class)
public class GooeyMixin {
    @Shadow @Final private Minecraft minecraft;
    @Unique
    Random random = new Random();

    @Inject(method = "tick()V", at = @At("TAIL"))
    public void ö(CallbackInfo ci) {
        Set<ParticleEmitter> toRemoveSet = new HashSet<>();

        for (Map.Entry<ParticleEmitter, List<ParticleData>> entry : ParticleStorage.EMITTERS.entrySet()) {
            ParticleEmitter emitter = entry.getKey();

            List<ParticleData> toRemove = new ArrayList<>();

            for (ParticleData data : entry.getValue()) {
                data.tick();
                if (data.lifetime <= 0) toRemove.add(data);
            }
            ParticleStorage.EMITTERS.get(emitter).removeAll(toRemove);

            if (ParticleStorage.EMITTERS.get(emitter).isEmpty()) toRemoveSet.add(emitter);
        }

        toRemoveSet.forEach(ParticleStorage.EMITTERS::remove);
    }

    @Inject(method = "render", at = @At("TAIL"))
    public void ä(GuiGraphics guiGraphics, float partialTick, CallbackInfo ci) {
        currentTime = System.currentTimeMillis();
        elapsedTime = currentTime - TickManager.lastExecutedTime;
        if (elapsedTime >= TARGET_INTERVAL_MS) {
            lastExecutedTime = currentTime;
        }

        for (ParticleData data : ParticleStorage.getParticlesData().stream().filter(data -> data.getPoseStackSnapshot().last().pose().getRowColumn(2,3) <= 50).toList()) {
            data.render(data.getPoseStackSnapshot(), guiGraphics, partialTick);
        }
    }
}
