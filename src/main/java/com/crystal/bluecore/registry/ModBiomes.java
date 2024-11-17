package com.crystal.bluecore.registry;

import com.crystal.bluecore.BlueCore;
import com.crystal.bluecore.world.biome.ColdestForestBiome;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;

public class ModBiomes {

    public static final RegistryKey<Biome> COLDEST_FOREST = registryBiomes("coldest_forest");

    private static RegistryKey<Biome> registryBiomes(String name) {
        return RegistryKey.of(RegistryKeys.BIOME, Identifier.of(BlueCore.MOD_ID, name));
    }
    public static void registerBiomeInfo() {
        BlueCore.LOGGER.info(BlueCore.MOD_ID + ": Registered Mod Biomes Success");
    }

    public static void bootstrap(Registerable<Biome> context) {
        // 生成群系
        context.register(COLDEST_FOREST, ColdestForestBiome.biomeGenerator(context));
    }
}
