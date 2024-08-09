package it.hurts.octostudios.immersiveui;

import it.hurts.octostudios.octolib.modules.config.annotations.Prop;
import it.hurts.octostudios.octolib.modules.config.impl.OctoConfig;
import lombok.Data;

@Data
public class Config implements OctoConfig {
    @Prop(comment = "Affects the speed of the hotbar selector.")
    private double hotbarSelectorSpeed = 3d;

    @Prop(comment = "Affects the size of the hovered item.")
    private float hoveredItemScale = 1.4f;

    @Prop(comment = "Affects the hover amplitude of items, that match to the item that is carried in the cursor.")
    private float matchingItemHoverAmplitude = 0.8f;

    @Prop(comment = "Affects the easing speed, that is applied to the rotation of the floating item.")
    private float floatingItemEasingSpeed = 0.75f;

    @Prop(comment = "Affects the rotation amplitude of the floating item.")
    private float floatingItemRotationAmplitude = 1f;
}
