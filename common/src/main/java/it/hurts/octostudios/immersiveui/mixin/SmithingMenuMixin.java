package it.hurts.octostudios.immersiveui.mixin;

import it.hurts.octostudios.immersiveui.client.VariableStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SmithingMenu.class)
public class SmithingMenuMixin {
    @Inject(method = "onTake", at = @At("HEAD"))
    public void onTake(Player player, ItemStack stack, CallbackInfo ci) {
        if (player != Minecraft.getInstance().player || Minecraft.getInstance().screen == null) {
            return;
        }

        VariableStorage.shakeScreen.add(Minecraft.getInstance().screen);
    }
}
