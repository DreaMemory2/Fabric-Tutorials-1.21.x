package com.crystal.bluecore.registry.worldgen;

import com.crystal.bluecore.BlueCore;
import com.crystal.bluecore.registry.ModBlocks;
import com.crystal.bluecore.world.biome.feature.FrostGeodeFeatureConfig;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.structure.rule.RuleTest;
import net.minecraft.structure.rule.TagMatchRuleTest;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.BlobFoliagePlacer;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.trunk.StraightTrunkPlacer;

import java.util.List;

public class ModConfiguredFeatures {
    // 自定义树
    public static final RegistryKey<ConfiguredFeature<?, ?>> MAPLE_TREE = registryKey("maple_tree");
    // 自定义矿石生成
    public static final RegistryKey<ConfiguredFeature<?, ?>> ORE_PINK_GEMSTONE = registryKey("ore_pink_gemstone");
    // 自定义晶洞
    public static final RegistryKey<ConfiguredFeature<?, ?>> FROST_GEODE = registryKey("frost_geode");

    /**
     * 构建树木模型
     */
    private static TreeFeatureConfig maple() {
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
        ).build();
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
        // 基石替代规则
        RuleTest stoneReplaceables = new TagMatchRuleTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslateReplaceables = new TagMatchRuleTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
        // 矿石生成目标
        List<OreFeatureConfig.Target> pinkOresTargets = List.of(
                OreFeatureConfig.createTarget(stoneReplaceables, ModBlocks.PINK_GEMSTONE_ORE.getDefaultState()),
                OreFeatureConfig.createTarget(deepslateReplaceables, ModBlocks.DEEPSLATE_PINK_GEMSTONE_ORE.getDefaultState()));
        // 树木组装注册
        register(context, MAPLE_TREE, Feature.TREE, maple());
        // 矿石生成注册
        // 中团簇大小为1-8
        register(context, ORE_PINK_GEMSTONE, Feature.ORE, new OreFeatureConfig(pinkOresTargets, 8));
        // 晶洞组装注册
        register(context, FROST_GEODE, Feature.GEODE, new FrostGeodeFeatureConfig());
    }
}
