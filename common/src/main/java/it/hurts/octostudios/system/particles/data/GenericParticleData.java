package it.hurts.octostudios.system.particles.data;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
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

    public GenericParticleData(Screen screen, int color, float speed, float xStart, float yStart, float size, int lifeTime, ParticleEmitter emitter) {
        super(screen == null ? null : screen.getClass(), TEXTURE, speed, lifeTime, xStart, yStart, emitter);
        this.color = color;
        this.size = size;
    }


    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public void render(PoseStack poseStack, float partialTick) {
        super.render(poseStack, partialTick);
    }
}