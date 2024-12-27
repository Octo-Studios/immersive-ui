package it.hurts.octostudios.immersiveui.mixin;

import it.hurts.octostudios.immersiveui.system.particles.ParticleStorage;
import it.hurts.octostudios.immersiveui.system.particles.data.FlameParticleData;
import it.hurts.octostudios.immersiveui.system.particles.data.ParticleEmitter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractFurnaceScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import org.joml.Vector2i;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Random;

@Mixin(AbstractFurnaceScreen.class)
public class AbstractFurnaceScreenMixin {
    @Unique
    public boolean shouldBurst = false;

    @Inject(method = "render", at = @At("HEAD"))
    public void render(GuiGraphics guiGraphics, int i, int j, float f, CallbackInfo ci) {
        AbstractFurnaceScreen<?> screen = ((AbstractFurnaceScreen<?>) (Object) this);
        AbstractFurnaceMenu menu = screen.getMenu();

        int leftPos = (screen.width - 176) / 2;
        int topPos = (screen.height - 166) / 2;

        if (!menu.isLit()) return;
        Random random = new Random();

        if (menu.getLitProgress() == 12 && !shouldBurst) {
            shouldBurst = true;
            ParticleEmitter emitter = new ParticleEmitter(guiGraphics.pose().last().pose(), new Vector2i(0,0));
            if (!ParticleStorage.EMITTERS.containsKey(emitter)) ParticleStorage.EMITTERS.put(emitter, new ArrayList<>());

            for (int ii = 0; ii < 8; ii++) {
                FlameParticleData particle = new FlameParticleData(leftPos+menu.getSlot(1).x+8+random.nextInt(-6,6), topPos+menu.getSlot(1).y+10+random.nextInt(-6,6), random.nextInt(30,50), emitter);
                ParticleStorage.addParticle(emitter, particle);
            }
        }
        if (menu.getLitProgress() != 12 && shouldBurst) {
            shouldBurst = false;
        }
    }
}