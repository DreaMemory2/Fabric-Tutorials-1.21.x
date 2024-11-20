package com.crystal.bluecore.world.biome;

import com.crystal.bluecore.registry.worldgen.ModPlacedFeatures;
import com.crystal.bluecore.world.biome.feature.ModBiomeFeatures;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.GenerationStep.Feature;
import net.minecraft.world.gen.carver.ConfiguredCarvers;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;

public class ColdestForestBiome {
    /**
     * <p>群系当中地物的生成器</p>
     */
    public static void biomeFeatures(GenerationSettings.LookupBackedBuilder builder) {
        // 添加洞穴湖雕刻器
        builder.carver(GenerationStep.Carver.AIR, ConfiguredCarvers.CAVE);
        builder.carver(GenerationStep.Carver.AIR, ConfiguredCarvers.CAVE_EXTRA_UNDERGROUND);
        builder.carver(GenerationStep.Carver.AIR, ConfiguredCarvers.CANYON);
        // 添加寒霜晶洞
        ModBiomeFeatures.addFrostGeodes(builder);
        // 添加地牢
        DefaultBiomeFeatures.addDungeons(builder);
        // 添加矿石
        /* DefaultBiomeFeatures.addMineables(builder); */
        // 添加温泉（隔水层）
        DefaultBiomeFeatures.addSprings(builder);
        // 添加冰面
        DefaultBiomeFeatures.addFrozenTopLayer(builder);
    }

    /**
     * 更多信息，请参考：document/biome/CustomBiome.md
     * @return 构建生态群系
     */
    public static Biome biomeGenerator(Registerable<Biome> context) {
        /* 生成生物 */
        SpawnSettings.Builder spawnSettings = new SpawnSettings.Builder();
        // 生成生物实体Entity，权重为2，数量：最小群体3，最大群体为5
        /* spawnSettings.spawn(SpawnGroup.CREATURE, new SpawnSettings.SpawnEntry(EntityType.PIG, 2, 3, 5)); */
        // 生成可驯养的动物
        /* DefaultBiomeFeatures.addFarmAnimals(spawnSettings); */
        // 生成蝙蝠和敌对生物（例如：僵尸和小白）
        /* DefaultBiomeFeatures.addBatsAndMonsters(spawnSettings); */
        /* 生成地物和雕刻器 */
        // 生成世界的地形，提供放置地物PlacedFeatures和配置地物ConfiguredCarver
        GenerationSettings.LookupBackedBuilder generationSettings = new GenerationSettings.LookupBackedBuilder(
                context.getRegistryLookup(RegistryKeys.PLACED_FEATURE),
                context.getRegistryLookup(RegistryKeys.CONFIGURED_CARVER));
        // 雕刻器
        biomeFeatures(generationSettings);
        // 生成苔石地物
        /* DefaultBiomeFeatures.addMossyRocks(generationSettings); */
        // 生成矿石
        /* DefaultBiomeFeatures.addDefaultOres(generationSettings);*/
        // 生成格外的金矿石，例如恶地
        /* DefaultBiomeFeatures.addExtraGoldOre(generationSettings);*/
        /* 生成植被 */
        // 生成树木（平原类型）
        /* generationSettings.feature(GenerationStep.Feature.VEGETAL_DECORATION, VegetationPlacedFeatures.TREES_PLAINS);*/
        // 生成花朵（森林类型）
        /* DefaultBiomeFeatures.addForestFlowers(generationSettings);*/
        // 生成大型蕨类
        /* DefaultBiomeFeatures.addLargeFerns(generationSettings);*/
        // 生成蘑菇
        /* DefaultBiomeFeatures.addDefaultMushrooms(generationSettings);*/
        // 生成植被（比如甘泽和南瓜）
        /* DefaultBiomeFeatures.addDefaultVegetation(generationSettings);*/
        // 生成树木植被
        generationSettings.feature(Feature.VEGETAL_DECORATION, ModPlacedFeatures.FROZEN_FIR);
        // 添加植被斑块
        generationSettings.feature(Feature.VEGETAL_DECORATION, ModPlacedFeatures.PATCH_FROZEN_GRASS);
        generationSettings.feature(Feature.VEGETAL_DECORATION, ModPlacedFeatures.PATCH_FANBRUSH);
        generationSettings.feature(Feature.VEGETAL_DECORATION, ModPlacedFeatures.FROZEN_FLOWER_DEFAULT);
        BiomeEffects.Builder builder = new BiomeEffects.Builder();
        // 天空颜色、雾气颜色、水体颜色、水雾颜色
        builder.skyColor(0xA1B4E8)
                .fogColor(0xC0D8FF)
                .waterColor(0x3F76E4)
                .waterFogColor(0x050533);
        return new Biome.Builder()
                // 是否可以降水
                .precipitation(true)
                // 湿度
                .downfall(0.5f)
                // 温度
                .temperature(-0.5f)
                // 构造生成地物
                .generationSettings(generationSettings.build())
                // 生成生物实体Entity
                .spawnSettings(spawnSettings.build())
                // 群系效果
                .effects(builder.build())
                .build();
    }
}
