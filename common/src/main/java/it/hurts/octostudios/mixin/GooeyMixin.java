package it.hurts.octostudios.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import it.hurts.octostudios.client.TickManager;
import it.hurts.octostudios.system.particles.ParticleStorage;
import it.hurts.octostudios.system.particles.data.GenericParticleData;
import it.hurts.octostudios.system.particles.data.ParticleData;
import it.hurts.octostudios.system.particles.data.ParticleEmitter;
import it.hurts.octostudios.util.VectorUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.struct.InjectorGroupInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static it.hurts.octostudios.client.TickManager.*;

@Mixin(Gui.class)
public class GooeyMixin {
    @Shadow @Final private Minecraft minecraft;
    @Unique
    Random random = new Random();

    @Inject(method = "tick()V", at = @At("TAIL"))
    public void ö(CallbackInfo ci) {
        List<ParticleData> toRemove = new ArrayList<>();
        for (ParticleData data : ParticleStorage.getParticlesData()) {
            data.tick();
            if (data.lifetime <= 0) toRemove.add(data);
        }
        ParticleStorage.getParticlesData().removeAll(toRemove);
    }

    @Inject(method = "render", at = @At("TAIL"))
    public void ä(GuiGraphics guiGraphics, float partialTick, CallbackInfo ci) {
        currentTime = System.currentTimeMillis();
        elapsedTime = currentTime - TickManager.lastExecutedTime;
        if (elapsedTime >= TARGET_INTERVAL_MS) {
            lastExecutedTime = currentTime;

            if (!minecraft.isPaused()) for (ParticleEmitter emitter : ParticleStorage.EMITTERS) {
                int x = emitter.position().x;
                int y = emitter.position().y;

                GenericParticleData particle = new GenericParticleData(
                        minecraft.screen,
                        0xFFFF0000,
                        random.nextFloat(2f, 3f),
                        x + 8 + random.nextFloat(-4, 4),
                        y + 8 + random.nextFloat(-4, 4),
                        random.nextFloat(0.8f, 1.2f),
                        random.nextInt(30, 60),
                        emitter
                );
                particle.direction = new Vector2f(random.nextFloat(-1, 1), random.nextFloat(-1, 1));
                particle.angularVelocity = random.nextFloat(-20, 20);
                particle.friction = random.nextFloat(0.05f,0.2f);
                ParticleStorage.addParticle(particle);
            }
        }
        ParticleStorage.EMITTERS.clear();

        for (ParticleData data : ParticleStorage.getParticlesData()) {
            PoseStack poseStack = new PoseStack();
            poseStack.pushPose();
            poseStack.mulPoseMatrix(data.emitter().pose());
            poseStack.translate(0, 0, 500);
            data.render(poseStack, partialTick);
            poseStack.popPose();
        }
    }
}
