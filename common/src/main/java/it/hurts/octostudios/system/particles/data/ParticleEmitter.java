package it.hurts.octostudios.system.particles.data;

import org.joml.Matrix4f;
import org.joml.Vector2i;

import java.util.Objects;

public class ParticleEmitter {
    public Matrix4f pose() {
        return pose;
    }

    public Vector2i position() {
        return position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParticleEmitter that = (ParticleEmitter) o;
        return Objects.equals(pose, that.pose) && Objects.equals(position, that.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pose, position);
    }

    private final Matrix4f pose;
    private final Vector2i position;

    public ParticleEmitter(Matrix4f pose, Vector2i position) {
        this.pose = pose;
        this.position = position;
    }
}
