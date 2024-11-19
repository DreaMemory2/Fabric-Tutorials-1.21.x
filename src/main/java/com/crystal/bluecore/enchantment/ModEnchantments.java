package com.crystal.bluecore.enchantment;

import com.crystal.bluecore.BlueCore;
import com.crystal.bluecore.enchantment.custom.LightningStrikerEnchantmentEffect;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.effect.EnchantmentEffectTarget;
import net.minecraft.item.Item;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.registry.tag.ItemTags;

public class ModEnchantments {

    public static final RegistryKey<Enchantment> LIGHTNING_STRIKER = RegistryKey.of(RegistryKeys.ENCHANTMENT, BlueCore.of("lightning_striker"));

    public static void bootstrap(Registerable<Enchantment> registerable) {
        RegistryEntryLookup<Enchantment> enchantments = registerable.getRegistryLookup(RegistryKeys.ENCHANTMENT);
        RegistryEntryLookup<Item> items = registerable.getRegistryLookup(RegistryKeys.ITEM);

        register(registerable, LIGHTNING_STRIKER, Enchantment.builder(Enchantment.definition(
                items.getOrThrow(ItemTags.WEAPON_ENCHANTABLE), // supportedItems：支持附魔物品
                items.getOrThrow(ItemTags.SWORD_ENCHANTABLE), // primaryItems：主要附魔物品
                5, // weight: 比重
                2, // maxLevel：最大附魔等级
                Enchantment.leveledCost(5, 7), // minCost：最小等级经验花费，基础为5，每个等级增加7
                Enchantment.leveledCost(25, 9), // maxCost：最大等级经验花费，基础为25，每个等级增加9
                2, // anvilCost：铁砧经验花费为2
                AttributeModifierSlot.MAINHAND // slots:附魔修饰类型：主手
        ))
                .exclusiveSet(enchantments.getOrThrow(EnchantmentTags.DAMAGE_EXCLUSIVE_SET)) // // exclusive_set/damage：不能共存的伤害增幅类魔咒
                .addEffect( // 添加附魔效果
                        EnchantmentEffectComponentTypes.POST_ATTACK, // 生效机制，当玩家攻击后时触发
                        EnchantmentEffectTarget.ATTACKER, // 附魔目标攻击者(主动攻击)
                        EnchantmentEffectTarget.VICTIM, // 附魔目标受害者(被动攻击)
                        new LightningStrikerEnchantmentEffect() // 生成闪电实体
                ));
    }

    /**
     *
     * @param registry 注册接口
     * @param key 注册类型
     * @param builder 注册构建
     */
    private static void register(Registerable<Enchantment> registry, RegistryKey<Enchantment> key, Enchantment.Builder builder) {
        registry.register(key, builder.build(key.getValue()));
    }
}
