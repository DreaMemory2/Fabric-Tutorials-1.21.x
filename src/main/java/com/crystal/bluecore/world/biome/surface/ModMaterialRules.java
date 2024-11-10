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
        MaterialRules.MaterialRule dirtSurface = MaterialRules.condition(MaterialRules.stoneDepth(3, false, 0, VerticalSurfaceType.FLOOR), FROZEN_DIRT);
        MaterialRules.MaterialRule bedrockSurface = MaterialRules.condition(MaterialRules.verticalGradient("minecraft:bedrock_floor", YOffset.aboveBottom(0), YOffset.aboveBottom(5)), BEDROCK);
        MaterialRules.MaterialCondition coldestForest = MaterialRules.biome(ModBiomes.COLDEST_FOREST);

        return MaterialRules.sequence(
                bedrockSurface,
                MaterialRules.condition(MaterialRules.surface(),
                        MaterialRules.condition(coldestForest,
                                MaterialRules.sequence(
                                        MaterialRules.condition(water, MaterialRules.sequence(grassSurface, dirtSurface)),
                                        FROZEN_STONE))));
    }
}
