package it.hurts.octostudios.mixin;

import com.mojang.math.Axis;
import it.hurts.octostudios.system.particles.ParticleStorage;
import it.hurts.octostudios.system.particles.data.GenericParticleData;
import it.hurts.octostudios.system.particles.data.ParticleData;
import it.hurts.octostudios.system.particles.data.ParticleEmitter;
import it.hurts.octostudios.util.Easing;
import it.hurts.octostudios.util.VectorUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Mixin(AbstractContainerScreen.class)
public abstract class FloatingItemMixin {
    @Shadow
    protected abstract boolean isHovering(Slot slot, double mouseX, double mouseY);

    @Unique
    Random random = new Random();
    @Unique
    Map<Slot, Float> expandingProgress = new HashMap<>();

    @Shadow
    @Nullable
    protected Slot hoveredSlot;
    @Unique
    private float immersiveui$ticker;
    @Unique
    private int oX = Integer.MIN_VALUE; // Initially not set
    @Unique
    private int oY = Integer.MIN_VALUE; // Initially not set
    @Unique
    private float currentAngle = 0.0f;
    @Unique
    private float targetAngle = 0.0f;

    @Unique
    private float xRot = 0.0f;
    @Unique
    private float xRotTarget = 0.0f;

    @Unique
    private float currentAngleVelocity = 0.0f;
    @Unique
    private float xRotVelocity = 0.0f;

    @Unique
    private final float easingSpeed = 0.75f; // Speed of easing to target angle
    @Unique
    private final float inertiaDamping = 0.75f; // Damping factor for inertia

    @Inject(method = "renderFloatingItem", at = @At("HEAD"), cancellable = true)
    public void renderFunkyItem(GuiGraphics guiGraphics, ItemStack itemStack, int i, int j, String string, CallbackInfo ci) {
        float deltaTime = Minecraft.getInstance().getDeltaFrameTime();
        float deltaX = 0f;
        float deltaY = 0f;

        if (oX != Integer.MIN_VALUE && oY != Integer.MIN_VALUE) { // Only calculate if previous values are set
            deltaX = (oX - i) / deltaTime / 20f;
            deltaY = (oY - j) / deltaTime / 20f;

            xRotTarget = Mth.clamp(deltaY / 8f, -Mth.HALF_PI, Mth.HALF_PI);
            targetAngle = Mth.clamp(-deltaX / 8f, -Mth.HALF_PI, Mth.HALF_PI);

            // Update velocities based on change in target positions
            currentAngleVelocity += (targetAngle - currentAngle) * easingSpeed * deltaTime;
            xRotVelocity += (xRotTarget - xRot) * easingSpeed * deltaTime;
        }

        // Apply velocities to current angles
        currentAngle += currentAngleVelocity * deltaTime;
        xRot += xRotVelocity * deltaTime;

        // Apply damping to velocities
        currentAngleVelocity *= (float) Math.pow(inertiaDamping, deltaTime);
        xRotVelocity *= (float) Math.pow(inertiaDamping, deltaTime);

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(i + 8, j + 4, 232.0f);
        guiGraphics.pose().scale(1.5f, 1.5f, 1f);
        guiGraphics.pose().mulPose(Axis.ZP.rotation(Mth.abs(currentAngle) > 0.01f ? currentAngle : 0f));
        guiGraphics.renderItem(itemStack, -8, 0);
        if (itemStack.hasFoil()) {
            ParticleEmitter emitter = new ParticleEmitter(guiGraphics.pose().last().pose(), new Vector2i(-8, 0));
            if (!ParticleStorage.EMITTERS.containsKey(emitter) && (Mth.abs(deltaX) > 0f || Mth.abs(deltaY) > 0)) {
                ParticleStorage.EMITTERS.put(emitter, new ArrayList<>());
                ParticleData particle = new GenericParticleData(
                        0xFFFF00FF,
                        0x000088FF,
                        0.8f,
                        emitter.position().x + 8,
                        emitter.position().y + 10,
                        random.nextFloat(0.8f, 1.5f),
                        random.nextInt(7, 15),
                        emitter
                );
                particle.direction = VectorUtils.rotate(new Vector2f(0, -1), random.nextInt(0, 360));
                ParticleStorage.addParticle(
                        emitter,
                        particle
                );
            }
        }
        Font font = Minecraft.getInstance().font;
        guiGraphics.renderItemDecorations(font, itemStack, -8, 0, string);
        //guiGraphics.drawString(font, expandingProgress.values().toString(), 0, 0, 0xFFFFFF, true);
        guiGraphics.pose().popPose();

        oX = i;
        oY = j;

        ci.cancel();
    }

    @Inject(method = "renderSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;renderItem(Lnet/minecraft/world/item/ItemStack;III)V", shift = At.Shift.BEFORE))
    public void renderSize(GuiGraphics guiGraphics, Slot slot, CallbackInfo ci) {
        boolean hovering = hoveredSlot == slot;
        float deltaTime = Minecraft.getInstance().getDeltaFrameTime() / 4f;

        expandingProgress.put(slot, Mth.clamp(expandingProgress.getOrDefault(slot, 0f) + deltaTime * (hovering ? 1 : -1), 0, 1f));

        float progress = Easing.lerp(1, 1.5f, Easing.animate(hovering ? Easing.Type.EASE_OUT : Easing.Type.EASE_IN, expandingProgress.get(slot)));
        //if (!hovering) return;
        guiGraphics.pose().translate(slot.x + 8, slot.y + 8, 0);
        guiGraphics.pose().scale(progress, progress, 1f);
        guiGraphics.pose().translate(-slot.x - 8, -slot.y - 8, 0);
    }

    @Inject(method = "renderSlotHighlight", at = @At(value = "HEAD"), cancellable = true)
    private static void vpizdu(GuiGraphics guiGraphics, int x, int y, int blitOffset, @NotNull CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/Slot;isActive()Z", shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILSOFT)
    public void ebalRot(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci, int i, int j, int k, Slot slot) {
        if (this.isHovering(slot, mouseX, mouseY) && slot.isActive()) {
            this.hoveredSlot = slot;
        }
    }
}
