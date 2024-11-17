package com.crystal.bluecore.registry;

import com.crystal.bluecore.BlueCore;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;

public class ModPotions {

    public static final RegistryEntry<Potion> SLIME_POTION = registerPotions("slime_potion",
            // 饮用后的效果：粘稠，时间1200游戏刻，等级为I
            new Potion(new StatusEffectInstance(ModEffects.SLIME, 1200, 0)));

    /**
     * 用于注册药水的方法
     * @param name 药水命名空间
     * @param potion 药水效果
     * @return 注册药水的方法
     */
    private static RegistryEntry<Potion> registerPotions(String name, Potion potion) {
        return Registry.registerReference(Registries.POTION, BlueCore.of(name), potion);
    }

    public static void registerPotionInfo() {
        BlueCore.LOGGER.info("Registering Mod Potions for " + BlueCore.MOD_ID);
    }
}
