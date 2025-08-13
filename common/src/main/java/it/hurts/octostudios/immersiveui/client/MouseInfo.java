package it.hurts.octostudios.immersiveui.client;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class MouseInfo {
    public float deltaX = 0f;
    public float deltaY = 0f;
    public int oX = Integer.MIN_VALUE; // Initially not set
    public int oY = Integer.MIN_VALUE; // Initially not set
}
