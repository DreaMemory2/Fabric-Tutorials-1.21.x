package com.crystal.bluecore.registry.worldgen;

import com.crystal.bluecore.BlueCore;
import com.crystal.bluecore.registry.ModBlocks;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.BlobFoliagePlacer;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.trunk.StraightTrunkPlacer;

public class ModConfigureFeatures {
    // 自定义树
    public static final RegistryKey<ConfiguredFeature<?, ?>> MAPLE_TREE = registryKey("maple_tree");

    /**
     * 构建树木模型
     */
    private static TreeFeatureConfig.Builder maple() {
        return new TreeFeatureConfig.Builder(
                // 原木部分
                BlockStateProvider.of(ModBlocks.MAPLE_LOG),
                // 基础高度，第一个随机高度，第二随机高度
                // BaseHeight FirstRandomHeight SecondRandomHeight
                new StraightTrunkPlacer(4, 2, 0),
                // 树叶部分
                BlockStateProvider.of(ModBlocks.MAPLE_LEAVES),
                // 树叶覆盖范围为2，树叶偏移量为0，高度为3
                new BlobFoliagePlacer(ConstantIntProvider.create(2), ConstantIntProvider.create(0), 3),
                // 实际树木放置特征：两层放置特征尺寸
                new TwoLayersFeatureSize(1, 0, 1)
        );
    }
    /**
     * 注册配置地物方法
     * @param name 配置地物命名空间
     */
    private static RegistryKey<ConfiguredFeature<?, ?>> registryKey(String name) {
        return RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, Identifier.of(BlueCore.MOD_ID, name));
    }

    private static <FC extends FeatureConfig, F extends Feature<FC>> void register(Registerable<ConfiguredFeature<?, ?>> context, RegistryKey<ConfiguredFeature<?, ?>> key, F feature, FC featureConfig) {
        context.register(key, new ConfiguredFeature<>(feature, featureConfig));
    }

    public static void bootstrap(Registerable<ConfiguredFeature<?, ?>> context) {
        // 树木组装注册
        register(context, MAPLE_TREE, Feature.TREE, maple().build());
    }
}
