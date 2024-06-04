package it.hurts.octostudios.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import it.hurts.octostudios.system.particles.ParticleStorage;
import it.hurts.octostudios.system.particles.data.ParticleData;
import it.hurts.octostudios.system.particles.data.ParticleEmitter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.joml.Vector2i;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static it.hurts.octostudios.client.TickManager.*;

@Mixin(GuiGraphics.class)
public abstract class GooeyGraphicsMixin {

    @Shadow
    public abstract void fill(int minX, int minY, int maxX, int maxY, int color);

    @Shadow
    @Final
    private Minecraft minecraft;

    @Shadow @Final private PoseStack pose;

    @Shadow public abstract PoseStack pose();

    @Inject(method = "renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;IIII)V", at = @At("TAIL"))
    public void renderParticles(LivingEntity entity, Level level, ItemStack stack, int x, int y, int seed, int guiOffset, CallbackInfo ci) {
        //if (stack.hasFoil()) ParticleStorage.EMITTERS.add(new ParticleEmitter(this.pose().last().pose(), new Vector2i(x,y)));
    }
}
