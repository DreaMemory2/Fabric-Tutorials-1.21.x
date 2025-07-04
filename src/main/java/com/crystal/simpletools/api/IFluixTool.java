package com.crystal.simpletools.api;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;

/**
 * @author Crystal
 * @version 1.0
 * @since 1.0
 */
public interface IFluixTool extends IIntrinsicEnchantItem {
    default int getIntrinsicEnchantLevel(ItemStack stack, RegistryEntry<Enchantment> enchantment) {
        return enchantment.matchesKey(Enchantments.FORTUNE) ? 10 : 0;
    }
}
