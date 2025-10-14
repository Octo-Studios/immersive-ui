package it.hurts.shatterbyte.immersiveui.client;

import it.hurts.shatterbyte.immersiveui.ImmersiveUI;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RenderInfo {
    public float currentAngle = 0.0f;
    public float targetAngle = 0.0f;
    public float currentAngleVelocity = 0.0f;
    public float easingSpeed = ImmersiveUI.CONFIG.getFloatingItemEasingSpeed();; // Speed of easing to target angle
    public final float inertiaDamping = 0.75f; // Damping factor for inertia
}
