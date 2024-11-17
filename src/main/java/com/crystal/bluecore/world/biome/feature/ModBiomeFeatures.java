package com.crystal.bluecore.world.biome.feature;

import com.crystal.bluecore.registry.worldgen.ModPlacedFeatures;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.gen.GenerationStep;

public class ModBiomeFeatures {

    public static void addFrostGeodes(GenerationSettings.LookupBackedBuilder builder) {
        builder.feature(GenerationStep.Feature.LOCAL_MODIFICATIONS, ModPlacedFeatures.FROST_GEODE);
    }
}
