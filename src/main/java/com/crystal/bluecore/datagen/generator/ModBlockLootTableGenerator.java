package com.crystal.bluecore.datagen.generator;

import com.crystal.bluecore.block.custom.HoneyBerryBushBlock;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.BlockStatePropertyLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.ApplyBonusLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.predicate.StatePredicate;
import net.minecraft.registry.entry.RegistryEntry;

/**
 * @author Crystal
 * @version 1.0
 * @since 1.0
 */
public class ModBlockLootTableGenerator {
    public static LootTable.Builder honeyBerryBuilder(Block block, Item item, RegistryEntry<Enchantment> enchantment) {
        return new LootTable.Builder()
                .pool(LootPool.builder()
                        .conditionally(BlockStatePropertyLootCondition.builder(block).properties(
                                StatePredicate.Builder.create().exactMatch(HoneyBerryBushBlock.AGE, 3)))
                        .with(ItemEntry.builder(item))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0F, 3.0F)))
                        .apply(ApplyBonusLootFunction.uniformBonusCount(enchantment)))
                .pool(LootPool.builder()
                        .conditionally(BlockStatePropertyLootCondition.builder(block).properties(
                                StatePredicate.Builder.create().exactMatch(HoneyBerryBushBlock.AGE, 2)))
                        .with(ItemEntry.builder(item))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 2.0F)))
                        .apply(ApplyBonusLootFunction.uniformBonusCount(enchantment)));
    }
}
