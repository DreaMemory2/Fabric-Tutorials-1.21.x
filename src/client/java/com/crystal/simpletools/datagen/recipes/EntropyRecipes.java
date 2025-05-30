package com.crystal.simpletools.datagen.recipes;

import com.crystal.simpletools.SimpleToolsMod;
import com.crystal.simpletools.recipe.entropy.EntropyRecipeBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataOutput;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

/**
* ClassName: EntropyRecipes<br>
* Description: <br>
* Datetime: 2025/5/28 13:06<br>
* @author Crystal
* @version 1.0
* @since 1.0
*/
public class EntropyRecipes extends RecipeGenerator.RecipeProvider {

    public EntropyRecipes(DataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> future) {
        super(output, future);
    }

    @Override
    protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup registries, RecipeExporter exporter) {
        return new RecipeGenerator(registries, exporter) {
            @Override
            public void generate() {
                buildCoolRecipes(exporter);
                buildHeatRecipes(exporter);
            }
        };
    }

    private void buildCoolRecipes(RecipeExporter consumer) {
        EntropyRecipeBuilder.cool()
                .setInputFluid(Fluids.FLOWING_WATER.getDefaultState())
                .setDrops(new ItemStack(Items.SNOWBALL))
                .save(consumer, SimpleToolsMod.of("entropy/cool/flowing_water_snowball"));

        EntropyRecipeBuilder.cool()
                .setInputBlock(Blocks.GRASS_BLOCK.getDefaultState())
                .setOutputBlock(Blocks.DIRT.getDefaultState())
                .save(consumer, SimpleToolsMod.of("entropy/cool/grass_block_dirt"));

        EntropyRecipeBuilder.cool()
                .setInputFluid(Fluids.LAVA.getDefaultState())
                .setOutputBlock(Blocks.OBSIDIAN.getDefaultState())
                .save(consumer, SimpleToolsMod.of("entropy/cool/lava_obsidian"));

        EntropyRecipeBuilder.cool()
                .setInputBlock(Blocks.STONE_BRICKS.getDefaultState())
                .setOutputBlock(Blocks.CRACKED_STONE_BRICKS.getDefaultState())
                .save(consumer, SimpleToolsMod.of("entropy/cool/stone_bricks_cracked_stone_bricks"));

        EntropyRecipeBuilder.cool()
                .setInputBlock(Blocks.STONE.getDefaultState())
                .setOutputBlock(Blocks.COBBLESTONE.getDefaultState())
                .save(consumer, SimpleToolsMod.of("entropy/cool/stone_cobblestone"));

        EntropyRecipeBuilder.cool()
                .setInputFluid(Fluids.WATER.getDefaultState())
                .setOutputBlock(Blocks.ICE.getDefaultState())
                .save(consumer, SimpleToolsMod.of("entropy/cool/water_ice"));
    }

    private void buildHeatRecipes(RecipeExporter consumer) {

        EntropyRecipeBuilder.heat()
                .setInputBlock(Blocks.COBBLESTONE.getDefaultState())
                .setOutputBlock(Blocks.STONE.getDefaultState())
                .save(consumer, SimpleToolsMod.of("entropy/heat/cobblestone_stone"));

        EntropyRecipeBuilder.heat()
                .setInputBlock(Blocks.ICE.getDefaultState())
                .setOutputFluid(Fluids.WATER.getDefaultState())
                .save(consumer, SimpleToolsMod.of("entropy/heat/ice_water"));

        EntropyRecipeBuilder.heat()
                .setInputBlock(Blocks.SNOW.getDefaultState())
                .setOutputFluid(Fluids.FLOWING_WATER.getDefaultState())
                .save(consumer, SimpleToolsMod.of("entropy/heat/snow_water"));

        EntropyRecipeBuilder.heat()
                .setInputFluid(Fluids.WATER.getDefaultState())
                .setOutputBlock(Blocks.AIR.getDefaultState())
                .save(consumer, SimpleToolsMod.of("entropy/heat/water_air"));

    }

    @Override
    public String getName() {
        return "AE2 Entropy Manipulator Recipes";
    }
}
