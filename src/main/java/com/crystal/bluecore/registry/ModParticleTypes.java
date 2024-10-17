package com.crystal.bluecore.registry;

import com.crystal.bluecore.BlueCore;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModParticleTypes {
    public static final SimpleParticleType PINK_FLAME = registerParticles("pink_flame", FabricParticleTypes.simple());

    private static SimpleParticleType registerParticles(String name, SimpleParticleType particleType) {
        return Registry.register(Registries.PARTICLE_TYPE, Identifier.of(BlueCore.MOD_ID, name), particleType);
    }

    public static void registerParticleInfo() {
        BlueCore.LOGGER.info(BlueCore.MOD_ID + "Accuse Register Particles");
    }
}
