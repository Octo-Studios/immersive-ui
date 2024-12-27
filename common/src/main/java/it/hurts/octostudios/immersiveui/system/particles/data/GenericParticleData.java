package it.hurts.octostudios.immersiveui.system.particles.data;

import it.hurts.octostudios.immersiveui.ImmersiveUI;
import net.minecraft.resources.ResourceLocation;

public class GenericParticleData extends ParticleData {
    private static final Texture2D TEXTURE = new Texture2D(new ResourceLocation(ImmersiveUI.MOD_ID, "textures/gui/spark.png"), 8, 8);

    public GenericParticleData(int startColor, int endColor, float speed, float xStart, float yStart, float size, int lifeTime, ParticleEmitter emitter) {
        super(TEXTURE, speed, lifeTime, xStart, yStart, emitter);
        this.startColor = startColor;
        this.endColor = endColor;
        this.size = size;
    }
}