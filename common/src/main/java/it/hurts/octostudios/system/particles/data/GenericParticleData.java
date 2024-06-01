package it.hurts.octostudios.system.particles.data;

import com.mojang.blaze3d.systems.RenderSystem;
import it.hurts.octostudios.ImmersiveUI;
import it.hurts.octostudios.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

public class GenericParticleData extends ParticleData {
    private static final ResourceLocation TEXTURE = new ResourceLocation(ImmersiveUI.MOD_ID, "textures/gui/test.png");

    public GenericParticleData(Screen screen, int color, float speed, float xStart, float yStart, float size, int lifeTime) {
        super(screen == null ? null : screen.getClass(), TEXTURE, speed, lifeTime, xStart, yStart);
        this.color = color;
        this.size = size;
    }


    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public void render(Screen screen, GuiGraphics guiGraphics, float partialTick) {
        super.render(screen, guiGraphics, partialTick);
    }
}