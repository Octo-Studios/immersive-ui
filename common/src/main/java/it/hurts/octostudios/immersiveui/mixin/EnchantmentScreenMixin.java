package it.hurts.octostudios.immersiveui.mixin;

import it.hurts.octostudios.immersiveui.ImmersiveUI;

import it.hurts.octostudios.immersiveui.util.VectorUtils;
import it.hurts.octostudios.octolib.client.particle.ExtendedUIParticle;
import it.hurts.octostudios.octolib.client.particle.GalacticUIParticle;
import it.hurts.octostudios.octolib.client.particle.ParticleSystem;
import it.hurts.octostudios.octolib.client.particle.UIParticle;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.EnchantmentScreen;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.inventory.Slot;
import org.joml.Vector2f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantmentScreen.class)
public class EnchantmentScreenMixin {
    @Inject(method = "mouseClicked", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;handleInventoryButtonClick(II)V"))
    public void onMouseClicked(double d, double e, int i, CallbackInfoReturnable<Boolean> ci) {
        if (!ImmersiveUI.CONFIG.isEnableEnchantParticles()) return;

        AbstractContainerScreen<EnchantmentMenu> screen = ((AbstractContainerScreen<EnchantmentMenu>) (Object) this);
        Slot slot = screen.getMenu().slots.getFirst();

        AbstractContainerScreenAccessor accessor = (AbstractContainerScreenAccessor) this;
        int leftPos = accessor.getLeftPos();
        int topPos = accessor.getTopPos();

        for (int ii = 0; ii < 8; ii++) {
            ExtendedUIParticle particleData = new GalacticUIParticle(3f, 52+ii, leftPos+slot.x+8, topPos+slot.y+8, UIParticle.Layer.SCREEN, 200);
            particleData.setDirection(VectorUtils.rotate(new Vector2f(0, 1), ii*45f));
            particleData.getTransform().setSize(new Vector2f(0.75f, 0.75f));
            particleData.setSpeed(2f);
            particleData.setAngularVelocity(-8);
            particleData.setScreen(screen);
            particleData.setGravity(0);
            particleData.setFriction(0);
            particleData.instantiate();

            particleData = new GalacticUIParticle(3f, 40+ii, leftPos+slot.x+8, topPos+slot.y+8, UIParticle.Layer.SCREEN, 200);
            particleData.setDirection(VectorUtils.rotate(new Vector2f(0, 1), ii*45f));
            particleData.setSpeed(0.75f);
            particleData.setAngularVelocity(0);
            particleData.setScreen(screen);
            particleData.setGravity(0);
            particleData.setFriction(0);
            particleData.instantiate();
        }
    }
}
