package it.hurts.octostudios.immersiveui.mixin;

import com.mojang.math.Axis;
import it.hurts.octostudios.immersiveui.ImmersiveUI;
import it.hurts.octostudios.immersiveui.client.MouseInfo;
import it.hurts.octostudios.immersiveui.client.RenderInfo;
import it.hurts.octostudios.immersiveui.client.VariableStorage;
import it.hurts.octostudios.immersiveui.client.particle.RarityUIParticle;
import it.hurts.octostudios.immersiveui.compat.ExtraScreenData;
import it.hurts.octostudios.immersiveui.util.CommonCode;
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
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreenMixin implements ExtraScreenData {
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
    private MouseInfo mouseInfo = new MouseInfo();
    @Unique
    private RenderInfo renderInfo = new RenderInfo();

    @Unique
    AtomicReference<Float> timerCommon = new AtomicReference<>(0f);
//    @Unique
//    AtomicBoolean shakeScreenCommon = new AtomicBoolean(false);


    @Override
    public MouseInfo getMouseInfo() {
        if (mouseInfo == null) {
            mouseInfo = new MouseInfo();
        }

        return mouseInfo;
    }

    @Override
    public RenderInfo getRenderInfo() {
        if (renderInfo == null) {
            renderInfo = new RenderInfo();
        }

        return renderInfo;
    }

    @Override
    public Random getRandom() {
        if (random == null) {
            random = new Random();
        }

        return random;
    }

    @Override
    public Map<Slot, Float> getExpandingProgress() {
        if (expandingProgress == null) {
            expandingProgress = new HashMap<>();
        }

        return expandingProgress;
    }

    @Inject(method = "renderBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/AbstractContainerScreen;renderBg(Lnet/minecraft/client/gui/GuiGraphics;FII)V", shift = At.Shift.BEFORE))
    public void renderBg(GuiGraphics guiGraphics, int i, int j, float f, CallbackInfo ci) {
        if (ImmersiveUI.SOPHISTICATED_COMPAT.isStorageScreenBase((Screen) (Object) this)) {
            return;
        }

        CommonCode.shakeScreen(guiGraphics, (Screen) (Object) this, timerCommon, 1f);
    }

//    @Inject(method = "render", at = @At("TAIL"))
//    public void renderParticles(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
//        guiGraphics.pose().pushPose();
//        guiGraphics.pose().translate(leftPos, topPos, 232f);
//        ParticleSystem.renderScreenParticles((Screen) (Object) this, guiGraphics, Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(false));
//        guiGraphics.pose().popPose();
//    }

    @Inject(method = "renderFloatingItem", at = @At("HEAD"), cancellable = true)
    public void renderFunkyItem(GuiGraphics guiGraphics, ItemStack itemStack, int i, int j, String string, CallbackInfo ci) {
//        if (ImmersiveUI.SOPHISTICATED_COMPAT.isStorageScreenBase((Screen) (Object) this)) {
//            return;
//        }

        CommonCode.renderFloating((Screen) (Object) this, guiGraphics, getMouseInfo(), i, j, itemStack, getRandom(), getRenderInfo(), string, ci);
        getMouseInfo().oX = i; getMouseInfo().oY = j;
    }

    @Inject(method = "renderSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;renderItem(Lnet/minecraft/world/item/ItemStack;III)V", shift = At.Shift.BEFORE), require = 0)
    public void renderSize(GuiGraphics guiGraphics, Slot slot, CallbackInfo ci) {
        CommonCode.floatingRenderSize(guiGraphics, slot, hoveredSlot, getExpandingProgress());
    }

    @Inject(method = "renderSlotHighlight", at = @At(value = "HEAD"), cancellable = true)
    private static void disableSlotHighlight(GuiGraphics guiGraphics, int x, int y, int blitOffset, @NotNull CallbackInfo ci) {
        if (!ImmersiveUI.CONFIG.isEnableVanillaSlotHighlighting()) {
            ci.cancel();
        }
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/Slot;isActive()Z", shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILSOFT)
    public void fixHovering(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci, int i, int j, int k, Slot slot) {
        if (this.isHovering(slot, mouseX, mouseY) && slot.isActive()) {
            this.hoveredSlot = slot;
        }
    }

    @Inject(method = "renderBackground", at = @At("HEAD"))
    public void resetOldMouse(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        float deltaTime = Minecraft.getInstance().getTimer().getRealtimeDeltaTicks();
        getMouseInfo().deltaX = (mouseInfo.oX - mouseX) / deltaTime / 20f;
        getMouseInfo().deltaY = (mouseInfo.oY - mouseY) / deltaTime / 20f;
    }

    @Inject(method = "render", at = @At("RETURN"))
    public void resetOldMouse2(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        getMouseInfo().oX = mouseX; getMouseInfo().oY = mouseY;
    }
}
