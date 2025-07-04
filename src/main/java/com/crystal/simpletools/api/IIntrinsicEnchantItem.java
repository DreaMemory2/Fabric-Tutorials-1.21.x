package com.crystal.simpletools.api;

import com.crystal.simpletools.mixin.EnchantmentHelperMixin;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;

/**
* 用于实现物品的初始附魔，继承此接口即可生效。虽然看起来是“初始”的附魔，<br>
* 但实际上物品并没有真的拥有附魔，而是在MC尝试获取附魔的时候手动返回<br>
* <br>
* 如果通过其他方式获得了与初始附魔相同类型的附魔，则初始附魔会失效。如：<br>
* 初始时运1 + 铁砧附魔的时运1 = 时运1<br>
* ClassName: I Intrinsic Enchant Item<br>
* Datetime: 2025/6/4 20:54<br>
* @author Crystal
* @version 1.0
* @since 1.0
* @see EnchantmentHelperMixin
* @see net.minecraft.enchantment.EnchantmentHelper#getLevel(RegistryEntry, ItemStack)
*/
public interface IIntrinsicEnchantItem {
    int getIntrinsicEnchantLevel(ItemStack stack, RegistryEntry<Enchantment> enchantment);
}
