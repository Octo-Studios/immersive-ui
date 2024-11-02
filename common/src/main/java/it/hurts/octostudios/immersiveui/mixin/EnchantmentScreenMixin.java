package it.hurts.octostudios.immersiveui.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import it.hurts.octostudios.immersiveui.system.particles.ParticleStorage;
import it.hurts.octostudios.immersiveui.system.particles.data.GalacticParticleData;
import it.hurts.octostudios.immersiveui.system.particles.data.ParticleData;
import it.hurts.octostudios.immersiveui.system.particles.data.ParticleEmitter;
import it.hurts.octostudios.immersiveui.util.VectorUtils;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.EnchantmentScreen;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.inventory.Slot;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(EnchantmentScreen.class)
public class EnchantmentScreenMixin {
    @Inject(method = "mouseClicked", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;handleInventoryButtonClick(II)V"))
    public void onMouseClicked(double d, double e, int i, CallbackInfoReturnable<Boolean> ci) {
        AbstractContainerScreen<EnchantmentMenu> screen = ((AbstractContainerScreen<EnchantmentMenu>) (Object) this);
        Slot slot = screen.getMenu().slots.getFirst();
        int j = (screen.width - 176) / 2;
        int k = (screen.height - 166) / 2;
        ParticleEmitter emitter = new ParticleEmitter(new Matrix4f(), new Vector2i(slot.x, slot.y));
        if (!ParticleStorage.EMITTERS.containsKey(emitter)) ParticleStorage.EMITTERS.put(emitter, new ArrayList<>());

        List<GalacticParticleData> list = new ArrayList<>();
        for (int ii = 0; ii < 8; ii++) {
            GalacticParticleData particleData = new GalacticParticleData(3f, 52+ii, j+slot.x+8, k+slot.y+8, emitter);
            particleData.direction = VectorUtils.rotate(new Vector2f(0, 1), ii*45f);
            particleData.speed = 1.5f;
            particleData.angularVelocity = -8;
            list.add(particleData);
            particleData = new GalacticParticleData(3f, 60+ii, j+slot.x+8, k+slot.y+8, emitter);
            particleData.direction = VectorUtils.rotate(new Vector2f(0, 1), ii*45f);
            particleData.speed = 0.75f;
            list.add(particleData);
        }

        ParticleStorage.addParticle(
                emitter,
                list.toArray(ParticleData[]::new)
        );
    }
}
