package it.hurts.shatterbyte.immersiveui.mixin;

import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Screen.class)
public class ScreenMixin {
//    @Inject(method = "onClose", at = @At("TAIL"))
//    public void clearScreenParticles(CallbackInfo ci) {
//        ParticleStorage.EMITTERS.clear();
//    }
}
