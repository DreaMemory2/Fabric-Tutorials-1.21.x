package com.crystal.bluecore.registry;

import com.crystal.bluecore.BlueCore;
import com.crystal.bluecore.effect.SlimeEffects;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;

public class ModEffects {

    public static final RegistryEntry<StatusEffect> SLIME = registerEffects("slime", new SlimeEffects(StatusEffectCategory.NEUTRAL, 0x36EBAB)
            // 影响实体的移动速度，添加粘稠效果，并玩家速度降低25%，最后数值相乘在求和
            .addAttributeModifier(EntityAttributes.MOVEMENT_SPEED, BlueCore.of("slime"), -0.25F, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

    /**
     * <p>注册效果</p>
     * <p>不是register()方法，而是registerReference()方法</p>
     * @param name 效果的ID
     * @param statusEffect 效果状态图标
     * @return 注册效果
     */
    private static RegistryEntry<StatusEffect> registerEffects(String name, StatusEffect statusEffect) {
        return Registry.registerReference(Registries.STATUS_EFFECT, BlueCore.of(name), statusEffect);
    }

    public static void registerEffectsInfo() {
        BlueCore.LOGGER.info("Register Mod Effects for" + BlueCore.MOD_ID);
    }
}
