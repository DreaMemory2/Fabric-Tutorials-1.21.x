package com.crystal.bluecore.datagen;

import com.crystal.bluecore.registry.ModBlocks;
import com.crystal.bluecore.registry.ModItems;
import com.crystal.bluecore.util.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider.ItemTagProvider;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;

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
        // 武器和工具标签
        getOrCreateTagBuilder(ItemTags.SWORDS).add(ModItems.PINK_GEMSTONE_SWORD);
        getOrCreateTagBuilder(ItemTags.PICKAXES).add(ModItems.PINK_GEMSTONE_PICKAXE).addTag(ModTags.Items.HAMMER);
        getOrCreateTagBuilder(ItemTags.SHOVELS).add(ModItems.PINK_GEMSTONE_SHOVEL);
        getOrCreateTagBuilder(ItemTags.AXES).add(ModItems.PINK_GEMSTONE_AXE);
        getOrCreateTagBuilder(ItemTags.HOES).add(ModItems.PINK_GEMSTONE_HOE);
        getOrCreateTagBuilder(ModTags.Items.HAMMER).add(ModItems.PINK_GEMSTONE_HAMMER);
        // 装备标签
        getOrCreateTagBuilder(ItemTags.TRIMMABLE_ARMOR)
                .add(ModItems.PINK_GEMSTONE_HELMET)
                .add(ModItems.PINK_GEMSTONE_CHESTPLATE)
                .add(ModItems.PINK_GEMSTONE_LEGGING)
                .add(ModItems.PINK_GEMSTONE_BOOST);
        // 工具维修物品标签
        getOrCreateTagBuilder(ModTags.Items.PINK_GEMSTONE_REPAIR)
                .add(ModItems.PINK_GEMSTONE);
        // 添加盔甲纹饰模板
        getOrCreateTagBuilder(ItemTags.TRIM_MATERIALS).add(ModItems.PINK_GEMSTONE);
        getOrCreateTagBuilder(ItemTags.TRIM_TEMPLATES).add(ModItems.PINK_SMITHING_TEMPLATE);
        // 原木和树叶标签，防止树叶腐烂作用
        getOrCreateTagBuilder(ItemTags.COMPLETES_FIND_TREE_TUTORIAL)
                .add(ModBlocks.MAPLE_LEAVES.asItem())
                .add(ModBlocks.FROZEN_LEAVES.asItem())
                .add(ModBlocks.FIR_LEAVES.asItem());
        getOrCreateTagBuilder(ItemTags.LOGS)
                .add(ModBlocks.MAPLE_LOG.asItem())
                .add(ModBlocks.FIR_LOG.asItem());
        // 泥土标签，提供扩散效果
        getOrCreateTagBuilder(ItemTags.DIRT).add(ModBlocks.FROZEN_DIRT.asItem()).add(ModBlocks.FROST_GRASS_BLOCK.asItem());
    }
}
