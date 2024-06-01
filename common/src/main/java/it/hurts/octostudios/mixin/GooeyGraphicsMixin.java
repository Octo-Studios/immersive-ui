package it.hurts.octostudios.mixin;

import it.hurts.octostudios.system.particles.ParticleStorage;
import it.hurts.octostudios.system.particles.data.GenericParticleData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.joml.Vector2f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

import static it.hurts.octostudios.client.TickManager.*;

@Mixin(GuiGraphics.class)
public abstract class GooeyGraphicsMixin {

    @Shadow
    public abstract void fill(int minX, int minY, int maxX, int maxY, int color);

    @Shadow
    @Final
    private Minecraft minecraft;

    @Inject(method = "renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;IIII)V", at = @At("TAIL"))
    public void renderParticles(LivingEntity entity, Level level, ItemStack stack, int x, int y, int seed, int guiOffset, CallbackInfo ci) {
        if (stack.hasFoil()) {
            if (minecraft.isPaused()) return;
            if (elapsedTime >= TARGET_INTERVAL_MS * 2) {
                Random random = new Random();

                GenericParticleData particle = new GenericParticleData(
                        minecraft.screen,
                        0xFFFF0000,
                        random.nextFloat(2f, 3f),
                        x + 8 + random.nextFloat(-4, 4),
                        y + 8 + random.nextFloat(-4, 4),
                        random.nextFloat(0.8f, 1.2f),
                        random.nextInt(30, 60)
                );
                particle.direction = new Vector2f(random.nextFloat(-1, 1), random.nextFloat(-1, 1));
                particle.angularVelocity = random.nextFloat(-20, 20);
                particle.friction = random.nextFloat(0.05f,0.2f);
                ParticleStorage.addParticle(particle);
            }
        }
    }
}
