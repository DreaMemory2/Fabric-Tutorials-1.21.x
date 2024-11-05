package com.crystal.bluecore.registry.worldgen;

import com.crystal.bluecore.BlueCore;
import com.crystal.bluecore.registry.ModBlocks;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.PlacedFeatures;
import net.minecraft.world.gen.feature.VegetationPlacedFeatures;
import net.minecraft.world.gen.placementmodifier.*;

import java.util.List;

public class ModPlacedFeatures {
    // 放置地物
    // 树木
    public static final RegistryKey<PlacedFeature> MAPLE_TREE = registerKey("maple_tree");
    // 矿石
    public static final RegistryKey<PlacedFeature> ORE_PINK_GEMSTONE = registerKey("ore_pink_gemstone");
    // 晶洞
    public static final RegistryKey<PlacedFeature> FOREST_GEODE = registerKey("forest_geode");

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
        /* 矿物 */
        register(context, ORE_PINK_GEMSTONE, registryLookup.getOrThrow(ModConfiguredFeatures.ORE_PINK_GEMSTONE),
                // 中团簇大小为1-8，每个区块尝试生成2次，均匀生成在Y=-64到-4之间
                Modifiers.modifiersCount(2, HeightRangePlacementModifier.uniform(YOffset.fixed(-64), YOffset.fixed(-4))));
        /* 晶洞 */
        register(context, FOREST_GEODE, registryLookup.getOrThrow(ModConfiguredFeatures.FOREST_GEODE),
                RarityFilterPlacementModifier.of(42), // 稀有度
                SquarePlacementModifier.of(),
                HeightRangePlacementModifier.uniform(YOffset.aboveBottom(6), YOffset.fixed(50)), // 生成范围（60 ~ 50）
                BiomePlacementModifier.of()); // 生态群系放置修改器
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
}
