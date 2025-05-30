package com.crystal.simpletools;

import com.crystal.simpletools.datagen.TutorialModelGenerator;
import com.crystal.simpletools.datagen.recipes.EntropyRecipes;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;

public class SimpleToolsModDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator generator) {
		var registries = generator.getRegistries();
		// 获取数据构造器的打包器，进行类名封装处理
		FabricDataGenerator.Pack pack = generator.createPack();
		// 添加名单
		pack.addProvider(TutorialModelGenerator::new);

		// Recipes
		pack.addProvider(bindRegistries(EntropyRecipes::new, registries));
	}

	private static <T extends DataProvider> DataProvider.Factory<T> bindRegistries(
			BiFunction<DataOutput, CompletableFuture<RegistryWrapper.WrapperLookup>, T> factory,
			CompletableFuture<RegistryWrapper.WrapperLookup> factories
	) {
		return packOutput -> factory.apply(packOutput, factories);
	}
}
