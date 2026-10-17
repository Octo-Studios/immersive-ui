package it.hurts.shatterbyte.immersiveui.mixin;

import it.hurts.shatterbyte.immersiveui.ImmersiveUI;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
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

    @Inject(method = "extractRenderState", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;fakeItem(Lnet/minecraft/world/item/ItemStack;II)V", shift = At.Shift.BEFORE))
    public void renderItem(GuiGraphicsExtractor graphics, Font font, long fullyVisibleForMs, CallbackInfo ci) {
        if (!ImmersiveUI.CONFIG.misc.enableAdvancementToastItems) return;

        Minecraft mc = Minecraft.getInstance();
        float delta = mc.player.tickCount + mc.getDeltaTracker().getGameTimeDeltaPartialTick(true) + (float) this.advancement.id().hashCode() /1000;

        graphics.pose().pushMatrix();
        graphics.pose().translate(16,16);
        //guiGraphics.pose().rotate(Mth.sin((delta)*0.05f)*0.15f);
        graphics.pose().rotate(Mth.cos((delta)*0.1f)*0.2f);
        //guiGraphics.pose().rotate(Mth.cos((delta)*0.075f)*0.3f);
        graphics.pose().translate(0, Mth.sin(delta*0.1f));
        graphics.pose().translate(-16,-16);
    }

    @Inject(method = "extractRenderState", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;fakeItem(Lnet/minecraft/world/item/ItemStack;II)V", shift = At.Shift.AFTER))
    public void renderItemEnd(GuiGraphicsExtractor graphics, Font font, long fullyVisibleForMs, CallbackInfo ci) {
        if (!ImmersiveUI.CONFIG.misc.enableAdvancementToastItems) return;
        graphics.pose().popMatrix();
    }
}
