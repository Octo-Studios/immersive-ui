package it.hurts.octostudios.immersiveui.compat;

import it.hurts.octostudios.immersiveui.client.MouseInfo;
import it.hurts.octostudios.immersiveui.client.RenderInfo;
import net.minecraft.world.inventory.Slot;

import java.util.Map;
import java.util.Random;

public interface ExtraScreenData {
    MouseInfo getMouseInfo();
    RenderInfo getRenderInfo();
    Random getRandom();
    Map<Slot, Float> getExpandingProgress();
}