package it.hurts.octostudios.immersiveui.neoforge.mixin;

import it.hurts.octostudios.immersiveui.ImmersiveUI;
import it.hurts.octostudios.immersiveui.util.CommonCode;
import it.hurts.octostudios.immersiveui.util.Easing;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderType;
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

import java.util.*;

@Mixin(AbstractContainerScreen.class)
public abstract class FloatingItemMixin {
    @Unique
    Map<Slot, Float> expandingProgress = new HashMap<>();

    @Shadow
    @Nullable
    protected Slot hoveredSlot;



    @Inject(method = "renderSlotContents", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;renderItem(Lnet/minecraft/world/item/ItemStack;III)V", shift = At.Shift.BEFORE))
    public void renderSize(GuiGraphics guiGraphics, ItemStack itemstack, Slot slot, String countString, CallbackInfo ci) {
        guiGraphics.pose().pushMatrix();
        CommonCode.floatingRenderSize(guiGraphics, slot, hoveredSlot, expandingProgress);
    }

    @Inject(method = "renderSlotContents", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V", shift = At.Shift.AFTER))
    public void renderSizeEnd(GuiGraphics guiGraphics, ItemStack itemstack, Slot slot, String countString, CallbackInfo ci) {
        guiGraphics.pose().popMatrix();
    }

//    @Inject(method = "renderSlotHighlight(Lnet/minecraft/client/gui/GuiGraphics;IIII)V", at = @At(value = "HEAD"), cancellable = true)
//    private static void disableSlotHighlight(GuiGraphics arg, int i, int j, int k, int color, CallbackInfo ci) {
//        if (!ImmersiveUI.CONFIG.isEnableVanillaSlotHighlighting()) {
//            ci.cancel();
//            return;
//        }
//        arg.fillGradient(RenderType.gui(), i, j, i + 16, j + 16, color, color, k);
//        ci.cancel();
//    }
}
