package it.hurts.octostudios.immersiveui.system.particles.data;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import it.hurts.octostudios.immersiveui.util.RenderUtils;
import it.hurts.octostudios.immersiveui.util.VectorUtils;
import lombok.Data;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL11;
import oshi.util.tuples.Pair;

public class ParticleData {
    @Data
    public static class Texture2D {
        private ResourceLocation rl;
        private float width, height;
        private float texOffX, texOffY;
        private float texWidth, texHeight;

        public Texture2D(ResourceLocation texture, int texOffX, int texOffY, int texWidth, int texHeight, int width, int height) {
            this.rl = texture;
            this.texOffX = texOffX;
            this.texOffY = texOffY;
            this.texWidth = texWidth;
            this.texHeight = texHeight;
            this.width = width;
            this.height = height;
        }

        public Texture2D(ResourceLocation texture, int width, int height) {
            this.rl = texture;
            this.width = width;
            this.height = height;
            this.texWidth = width;
            this.texHeight = height;
        }
    }

    @Getter
    private final Texture2D texture;

    public ParticleEmitter emitter() {
        return emitter;
    }

    @Getter
    private final float maxSpeed;
    @Getter
    private final int maxLifetime;
    @Getter
    private final Vector2f startPos;
    @Getter
    private Vector2f oldPos;

    private final ParticleEmitter emitter;

    public Vector2f position;
    public Vector2f direction;
    public Vector2f gravityDirection;
    public float speed;
    public float gravity;
    private float gravityAccel;
    public float friction;
    public float size;
    public float angularVelocity;
    public int lifetime;
    public int startColor;
    public int endColor;
    public Pair<Integer, Integer> blendFunc;
    public boolean enableBlend;
    public boolean resizeWithLifetime;

    @Getter
    private int tickCount;

    public ParticleData(Texture2D texture, float maxSpeed, int maxLifetime, float xStart, float yStart, ParticleEmitter emitter) {
        this.texture = texture;
        this.maxSpeed = maxSpeed;
        this.maxLifetime = maxLifetime;
        this.lifetime = maxLifetime;
        this.speed = maxSpeed;
        this.emitter = emitter;
        this.startPos = new Vector2f(xStart, yStart);
        this.position = startPos;
        this.oldPos = startPos;
        this.size = 1f;
        this.friction = 0f;
        this.startColor = 0xFFFFFFFF;
        this.endColor = 0;
        this.direction = new Vector2f(0,1);
        this.gravityDirection = new Vector2f(0, 1);
        this.angularVelocity = 0f;
        this.resizeWithLifetime = true;

        this.enableBlend = true;
        this.blendFunc = new Pair<>(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
    }

    public PoseStack getPoseStackSnapshot() {
        PoseStack stack = new PoseStack();
        stack.mulPose(emitter.pose());
        return stack;
    }

    public void tick() {
        this.oldPos = new Vector2f(position);

        if (angularVelocity != 0) this.direction = VectorUtils.rotate(this.direction.normalize(), angularVelocity);
        else this.direction.normalize();

        this.speed = Mth.clamp(this.speed*(1-friction), 0, maxSpeed);
        this.gravityAccel += gravity/20f;
        this.lifetime = Mth.clamp(this.lifetime - 1, 0, maxLifetime);

        this.position.add(direction.mul(speed));
        if (gravityAccel != 0) this.position.add(gravityDirection.x * gravityAccel, gravityDirection.y * gravityAccel);

        this.lifetime -= 1;
        tickCount += 1;
    }

    public void render(PoseStack pose, float partialTick) {
        Minecraft MC = Minecraft.getInstance();
        Texture2D tex = getTexture();

        float lifePercentage = (float) lifetime / maxLifetime;

        int color = RenderUtils.lerpColor(startColor, endColor, 1-lifePercentage);

        int alpha = (color >> 24) & 0xFF;
        int red = (color >> 16) & 0xFF;
        int green = (color >> 8) & 0xFF;
        int blue = color & 0xFF;

        RenderSystem.setShaderColor(red / 255F, green / 255F, blue / 255F, alpha / 255f);
        RenderSystem.setShaderTexture(0, getTexture().rl);

        RenderSystem.enableBlend();
        if (enableBlend) {
            RenderSystem.blendFunc(blendFunc.getA(), blendFunc.getB());
        }

        RenderUtils.renderTextureFromCenter(pose, Mth.lerp(partialTick, oldPos.x, position.x), Mth.lerp(partialTick, oldPos.y, position.y), tex.texOffX, tex.texOffY, tex.texWidth, tex.texHeight, tex.width, tex.height, size * (resizeWithLifetime?lifePercentage:1));

        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);

        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
    }
}
