package it.hurts.shatterbyte.immersiveui.client.particle;

import it.hurts.shatterbyte.immersiveui.ImmersiveUI;
import it.hurts.shatterbyte.shatterlib.client.particle.ExtendedUIParticle;
import it.hurts.shatterbyte.shatterlib.client.particle.UIParticle;
import it.hurts.shatterbyte.shatterlib.util.ShatterColor;
import net.minecraft.resources.Identifier;
import org.joml.Vector2f;

public class RarityUIParticle extends ExtendedUIParticle {
    public static final Texture2D TEXTURE = new Texture2D(Identifier.fromNamespaceAndPath(ImmersiveUI.MOD_ID, "textures/gui/spark.png"), 8, 8);

    public RarityUIParticle(float maxSpeed, int lifetime, float xStart, float yStart, float xDir, float yDir, float roll, int rarityColor,  Layer layer, float zOffset) {
        super(TEXTURE, maxSpeed, lifetime, xStart, yStart, layer, zOffset);
        this.setRenderPipeline(UIParticle.ADDITIVE_PIPELINE);
        this.setDirection(xDir, yDir);
        this.setRollVelocity(roll);
        this.setColors(new ShatterColor(0xff000000+rarityColor), new ShatterColor(rarityColor));
    }

    @Override
    public void tick() {
        super.tick();

        float timeRatio = this.getTimeRatio(0f);
        this.getTransform().setSize(new Vector2f(1-timeRatio, 1-timeRatio));
    }
}
