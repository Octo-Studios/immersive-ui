package it.hurts.octostudios.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerScreen.class)
public class FloatingItemMixin {
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
    private final float easingSpeed = 0.75f; // Speed of easing to target angle

    @Inject(method = "renderFloatingItem", at = @At("HEAD"), cancellable = true)
    public void renderFunkyItem(GuiGraphics guiGraphics, ItemStack itemStack, int i, int j, String string, CallbackInfo ci) {
        //immersiveui$ticker += Minecraft.getInstance().getDeltaFrameTime();
        if (oX != Integer.MIN_VALUE && oY != Integer.MIN_VALUE) { // Only calculate if previous values are set
            float deltaX = oX - i;
            float deltaY = oY - j;

            xRotTarget = Mth.clamp(deltaY / 8f, -Mth.HALF_PI, Mth.HALF_PI);
            targetAngle = Mth.clamp(-deltaX / 8f, -Mth.HALF_PI, Mth.HALF_PI);
        }
        // Gradually move currentAngle towards targetAngle

        currentAngle += (targetAngle - currentAngle) * easingSpeed * Minecraft.getInstance().getDeltaFrameTime();
        xRot += (xRotTarget - xRot) * easingSpeed * Minecraft.getInstance().getDeltaFrameTime();

        guiGraphics.pose().pushPose();
        //guiGraphics.pose().mulPose(Axis.XP.rotation(xRot));
        guiGraphics.pose().translate(i + 8, j + 4, 232.0f);
        guiGraphics.pose().mulPose(Axis.ZP.rotation(currentAngle));
        guiGraphics.renderItem(itemStack, -8, 0);
        Font font = Minecraft.getInstance().font;
        guiGraphics.renderItemDecorations(font, itemStack, -8, 0, string);
        guiGraphics.pose().popPose();


//        guiGraphics.pose().pushPose();
//        guiGraphics.pose().translate(i + 8, j + 4, 232.0f);
//        Font font = Minecraft.getInstance().font;
//        guiGraphics.renderItemDecorations(font, itemStack, -8, 0, string);
//        guiGraphics.pose().popPose();

        oX = i;
        oY = j;

        ci.cancel();

    }

    @Inject(method = "renderSlot", at = @At("HEAD"))
    public void renderSize(GuiGraphics guiGraphics, Slot slot, CallbackInfo ci) {
        //guiGraphics.pose().pushPose();
        //guiGraphics.pose().scale(1.25f,1.25f,1);
    }
}
