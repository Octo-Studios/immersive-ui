package it.hurts.octostudios.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
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
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
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
    @Shadow private ItemStack draggingItem;
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

        if (oX != Integer.MIN_VALUE && oY != Integer.MIN_VALUE) { // Only calculate if previous values are set
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
        guiGraphics.pose().scale(1.4f, 1.4f, 1f);
        guiGraphics.pose().mulPose(Axis.ZP.rotation(Mth.abs(currentAngle) > 0.01f ? currentAngle : 0f));
        guiGraphics.renderItem(itemStack, -8, 0);
        if (itemStack.getRarity() != Rarity.COMMON) {
            ParticleEmitter emitter = new ParticleEmitter(guiGraphics.pose().last().pose(), new Vector2i(-8, 0));
            if (!ParticleStorage.EMITTERS.containsKey(emitter) && (Mth.abs(deltaX) > 0f || Mth.abs(deltaY) > 0)) {
                ParticleStorage.EMITTERS.put(emitter, new ArrayList<>());
                ParticleData particle = new GenericParticleData(
                        itemStack.getRarity().color.getColor() != null?itemStack.getRarity().color.getColor()+0xff000000:0xffff00ff,
                        0x0,
                        Mth.abs(deltaY)+Mth.abs(deltaX),
                        0 + random.nextFloat(-1,1),
                        8 + random.nextFloat(-1,1),
                        random.nextFloat(0.8f, 1.25f),
                        random.nextInt(12, 30),
                        emitter
                );
                particle.direction = new Vector2f(-deltaX, -deltaY).normalize();
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

        ci.cancel();
    }

    @Inject(method = "renderSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;renderItem(Lnet/minecraft/world/item/ItemStack;III)V", shift = At.Shift.BEFORE))
    public void renderSize(GuiGraphics guiGraphics, Slot slot, CallbackInfo ci) {
        LocalPlayer player = Minecraft.getInstance().player;

        if (player == null)
            return;

        ItemStack carried = player.inventoryMenu.getCarried();

        boolean hovering = hoveredSlot == slot && (carried.isEmpty() || ItemStack.isSameItemSameTags(slot.getItem(), carried));
        float deltaTime = Minecraft.getInstance().getDeltaFrameTime() / 4f;

        expandingProgress.put(slot, Mth.clamp(expandingProgress.getOrDefault(slot, 0f) + deltaTime * (hovering ? 1 : -1), 0, 1f));

        float progress = Easing.lerp(1, 1.4f, Easing.animate(hovering ? Easing.Type.EASE_OUT : Easing.Type.EASE_IN, expandingProgress.get(slot)));
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

    @Inject(method = "render", at = @At("HEAD"))
    public void resetOldMouse(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        float deltaTime = Minecraft.getInstance().getDeltaFrameTime();
        deltaX = (oX - mouseX) / deltaTime / 20f;
        deltaY = (oY - mouseY) / deltaTime / 20f;
    }

    @Inject(method = "render", at = @At("TAIL"))
    public void resetOldMouse2(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        oX = mouseX; oY = mouseY;
    }
}
