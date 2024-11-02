package it.hurts.octostudios.immersiveui.mixin;

import it.hurts.octostudios.immersiveui.client.VariableStorage;
import net.minecraft.world.inventory.AnvilMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilMenu.class)
public class AnvilMenuMixin {
    @Inject(method = "onTake", at = @At("HEAD"))
    public void onTake(CallbackInfo ci) {
        VariableStorage.anvilParticleSpawn = true;
    }
}
