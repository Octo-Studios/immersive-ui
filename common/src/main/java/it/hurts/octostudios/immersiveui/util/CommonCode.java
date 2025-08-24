package it.hurts.octostudios.immersiveui.util;

import com.mojang.math.Axis;
import it.hurts.octostudios.immersiveui.ImmersiveUI;
import it.hurts.octostudios.immersiveui.client.MouseInfo;
import it.hurts.octostudios.immersiveui.client.RenderInfo;
import it.hurts.octostudios.immersiveui.client.VariableStorage;
import it.hurts.octostudios.immersiveui.client.particle.FlameUIParticle;
import it.hurts.octostudios.immersiveui.client.particle.RarityUIParticle;
import it.hurts.octostudios.immersiveui.mixin.AbstractContainerScreenAccessor;
import it.hurts.octostudios.octolib.client.particle.UIParticle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix3x2f;
import org.joml.Vector2f;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static it.hurts.octostudios.immersiveui.client.VariableStorage.*;

public class CommonCode {
    public static void gooeyRenderCode(float partialTick) {
        currentTime = System.currentTimeMillis();
        elapsedTime = currentTime - VariableStorage.lastExecutedTime;
        if (elapsedTime >= TARGET_INTERVAL_MS) {
            lastExecutedTime = currentTime;
        }
    }

    public static void renderFurnaceParticles(Screen screen, Slot fuelSlot, boolean burstCondition, boolean isLit, AtomicBoolean shouldBurst) {
        if (!isLit) return;
        Random random = new Random();

        if (burstCondition && !shouldBurst.get()) {
            shouldBurst.set(true);

            AbstractContainerScreenAccessor accessor = (AbstractContainerScreenAccessor) screen;
            int leftPos = accessor.getLeftPos();
            int topPos = accessor.getTopPos();

            for (int ii = 0; ii < 8; ii++) {
                FlameUIParticle particle = new FlameUIParticle(leftPos+fuelSlot.x+8+random.nextInt(-6,6), topPos+fuelSlot.y+10+random.nextInt(-6,6), random.nextInt(16,24));
                particle.setScreen(screen);
                particle.instantiate();
            }
        }

        if (!burstCondition && shouldBurst.get()) {
            shouldBurst.set(false);
        }
    }

    public static void renderFloating(Screen screen, GuiGraphics guiGraphics, MouseInfo mouseInfo, int i, int j, ItemStack itemStack, Random random, RenderInfo renderInfo, String string, CallbackInfo ci) {
        float scale = ImmersiveUI.CONFIG.getFloatingItemScale();
        float deltaTime = Minecraft.getInstance().getDeltaTracker().getRealtimeDeltaTicks();
        float amplitude = ImmersiveUI.CONFIG.getFloatingItemRotationAmplitude();

        if (mouseInfo.oX != Integer.MIN_VALUE && mouseInfo.oY != Integer.MIN_VALUE) { // Only calculate if previous values are set
            renderInfo.targetAngle = Mth.clamp(-mouseInfo.deltaX / 8f * amplitude, -Mth.HALF_PI/(2/amplitude), Mth.HALF_PI/(2/amplitude));

            // Update velocities based on change in target positions
            renderInfo.currentAngleVelocity += (renderInfo.targetAngle - renderInfo.currentAngle) * renderInfo.easingSpeed * deltaTime;
        }

        //currentAngleVelocity = Mth.clamp(currentAngleVelocity,-0.5f,0.5f);

        // Apply velocities to current angles
        renderInfo.currentAngle = Mth.clamp(renderInfo.currentAngle + renderInfo.currentAngleVelocity * deltaTime, -Mth.HALF_PI/(2/amplitude), Mth.HALF_PI/(2/amplitude));

        // Apply damping to velocities
        renderInfo.currentAngleVelocity = renderInfo.currentAngleVelocity * (float) Math.pow(renderInfo.inertiaDamping, deltaTime);

//        guiGraphics.pose().pushPose();
//        guiGraphics.pose().translate(-300, -100, 0);
//        guiGraphics.pose().scale(0.5f, 0.5f, 1f);
//        guiGraphics.drawString(Minecraft.getInstance().font, itemStack.getDisplayName().toString(), 2, 2, 0xffffff, true);
//        guiGraphics.drawString(Minecraft.getInstance().font, itemStack.getHoverName().toString(), 2, 11, 0xffffff, true);
//        guiGraphics.pose().popPose();

        guiGraphics.pose().pushMatrix();
        guiGraphics.pose().translate(i + 8, j + 8);
        Matrix3x2f matrix = new Matrix3x2f(guiGraphics.pose());
        guiGraphics.pose().scale(scale, scale);
        if (ImmersiveUI.CONFIG.isEnableFloatingItemRotation()) guiGraphics.pose().rotate(Mth.abs(renderInfo.currentAngle) > 0.01f ? renderInfo.currentAngle : 0f);
        guiGraphics.renderItem(itemStack, -8, -8);

        if (ImmersiveUI.CONFIG.isEnableRarityParticles()) {
            List<Integer> colors = itemStack.getHoverName().getSiblings().stream().map(c -> c.getStyle().getColor() == null ? 0 : c.getStyle().getColor().getValue()).toList();
            int color = colors.isEmpty() ? 0xffffff : colors.get(random.nextInt(colors.size()));
            color = colors.isEmpty() ? itemStack.getDisplayName().getStyle().getColor() != null ? itemStack.getDisplayName().getStyle().getColor().getValue() : 0xffffff : color;

            if (color != 0xffffff) {
                if (Mth.abs(mouseInfo.deltaX) > 0f || Mth.abs(mouseInfo.deltaY) > 0) {
                    Vector2f direction = new Vector2f(mouseInfo.deltaX, mouseInfo.deltaY);
                    UIParticle particle = new RarityUIParticle(
                            random.nextFloat(0.5f, 0.625f)*direction.length(),
                            random.nextInt(12, 20),
                            random.nextFloat(-4,4),
                            random.nextFloat(-4,4),
                            -direction.x,
                            -direction.y,
                            random.nextFloat(-10, 10),
                            color,
                            UIParticle.Layer.SCREEN,
                            233f
                    );
                    particle.setMatrix(matrix);
                    particle.setScreen(screen);
                    particle.instantiate();
                }
            }
        }

        Font font = Minecraft.getInstance().font;
        guiGraphics.renderItemDecorations(font, itemStack, -8, -8, string);
        //guiGraphics.drawString(font, expandingProgress.values().toString(), 0, 0, 0xFFFFFF, true);
        guiGraphics.pose().popMatrix();

        ci.cancel();
    }

    public static void shakeScreen(GuiGraphics guiGraphics, Screen screen, AtomicReference<Float> timer, float durationMultiplier) {
        boolean shouldShake = shakeScreen.contains(screen);
        if (shouldShake) {
            shakeScreen.remove(screen);
            timer.set((float) ImmersiveUI.CONFIG.getShakeTimer() * durationMultiplier);
        }
        if (!ImmersiveUI.CONFIG.isEnableScreenShake()) return;

        if (timer.get() > 0) {
            Random rand = new Random();
            timer.set(Mth.clamp(timer.get()-Minecraft.getInstance().getDeltaTracker().getRealtimeDeltaTicks(), 0, ImmersiveUI.CONFIG.getShakeTimer()));
            Vector2f shakeDirection = new Vector2f(rand.nextFloat(-1, 1), rand.nextFloat(-1, 1)).normalize(ImmersiveUI.CONFIG.getShakeAmplitude());
            guiGraphics.pose().translate(shakeDirection.x*(timer.get()/ImmersiveUI.CONFIG.getShakeTimer()), shakeDirection.y*(timer.get()/ImmersiveUI.CONFIG.getShakeTimer()));
        }
    }

    public static void floatingRenderSize(GuiGraphics guiGraphics, Slot slot, Slot hoveredSlot, Map<Slot, Float> expandingProgress) {
        LocalPlayer player = Minecraft.getInstance().player;

        if (player == null || slot == null)
            return;

        ItemStack carried = player.containerMenu.getCarried();
        if (!carried.isEmpty() && ItemStack.isSameItemSameComponents(slot.getItem(), carried) && ImmersiveUI.CONFIG.isEnableMatchingItemHovering()) {
            guiGraphics.pose().translate(Mth.sin(Minecraft.getInstance().player.tickCount*0.215f + Objects.hash(slot.x, slot.y))*ImmersiveUI.CONFIG.getMatchingItemHoverAmplitude(), Mth.cos(Minecraft.getInstance().player.tickCount*0.13f + Objects.hash(slot.x, slot.y))*ImmersiveUI.CONFIG.getMatchingItemHoverAmplitude());
        }

        boolean hovering = hoveredSlot == slot && (carried.isEmpty() || ItemStack.isSameItemSameComponents(slot.getItem(), carried));
        float deltaTime = Minecraft.getInstance().getDeltaTracker().getRealtimeDeltaTicks() / 4f;

        expandingProgress.put(slot, Mth.clamp(expandingProgress.getOrDefault(slot, 0f) + deltaTime * (hovering ? 1 : -1), 0, 1f));

        float progress = Easing.lerp(1F, ImmersiveUI.CONFIG.getHoveredItemScale(), Easing.animate(hovering ? Easing.Type.EASE_OUT : Easing.Type.EASE_IN, expandingProgress.get(slot)));
        //if (!hovering) return;


        guiGraphics.pose().translate(slot.x + 8, slot.y + 8);
        guiGraphics.pose().scale(progress, progress);
        guiGraphics.pose().translate(-slot.x - 8, -slot.y - 8);
    }
}
