package com.crystal.simpletools.datagen;

import com.crystal.simpletools.block.ModBlocks;
import com.crystal.simpletools.item.ModItems;
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
public class ModModelGenerator extends FabricModelProvider {

    public ModModelGenerator(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator create) {
        create.registerSimpleCubeAll(ModBlocks.BLAST_FURNACE);
        create.registerSimpleCubeAll(ModBlocks.AZALEA_LEAVES);
        create.createLogTexturePool(ModBlocks.AZALEA_LOG).log(ModBlocks.AZALEA_LOG);
    }

    @Override
    public void generateItemModels(ItemModelGenerator create) {
        create.register(ModItems.DIAMOND_APPLE_CORE, Models.GENERATED);
        create.register(ModItems.DIAMOND_APPLE, Models.GENERATED);
        create.register(ModItems.ENTROPY_MANIPULATOR, Models.GENERATED);
    }
}
