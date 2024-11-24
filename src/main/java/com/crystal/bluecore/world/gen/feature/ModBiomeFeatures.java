package com.crystal.bluecore.world.gen.feature;

import com.crystal.bluecore.registry.worldgen.ModPlacedFeatures;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.carver.ConfiguredCarvers;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;

public class ModBiomeFeatures {

    /**
     * @param builder 地物构建
     * @see DefaultBiomeFeatures#addAmethystGeodes(GenerationSettings.LookupBackedBuilder) addAmethystGeodes()方法
     */
    public static void addFrostGeodes(GenerationSettings.LookupBackedBuilder builder) {
        builder.feature(GenerationStep.Feature.LOCAL_MODIFICATIONS, ModPlacedFeatures.FROST_GEODE);
    }

    /**
     * @param builder 地物构建
     * @see DefaultBiomeFeatures#addLandCarvers(GenerationSettings.LookupBackedBuilder)  addLandCarvers()的方法
     */
    public static void addLandCarvers(GenerationSettings.LookupBackedBuilder builder) {
        builder.carver(GenerationStep.Carver.AIR, ConfiguredCarvers.CAVE);
        builder.carver(GenerationStep.Carver.AIR, ConfiguredCarvers.CAVE_EXTRA_UNDERGROUND);
        builder.carver(GenerationStep.Carver.AIR, ConfiguredCarvers.CANYON);
        // 添加凛冰湖
        builder.feature(GenerationStep.Feature.LAKES, ModPlacedFeatures.LAKE_CRYOTHEUM_UNDERGROUND);
        builder.feature(GenerationStep.Feature.LAKES, ModPlacedFeatures.LAKE_CRYOTHEUM_SURFACE);
    }
}
