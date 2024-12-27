package it.hurts.octostudios.immersiveui.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import it.hurts.octostudios.immersiveui.ImmersiveUI;
import it.hurts.octostudios.immersiveui.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(Enchantment.class)
public class EnchantmentMixin {
    @Inject(method = "getFullname", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/chat/MutableComponent;withStyle(Lnet/minecraft/ChatFormatting;)Lnet/minecraft/network/chat/MutableComponent;", ordinal = 0, shift = At.Shift.AFTER))
    private void obfuscateCursedEnchantments(int i, CallbackInfoReturnable<Component> cir, @Local LocalRef<MutableComponent> mutableComponentLocalRef) {
        if (!ImmersiveUI.CONFIG.isEnableCurseFormatting()) return;
        int ticks = Minecraft.getInstance().player.tickCount * 10000 + this.hashCode();
        if (new Random(ticks).nextBoolean()) mutableComponentLocalRef.set(RenderUtils.obfuscate(mutableComponentLocalRef.get(), 0.15d, ticks));
        //mutableComponentLocalRef.set(Component.literal(new Random(ticks).nextBoolean() + " | " + (ticks)));
    }
}