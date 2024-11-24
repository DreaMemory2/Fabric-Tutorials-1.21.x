package com.crystal.bluecore.world.gen.feature;

import com.crystal.bluecore.registry.ModBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.gen.feature.GeodeCrackConfig;
import net.minecraft.world.gen.feature.GeodeFeatureConfig;
import net.minecraft.world.gen.feature.GeodeLayerConfig;
import net.minecraft.world.gen.feature.GeodeLayerThicknessConfig;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

import java.util.List;

public class FrostGeodeFeatureConfig extends GeodeFeatureConfig {

    public FrostGeodeFeatureConfig() {
        super(
                new FrostGeodeLayerConfig(), // 晶洞构成方块
                new FrostGeodeLayerThicknessConfig(), // 晶洞厚度配置
                new ForestGeodeCrackConfig(), // 晶洞裂纹程度
                0.35D, //Use Potential Placements Chance：放置晶洞的概率为35%
                0.083D, //Use Alternate Layer0 Chance：内部方块被替换的概率；原版：8.3%的紫水晶块会被替换为紫水晶母岩
                true, // Placements Require Layer0 Alternate：放置是否必须使用交替层
                UniformIntProvider.create(4, 6), // Outer Wall Distance：外墙的厚度[4, 6]
                UniformIntProvider.create(3, 4), // Distribution Points：分布点[3, 4]
                UniformIntProvider.create(1, 2), // Point Offset：点偏移[1, 2]
                -16, // Min Gen Offset：最小生成偏移
                16, // Max Gen Offset：最大生成偏移
                0.05D, // Noise Multiplier：噪音增倍器
                1); // Invalid Blocks Threshold：无效块阈值，默认为1
    }

    private static class FrostGeodeLayerConfig extends GeodeLayerConfig {

        public FrostGeodeLayerConfig() {
            super(
                    BlockStateProvider.of(Blocks.AIR), // Filling Provider：填充方块，通常填充空气，用于形成空洞
                    BlockStateProvider.of(Blocks.SNOW_BLOCK), // Inner Layer Provider：内部层方块，例如紫水晶块层
                    BlockStateProvider.of(ModBlocks.BUDDING_FROST), // Alternate Inner Layer Provider：可被替换内层方块，例如：紫水晶母岩
                    BlockStateProvider.of(ModBlocks.DENSE_SNOW), // Middle Layer Provider：中间层方块，例如方解石
                    BlockStateProvider.of(ModBlocks.FROST_ICE), // Outer Layer Provider：外部层方块，例如：平滑玄武岩
                    List.of(
                            // Inner Blocks：内部方块，例如：紫晶芽或者紫水晶簇
                            ModBlocks.SMALL_FROST_BUD.getDefaultState(),
                            ModBlocks.MEDIUM_FROST_BUD.getDefaultState(),
                            ModBlocks.LARGE_FROST_BUD.getDefaultState(),
                            ModBlocks.FROST_CLUSTER.getDefaultState()),
                    BlockTags.FEATURES_CANNOT_REPLACE, // Cannot Replace：不可替换方块，一般有功能方块标签，例如：箱子，传送门方块，基岩等
                    BlockTags.GEODE_INVALID_BLOCKS); // Invalid Blocks：无效方块，一般是晶洞无效方块标签：例如：水体、岩浆，蓝冰（Blue Ice）、浮冰（Packed Ice）和基岩
        }
    }

    private static class FrostGeodeLayerThicknessConfig extends GeodeLayerThicknessConfig {

        /**
         * <p>更改晶洞的体积大小和厚度</p>
         * <p>原版晶洞参数：</p>
         * <ul>
         *     <li>Filling: 1.7D</li>
         *     <li>Inner Layer：2.2D</li>
         *     <li>Middle Layer：3.2D</li>
         *     <li>Outer Layer：4.2D</li>
         * </ul>
         */
        public FrostGeodeLayerThicknessConfig() {
            super(1.7D, 2.2D, 3.2D, 4.2D);
        }
    }

    private static class ForestGeodeCrackConfig extends GeodeCrackConfig {
        /**
         * <p>调试晶洞裂纹程度</p>
         * <p>参数含义：</p>
         * <ul>
         *     <li>Generate Crack Chance: 晶洞产生裂缝概率，例如紫晶洞有95%的概率产生裂缝</li>
         *     <li>Base Crack Size：晶洞产生裂缝大小</li>
         *     <li>Crack Point Offset：晶洞产生裂缝偏移位置</li>
         * </ul>
         * <p>原版晶洞参数：</p>
         * <ul>
         *     <li>Generate Crack Chance: 0.95D</li>
         *     <li>Base Crack Size：2.0D</li>
         *     <li>Crack Point Offset：2</li>
         * </ul>
         */
        public ForestGeodeCrackConfig() {
            super(0.50D, 2.0D, 2);
        }
    }
}
