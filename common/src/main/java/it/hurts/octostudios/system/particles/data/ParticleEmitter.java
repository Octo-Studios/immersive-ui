package it.hurts.octostudios.system.particles.data;

import org.joml.Matrix4f;
import org.joml.Vector2i;

public record ParticleEmitter(Matrix4f pose, Vector2i position) {}
