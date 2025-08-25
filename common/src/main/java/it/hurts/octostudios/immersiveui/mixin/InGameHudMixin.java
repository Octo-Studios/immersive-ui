package it.hurts.octostudios.immersiveui.mixin;

import it.hurts.octostudios.immersiveui.ImmersiveUI;
import it.hurts.octostudios.octolib.OctoLib;
import it.hurts.octostudios.octolib.OctoLibClient;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Gui.class, priority = -1)
public abstract class InGameHudMixin {

    @Shadow
    protected abstract Player getCameraPlayer();

    @Shadow public abstract void resetTitleTimes();

    @Unique
    int scaledWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();

    @Unique
    Minecraft client = Minecraft.getInstance();

    @Unique
    private double position = 0.0;

//    @Inject(method = "renderItemHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blitSprite(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/ResourceLocation;IIII)V", ordinal = 1))
//    private void translatePose(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
//        //guiGraphics.pose().pushMatrix();
//        //guiGraphics.pose().translate(0,0, ImmersiveUI.CONFIG.isRenderHotbarSelectorAboveItems()?400:0);
//    }
//    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blitSprite(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/ResourceLocation;IIII)V", ordinal = 1, shift = At.Shift.AFTER), method = "renderItemHotbar")
//    private void popPose(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
//        //guiGraphics.pose().popMatrix();
//    }

    @ModifyArg(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blitSprite(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/ResourceLocation;IIII)V", ordinal = 1), index = 2, method = "renderItemHotbar")
    private int selectedSlotPositionX(int originalX) {
        if (!ImmersiveUI.CONFIG.isEnableHotbarSelectorAnimation()) return originalX;

        double speed = ImmersiveUI.CONFIG.getHotbarSelectorSpeed();

        int i = Minecraft.getInstance().getWindow().getGuiScaledWidth() / 2;
        Player playerEntity = this.getCameraPlayer();

        assert playerEntity != null;
        int selectedSlot = playerEntity.getInventory().getSelectedSlot();

        double toMove = Math.abs(selectedSlot - position) / 2f * OctoLibClient.getDeltaTime() * speed * 20f;

        if (position > selectedSlot) {
            position = Mth.clamp(position - toMove, selectedSlot, position);
        } else if (position < selectedSlot) {
            position = Mth.clamp(position + toMove, position, selectedSlot);
        }

        if (Math.abs(position - selectedSlot) < 0.05d) position = selectedSlot;

        return (int) (i - 91 - 1 + Math.round(position * 20));
    }
}