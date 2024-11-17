package com.crystal.bluecore.api;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.effect.EnchantmentEffectTarget;
import net.minecraft.registry.RegistryKey;

import java.util.Collections;
import java.util.List;

/**
 * 附魔处理器程序接口
 *
 * @author Draylar
 * @see <a href="https://www.mcmod.cn/class/6337.html">王之财宝</a>，
 * 接口：<a href="https://github.com/Draylar/gate-of-babylon/blob/1.19.2/src/main/java/draylar/gateofbabylon/api/EnchantmentHandler.java">EnchantmentHandler</a>
 */
public interface EnchantmentHandler {

    /**
     * @return 获取附魔类型
     */
    default List<EnchantmentEffectTarget> getEnchantmentTypes() {
        return Collections.emptyList();
    }

    /**
     * 判断附魔类型是否无效
     *
     * @param enchantment 附魔
     * @return 是否附魔类型无效
     */
    default boolean isInvalid(RegistryKey<Enchantment> enchantment) {
        return false;
    }

    /**
     * 判断附魔类型是否有效
     *
     * @param enchantment 附魔
     * @return 是否附魔类型有效
     */
    default boolean isExplicitlyValid(Enchantment enchantment) {
        return false;
    }
}
