package it.hurts.shatterbyte.immersiveui.mixin;

import it.hurts.shatterbyte.immersiveui.ImmersiveUI;
import it.hurts.shatterbyte.immersiveui.util.CommonCode;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.atomic.AtomicReference;

@Mixin(Screen.class)
public class ScreenMixin {
    @Unique
    AtomicReference<Float> timerCommon = new AtomicReference<>(0f);
//    @Inject(method = "onClose", at = @At("TAIL"))
//    public void clearScreenParticles(CallbackInfo ci) {
//        ParticleStorage.EMITTERS.clear();
//    }
    @Inject(method = "extractBackground", at = @At("TAIL"))
    public void renderBg(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a, CallbackInfo ci) {
        if (ImmersiveUI.SOPHISTICATED_COMPAT.isStorageScreenBase((Screen) (Object) this)) {
            return;
        }

        CommonCode.shakeScreen(graphics, (Screen) (Object) this, timerCommon, 1f);
    }
}
