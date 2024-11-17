package com.crystal.bluecore.world.biome;

import com.crystal.bluecore.BlueCore;
import com.crystal.bluecore.registry.worldgen.ModPlacedFeatures;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.gen.GenerationStep;

public class ModBiomeModifications {
    public static void overworldBiome() {
        // 粉红色宝石矿石生成在主世界生态群系地下中去
        BiomeModifications.addFeature(
                BiomeSelectors.foundInOverworld(),
                GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.ORE_PINK_GEMSTONE
        );
        // 将晶洞生成在主世界生态群系地下中去
        BiomeModifications.addFeature(
                BiomeSelectors.foundInOverworld(),
                GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.FROST_GEODE
        );
    }


    public static void registerBiomeInfo() {
        BlueCore.LOGGER.info(BlueCore.MOD_ID + ": Registered Mod Biome Success");
        // 主世界生态群系生成规则
        overworldBiome();
    }
}
