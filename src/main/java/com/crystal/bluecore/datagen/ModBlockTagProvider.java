package com.crystal.bluecore.datagen;

import com.crystal.bluecore.registry.ModBlocks;
import com.crystal.bluecore.util.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider.BlockTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends BlockTagProvider {

    public ModBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryFuture) {
        super(output, registryFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        // 添加一些需要镐子来挖掘的方块标签
        getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)
                .add(ModBlocks.PINK_GEMSTONE_BLOCK)
                .add(ModBlocks.PINK_GEMSTONE_ORE)
                .add(ModBlocks.RAW_PINK_GEMSTONE_BLOCK)
                .add(ModBlocks.DEEPSLATE_PINK_GEMSTONE_ORE)
                .add(ModBlocks.MAGIC_BLOCK)
                .add(ModBlocks.OAK_CHEST);
        // 添加一些需要钻石稿来挖掘的方块标签
        getOrCreateTagBuilder(BlockTags.NEEDS_DIAMOND_TOOL).add(ModBlocks.RAW_PINK_GEMSTONE_BLOCK);
        // 添加一些装饰物标签，例如：栅栏、栅栏门和石墙标签
        getOrCreateTagBuilder(BlockTags.FENCES).add(ModBlocks.PINK_GEMSTONE_FENCE);
        getOrCreateTagBuilder(BlockTags.FENCE_GATES).add(ModBlocks.PINK_GEMSTONE_FENCE_GATE);
        getOrCreateTagBuilder(BlockTags.WALLS).add(ModBlocks.PINK_GEMSTONE_WALL);
        // 添加粉红色宝石武器和工具
        getOrCreateTagBuilder(BlockTags.NEEDS_DIAMOND_TOOL).add(ModBlocks.MAGIC_BLOCK);
        getOrCreateTagBuilder(ModTags.Blocks.NEEDS_PINK_GEMSTONE).addTag(BlockTags.NEEDS_DIAMOND_TOOL);
        // 添加箱子
        getOrCreateTagBuilder(BlockTags.LAVA_POOL_STONE_CANNOT_REPLACE).add(ModBlocks.OAK_CHEST);
        getOrCreateTagBuilder(BlockTags.FEATURES_CANNOT_REPLACE).add(ModBlocks.OAK_CHEST);
        // 原木和树叶标签，防止树叶腐烂作用
        getOrCreateTagBuilder(BlockTags.LEAVES).add(ModBlocks.MAPLE_LEAVES);
        getOrCreateTagBuilder(BlockTags.LOGS).add(ModBlocks.MAPLE_LOG);
    }
}
