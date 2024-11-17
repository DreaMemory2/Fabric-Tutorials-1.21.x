package com.crystal.bluecore.world.biome.surface;

import com.crystal.bluecore.registry.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;

/**
 * <p>地表方块</p>
 * @author Crystal
 */
public class ModMaterialRuleBlocks {
    /* 基本地表 */
    public static final MaterialRules.MaterialRule GRASS = makeRule(Blocks.GRASS_BLOCK);
    public static final MaterialRules.MaterialRule DIRT = makeRule(Blocks.DIRT);
    public static final MaterialRules.MaterialRule STONE = makeRule(Blocks.STONE);
    /* 沙滩或河流地表 */
    public static final MaterialRules.MaterialRule SAND = makeRule(Blocks.SAND);
    public static final MaterialRules.MaterialRule GRAVEL = makeRule(Blocks.GRAVEL);
    public static final MaterialRules.MaterialRule CLAY = makeRule(Blocks.CLAY);
    /* 基岩层 */
    public static final MaterialRules.MaterialRule BEDROCK = makeRule(Blocks.BEDROCK);
    /* 雪花晶界维度中极寒森林的地表特征 */
    public static final MaterialRules.MaterialRule FROZEN_DIRT = makeRule(ModBlocks.FROZEN_DIRT);
    public static final MaterialRules.MaterialRule FROST_GRASS_BLOCK = makeRule(ModBlocks.FROST_GRASS_BLOCK);
    public static final MaterialRules.MaterialRule FROZEN_STONE = makeRule(ModBlocks.FROZEN_STONE);

    /**
     * 用于生成群系地表覆盖物（方块），例如：草方块、泥土，石头等
     * @param block 方块
     * @return 方块的生成规则
     */
    private static MaterialRules.MaterialRule makeRule(Block block) {
        return MaterialRules.block(block.getDefaultState());
    }
}
