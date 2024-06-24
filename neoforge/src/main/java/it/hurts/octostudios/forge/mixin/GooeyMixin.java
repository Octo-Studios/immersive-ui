package it.hurts.octostudios.forge.mixin;

import it.hurts.octostudios.util.CommonCode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ForgeGui.class)
public abstract class GooeyMixin {
    @Shadow public abstract Minecraft getMinecraft();

    @Inject(method = "render", at = @At("TAIL"))
    public void ää(GuiGraphics guiGraphics, float partialTick, CallbackInfo ci) {
        CommonCode.gooeyRenderCode(getMinecraft().getFrameTime());
    }
}
