package com.crystal.bluecore.world.gen.chunk;

import com.crystal.bluecore.BlueCore;
import com.crystal.bluecore.registry.ModBlocks;
import com.crystal.bluecore.world.biome.surface.ModMaterialRules;
import com.crystal.bluecore.world.gen.VanillaDensityFunctions;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.biome.source.util.VanillaBiomeParameters;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.GenerationShapeConfig;

public class ModChunkGeneratorSettings {

    public static final RegistryKey<ChunkGeneratorSettings> SNOWFLAKE_CRYSTAL = RegistryKey.of(RegistryKeys.CHUNK_GENERATOR_SETTINGS, BlueCore.of("snowflake_crystal"));

    public static void bootstrap(Registerable<ChunkGeneratorSettings> registerable) {
        registerable.register(SNOWFLAKE_CRYSTAL, settings(registerable));
    }

    public static ChunkGeneratorSettings settings(Registerable<?> context) {
        return new ChunkGeneratorSettings(
                /* 噪音选项
                    1. 最小高度：地形开始生成的最低高度。取值为-2032到2031的闭区间，必须是16的倍数
                    2. 高度：地形生成的总高度。取值为0到4064的闭区间，必须是16的整数倍，minY+height不能超过2032
                    3. 水平大小：取值为0到4的闭区间
                    4. 垂直大小：取值为0到4的闭区间
                 */
                GenerationShapeConfig.create(-64, 384, 1, 2),
                // 默认方块：该维度地形的默认的方块
                ModBlocks.FROZEN_STONE.getDefaultState(),
                // 默认流体：该维度默认的流体，用于生成海洋和湖
                Blocks.WATER.getDefaultState(),
                // 密度函数集（Noise Router）：将密度函数应用于用于世界生成的噪声参数
                VanillaDensityFunctions.createSurfaceNoiseRouter(
                        context.getRegistryLookup(RegistryKeys.DENSITY_FUNCTION),
                        context.getRegistryLookup(RegistryKeys.NOISE_PARAMETERS)),
                // 地表规则：为地形填充方块
                ModMaterialRules.coldestForestSurface(),
                // 生成目标：一个生物群系参数区间/值的列表，用于决定玩家出生点的环境条件
                new VanillaBiomeParameters().getSpawnSuitabilityNoises(),
                // seaLevel：此维度的海平面高度
                63,
                // mobGenerationDisabled：是否禁止普通动物随地形一起生成
                true,
                // aquifers：是否生成含水层和含熔岩层
                true,
                // oreVeins：是否生成矿脉
                false,
                // usesLegacyRandom：是否使用1.18之前的旧的随机数生成器来生成世界
                false
        );
    }
}