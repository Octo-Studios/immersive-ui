package it.hurts.shatterbyte.immersiveui.mixin;

import it.hurts.shatterbyte.immersiveui.util.CommonCode;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractFurnaceScreen;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.atomic.AtomicBoolean;

@Mixin(AbstractFurnaceScreen.class)
public class AbstractFurnaceScreenMixin {
    @Unique
    public AtomicBoolean shouldBurst = new AtomicBoolean(false);

    @Inject(method = "extractBackground", at = @At("TAIL"))
    public void render(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a, CallbackInfo ci) {
        AbstractFurnaceScreen<?> screen = ((AbstractFurnaceScreen<?>) (Object) this);
        AbstractFurnaceMenu menu = screen.getMenu();

        CommonCode.renderFurnaceParticles(screen, menu.getSlot(1), menu.getLitProgress() == 1, menu.isLit(), shouldBurst);
    }
}
