package com.crystal.bluecore.util;

import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.ToolMaterials;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.Set;

@SuppressWarnings("DuplicatedCode")
public interface IWeaponStats {

    static MeleeStats meleeStats(
            boolean isEnabled,
            boolean isLootable,
            ToolMaterial material,
            int damage,
            float attackSpeed,
            double additionalAttackRange,
            int weaponSpawnWeight,
            Identifier repairIngredient)
    {
        MeleeStats stats = new MeleeStats();
        stats.isEnabled = isEnabled;
        stats.isLootable = isLootable;
        stats.material = material;
        stats.damage = damage;
        return stats;
    }

    static MeleeStats meleeStats(
            boolean isEnabled,
            boolean isLootable,
            ToolMaterial material,
            int damage,
            float attackSpeed,
            double additionalAttackRange,
            int weaponSpawnWeight,
            Set<Object> repairIngredients)
    {
        MeleeStats stats = new MeleeStats();
        stats.isEnabled = isEnabled;
        stats.isLootable = isLootable;
        stats.material = material;
        stats.damage = damage;
        return stats;
    }

    static MeleeStats meleeStats(
            boolean isEnabled,
            boolean isLootable,
            ToolMaterial material,
            int damage,
            float attackSpeed,
            double additionalAttackRange,
            int weaponSpawnWeight,
            TagKey<Item> repairIngredientTag)
    {
        MeleeStats stats = new MeleeStats();
        stats.isEnabled = isEnabled;
        stats.isLootable = isLootable;
        stats.material = material;
        stats.damage = damage;
        return stats;
    }

    static RangedStats rangedStats(
            boolean isEnabled,
            boolean isLootable,
            ToolMaterial material,
            int projectileDamage,
            int drawSpeed,
            int range,
            int weaponSpawnWeight,
            Identifier repairIngredient)
    {
        RangedStats stats = new RangedStats();
        stats.isEnabled = isEnabled;
        stats.isLootable = isLootable;
        stats.material = material;
        return stats;
    }

    static RangedStats rangedStats(
            boolean isEnabled,
            boolean isLootable,
            ToolMaterial material,
            int projectileDamage,
            int drawSpeed,
            int range,
            int weaponSpawnWeight,
            Set<Object> repairIngredients)
    {
        RangedStats stats = new RangedStats();
        stats.isEnabled = isEnabled;
        stats.isLootable = isLootable;
        stats.material = material;
        return stats;
    }

    static RangedStats rangedStats(
            boolean isEnabled,
            boolean isLootable,
            ToolMaterial material,
            int projectileDamage,
            int drawSpeed,
            int range,
            int weaponSpawnWeight,
            TagKey<Item> repairIngredientTag)
    {
        RangedStats stats = new RangedStats();
        stats.isEnabled = isEnabled;
        stats.isLootable = isLootable;
        stats.material = material;
        return stats;
    }

    static ShieldStats shieldStats(
            boolean isEnabled,
            boolean isLootable,
            ToolMaterial material,
            int weaponSpawnWeight,
            Identifier repairIngredient)
    {
        ShieldStats stats = new ShieldStats();
        stats.isEnabled = isEnabled;
        stats.isLootable = isLootable;
        stats.material = material;
        stats.weaponSpawnWeight = weaponSpawnWeight;
        return stats;
    }

    static ShieldStats shieldStats(
            boolean isEnabled,
            boolean isLootable,
            ToolMaterial material,
            int weaponSpawnWeight,
            Set<Object> repairIngredients)
    {
        ShieldStats stats = new ShieldStats();
        stats.isEnabled = isEnabled;
        stats.isLootable = isLootable;
        stats.material = material;
        stats.weaponSpawnWeight = weaponSpawnWeight;
        return stats;
    }

    static ShieldStats shieldStats(
            boolean isEnabled,
            boolean isLootable,
            ToolMaterial material,
            int weaponSpawnWeight,
            TagKey<Item> repairIngredientTag)
    {
        ShieldStats stats = new ShieldStats();
        stats.isEnabled = isEnabled;
        stats.isLootable = isLootable;
        stats.material = material;
        stats.weaponSpawnWeight = weaponSpawnWeight;
        return stats;
    }

    class MeleeStats {
        public boolean isEnabled = true;
        public boolean isLootable = true;
        public ToolMaterial material = ToolMaterials.IRON;
        public int damage = 0;
    }

    class RangedStats {
        public boolean isEnabled = true;
        public boolean isLootable = true;
        public ToolMaterial material = ToolMaterials.IRON;
    }

    class ShieldStats {
        public boolean isEnabled = true;
        public boolean isLootable = true;
        public ToolMaterial material = ToolMaterials.IRON;
        public int weaponSpawnWeight = 0;
    }
}