package com.crystal.bluecore.registry.worldgen;

import com.crystal.bluecore.BlueCore;
import com.crystal.bluecore.registry.ModBlocks;
import com.google.common.collect.ImmutableList;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.blockpredicate.BlockPredicate;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.PlacedFeatures;
import net.minecraft.world.gen.feature.VegetationPlacedFeatures;
import net.minecraft.world.gen.heightprovider.UniformHeightProvider;
import net.minecraft.world.gen.placementmodifier.*;

import java.util.List;

public class ModPlacedFeatures {
    // 放置地物
    // 树木
    public static final RegistryKey<PlacedFeature> MAPLE_TREE = registerKey("maple_tree");
    public static final RegistryKey<PlacedFeature> FIR_TREE = registerKey("fir_tree");
    public static final RegistryKey<PlacedFeature> FROZEN_TREE = registerKey("frozen_tree");
    public static final RegistryKey<PlacedFeature> FROZEN_FIR = registerKey("frozen_fir");
    // 矿石
    public static final RegistryKey<PlacedFeature> ORE_PINK_GEMSTONE = registerKey("ore_pink_gemstone");
    // 晶洞
    public static final RegistryKey<PlacedFeature> FROST_GEODE = registerKey("frost_geode");
    // 斑块
    public static final RegistryKey<PlacedFeature> PATCH_FROZEN_GRASS = registerKey("patch_frozen_grass");
    public static final RegistryKey<PlacedFeature> PATCH_FANBRUSH = registerKey("patch_fanbrush");
    public static final RegistryKey<PlacedFeature> FROZEN_FLOWER_DEFAULT = registerKey("frozen_flower_default");
    // 湖泊：地下凛冰湖和表面凛冰湖
    public static final RegistryKey<PlacedFeature> LAKE_CRYOTHEUM_UNDERGROUND = registerKey("lake_cryotheum_underground");
    public static final RegistryKey<PlacedFeature> LAKE_CRYOTHEUM_SURFACE = registerKey("lake_cryotheum_surface");

    private static final PlacementModifier NOT_IN_SURFACE_WATER_MODIFIER = SurfaceWaterDepthFilterPlacementModifier.of(0);

    /**
     * <p>提供可以复制放置修饰器</p>
     * @param featureRegisterable 注册接口
     * @param key 放置地物
     * @param feature 配置地物
     * @param modifiers 放置修饰器
     */
    public static void register(Registerable<PlacedFeature> featureRegisterable, RegistryKey<PlacedFeature> key, RegistryEntry<ConfiguredFeature<?, ?>> feature, List<PlacementModifier> modifiers) {
        featureRegisterable.register(key, new PlacedFeature(feature, List.copyOf(modifiers)));
    }

    /**
     * <p>提供更多放置修饰器</p>
     * @param featureRegisterable 注册接口
     * @param key 放置地物
     * @param feature 配置地物
     * @param modifiers 放置修饰器
     */
    public static void register(Registerable<PlacedFeature> featureRegisterable, RegistryKey<PlacedFeature> key, RegistryEntry<ConfiguredFeature<?, ?>> feature, PlacementModifier... modifiers) {
        register(featureRegisterable, key, feature, List.of(modifiers));
    }

    /**
     * 注册放置地物方法
     * @param name 放置地物命名空间
     */
    public static RegistryKey<PlacedFeature> registerKey(String name) {
        return RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(BlueCore.MOD_ID, name));
    }

    public static void bootstrap(Registerable<PlacedFeature> context) {
        // 注册检测表
        RegistryEntryLookup<ConfiguredFeature<?, ?>> registryLookup = context.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE);
        /* 树木 */
        // 组装放置地物的树结构，还要添加植被放置器（具有生存能力的树木修改器）：树木可以通过树苗（必须是泥土作为营养来源）来进行放置，
        register(context, MAPLE_TREE, registryLookup.getOrThrow(ModConfiguredFeatures.MAPLE_TREE), VegetationPlacedFeatures.treeModifiersWithWouldSurvive(
                // 放置数量设置为一个；额外机会必须是整除，例如0.1是被10整除，提供3个额外机会，否则报出异常。
                PlacedFeatures.createCountExtraModifier(1, 0.2f, 3), ModBlocks.MAPLE_SAPLING)
        );
        PlacedFeatures.register(context, FIR_TREE, registryLookup.getOrThrow(ModConfiguredFeatures.FIR_TREE), PlacedFeatures.wouldSurvive(ModBlocks.FIR_SAPLING));
        PlacedFeatures.register(context, FROZEN_TREE, registryLookup.getOrThrow(ModConfiguredFeatures.FROZEN_TREE), PlacedFeatures.wouldSurvive(ModBlocks.FROZEN_SAPLING));
        // 冰霜树和冷杉结合生成
        PlacedFeatures.register(context, FROZEN_FIR, registryLookup.getOrThrow(ModConfiguredFeatures.FROZEN_FIR),
                treeModifiersBuilder(PlacedFeatures.createCountExtraModifier(10, 0.1F, 1))
                        // 向世界维度生成树
                        .add(BlockFilterPlacementModifier.of(BlockPredicate.wouldSurvive(ModBlocks.FIR_SAPLING.getDefaultState(), BlockPos.ORIGIN)))
                        .add(BlockFilterPlacementModifier.of(BlockPredicate.wouldSurvive(ModBlocks.FROZEN_SAPLING.getDefaultState(), BlockPos.ORIGIN)))
                        .build()
        );
        /* 矿物 */
        register(context, ORE_PINK_GEMSTONE, registryLookup.getOrThrow(ModConfiguredFeatures.ORE_PINK_GEMSTONE),
                // 中团簇大小为1-8，每个区块尝试生成2次，均匀生成在Y=-64到-4之间
                Modifiers.modifiersCount(2, HeightRangePlacementModifier.uniform(YOffset.fixed(-64), YOffset.fixed(-4))));
        /* 晶洞 */
        register(context, FROST_GEODE, registryLookup.getOrThrow(ModConfiguredFeatures.FROST_GEODE),
                // 有1 / chance的概率返回当前坐标。否则返回空
                RarityFilterPlacementModifier.of(42), // 稀有度
                // in_square：X轴坐标和Z轴坐标各自增加一个0到15内随机数
                SquarePlacementModifier.of(),
                // 修改坐标的y值，并返回修改后的坐标
                HeightRangePlacementModifier.uniform(YOffset.aboveBottom(6), YOffset.fixed(50)), // 生成范围（60 ~ 50）
                // biome：如果该位置的生物群系可以生成该地物，返回当前坐标
                BiomePlacementModifier.of()); // 生态群系放置修改器
        /* 草丛斑块 */
        PlacedFeatures.register(context, PATCH_FROZEN_GRASS, registryLookup.getOrThrow(ModConfiguredFeatures.PATCH_FROZEN_GRASS),
                // noise_threshold_count：复制并返回多个当前坐标。有两种指定的数量，根据噪声值大于或小于指定阈值决定
                NoiseThresholdCountPlacementModifier.of(-0.8, 5, 10),
                SquarePlacementModifier.of(),
                // heightmap：返回该水平位置的heightmap高度的上方一格的坐标
                PlacedFeatures.WORLD_SURFACE_WG_HEIGHTMAP,
                BiomePlacementModifier.of());
        PlacedFeatures.register(context, PATCH_FANBRUSH, registryLookup.getOrThrow(ModConfiguredFeatures.PATCH_FANBRUSH),
                SquarePlacementModifier.of(),
                PlacedFeatures.WORLD_SURFACE_WG_HEIGHTMAP,
                BiomePlacementModifier.of());
        /* 花丛斑块 */
        PlacedFeatures.register(context, FROZEN_FLOWER_DEFAULT, registryLookup.getOrThrow(ModConfiguredFeatures.FROZEN_FLOWER_DEFAULT),
                NoiseThresholdCountPlacementModifier.of(-0.8, 15, 4),
                RarityFilterPlacementModifier.of(32),
                SquarePlacementModifier.of(),
                PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP,
                BiomePlacementModifier.of());
        // 地下凛冰湖：有1⁄9的概率在地表5格以下、Y=0以上出现一个地下熔岩湖
        PlacedFeatures.register(context, LAKE_CRYOTHEUM_UNDERGROUND, registryLookup.getOrThrow(ModConfiguredFeatures.LAKE_CRYOTHEUM),
                RarityFilterPlacementModifier.of(9),
                SquarePlacementModifier.of(),
                HeightRangePlacementModifier.of(UniformHeightProvider.create(YOffset.fixed(0), YOffset.getTop())),
                EnvironmentScanPlacementModifier.of(Direction.DOWN,
                        BlockPredicate.bothOf(BlockPredicate.not(BlockPredicate.IS_AIR), BlockPredicate.insideWorldBounds(new BlockPos(0, -5, 0))), 32),
                SurfaceThresholdFilterPlacementModifier.of(Heightmap.Type.OCEAN_FLOOR_WG, Integer.MIN_VALUE, -5),
                BiomePlacementModifier.of());
        // 表面凛冰湖：每个区块有1⁄200的概率在地表出现一个熔岩湖
        PlacedFeatures.register(context, LAKE_CRYOTHEUM_SURFACE, registryLookup.getOrThrow(ModConfiguredFeatures.LAKE_CRYOTHEUM),
                RarityFilterPlacementModifier.of(200),
                SquarePlacementModifier.of(),
                PlacedFeatures.WORLD_SURFACE_WG_HEIGHTMAP,
                BiomePlacementModifier.of());
    }

    /**
     * <p>自定义修饰器</p>
     */
    public static class Modifiers {
        public static List<PlacementModifier> modifiers(PlacementModifier countModifier, PlacementModifier heightModifier) {
            return List.of(countModifier, SquarePlacementModifier.of(), heightModifier, BiomePlacementModifier.of());
        }

        public static List<PlacementModifier> modifiersCount(int count, PlacementModifier heightModifier) {
            return modifiers(CountPlacementModifier.of(count), heightModifier);
        }

        public static List<PlacementModifier> modifierRarity(int chance, PlacementModifier heightModifier) {
            return modifiers(RarityFilterPlacementModifier.of(chance), heightModifier);
        }
    }

    /**
     *
     * @param countModifier 数量放置修改器
     */
    private static ImmutableList.Builder<PlacementModifier> treeModifiersBuilder(PlacementModifier countModifier) {
        return ImmutableList.<PlacementModifier>builder()
                .add(countModifier)
                .add(SquarePlacementModifier.of())
                .add(NOT_IN_SURFACE_WATER_MODIFIER)
                .add(PlacedFeatures.OCEAN_FLOOR_HEIGHTMAP)
                .add(BiomePlacementModifier.of());
    }
}
