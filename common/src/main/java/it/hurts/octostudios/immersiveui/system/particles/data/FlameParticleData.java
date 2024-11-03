package it.hurts.octostudios.immersiveui.system.particles.data;

import it.hurts.octostudios.immersiveui.util.VectorUtils;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector2f;

import java.util.Random;

public class FlameParticleData extends ParticleData {
    private static final Texture2D TEXTURE = new Texture2D(ResourceLocation.withDefaultNamespace("textures/particle/flame.png"), 8, 8);

    public FlameParticleData(float xStart, float yStart, int lifeTime, ParticleEmitter emitter) {
        super(TEXTURE, 0.5f, lifeTime, xStart, yStart, emitter);
        Random rand = new Random();

        this.enableBlend = false;
        this.size = 1.25f;
        this.startColor = 0xffffffff;
        this.endColor = 0xffffffff;
        this.gravityDirection = new Vector2f(0, -1);
        this.gravity = 1f;
        this.direction = VectorUtils.rotate(new Vector2f(0, -1), (rand.nextFloat()-0.5f)*60f);
    }
}