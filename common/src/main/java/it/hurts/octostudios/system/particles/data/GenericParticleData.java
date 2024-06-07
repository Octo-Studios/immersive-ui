package it.hurts.octostudios.system.particles.data;

import com.mojang.blaze3d.vertex.PoseStack;
import it.hurts.octostudios.ImmersiveUI;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class GenericParticleData extends ParticleData {
    private static final ResourceLocation TEXTURE = new ResourceLocation(ImmersiveUI.MOD_ID, "textures/gui/test.png");

    public GenericParticleData(int startColor, int endColor, float speed, float xStart, float yStart, float size, int lifeTime, ParticleEmitter emitter) {
        super(TEXTURE, speed, lifeTime, xStart, yStart, emitter);
        this.startColor = startColor;
        this.endColor = endColor;
        this.size = size;
    }


    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public void render(PoseStack poseStack, GuiGraphics guiGraphics, float partialTick) {
        super.render(poseStack, guiGraphics, partialTick);
    }
}