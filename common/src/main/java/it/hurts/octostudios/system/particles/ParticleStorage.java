package it.hurts.octostudios.system.particles;

import it.hurts.octostudios.system.particles.data.ParticleData;
import it.hurts.octostudios.system.particles.data.ParticleEmitter;

import java.util.*;

public class ParticleStorage {
    public static final HashMap<ParticleEmitter, List<ParticleData>> EMITTERS = new HashMap<>();

    public static List<ParticleData> getParticlesData() {
        return EMITTERS.values().stream().flatMap(List::stream).toList();
    }

    public static void addParticle(ParticleEmitter emitter, ParticleData... data) {
        EMITTERS.get(emitter).addAll(new ArrayList<>(List.of(data)));
    }
}