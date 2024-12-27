package it.hurts.octostudios.immersiveui.mixin;

import com.mojang.math.Axis;
import it.hurts.octostudios.immersiveui.ImmersiveUI;
import net.minecraft.advancements.Advancement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.AdvancementToast;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AdvancementToast.class)
public class AdvancementToastMixin {
    @Shadow @Final private Advancement advancement;

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;renderFakeItem(Lnet/minecraft/world/item/ItemStack;II)V", shift = At.Shift.BEFORE))
    public void renderItem(GuiGraphics guiGraphics, ToastComponent toastComponent, long l, CallbackInfoReturnable<Toast.Visibility> cir) {
        if (!ImmersiveUI.CONFIG.isEnableAdvancementToastItems()) return;

        Minecraft mc = Minecraft.getInstance();
        float delta = mc.player.tickCount + mc.getFrameTime() + this.advancement.getId().hashCode()/1000;

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(16,16,150);
        guiGraphics.pose().mulPose(Axis.ZP.rotation(Mth.sin((delta)*0.05f)*0.15f));
        guiGraphics.pose().mulPose(Axis.XP.rotation(Mth.cos((delta)*0.1f)*0.2f));
        guiGraphics.pose().mulPose(Axis.YP.rotation(Mth.cos((delta)*0.075f)*0.3f));
        guiGraphics.pose().translate(0, Mth.sin(delta*0.1f),0);
        guiGraphics.pose().translate(-16,-16,-150);
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;renderFakeItem(Lnet/minecraft/world/item/ItemStack;II)V", shift = At.Shift.AFTER))
    public void renderItemEnd(GuiGraphics guiGraphics, ToastComponent toastComponent, long l, CallbackInfoReturnable<Toast.Visibility> cir) {
        if (!ImmersiveUI.CONFIG.isEnableAdvancementToastItems()) return;
        guiGraphics.pose().popPose();
    }
}