package it.hurts.octostudios.mixin;

import it.hurts.octostudios.client.VariableStorage;
import it.hurts.octostudios.system.particles.ParticleStorage;
import it.hurts.octostudios.system.particles.data.ParticleData;
import it.hurts.octostudios.system.particles.data.ParticleEmitter;
import it.hurts.octostudios.util.CommonCode;
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

import java.util.*;

import static it.hurts.octostudios.client.VariableStorage.shouldTick;

@Mixin(Gui.class)
public class GooeyMixin {
    @Shadow @Final private Minecraft minecraft;
    @Unique
    Random random = new Random();

    @Inject(method = "render", at = @At("TAIL"))
    public void ä(GuiGraphics guiGraphics, float partialTick, CallbackInfo ci) {
        CommonCode.gooeyRenderCode(guiGraphics, partialTick);

        guiGraphics.drawString(Minecraft.getInstance().font, "EMITTERS.size(): "+ParticleStorage.EMITTERS.size(), 1, 1, 0xFFFFFF, true);
        guiGraphics.drawString(Minecraft.getInstance().font, "getParticlesData().size(): "+ParticleStorage.getParticlesData().size(), 1, 11, 0xFFFFFF, true);

        if (Minecraft.getInstance().player == null || !Minecraft.getInstance().player.isShiftKeyDown()) return;
        int l = 31;
        for (Map.Entry<ParticleEmitter, List<ParticleData>> entry : ParticleStorage.EMITTERS.entrySet().stream().limit(10).toList()) {
            guiGraphics.drawString(Minecraft.getInstance().font, "emitter + list: "+entry.getKey()+" | "+entry.getValue().size(), 1, l, 0xffffff, true);
            l+=10;
        }
    }

    @Inject(method = "tick()V", at = @At("TAIL"))
    public void ö(CallbackInfo ci) {
        shouldTick = true;
        Set<ParticleEmitter> toRemoveSet = new HashSet<>();

        for (Map.Entry<ParticleEmitter, List<ParticleData>> entry : ParticleStorage.EMITTERS.entrySet()) {
            ParticleEmitter emitter = entry.getKey();

            List<ParticleData> toRemove = new ArrayList<>();

            for (ParticleData data : entry.getValue()) {
                data.tick();
                if (data.lifetime <= 0) toRemove.add(data);
            }
            //if (!ParticleStorage.EMITTERS.containsKey(emitter)) continue;
            entry.getValue().removeAll(toRemove);

            if (entry.getValue().isEmpty()) toRemoveSet.add(emitter);
        }

        toRemoveSet.forEach(ParticleStorage.EMITTERS::remove);
    }
}
