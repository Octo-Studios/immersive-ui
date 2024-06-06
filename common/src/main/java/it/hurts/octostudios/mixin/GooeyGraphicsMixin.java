package it.hurts.octostudios.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import it.hurts.octostudios.client.TickManager;
import it.hurts.octostudios.system.particles.ParticleStorage;
import it.hurts.octostudios.system.particles.data.GenericParticleData;
import it.hurts.octostudios.system.particles.data.ParticleData;
import it.hurts.octostudios.system.particles.data.ParticleEmitter;
import it.hurts.octostudios.util.VectorUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Random;

import static it.hurts.octostudios.client.TickManager.*;

@Mixin(GuiGraphics.class)
public abstract class GooeyGraphicsMixin {

    @Shadow public abstract PoseStack pose();

    @Shadow @Final private Minecraft minecraft;
    @Unique
    Random random = new Random();

    @Inject(method = "renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;IIII)V", at = @At("TAIL"))
    public void renderParticles(LivingEntity entity, Level level, ItemStack stack, int x, int y, int seed, int guiOffset, CallbackInfo ci) {
        if (!stack.hasFoil()) return;
        ParticleEmitter emitter = new ParticleEmitter(this.pose().last().pose(), new Vector2i(x,y));
        if (!ParticleStorage.EMITTERS.containsKey(emitter)) ParticleStorage.EMITTERS.put(emitter, new ArrayList<>());

        if (!minecraft.isPaused() && elapsedTime >= TARGET_INTERVAL_MS) {
            ParticleData particle = new GenericParticleData(
                    0xFF8800FF,
                    random.nextFloat(0.3f,0.8f),
                    emitter.position().x+8,
                    emitter.position().y+8,
                    random.nextFloat(0.8f, 1.5f),
                    random.nextInt(60,80),
                    emitter
            );
            particle.direction = VectorUtils.rotate(new Vector2f(0,-1), random.nextInt(0, 360));
            particle.angularVelocity = random.nextFloat(-2, 2);
            particle.friction = 0.05f;

            ParticleStorage.addParticle(emitter, particle);
        }

//        for (ParticleData data : ParticleStorage.EMITTERS.get(emitter)) {
//            data.render(data.getPoseStackSnapshot(), (GuiGraphics) (Object) this, minecraft.isPaused() ? 0 : minecraft.getFrameTime());
//        }
    }
}
