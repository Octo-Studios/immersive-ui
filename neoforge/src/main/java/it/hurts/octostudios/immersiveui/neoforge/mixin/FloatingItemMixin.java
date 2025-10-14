package it.hurts.octostudios.immersiveui.neoforge.mixin;

import it.hurts.octostudios.immersiveui.compat.ExtraScreenData;
import it.hurts.octostudios.immersiveui.util.CommonCode;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

@Mixin(AbstractContainerScreen.class)
public abstract class FloatingItemMixin {
    @Shadow
    @Nullable
    protected Slot hoveredSlot;

    @Inject(method = "renderSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/AbstractContainerScreen;renderSlotContents(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/inventory/Slot;Ljava/lang/String;)V", shift = At.Shift.BEFORE))
    public void renderSize(GuiGraphics guiGraphics, Slot slot, CallbackInfo ci) {
        guiGraphics.pose().pushMatrix();
        CommonCode.floatingRenderSize(guiGraphics, slot, hoveredSlot, ((ExtraScreenData) this).getExpandingProgress());
    }

    @Inject(method = "renderSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/AbstractContainerScreen;renderSlotContents(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/inventory/Slot;Ljava/lang/String;)V", shift = At.Shift.AFTER))
    public void renderSizeEnd(GuiGraphics guiGraphics, Slot slot, CallbackInfo ci) {
        guiGraphics.pose().popMatrix();
    }
}
