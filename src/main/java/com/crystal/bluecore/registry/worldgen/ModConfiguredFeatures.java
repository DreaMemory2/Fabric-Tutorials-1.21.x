package com.crystal.bluecore.registry.worldgen;

import com.crystal.bluecore.BlueCore;
import com.crystal.bluecore.registry.ModBlocks;
import com.crystal.bluecore.world.gen.feature.FrostGeodeFeatureConfig;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.structure.rule.RuleTest;
import net.minecraft.structure.rule.TagMatchRuleTest;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DataPool;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.BlobFoliagePlacer;
import net.minecraft.world.gen.foliage.SpruceFoliagePlacer;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.stateprovider.WeightedBlockStateProvider;
import net.minecraft.world.gen.trunk.StraightTrunkPlacer;

import java.util.List;

public class ModConfiguredFeatures {
    // 自定义树
    public static final RegistryKey<ConfiguredFeature<?, ?>> MAPLE_TREE = registryKey("maple_tree");
    public static final RegistryKey<ConfiguredFeature<?, ?>> FIR_TREE = registryKey("fir_tree");
    public static final RegistryKey<ConfiguredFeature<?, ?>> FROZEN_TREE = registryKey("frozen_tree");
    public static final RegistryKey<ConfiguredFeature<?, ?>> FROZEN_FIR = registryKey("frozen_fir");
    // 自定义矿石生成
    public static final RegistryKey<ConfiguredFeature<?, ?>> ORE_PINK_GEMSTONE = registryKey("ore_pink_gemstone");
    // 自定义晶洞
    public static final RegistryKey<ConfiguredFeature<?, ?>> FROST_GEODE = registryKey("frost_geode");
    // 自定义草丛斑块和花丛斑块
    public static final RegistryKey<ConfiguredFeature<?, ?>> PATCH_FROZEN_GRASS = registryKey("patch_frozen_grass");
    public static final RegistryKey<ConfiguredFeature<?, ?>> PATCH_FANBRUSH = registryKey("patch_fanbrush");
    public static final RegistryKey<ConfiguredFeature<?, ?>> FROZEN_FLOWER_DEFAULT = registryKey("frozen_flower_default");
    // 凛冰湖
    public static final RegistryKey<ConfiguredFeature<?, ?>> LAKE_CRYOTHEUM = registryKey("lake_cryotheum");

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

    private static TreeFeatureConfig frozen() {
        return new TreeFeatureConfig.Builder(
                BlockStateProvider.of(Blocks.OAK_LOG),
                new StraightTrunkPlacer(4, 2, 0),
                BlockStateProvider.of(ModBlocks.FROZEN_LEAVES),
                new BlobFoliagePlacer(ConstantIntProvider.create(2), ConstantIntProvider.create(0), 3),
                new TwoLayersFeatureSize(1, 0, 1)
        ).dirtProvider(BlockStateProvider.of(ModBlocks.FROZEN_DIRT)).ignoreVines().build();
    }

    private static TreeFeatureConfig fir() {
        return new TreeFeatureConfig.Builder(
                BlockStateProvider.of(ModBlocks.FIR_LOG),
                new StraightTrunkPlacer(5, 2, 1),
                BlockStateProvider.of(ModBlocks.FIR_LEAVES),
                new SpruceFoliagePlacer(UniformIntProvider.create(2, 3), UniformIntProvider.create(0, 2), UniformIntProvider.create(1, 2)),
                new TwoLayersFeatureSize(2, 0, 2)
        ).dirtProvider(BlockStateProvider.of(ModBlocks.FROZEN_DIRT)).ignoreVines().build();
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
        RegistryEntryLookup<PlacedFeature> registryPlacedLookup = context.getRegistryLookup(RegistryKeys.PLACED_FEATURE);
        RegistryEntry<PlacedFeature> firTree = registryPlacedLookup.getOrThrow(ModPlacedFeatures.FIR_TREE);
        RegistryEntry<PlacedFeature> frozenTree = registryPlacedLookup.getOrThrow(ModPlacedFeatures.FROZEN_TREE);
        // 基石替代规则
        RuleTest stoneReplaceables = new TagMatchRuleTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslateReplaceables = new TagMatchRuleTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
        // 矿石生成目标
        List<OreFeatureConfig.Target> pinkOresTargets = List.of(
                OreFeatureConfig.createTarget(stoneReplaceables, ModBlocks.PINK_GEMSTONE_ORE.getDefaultState()),
                OreFeatureConfig.createTarget(deepslateReplaceables, ModBlocks.DEEPSLATE_PINK_GEMSTONE_ORE.getDefaultState()));
        // 树木组装注册
        register(context, MAPLE_TREE, Feature.TREE, maple());
        register(context, FIR_TREE, Feature.TREE, fir());
        register(context, FROZEN_TREE, Feature.TREE, frozen());
        // 矿石生成注册
        // 中团簇大小为1-8
        register(context, ORE_PINK_GEMSTONE, Feature.ORE, new OreFeatureConfig(pinkOresTargets, 8));
        // 晶洞组装注册
        register(context, FROST_GEODE, Feature.GEODE, new FrostGeodeFeatureConfig());
        // 组装草丛斑块配置
        register(context, PATCH_FROZEN_GRASS, Feature.RANDOM_PATCH, createRandomPatchFeatureConfig(BlockStateProvider.of(ModBlocks.FROZEN_GRASS), 32));
        // 加权块状态提供程序：有概率将草替换成蕨
        ConfiguredFeatures.register(context, PATCH_FANBRUSH, Feature.RANDOM_PATCH, createRandomPatchFeatureConfig(
                new WeightedBlockStateProvider(DataPool.<BlockState>builder().add(ModBlocks.FROZEN_GRASS.getDefaultState(), 1).add(ModBlocks.FANBRUSH.getDefaultState(), 4)), 32));
        ConfiguredFeatures.register(context, FROZEN_FLOWER_DEFAULT, Feature.RANDOM_PATCH, createRandomPatchFeatureConfig(
                new WeightedBlockStateProvider(DataPool.<BlockState>builder().add(ModBlocks.FROZEN_ROSE.getDefaultState(), 2).add(ModBlocks.FROZEN_DANDELION.getDefaultState(), 1)), 64));
        // 冰霜树和冷杉结合生成
        ConfiguredFeatures.register(context, FROZEN_FIR, Feature.RANDOM_SELECTOR,
                new RandomFeatureConfig(List.of(new RandomFeatureEntry(firTree, 0.2F), new RandomFeatureEntry(frozenTree, 0.1F)), firTree));
        // 凛冰湖组成成分：凛冰湖会将周围的方块替换为冻石，并在上方创造一个洞穴空气
        ConfiguredFeatures.register(context, LAKE_CRYOTHEUM, Feature.LAKE, new LakeFeature.Config(
                // 液体：凛冰，方块：冻石
                BlockStateProvider.of(ModBlocks.GELID_CRYOTHEUM.getDefaultState()),
                BlockStateProvider.of(ModBlocks.FROZEN_STONE.getDefaultState())
        ));
    }

    /**
     * @param block 方块
     * @param tries 尝试生成草的数量
     * @return 随机草丛斑块配置
     */
    private static RandomPatchFeatureConfig createRandomPatchFeatureConfig(BlockStateProvider block, int tries) {
        return ConfiguredFeatures.createRandomPatchFeatureConfig(tries, PlacedFeatures.createEntry(Feature.SIMPLE_BLOCK, new SimpleBlockFeatureConfig(block)));
    }
}
