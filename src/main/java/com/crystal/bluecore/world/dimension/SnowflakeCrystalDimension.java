package com.crystal.bluecore.world.dimension;

import com.crystal.bluecore.registry.ModBiomes;
import com.crystal.bluecore.registry.ModDimensions;
import com.crystal.bluecore.world.gen.ModDensityFunctions;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.FixedBiomeSource;
import net.minecraft.world.biome.source.util.VanillaBiomeParameters;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.GenerationShapeConfig;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;
import net.minecraft.world.gen.surfacebuilder.VanillaSurfaceRules;

public class SnowflakeCrystalDimension {

    public static DimensionOptions snowflakeCrystalDimension(Registerable<DimensionOptions> context) {
        RegistryEntryLookup<Biome> biome = context.getRegistryLookup(RegistryKeys.BIOME);
        RegistryEntryLookup<DimensionType> dimensionType = context.getRegistryLookup(RegistryKeys.DIMENSION_TYPE);

        return new DimensionOptions(dimensionType.getOrThrow(ModDimensions.SNOWFLAKE_CRYSTAL_TYPE),
                new NoiseChunkGenerator(new FixedBiomeSource(biome.getOrThrow(ModBiomes.COLDEST_FOREST)), settings(context)));
    }

    public static RegistryEntry<ChunkGeneratorSettings> settings(Registerable<DimensionOptions> context) {
        ChunkGeneratorSettings chunkGeneratorSettings = new ChunkGeneratorSettings(
                GenerationShapeConfig.create(-64, 384, 1, 2),
                Blocks.STONE.getDefaultState(),
                Blocks.WATER.getDefaultState(),
                ModDensityFunctions.createSurfaceNoiseRouter(context.getRegistryLookup(RegistryKeys.DENSITY_FUNCTION), context.getRegistryLookup(RegistryKeys.NOISE_PARAMETERS), false, false),
                VanillaSurfaceRules.createOverworldSurfaceRule(),
                new VanillaBiomeParameters().getSpawnSuitabilityNoises(),
                63,
                false,
                true,
                true,
                false
        );
        return RegistryEntry.of(chunkGeneratorSettings);
    }
}
