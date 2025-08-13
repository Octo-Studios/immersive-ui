package it.hurts.octostudios.immersiveui.client;

import it.hurts.octostudios.immersiveui.ImmersiveUI;
import lombok.NoArgsConstructor;
import org.spongepowered.asm.mixin.Unique;

@NoArgsConstructor
public class RenderInfo {
    public float currentAngle = 0.0f;
    public float targetAngle = 0.0f;
    public float currentAngleVelocity = 0.0f;
    public float easingSpeed = ImmersiveUI.CONFIG.getFloatingItemEasingSpeed();; // Speed of easing to target angle
    public final float inertiaDamping = 0.75f; // Damping factor for inertia
}
