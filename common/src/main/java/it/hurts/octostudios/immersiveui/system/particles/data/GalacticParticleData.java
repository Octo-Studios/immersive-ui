package it.hurts.octostudios.immersiveui.system.particles.data;

import net.minecraft.resources.ResourceLocation;
import org.joml.Vector2f;

import java.util.Random;

public class GalacticParticleData extends ParticleData {
    private static final Random RANDOM = new Random();
    private static final char[] ALPHABET = "abcdefghijklmnopqrstuvwxyz".toCharArray();

    public GalacticParticleData(float maxSpeed, int maxLifetime, float xStart, float yStart, ParticleEmitter emitter) {
        super(new Texture2D(ResourceLocation.withDefaultNamespace("textures/particle/sga_").withSuffix(ALPHABET[RANDOM.nextInt(ALPHABET.length)]+".png"),
                0, 0, 8, 8, 5, 7), maxSpeed, maxLifetime, xStart, yStart, emitter);

        this.angularVelocity = 6;
        this.speed = RANDOM.nextFloat()*2;
        this.endColor = 0x0000ffff;
        this.direction = new Vector2f(RANDOM.nextFloat()-0.5f, RANDOM.nextFloat()-0.5f).normalize();
        this.size = 1f;
    }
}
