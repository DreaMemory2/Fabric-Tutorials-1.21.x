package com.crystal.bluecore.datagen;

import com.crystal.bluecore.BlueCore;
import com.crystal.bluecore.registry.ModBlocks;
import com.crystal.bluecore.registry.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {

    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        List<ItemConvertible> PINK_GEMSTONE_SAMPLTABLES = List.of(ModItems.RAW_PINK_GEMSTONE, ModBlocks.PINK_GEMSTONE_ORE,
                ModBlocks.DEEPSLATE_PINK_GEMSTONE_ORE);
        // 粉色宝石熔炉配方和高炉配方
        offerSmelting(exporter, PINK_GEMSTONE_SAMPLTABLES, RecipeCategory.MISC, ModItems.PINK_GEMSTONE, 0.25f, 200, "pink_gemstone_smelting");
        offerBlasting(exporter, PINK_GEMSTONE_SAMPLTABLES, RecipeCategory.MISC, ModItems.PINK_GEMSTONE, 0.25f, 100, "pink_gemstone_blasting");
        // 粉红色宝石合成粉红色块配方
        offerReversibleCompactingRecipes(exporter, RecipeCategory.BUILDING_BLOCKS, ModItems.PINK_GEMSTONE, RecipeCategory.DECORATIONS, ModBlocks.PINK_GEMSTONE_BLOCK);
        // 粗粉红色宝石合成粗粉红色块配方
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModBlocks.RAW_PINK_GEMSTONE_BLOCK)
                .pattern("RRR").pattern("RRR").pattern("RRR").input('R', ModItems.RAW_PINK_GEMSTONE)
                .criterion(hasItem(ModItems.RAW_PINK_GEMSTONE), conditionsFromItem(ModItems.RAW_PINK_GEMSTONE)).offerTo(exporter);
        // 将粗粉红色块分解为9个粗粉红色宝石配方
        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.RAW_PINK_GEMSTONE, 9).input(ModBlocks.RAW_PINK_GEMSTONE_BLOCK)
                .criterion(hasItem(ModBlocks.RAW_PINK_GEMSTONE_BLOCK), conditionsFromItem(ModBlocks.RAW_PINK_GEMSTONE_BLOCK)).offerTo(exporter);
        // 将粉红色宝石分解为32个粗粉红色宝石配方
        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.RAW_PINK_GEMSTONE, 32).input(ModBlocks.MAGIC_BLOCK)
                .criterion(hasItem(ModBlocks.MAGIC_BLOCK), conditionsFromItem(ModBlocks.MAGIC_BLOCK))
                .offerTo(exporter, Identifier.of(BlueCore.MOD_ID, "pink_gemstone_from_magic_block"));
    }
}
