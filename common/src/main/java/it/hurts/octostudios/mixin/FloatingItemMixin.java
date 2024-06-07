package it.hurts.octostudios.mixin;

import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(AbstractContainerScreen.class)
public abstract class FloatingItemMixin {
    @Shadow protected abstract boolean isHovering(Slot slot, double mouseX, double mouseY);

    @Shadow @Nullable protected Slot hoveredSlot;
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
    private final float inertiaDamping = 0.9f; // Damping factor for inertia

    @Inject(method = "renderFloatingItem", at = @At("HEAD"), cancellable = true)
    public void renderFunkyItem(GuiGraphics guiGraphics, ItemStack itemStack, int i, int j, String string, CallbackInfo ci) {
        if (oX != Integer.MIN_VALUE && oY != Integer.MIN_VALUE) { // Only calculate if previous values are set
            float deltaX = oX - i;
            float deltaY = oY - j;

            xRotTarget = Mth.clamp(deltaY / 8f, -Mth.HALF_PI, Mth.HALF_PI);
            targetAngle = Mth.clamp(-deltaX / 8f, -Mth.HALF_PI, Mth.HALF_PI);

            // Update velocities based on change in target positions
            currentAngleVelocity += (targetAngle - currentAngle) * easingSpeed;
            xRotVelocity += (xRotTarget - xRot) * easingSpeed;
        }

        // Apply velocities to current angles
        currentAngle += currentAngleVelocity * Minecraft.getInstance().getDeltaFrameTime();
        xRot += xRotVelocity * Minecraft.getInstance().getDeltaFrameTime();

        // Apply damping to velocities
        currentAngleVelocity *= inertiaDamping * 3 * Minecraft.getInstance().getDeltaFrameTime();
        xRotVelocity *= inertiaDamping * 3 * Minecraft.getInstance().getDeltaFrameTime();

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(i + 8, j + 4, 232.0f);
        guiGraphics.pose().mulPose(Axis.ZP.rotation(currentAngle));
        guiGraphics.renderItem(itemStack, -8, 0);
        Font font = Minecraft.getInstance().font;
        guiGraphics.renderItemDecorations(font, itemStack, -8, 0, string);
        guiGraphics.pose().popPose();

        oX = i;
        oY = j;

        ci.cancel();
    }

    @Inject(method = "renderSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;renderItem(Lnet/minecraft/world/item/ItemStack;III)V", shift = At.Shift.BEFORE))
    public void renderSize(GuiGraphics guiGraphics, Slot slot, CallbackInfo ci) {
        if (hoveredSlot != slot) return;
        guiGraphics.pose().translate(slot.x+8, slot.y+8, 0);
        guiGraphics.pose().scale(1.25f, 1.25f, 1f);
        guiGraphics.pose().translate(-slot.x-8, -slot.y-8, 0);
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
