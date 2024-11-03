package com.crystal.bluecore.datagen.generator;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModRegistryDataGenerator extends FabricDynamicRegistryProvider {

    public ModRegistryDataGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup registries, Entries entries) {
        // 生成纹饰数据文件
        entries.addAll(registries.getWrapperOrThrow(RegistryKeys.TRIM_MATERIAL));
        entries.addAll(registries.getWrapperOrThrow(RegistryKeys.TRIM_PATTERN));
        // 配置地物和放置地物数据生成器
        entries.addAll(registries.getWrapperOrThrow(RegistryKeys.CONFIGURED_FEATURE));
        entries.addAll(registries.getWrapperOrThrow(RegistryKeys.PLACED_FEATURE));
        // 数据生成生态群系
        entries.addAll(registries.getWrapperOrThrow(RegistryKeys.BIOME));
        // 数据生成维度类型
        entries.addAll(registries.getWrapperOrThrow(RegistryKeys.DIMENSION_TYPE));
    }

    @Override
    public String getName() {
        return "World Gen Data Provider";
    }
}
