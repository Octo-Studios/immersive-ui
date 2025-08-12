package it.hurts.octostudios.immersiveui.mixin;

import com.mojang.math.Axis;
import it.hurts.octostudios.immersiveui.ImmersiveUI;
import it.hurts.octostudios.immersiveui.client.VariableStorage;
import it.hurts.octostudios.immersiveui.system.particle.RarityUIParticle;
import it.hurts.octostudios.immersiveui.util.CommonCode;
import it.hurts.octostudios.octolib.client.particle.ParticleSystem;
import it.hurts.octostudios.octolib.client.particle.UIParticle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.*;

@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreenMixin {
    @Unique
    Random random = new Random();
    @Unique
    Map<Slot, Float> expandingProgress = new HashMap<>();

    @Shadow
    @Nullable
    protected Slot hoveredSlot;
    @Shadow private ItemStack draggingItem;

    @Shadow protected abstract boolean isHovering(Slot slot, double d, double e);

    @Shadow protected int leftPos;
    @Shadow protected int topPos;
    @Unique
    private float immersiveui$ticker;
    @Unique
    float deltaX = 0f;
    @Unique
    float deltaY = 0f;
    @Unique
    private int oX = Integer.MIN_VALUE; // Initially not set
    @Unique
    private int oY = Integer.MIN_VALUE; // Initially not set
    @Unique
    private float currentAngle = 0.0f;
    @Unique
    private float targetAngle = 0.0f;

    @Unique
    private float currentAngleVelocity = 0.0f;

    @Unique
    private float easingSpeed = ImmersiveUI.CONFIG.getFloatingItemEasingSpeed();; // Speed of easing to target angle
    @Unique
    private final float inertiaDamping = 0.75f; // Damping factor for inertia

    @Unique
    float timer;

    @Inject(method = "renderBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/AbstractContainerScreen;renderBg(Lnet/minecraft/client/gui/GuiGraphics;FII)V", shift = At.Shift.BEFORE))
    public void renderBg(GuiGraphics guiGraphics, int i, int j, float f, CallbackInfo ci) {
        if (VariableStorage.shakeScreen) {
            VariableStorage.shakeScreen = false;
            timer = ImmersiveUI.CONFIG.getShakeTimer();
        }
        if (!ImmersiveUI.CONFIG.isEnableScreenShake()) return;

        if (timer > 0) {
            Random rand = new Random();
            timer = Mth.clamp(timer-Minecraft.getInstance().getTimer().getRealtimeDeltaTicks(), 0, ImmersiveUI.CONFIG.getShakeTimer());
            Vector2f shakeDirection = new Vector2f(rand.nextFloat(-1, 1), rand.nextFloat(-1, 1)).normalize(ImmersiveUI.CONFIG.getShakeAmplitude());
            guiGraphics.pose().translate(shakeDirection.x*(timer/ImmersiveUI.CONFIG.getShakeTimer()), shakeDirection.y*(timer/ImmersiveUI.CONFIG.getShakeTimer()), 0);
        }
    }

    @Inject(method = "render", at = @At("TAIL"))
    public void renderParticles(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(leftPos, topPos, 232f);
        ParticleSystem.renderScreenParticles((Screen) (Object) this, guiGraphics, Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(false));
        guiGraphics.pose().popPose();
    }

    @Inject(method = "renderFloatingItem", at = @At("HEAD"), cancellable = true)
    public void renderFunkyItem(GuiGraphics guiGraphics, ItemStack itemStack, int i, int j, String string, CallbackInfo ci) {
        float scale = ImmersiveUI.CONFIG.getFloatingItemScale();

        float deltaTime = Minecraft.getInstance().getTimer().getRealtimeDeltaTicks();
        float amplitude = ImmersiveUI.CONFIG.getFloatingItemRotationAmplitude();

        if (oX != Integer.MIN_VALUE && oY != Integer.MIN_VALUE) { // Only calculate if previous values are set
            targetAngle = Mth.clamp(-deltaX / 8f * amplitude, -Mth.HALF_PI/(2/amplitude), Mth.HALF_PI/(2/amplitude));

            // Update velocities based on change in target positions
            currentAngleVelocity += (targetAngle - currentAngle) * easingSpeed * deltaTime;
        }

        //currentAngleVelocity = Mth.clamp(currentAngleVelocity,-0.5f,0.5f);

        // Apply velocities to current angles
        currentAngle = Mth.clamp(currentAngle + currentAngleVelocity * deltaTime, -Mth.HALF_PI/(2/amplitude), Mth.HALF_PI/(2/amplitude));

        // Apply damping to velocities
        currentAngleVelocity = currentAngleVelocity * (float) Math.pow(inertiaDamping, deltaTime);

//        guiGraphics.pose().pushPose();
//        guiGraphics.pose().translate(-300, -100, 0);
//        guiGraphics.pose().scale(0.5f, 0.5f, 1f);
//        guiGraphics.drawString(Minecraft.getInstance().font, itemStack.getDisplayName().toString(), 2, 2, 0xffffff, true);
//        guiGraphics.drawString(Minecraft.getInstance().font, itemStack.getHoverName().toString(), 2, 11, 0xffffff, true);
//        guiGraphics.pose().popPose();

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(i + 8, j + 8, 232.0f);
        guiGraphics.pose().scale(scale, scale, 1f);
        if (ImmersiveUI.CONFIG.isEnableFloatingItemRotation()) guiGraphics.pose().mulPose(Axis.ZP.rotation(Mth.abs(currentAngle) > 0.01f ? currentAngle : 0f));
        guiGraphics.renderItem(itemStack, -8, -8);

        if (ImmersiveUI.CONFIG.isEnableRarityParticles()) {
            List<Integer> colors = itemStack.getHoverName().getSiblings().stream().map(c -> c.getStyle().getColor() == null ? 0 : c.getStyle().getColor().getValue()).toList();
            int color = colors.isEmpty() ? 0xffffff : colors.get(random.nextInt(colors.size()));
            color = colors.isEmpty() ? itemStack.getDisplayName().getStyle().getColor() != null ? itemStack.getDisplayName().getStyle().getColor().getValue() : 0xffffff : color;

            if (color != 0xffffff) {
                if (Mth.abs(deltaX) > 0f || Mth.abs(deltaY) > 0) {
                    Vector2f direction = new Vector2f(deltaX, deltaY);
                    UIParticle particle = new RarityUIParticle(
                            random.nextFloat(0.5f, 0.625f)*direction.length(),
                            random.nextInt(12, 20),
                            i+8+random.nextFloat(-4,4),
                            j+8+random.nextFloat(-4,4),
                            -deltaX,
                            -deltaY,
                            random.nextFloat(-10, 10),
                            color,
                            UIParticle.Layer.SCREEN,
                            232f
                    );
                    particle.setScreen((Screen) (Object) this);
                    particle.instantiate();
                }
            }
        }

        Font font = Minecraft.getInstance().font;
        guiGraphics.renderItemDecorations(font, itemStack, -8, -8, string);
        //guiGraphics.drawString(font, expandingProgress.values().toString(), 0, 0, 0xFFFFFF, true);
        guiGraphics.pose().popPose();

        ci.cancel();
    }

    @Inject(method = "renderSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;renderItem(Lnet/minecraft/world/item/ItemStack;III)V", shift = At.Shift.BEFORE), require = 0)
    public void renderSize(GuiGraphics guiGraphics, Slot slot, CallbackInfo ci) {
        CommonCode.floatingRenderSize(guiGraphics, slot, hoveredSlot, expandingProgress);
    }

    @Inject(method = "renderSlotHighlight", at = @At(value = "HEAD"), cancellable = true)
    private static void disableSlotHighlight(GuiGraphics guiGraphics, int x, int y, int blitOffset, @NotNull CallbackInfo ci) {
        if (!ImmersiveUI.CONFIG.isEnableVanillaSlotHighlighting()) {
            ci.cancel();
            return;
        }
        guiGraphics.fillGradient(RenderType.gui(), x, y, x + 16, y + 16, -2130706433, -2130706433, blitOffset);
        ci.cancel();
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/Slot;isActive()Z", shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILSOFT)
    public void fixHovering(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci, int i, int j, int k, Slot slot) {
        if (this.isHovering(slot, mouseX, mouseY) && slot.isActive()) {
            this.hoveredSlot = slot;
        }
    }

    @Inject(method = "render", at = @At("HEAD"))
    public void resetOldMouse(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        float deltaTime = Minecraft.getInstance().getTimer().getRealtimeDeltaTicks();
        deltaX = (oX - mouseX) / deltaTime / 20f;
        deltaY = (oY - mouseY) / deltaTime / 20f;
    }

    @Inject(method = "render", at = @At("TAIL"))
    public void resetOldMouse2(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        oX = mouseX; oY = mouseY;
    }


}
