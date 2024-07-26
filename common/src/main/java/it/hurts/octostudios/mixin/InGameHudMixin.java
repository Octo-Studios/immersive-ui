package it.hurts.octostudios.mixin;

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

    @Unique
    int scaledWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();

    @Unique
    Minecraft client = Minecraft.getInstance();

    @Unique
    private double position = 0.0;

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blitSprite(Lnet/minecraft/resources/ResourceLocation;IIII)V", ordinal = 1), method = "renderItemHotbar")
    private void boo(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0,0,400);
    }
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blitSprite(Lnet/minecraft/resources/ResourceLocation;IIII)V", ordinal = 1, shift = At.Shift.AFTER), method = "renderItemHotbar")
    private void boo2(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        guiGraphics.pose().popPose();
    }

    @ModifyArg(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blitSprite(Lnet/minecraft/resources/ResourceLocation;IIII)V", ordinal = 1), index = 1, method = "renderItemHotbar")
    private int selectedSlotPositionX(int originalX) {
        double speed = 3d;

        int i = Minecraft.getInstance().getWindow().getGuiScaledWidth() / 2;
        Player playerEntity = this.getCameraPlayer();

        assert playerEntity != null;
        int selectedSlot = playerEntity.getInventory().selected;

        double toMove = Math.abs(selectedSlot - position) / 2f * client.getTimer().getRealtimeDeltaTicks() * speed;

        if (position > selectedSlot) {
            position = Mth.clamp(position - toMove, selectedSlot, position);
        } else if (position < selectedSlot) {
            position = Mth.clamp(position + toMove, position, selectedSlot);
        }

        if (Math.abs(position - selectedSlot) < 0.05d) position = selectedSlot;

        return (int) (i - 91 - 1 + Math.round(position * 20));
    }
}