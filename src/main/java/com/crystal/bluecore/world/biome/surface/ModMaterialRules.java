package com.crystal.bluecore.world.biome.surface;

import com.crystal.bluecore.registry.ModBiomes;
import net.minecraft.util.math.VerticalSurfaceType;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;

import static com.crystal.bluecore.world.biome.surface.ModMaterialRuleBlocks.*;

/**
 * 详细请参考：document/biome/地表规则.md
 */
public class ModMaterialRules {

    public static MaterialRules.MaterialRule coldestForestSurface() {
        MaterialRules.MaterialCondition water = MaterialRules.water(-1, 0);
        MaterialRules.MaterialRule grassSurface = MaterialRules.condition(MaterialRules.stoneDepth(0, false, 0, VerticalSurfaceType.FLOOR), FROST_GRASS_BLOCK);
        MaterialRules.MaterialRule dirtSurface = MaterialRules.condition(MaterialRules.stoneDepth(2, false, 0, VerticalSurfaceType.FLOOR), FROZEN_DIRT);
        MaterialRules.MaterialRule bedrockSurface = MaterialRules.condition(MaterialRules.verticalGradient("minecraft:bedrock_floor", YOffset.aboveBottom(0), YOffset.aboveBottom(5)), BEDROCK);
        MaterialRules.MaterialRule deepslateSurface = MaterialRules.condition(MaterialRules.verticalGradient("minecraft:deepslate", YOffset.fixed(0), YOffset.fixed(8)), FROZEN_STONE);
        MaterialRules.MaterialCondition coldestForest = MaterialRules.biome(ModBiomes.COLDEST_FOREST);

        return MaterialRules.sequence(
                // 基岩层
                bedrockSurface,
                // 地表层
                MaterialRules.sequence(
                        // 是否要尝试生成表层（例如平原的草方块和泥土）。
                        // preliminary_surface为initial_density_without_jaggedness产生的初始地表高度向下整体偏移约2到8格后的高度，
                        // 该高度通常相对于实际表层厚度很低，以保证能够生成表层。若无此条件，洞穴的地面也会生成表层
                        MaterialRules.condition(MaterialRules.surface(),
                                MaterialRules.condition(water,
                                        MaterialRules.sequence(
                                                // 生物群系
                                                MaterialRules.condition(coldestForest, MaterialRules.sequence(grassSurface, dirtSurface))
                                        )
                                ))),
                deepslateSurface
        );
    }
}
