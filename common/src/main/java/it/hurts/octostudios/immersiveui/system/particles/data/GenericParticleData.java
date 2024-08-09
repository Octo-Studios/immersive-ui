package it.hurts.octostudios.immersiveui.system.particles.data;

import it.hurts.octostudios.immersiveui.ImmersiveUI;
import net.minecraft.resources.ResourceLocation;

public class GenericParticleData extends ParticleData {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(ImmersiveUI.MOD_ID, "textures/gui/spark.png");

    public GenericParticleData(int startColor, int endColor, float speed, float xStart, float yStart, float size, int lifeTime, ParticleEmitter emitter) {
        super(TEXTURE, speed, lifeTime, xStart, yStart, emitter);
        this.startColor = startColor;
        this.endColor = endColor;
        this.size = size;
    }
}