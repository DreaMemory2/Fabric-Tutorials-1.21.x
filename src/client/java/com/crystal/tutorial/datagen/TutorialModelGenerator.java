package com.crystal.tutorial.datagen;

import com.crystal.tutorial.block.ModBlocks;
import com.crystal.tutorial.item.ModItems;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.ItemModelGenerator;
import net.minecraft.client.data.Models;

/**
 * @author Crystal
 * @version 1.0
 * @since 1.0
 */
public class TutorialModelGenerator extends FabricModelProvider {

    public TutorialModelGenerator(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator create) {
        create.registerSimpleCubeAll(ModBlocks.CRUCIATE_STONE);
    }

    @Override
    public void generateItemModels(ItemModelGenerator create) {
        create.register(ModItems.DIAMOND_APPLE, Models.GENERATED);
    }
}
