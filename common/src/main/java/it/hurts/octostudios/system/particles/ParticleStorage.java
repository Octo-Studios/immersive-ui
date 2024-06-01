package it.hurts.octostudios.system.particles;

import it.hurts.octostudios.system.particles.data.ParticleData;
import net.minecraft.client.gui.screens.Screen;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ParticleStorage {
    private static final List<ParticleData> PARTICLES = new ArrayList<>();

    public static List<ParticleData> getParticlesData() {
        return PARTICLES;
    }

    public static List<ParticleData> getParticles(Screen screen) {
        return getParticlesData().stream().filter(particleData -> particleData.getScreen() == (screen == null ? null : screen.getClass())).toList();
    }

    public static void addParticle(ParticleData... data) {
        getParticlesData().addAll(new ArrayList<>(List.of(data)));
    }
}