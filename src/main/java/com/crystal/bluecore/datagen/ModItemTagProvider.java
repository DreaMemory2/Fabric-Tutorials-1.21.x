package com.crystal.bluecore.datagen;

import com.crystal.bluecore.registry.ModItems;
import com.crystal.bluecore.util.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider.ItemTagProvider;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends ItemTagProvider {
    public ModItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        // 添加”魔法石块配方“标签
        getOrCreateTagBuilder(ModTags.Items.GEMSTONE_ITEM)
                .add(ModItems.PINK_GEMSTONE)
                .add(ModItems.RAW_PINK_GEMSTONE)
                .add(Items.COAL)
                .add(Items.STICK)
                .add(Items.APPLE);
    }
}
