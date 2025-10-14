package it.hurts.shatterbyte.immersiveui.compat;

import it.hurts.shatterbyte.immersiveui.client.MouseInfo;
import it.hurts.shatterbyte.immersiveui.client.RenderInfo;
import net.minecraft.world.inventory.Slot;

import java.util.Map;
import java.util.Random;

public interface ExtraScreenData {
    MouseInfo getMouseInfo();
    RenderInfo getRenderInfo();
    Random getRandom();
    Map<Slot, Float> getExpandingProgress();
}