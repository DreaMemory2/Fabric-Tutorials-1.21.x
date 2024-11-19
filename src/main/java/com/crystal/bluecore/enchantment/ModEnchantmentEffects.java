package com.crystal.bluecore.enchantment;

import com.crystal.bluecore.BlueCore;
import com.crystal.bluecore.enchantment.custom.LightningStrikerEnchantmentEffect;
import com.mojang.serialization.MapCodec;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModEnchantmentEffects {
    // 闪电附魔效果
    public static final MapCodec<? extends EnchantmentEntityEffect> LIGHTNING_STRIKER = registerEntityEffect("lightning_striker", LightningStrikerEnchantmentEffect.CODEC);

    /**
     * <p>用于注册附魔的方法</p>
     * @param name 附魔效果命名空间
     * @param codec 编码器
     * @return 注册附魔实体效果类型注册器
     */
    private static MapCodec<? extends EnchantmentEntityEffect> registerEntityEffect(String name, MapCodec<? extends EnchantmentEntityEffect> codec) {
        return Registry.register(Registries.ENCHANTMENT_ENTITY_EFFECT_TYPE, BlueCore.of(name), codec);
    }

    public static void registerEntityEffectInfo() {
        BlueCore.LOGGER.info("Registering Mod Enchantment Effects for " + BlueCore.MOD_ID);
    }
}
