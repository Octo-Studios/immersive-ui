package it.hurts.octostudios.immersiveui.mixin;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractFurnaceScreen;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

@Mixin(AbstractFurnaceScreen.class)
public class AbstractFurnaceScreenMixin {
    @Inject(method = "render", at = @At("HEAD"))
    public void render(GuiGraphics guiGraphics, int i, int j, float f) {
        AbstractFurnaceMenu menu = ((AbstractFurnaceScreen<?>) (Object) this).getMenu();
    }
}
