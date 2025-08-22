package it.hurts.octostudios.immersiveui.client.particle;

import it.hurts.octostudios.immersiveui.util.VectorUtils;
import it.hurts.octostudios.octolib.client.particle.ExtendedUIParticle;
import it.hurts.octostudios.octolib.util.OctoColor;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector2f;

import java.util.Random;

public class FlameUIParticle extends ExtendedUIParticle {
    private static final Texture2D TEXTURE = new Texture2D(ResourceLocation.withDefaultNamespace("textures/particle/flame.png"), 8, 8);

    public FlameUIParticle(float xStart, float yStart, int lifeTime) {
        super(TEXTURE, 1f, lifeTime, xStart, yStart, Layer.SCREEN, 232f);
        Random rand = new Random();
        ;
        this.getTransform().setSize(new Vector2f(1.25f, 1.25f));
        this.setColors(OctoColor.WHITE);
        this.setGravityDirection(new Vector2f(0, -1));
        this.setGravity(0.2f);
        this.setDirection(VectorUtils.rotate(new Vector2f(0, -1), (rand.nextFloat()-0.5f)*60f));

        this.getTransform().updateOldValues();
    }

    @Override
    public void tick() {
        super.tick();

        float timeRatio = this.getTimeRatio(0f);
        this.getTransform().setSize(new Vector2f(1-timeRatio, 1-timeRatio));
    }
}