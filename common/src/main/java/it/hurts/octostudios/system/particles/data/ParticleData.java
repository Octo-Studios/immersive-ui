package it.hurts.octostudios.system.particles.data;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import it.hurts.octostudios.util.RenderUtils;
import it.hurts.octostudios.util.VectorUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL11;

import java.util.Random;

public class ParticleData {
    public ResourceLocation getTexture() {
        return texture;
    }

    public int getTickCount() {
        return tickCount;
    }

    private final ResourceLocation texture;

    public float getMaxSpeed() {
        return maxSpeed;
    }

    public int getMaxLifetime() {
        return maxLifetime;
    }

    public Vector2f getStartPos() {
        return startPos;
    }

    public ParticleEmitter emitter() {
        return emitter;
    }

    private final float maxSpeed;
    private final int maxLifetime;
    private final Vector2f startPos;
    private Vector2f oldPos;

    private final ParticleEmitter emitter;

    public @Nullable Class<? extends Screen> getScreen() {
        return screen;
    }

    private final @Nullable Class<? extends Screen> screen;

    public Vector2f position;
    public Vector2f direction;
    public float speed;
    public float friction;
    public float size;
    public float angularVelocity;
    public int lifetime;
    public int color;


    public boolean isBeingRendered;

    private int tickCount;

    public ParticleData(@Nullable Class<? extends Screen> screen, ResourceLocation texture, float maxSpeed, int maxLifetime, float xStart, float yStart, ParticleEmitter emitter) {
        this.screen = screen;
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
        this.color = 0xFFFFFFFF;
        this.direction = new Vector2f(0,1);
        this.angularVelocity = 0f;
    }

    public PoseStack getPoseStackSnapshot() {
        PoseStack stack = new PoseStack();
        stack.mulPoseMatrix(emitter.pose());
        return stack;
    }

    public void tick() {
        this.oldPos = new Vector2f(position);

        if (angularVelocity != 0) this.direction = VectorUtils.rotate(this.direction.normalize(), angularVelocity);
        else this.direction.normalize();

        this.speed = Mth.clamp(this.speed*(1-friction), 0, maxSpeed);
        this.lifetime = Mth.clamp(this.lifetime - 1, 0, maxLifetime);

        this.position.add(direction.mul(speed));

        this.lifetime -= 1;
        tickCount += 1;
    }

    public void render(PoseStack pose, float partialTick) {
        Minecraft MC = Minecraft.getInstance();

        float lifePercentage = (float) lifetime / maxLifetime;

        int alpha = (color >> 24) & 0xFF;
        int red = (color >> 16) & 0xFF;
        int green = (color >> 8) & 0xFF;
        int blue = color & 0xFF;

        RenderSystem.setShaderColor(red / 255F, green / 255F, blue / 255F, lifePercentage);
        RenderSystem.setShaderTexture(0, getTexture());

        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);

        RenderUtils.renderTextureFromCenter(pose, Mth.lerp(partialTick, oldPos.x, position.x), Mth.lerp(partialTick, oldPos.y, position.y), 8, 8, size * lifePercentage);

        //System.out.println(oldPos == position);
        //guiGraphics.blit(getTexture(), (int) position.x, (int) position.y, 8, 8, 0, 0, 8, 8, 8, 8);

        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);

        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
    }
}
