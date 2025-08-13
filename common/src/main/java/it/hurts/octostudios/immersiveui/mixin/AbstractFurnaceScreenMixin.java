package it.hurts.octostudios.immersiveui.mixin;

import it.hurts.octostudios.immersiveui.client.particle.FlameUIParticle;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractFurnaceScreen;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(AbstractFurnaceScreen.class)
public class AbstractFurnaceScreenMixin {
    @Unique
    public boolean shouldBurst = false;

    @Inject(method = "render", at = @At("HEAD"))
    public void render(GuiGraphics guiGraphics, int i, int j, float f, CallbackInfo ci) {
        AbstractFurnaceScreen<?> screen = ((AbstractFurnaceScreen<?>) (Object) this);
        AbstractFurnaceMenu menu = screen.getMenu();

        if (!menu.isLit()) return;
        Random random = new Random();

        if (menu.getLitProgress() == 1 && !shouldBurst) {
            shouldBurst = true;

            for (int ii = 0; ii < 8; ii++) {
                FlameUIParticle particle = new FlameUIParticle(menu.getSlot(1).x+8+random.nextInt(-6,6), menu.getSlot(1).y+10+random.nextInt(-6,6), random.nextInt(16,24));
                particle.setScreen(screen);
                particle.instantiate();
            }
        }
        if (menu.getLitProgress() != 1 && shouldBurst) {
            shouldBurst = false;
        }
    }
}
