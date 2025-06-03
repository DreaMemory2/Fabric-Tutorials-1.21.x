package com.crystal.simpletools.datagen;

import com.crystal.simpletools.block.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;

import java.util.concurrent.CompletableFuture;

/**
* ClassName: ModBlockTagProvider<br>
* Description: <br>
* Datetime: 2025/6/1 18:28<br>
* @author Crystal
* @version 1.0
* @since 1.0
*/
public class ModBlockTagProvider extends FabricTagProvider.BlockTagProvider {
    public ModBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(BlockTags.OVERWORLD_NATURAL_LOGS).add(ModBlocks.AZALEA_LOG);
        getOrCreateTagBuilder(BlockTags.LOGS_THAT_BURN).add(ModBlocks.AZALEA_LOG);
        getOrCreateTagBuilder(BlockTags.LOGS).add(ModBlocks.AZALEA_LOG);
        getOrCreateTagBuilder(BlockTags.LEAVES).add(ModBlocks.AZALEA_LEAVES);
    }
}
