package it.hurts.octostudios.immersiveui;


import lombok.Data;

@Data
public class Config { //implements OctoConfig {
    //@Prop(comment = "Enables the hotbar selector animation.")
    private boolean enableHotbarSelectorAnimation = true;

    //@Prop(comment = "Enables floating item rotation.")
    private boolean enableFloatingItemRotation = true;

    //@Prop(comment = "Enables hovering of matching items.")
    private boolean enableMatchingItemHovering = true;
    
    //@Prop(comment = "Enables particles for rare items.")
    private boolean enableRarityParticles = true;

    //@Prop(comment = "Enables vanilla slot highlighting")
    private boolean enableVanillaSlotHighlighting = true;

    //@Prop(comment = "Affects the speed of the hotbar selector.")
    private double hotbarSelectorSpeed = 3d;

    //@Prop(comment = "Affects the size of the hovered item.")
    private float hoveredItemScale = 1.4f;

    //@Prop(comment = "Affects the hover amplitude of items, that match to the item that is carried in the cursor.")
    private float matchingItemHoverAmplitude = 0.8f;

    //@Prop(comment = "Affects the easing speed, that is applied to the rotation of the floating item.")
    private float floatingItemEasingSpeed = 0.75f;

    //@Prop(comment = "Affects the rotation amplitude of the floating item.")
    private float floatingItemRotationAmplitude = 1f;

    //@Prop(comment = "Moves the hotbar selector above the items. You might want to disable this, if you use resource packs that change the default selector texture.")
    private boolean renderHotbarSelectorAboveItems = true;
}
