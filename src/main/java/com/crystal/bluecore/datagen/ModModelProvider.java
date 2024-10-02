package com.crystal.bluecore.datagen;

import com.crystal.bluecore.block.custom.PinkGemStoneLamp;
import com.crystal.bluecore.registry.ModBlocks;
import com.crystal.bluecore.registry.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;

public class ModModelProvider extends FabricModelProvider {

    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        // 设置所有装饰物基本纹理（材质）类型
        BlockStateModelGenerator.BlockTexturePool pinkGemstonePool = blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.PINK_GEMSTONE_BLOCK);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.RAW_PINK_GEMSTONE_BLOCK);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.PINK_GEMSTONE_ORE);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.DEEPSLATE_PINK_GEMSTONE_ORE);

        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.MAGIC_BLOCK);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.VERTICAL_EXCAVATION_PROCESSOR);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.PIG_GENERATOR);
        // 各种装饰物模型设置
        pinkGemstonePool.stairs(ModBlocks.PINK_GEMSTONE_STAIRS);
        pinkGemstonePool.slab(ModBlocks.PINK_GEMSTONE_SLAB);
        pinkGemstonePool.button(ModBlocks.PINK_GEMSTONE_BUTTON);
        pinkGemstonePool.pressurePlate(ModBlocks.PINK_GEMSTONE_PRESSURE_PLATE);
        pinkGemstonePool.fence(ModBlocks.PINK_GEMSTONE_FENCE);
        pinkGemstonePool.fenceGate(ModBlocks.PINK_GEMSTONE_FENCE_GATE);
        pinkGemstonePool.wall(ModBlocks.PINK_GEMSTONE_WALL);
        // 注册铁门和活把门模型
        blockStateModelGenerator.registerDoor(ModBlocks.PINK_GEMSTONE_DOOR);
        blockStateModelGenerator.registerTrapdoor(ModBlocks.PINK_GEMSTONE_TRAPDOOR);
        // 注册粉红色宝石灯模型（未点亮和已点亮的状态材质渲染）
        Identifier lampOffIdentifier = TexturedModel.CUBE_ALL.upload(ModBlocks.PINK_GEMSTONE_LAMP, blockStateModelGenerator.modelCollector);
        Identifier lampOnIdentifier = blockStateModelGenerator.createSubModel(ModBlocks.PINK_GEMSTONE_LAMP, "_on", Models.CUBE_ALL, TextureMap::all);
        blockStateModelGenerator.blockStateCollector.accept(VariantsBlockStateSupplier.create(ModBlocks.PINK_GEMSTONE_LAMP)
                .coordinate(BlockStateModelGenerator.createBooleanModelMap(PinkGemStoneLamp.CLICKED, lampOnIdentifier, lampOffIdentifier)));
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ModItems.PINK_GEMSTONE, Models.GENERATED);
        itemModelGenerator.register(ModItems.RAW_PINK_GEMSTONE, Models.GENERATED);

        itemModelGenerator.register(ModItems.CAULIFLOWER, Models.GENERATED);
        itemModelGenerator.register(ModItems.CHISEL, Models.GENERATED);
        itemModelGenerator.register(ModItems.STARLIGHT_ASHES, Models.GENERATED);
        itemModelGenerator.register(ModItems.BLUEY_THEME_DISC, Models.TEMPLATE_MUSIC_DISC);
        itemModelGenerator.register(ModItems.OCTOPUS_DISC, ModItems.BLUEY_THEME_DISC, Models.TEMPLATE_MUSIC_DISC);
        itemModelGenerator.register(ModItems.ONESIES_DISC, ModItems.BLUEY_THEME_DISC, Models.TEMPLATE_MUSIC_DISC);
        // 武器与工具模型
        itemModelGenerator.register(ModItems.PINK_GEMSTONE_SWORD, Models.HANDHELD);
        itemModelGenerator.register(ModItems.PINK_GEMSTONE_PICKAXE, Models.HANDHELD);
        itemModelGenerator.register(ModItems.PINK_GEMSTONE_SHOVEL, Models.HANDHELD);
        itemModelGenerator.register(ModItems.PINK_GEMSTONE_AXE, Models.HANDHELD);
        itemModelGenerator.register(ModItems.PINK_GEMSTONE_HOE, Models.HANDHELD);
        itemModelGenerator.register(ModItems.PINK_GEMSTONE_HAMMER, Models.HANDHELD);
    }
}
