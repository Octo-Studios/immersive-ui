package it.hurts.octostudios.immersiveui.system.particles.data;

import it.hurts.octostudios.immersiveui.ImmersiveUI;
import it.hurts.octostudios.immersiveui.util.VectorUtils;
import net.minecraft.resources.ResourceLocation;
import org.joml.Random;
import org.joml.Vector2f;

import java.awt.*;

public class AnvilParticleData extends ParticleData {
    private static final Texture2D TEXTURE = new Texture2D(ResourceLocation.fromNamespaceAndPath(ImmersiveUI.MOD_ID, "textures/gui/hammer.png"), 8, 8);

    public AnvilParticleData(float speed, float xStart, float yStart, float size, int lifeTime, ParticleEmitter emitter) {
        super(TEXTURE, speed, lifeTime, xStart, yStart, emitter);
        Random rand = new Random();

        this.size = size;
        int gray = 170+rand.nextInt(77);
        Color color = new Color(255,255,255, 255);


        this.startColor = color.getRGB();
        this.endColor = color.getRGB();
        this.speed = rand.nextFloat()*speed+3f;
        this.direction = VectorUtils.rotate(new Vector2f(0, -1), (rand.nextFloat()-0.5f)*30f);
        this.gravity = 18;
        this.resizeWithLifetime = true;

        this.enableBlend = false;
    }
}