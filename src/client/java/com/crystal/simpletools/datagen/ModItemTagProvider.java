package com.crystal.simpletools.datagen;

import com.crystal.simpletools.block.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;

import java.util.concurrent.CompletableFuture;

/**
* ClassName: Mod Item Tag Provider<br>
* Description: <br>
* Datetime: 2025/6/1 18:26<br>
* @author Crystal
* @version 1.0
* @since 1.0
*/
public class ModItemTagProvider extends FabricTagProvider.ItemTagProvider {

    public ModItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(ItemTags.LOGS).add(ModBlocks.AZALEA_LOG.asItem());
        getOrCreateTagBuilder(ItemTags.LEAVES).add(ModBlocks.AZALEA_LEAVES.asItem());
    }
}
