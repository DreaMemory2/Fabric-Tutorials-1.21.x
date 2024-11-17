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
import com.crystal.bluecore.world.gen.chunk.ModChunkGeneratorSettings;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;

public class BlueCoreDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		// Provider
		pack.addProvider(ModBlockTagProvider::new);
		pack.addProvider(ModItemTagProvider::new);
		pack.addProvider(ModLootTableProvider::new);
		pack.addProvider(ModModelProvider::new);
		pack.addProvider(ModRecipeProvider::new);
		// Generator
		pack.addProvider(ModRegistryDataGenerator::new);
	}

	/**
	 * 构建注册
	 * @param registryBuilder a {@link RegistryBuilder} instance
	 */
	@Override
	public void buildRegistry(RegistryBuilder registryBuilder) {
		// 自定义唱片
		registryBuilder.addRegistry(RegistryKeys.JUKEBOX_SONG, ModSounds::bootstrap);
		// 自定义纹饰材料和纹饰样式
		registryBuilder.addRegistry(RegistryKeys.TRIM_MATERIAL, ModTrimMaterials::bootstrap);
		registryBuilder.addRegistry(RegistryKeys.TRIM_PATTERN, ModTrimPatterns::bootstrap);
		// 自定义配置地物和放置地物
		registryBuilder.addRegistry(RegistryKeys.CONFIGURED_FEATURE, ModConfiguredFeatures::bootstrap);
		registryBuilder.addRegistry(RegistryKeys.PLACED_FEATURE, ModPlacedFeatures::bootstrap);
		// 自定义群系
		registryBuilder.addRegistry(RegistryKeys.BIOME, ModBiomes::bootstrap);
		// 自定义维度
		registryBuilder.addRegistry(RegistryKeys.DIMENSION_TYPE, ModDimensions::bootstrap);
		registryBuilder.addRegistry(RegistryKeys.DIMENSION, ModDimensions::bootstrapDimension);
		// 自定义噪声设置
		registryBuilder.addRegistry(RegistryKeys.CHUNK_GENERATOR_SETTINGS, ModChunkGeneratorSettings::bootstrap);
	}
}
