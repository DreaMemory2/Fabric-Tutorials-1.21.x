package com.crystal.bluecore.api;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.effect.EnchantmentEffectTarget;
import net.minecraft.registry.RegistryKey;

import java.util.Collections;
import java.util.List;

public interface EnchantmentHandler {

    default List<EnchantmentEffectTarget> getEnchantmentTypes() {
        return Collections.emptyList();
    }

    default boolean isInvalid(RegistryKey<Enchantment> enchantment) {
        return false;
    }

    default boolean isExplicitlyValid(Enchantment enchantment) { return false; }
}
