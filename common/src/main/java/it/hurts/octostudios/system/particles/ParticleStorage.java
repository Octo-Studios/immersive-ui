package it.hurts.octostudios.system.particles;

import it.hurts.octostudios.system.particles.data.ParticleData;
import it.hurts.octostudios.system.particles.data.ParticleEmitter;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector2i;

import java.util.*;

public class ParticleStorage {
    //public static final HashMap<Vector2i, ItemStack> RENDERED_ITEMS = new HashMap<>();
    //public static final Set<ParticleEmitter> EMITTERS = new HashSet<>();
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