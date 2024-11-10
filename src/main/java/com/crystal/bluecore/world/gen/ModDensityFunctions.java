package com.crystal.bluecore.world.gen;

import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.densityfunction.DensityFunctionTypes;
import net.minecraft.world.gen.noise.NoiseParametersKeys;
import net.minecraft.world.gen.noise.NoiseRouter;

public class ModDensityFunctions {
    public static NoiseRouter createSurfaceNoiseRouter(RegistryEntryLookup<DensityFunction> densityFunctionLookup,
                                                       RegistryEntryLookup<DoublePerlinNoiseSampler.NoiseParameters> noiseParametersLookup,
                                                       boolean largeBiomes,
                                                       boolean amplified) {
        DensityFunction densityFunction = DensityFunctionTypes.noise(noiseParametersLookup.getOrThrow(NoiseParametersKeys.AQUIFER_BARRIER), 0.5);
        DensityFunction densityFunction2 = DensityFunctionTypes.noise(noiseParametersLookup.getOrThrow(NoiseParametersKeys.AQUIFER_FLUID_LEVEL_FLOODEDNESS), 0.67);
        DensityFunction densityFunction3 = DensityFunctionTypes.noise(
                noiseParametersLookup.getOrThrow(NoiseParametersKeys.AQUIFER_FLUID_LEVEL_SPREAD), 0.7142857142857143
        );
        DensityFunction densityFunction4 = DensityFunctionTypes.noise(noiseParametersLookup.getOrThrow(NoiseParametersKeys.AQUIFER_LAVA));
        DensityFunction densityFunction5 = entryHolder(densityFunctionLookup, of("shift_x"));
        DensityFunction densityFunction6 = entryHolder(densityFunctionLookup, of("shift_z"));
        DensityFunction densityFunction7 = DensityFunctionTypes.shiftedNoise(
                densityFunction5,
                densityFunction6,
                0.25,
                noiseParametersLookup.getOrThrow(largeBiomes ? NoiseParametersKeys.TEMPERATURE_LARGE : NoiseParametersKeys.TEMPERATURE)
        );
        DensityFunction densityFunction8 = DensityFunctionTypes.shiftedNoise(
                densityFunction5,
                densityFunction6,
                0.25,
                noiseParametersLookup.getOrThrow(largeBiomes ? NoiseParametersKeys.VEGETATION_LARGE : NoiseParametersKeys.VEGETATION)
        );
        DensityFunction densityFunction9 = entryHolder(
                densityFunctionLookup, largeBiomes ? of("overworld_large_biomes/factor") : (amplified ? of("overworld_amplified/factor") : of("overworld/factor"))
        );
        DensityFunction densityFunction10 = entryHolder(
                densityFunctionLookup, largeBiomes ? of("overworld_large_biomes/depth") : (amplified ? of("overworld_amplified/depth") : of("overworld/depth"))
        );
        DensityFunction densityFunction11 = createInitialDensityFunction(DensityFunctionTypes.cache2d(densityFunction9), densityFunction10);
        DensityFunction densityFunction12 = entryHolder(
                densityFunctionLookup, largeBiomes ? of("overworld_large_biomes/sloped_cheese") : (amplified ?  of("overworld_amplified/sloped_cheese") : of("overworld/sloped_cheese"))
        );
        DensityFunction densityFunction13 = DensityFunctionTypes.min(
                densityFunction12, DensityFunctionTypes.mul(DensityFunctionTypes.constant(5.0), entryHolder(densityFunctionLookup, of("overworld/caves/entrances")))
        );
        DensityFunction densityFunction14 = DensityFunctionTypes.rangeChoice(
                densityFunction12, -1000000.0, 1.5625, densityFunction13, densityFunction12
        );
        DensityFunction densityFunction15 = DensityFunctionTypes.min(
                applyBlendDensity(densityFunction14), entryHolder(densityFunctionLookup,  of("overworld/caves/noodle"))
        );
        return new NoiseRouter(
                densityFunction,
                densityFunction2,
                densityFunction3,
                densityFunction4,
                densityFunction5,
                densityFunction6,
                densityFunction7,
                densityFunction8,
                densityFunction9,
                densityFunction10,
                densityFunction11,
                densityFunction12,
                densityFunction13,
                densityFunction14,
                densityFunction15
        );
    }

    private static DensityFunction entryHolder(RegistryEntryLookup<DensityFunction> densityFunctionRegisterable, RegistryKey<DensityFunction> key) {
        return new DensityFunctionTypes.RegistryEntryHolder(densityFunctionRegisterable.getOrThrow(key));
    }

    private static RegistryKey<DensityFunction> of(String id) {
        return RegistryKey.of(RegistryKeys.DENSITY_FUNCTION, Identifier.ofVanilla(id));
    }

    private static DensityFunction createInitialDensityFunction(DensityFunction factor, DensityFunction depth) {
        DensityFunction densityFunction = DensityFunctionTypes.mul(depth, factor);
        return DensityFunctionTypes.mul(DensityFunctionTypes.constant(4.0), densityFunction.quarterNegative());
    }

    private static DensityFunction applyBlendDensity(DensityFunction density) {
        DensityFunction densityFunction = DensityFunctionTypes.blendDensity(density);
        return DensityFunctionTypes.mul(DensityFunctionTypes.interpolated(densityFunction), DensityFunctionTypes.constant(0.64)).squeeze();
    }
}
