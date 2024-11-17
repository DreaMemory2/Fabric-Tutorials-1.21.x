package com.crystal.bluecore.world.dimension;

import com.crystal.bluecore.registry.ModBiomes;
import com.crystal.bluecore.registry.ModDimensions;
import com.crystal.bluecore.world.gen.chunk.ModChunkGeneratorSettings;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.FixedBiomeSource;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;

public class SnowflakeCrystalDimension {

    public static DimensionOptions snowflakeCrystalDimension(Registerable<DimensionOptions> context) {
        RegistryEntryLookup<Biome> biome = context.getRegistryLookup(RegistryKeys.BIOME);
        RegistryEntryLookup<DimensionType> dimensionType = context.getRegistryLookup(RegistryKeys.DIMENSION_TYPE);
        RegistryEntryLookup<ChunkGeneratorSettings> chunkGeneratorSettings = context.getRegistryLookup(RegistryKeys.CHUNK_GENERATOR_SETTINGS);

        RegistryEntry.Reference<DimensionType> type = dimensionType.getOrThrow(ModDimensions.SNOWFLAKE_CRYSTAL_TYPE);

        RegistryEntry.Reference<ChunkGeneratorSettings> chunkGenerator = chunkGeneratorSettings.getOrThrow(ModChunkGeneratorSettings.SNOWFLAKE_CRYSTAL);
        NoiseChunkGenerator generator = new NoiseChunkGenerator(new FixedBiomeSource(biome.getOrThrow(ModBiomes.COLDEST_FOREST)), chunkGenerator);

        return new DimensionOptions(type, generator);
    }
}
