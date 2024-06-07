package it.hurts.octostudios.util;

import it.hurts.octostudios.client.VariableStorage;
import it.hurts.octostudios.system.particles.ParticleStorage;
import it.hurts.octostudios.system.particles.data.ParticleData;
import net.minecraft.client.gui.GuiGraphics;

import static it.hurts.octostudios.client.VariableStorage.*;

public class CommonCode {
    public static void gooeyRenderCode(GuiGraphics guiGraphics, float partialTick) {
        currentTime = System.currentTimeMillis();
        elapsedTime = currentTime - VariableStorage.lastExecutedTime;
        if (elapsedTime >= TARGET_INTERVAL_MS) {
            lastExecutedTime = currentTime;
        }

        for (ParticleData data : ParticleStorage.getParticlesData().stream().filter(data -> data.getPoseStackSnapshot().last().pose().getRowColumn(2,3) <= SCREEN_ZORDER).toList()) {
            data.render(data.getPoseStackSnapshot(), guiGraphics, partialTick);
        }
    }
}
