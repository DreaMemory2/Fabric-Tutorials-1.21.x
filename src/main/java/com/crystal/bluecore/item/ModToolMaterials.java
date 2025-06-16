package com.crystal.bluecore.item;

import com.crystal.bluecore.registry.ModItems;
import com.crystal.bluecore.registry.ModTags;
import com.google.common.base.Suppliers;
import net.minecraft.block.Block;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.TagKey;

import java.util.function.Supplier;

// 工具与武器
public enum ModToolMaterials implements ToolMaterial {
    PINK_GEMSTONE(ModTags.Blocks.INCORRECT_FOR_PINK_GEMSTONE_TOOL, 1200, 5.0F, 4.0F, 22, () -> Ingredient.ofItems(ModItems.PINK_GEMSTONE));

    // 反向标签
    private final TagKey<Block> inverseTag;
    // 物品的耐久值
    private final int itemDurability;
    // 最小攻击速度
    private final float miningSpeed;
    // 攻击伤害
    private final float attackDamage;
    // 附魔能力
    private final int enchantability;
    // 维修物品
    private final Supplier<Ingredient> repairIngredient;

    /**
     * <p>itemDurability：使用这个材料做的工具全都设置固定的耐久值</p>
     * @param inverseTag 反向标签
     * @param itemDurability 物品的耐久值
     * @param miningSpeed 最小攻击速度
     * @param attackDamage 攻击伤害
     * @param enchantability 附魔能力
     * @param repairIngredient 维修物品
     */
    ModToolMaterials(final TagKey<Block> inverseTag, final int itemDurability, final float miningSpeed, final float attackDamage, final int enchantability, final Supplier<Ingredient> repairIngredient){
        this.inverseTag = inverseTag;
        this.itemDurability = itemDurability;
        this.miningSpeed = miningSpeed;
        this.attackDamage = attackDamage;
        this.enchantability = enchantability;
        this.repairIngredient = Suppliers.memoize(repairIngredient::get);
    }

    @Override
    public int getDurability() {
        return itemDurability;
    }

    @Override
    public float getMiningSpeedMultiplier() {
        return miningSpeed;
    }

    @Override
    public float getAttackDamage() {
        return attackDamage;
    }

    @Override
    public TagKey<Block> getInverseTag() {
        return inverseTag;
    }

    @Override
    public int getEnchantability() {
        return enchantability;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return repairIngredient.get();
    }
}
