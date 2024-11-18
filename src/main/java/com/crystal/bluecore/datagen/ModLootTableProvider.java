package com.crystal.bluecore.datagen;

import com.crystal.bluecore.registry.ModBlocks;
import com.crystal.bluecore.registry.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LeafEntry;
import net.minecraft.loot.function.ApplyBonusLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModLootTableProvider extends FabricBlockLootTableProvider {
    public ModLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        addDrop(ModBlocks.PINK_GEMSTONE_BLOCK);
        addDrop(ModBlocks.RAW_PINK_GEMSTONE_BLOCK);
        addDrop(ModBlocks.MAGIC_BLOCK);

        addDrop(ModBlocks.PINK_GEMSTONE_ORE, oreDrops(ModBlocks.PINK_GEMSTONE_ORE, ModItems.RAW_PINK_GEMSTONE));
        addDrop(ModBlocks.DEEPSLATE_PINK_GEMSTONE_ORE, multipleOreDrops(ModBlocks.DEEPSLATE_PINK_GEMSTONE_ORE, ModItems.RAW_PINK_GEMSTONE, 3, 7));
        // 添加装饰物挖掘后的掉落物，例如：楼梯，半砖，按钮，压力板，栅栏，栅栏门，石墙，铁门，活把门
        addDrop(ModBlocks.PINK_GEMSTONE_STAIRS);
        // 有时一个半砖占据一格，挖掘后只掉落一个半砖；有时两个半砖合二为一，挖掘后掉落两个半砖
        addDrop(ModBlocks.PINK_GEMSTONE_SLAB, slabDrops(ModBlocks.PINK_GEMSTONE_SLAB));
        addDrop(ModBlocks.PINK_GEMSTONE_BLOCK);
        addDrop(ModBlocks.PINK_GEMSTONE_PRESSURE_PLATE);
        addDrop(ModBlocks.PINK_GEMSTONE_FENCE);
        addDrop(ModBlocks.PINK_GEMSTONE_FENCE_GATE);
        addDrop(ModBlocks.PINK_GEMSTONE_WALL);
        // 粉红色宝石门：占据两格高的位置，挖掘上部分或下部分，都可以掉落粉红色宝石门（物品）
        addDrop(ModBlocks.PINK_GEMSTONE_DOOR, doorDrops(ModBlocks.PINK_GEMSTONE_DOOR));
        addDrop(ModBlocks.PINK_GEMSTONE_TRAPDOOR);

        addDrop(ModBlocks.PINK_TORCH);
    }

    // 深板岩矿石配方
    public LootTable.Builder multipleOreDrops(Block drop, Item item, float minDrop, float maxDrop) {
        RegistryWrapper.Impl<Enchantment> impl = this.registries.getOrThrow(RegistryKeys.ENCHANTMENT);
        return this.dropsWithSilkTouch(drop, this.applyExplosionDecay(drop, ((LeafEntry.Builder<?>)
                ItemEntry.builder(item).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(minDrop, maxDrop))))
                .apply(ApplyBonusLootFunction.oreDrops(impl.getOrThrow(Enchantments.FORTUNE)))));
    }
}
