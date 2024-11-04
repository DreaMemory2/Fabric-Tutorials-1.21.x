package com.crystal.bluecore.world.biome.surface;

import com.crystal.bluecore.registry.ModBiomes;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;

import static com.crystal.bluecore.world.biome.surface.ModMaterialRuleBlocks.*;

/**
 * 详细请参考：document/biome/地表规则.md
 */
public class ModMaterialRules {

    public static MaterialRules.MaterialRule coldestForestSurface() {
        MaterialRules.MaterialCondition isAtOrAboveWaterLevel = MaterialRules.water(-1, 0);
        MaterialRules.MaterialRule grassSurface = MaterialRules.sequence(MaterialRules.condition(isAtOrAboveWaterLevel, GRASS), DIRT);

        return MaterialRules.sequence(
                MaterialRules.sequence(MaterialRules.condition(MaterialRules.biome(ModBiomes.COLDEST_FOREST),
                        MaterialRules.condition(MaterialRules.STONE_DEPTH_FLOOR, STONE)),
                        MaterialRules.condition(MaterialRules.STONE_DEPTH_FLOOR, grassSurface)));
    }
}
