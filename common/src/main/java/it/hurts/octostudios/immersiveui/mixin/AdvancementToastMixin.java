package it.hurts.octostudios.immersiveui.mixin;

import it.hurts.octostudios.immersiveui.ImmersiveUI;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.AdvancementToast;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AdvancementToast.class)
public class AdvancementToastMixin {
    @Shadow @Final private AdvancementHolder advancement;

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;renderFakeItem(Lnet/minecraft/world/item/ItemStack;II)V", shift = At.Shift.BEFORE))
    public void renderItem(GuiGraphics guiGraphics, Font font, long visibilityTime, CallbackInfo ci) {
        if (!ImmersiveUI.CONFIG.isEnableAdvancementToastItems()) return;

        Minecraft mc = Minecraft.getInstance();
        float delta = mc.player.tickCount + mc.getDeltaTracker().getGameTimeDeltaPartialTick(true) + (float) this.advancement.id().hashCode() /1000;

        guiGraphics.pose().pushMatrix();
        guiGraphics.pose().translate(16,16);
        guiGraphics.pose().rotate(Mth.sin((delta)*0.05f)*0.15f);
        guiGraphics.pose().rotate(Mth.cos((delta)*0.1f)*0.2f);
        guiGraphics.pose().rotate(Mth.cos((delta)*0.075f)*0.3f);
        guiGraphics.pose().translate(0, Mth.sin(delta*0.1f));
        guiGraphics.pose().translate(-16,-16);
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;renderFakeItem(Lnet/minecraft/world/item/ItemStack;II)V", shift = At.Shift.AFTER))
    public void renderItemEnd(GuiGraphics guiGraphics, Font font, long visibilityTime, CallbackInfo ci) {
        if (!ImmersiveUI.CONFIG.isEnableAdvancementToastItems()) return;
        guiGraphics.pose().popMatrix();
    }
}
