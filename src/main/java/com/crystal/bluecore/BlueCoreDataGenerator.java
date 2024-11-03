package com.crystal.bluecore;

import com.crystal.bluecore.datagen.*;
import com.crystal.bluecore.datagen.generator.ModRegistryDataGenerator;
import com.crystal.bluecore.registry.ModBiomes;
import com.crystal.bluecore.registry.ModDimensions;
import com.crystal.bluecore.registry.ModSounds;
import com.crystal.bluecore.registry.worldgen.ModConfiguredFeatures;
import com.crystal.bluecore.registry.worldgen.ModPlacedFeatures;
import com.crystal.bluecore.trim.ModTrimMaterials;
import com.crystal.bluecore.trim.ModTrimPatterns;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;

public class BlueCoreDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		pack.addProvider(ModBlockTagProvider::new);
		pack.addProvider(ModItemTagProvider::new);
		pack.addProvider(ModLootTableProvider::new);
		pack.addProvider(ModModelProvider::new);
		pack.addProvider(ModRecipeProvider::new);
		// Generator
		pack.addProvider(ModRegistryDataGenerator::new);
	}

	@Override
	public void buildRegistry(RegistryBuilder registryBuilder) {
		// 构建注册
		registryBuilder.addRegistry(RegistryKeys.JUKEBOX_SONG, ModSounds::bootstrap);
		registryBuilder.addRegistry(RegistryKeys.TRIM_MATERIAL, ModTrimMaterials::bootstrap);
		registryBuilder.addRegistry(RegistryKeys.TRIM_PATTERN, ModTrimPatterns::bootstrap);
		// 配置地物和放置地物构建注册
		registryBuilder.addRegistry(RegistryKeys.CONFIGURED_FEATURE, ModConfiguredFeatures::bootstrap);
		registryBuilder.addRegistry(RegistryKeys.PLACED_FEATURE, ModPlacedFeatures::bootstrap);
		// 自定义群系
		registryBuilder.addRegistry(RegistryKeys.BIOME, ModBiomes::bootstrap);
		// 自定义维度
		registryBuilder.addRegistry(RegistryKeys.DIMENSION_TYPE, ModDimensions::bootstrap);
	}
}
