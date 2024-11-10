package com.crystal.bluecore.world.dimension;

import com.crystal.bluecore.registry.ModBiomes;
import com.crystal.bluecore.registry.ModDimensions;
import com.crystal.bluecore.world.gen.ModDensityFunctions;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.FixedBiomeSource;
import net.minecraft.world.biome.source.util.VanillaBiomeParameters;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.*;
import net.minecraft.world.gen.noise.NoiseConfig;
import net.minecraft.world.gen.surfacebuilder.VanillaSurfaceRules;

import java.util.List;
import java.util.concurrent.CompletableFuture;

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

    public static class Builder extends ChunkGenerator {
        public static final MapCodec<NoiseChunkGenerator> CODEC = NoiseChunkGenerator.CODEC;

        public Builder(BiomeSource biomeSource) {
            super(biomeSource);
        }

        /**
         * 设置海平面
         * @return 设置海平面
         */
        @Override
        public int getSeaLevel() {
            return 63;
        }

        @Override
        protected MapCodec<? extends ChunkGenerator> getCodec() {
            return CODEC;
        }

        @Override
        public void carve(ChunkRegion chunkRegion, long seed, NoiseConfig noiseConfig, BiomeAccess biomeAccess, StructureAccessor structureAccessor, Chunk chunk, GenerationStep.Carver carverStep) {

        }

        @Override
        public void buildSurface(ChunkRegion region, StructureAccessor structures, NoiseConfig noiseConfig, Chunk chunk) {

        }

        @Override
        public void populateEntities(ChunkRegion region) {

        }

        @Override
        public int getWorldHeight() {
            return 0;
        }

        @Override
        public CompletableFuture<Chunk> populateNoise(Blender blender, NoiseConfig noiseConfig, StructureAccessor structureAccessor, Chunk chunk) {
            return null;
        }

        @Override
        public int getMinimumY() {
            return 0;
        }

        @Override
        public int getHeight(int x, int z, Heightmap.Type heightmap, HeightLimitView world, NoiseConfig noiseConfig) {
            return 0;
        }

        @Override
        public VerticalBlockSample getColumnSample(int x, int z, HeightLimitView world, NoiseConfig noiseConfig) {
            return null;
        }

        @Override
        public void getDebugHudText(List<String> text, NoiseConfig noiseConfig, BlockPos pos) {

        }
    }
}
