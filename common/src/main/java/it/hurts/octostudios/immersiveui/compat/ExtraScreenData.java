package it.hurts.octostudios.immersiveui.compat;

import it.hurts.octostudios.immersiveui.client.MouseInfo;
import it.hurts.octostudios.immersiveui.client.RenderInfo;

public interface ExtraScreenData {
    MouseInfo getMouseInfo();
    RenderInfo getRenderInfo();
}