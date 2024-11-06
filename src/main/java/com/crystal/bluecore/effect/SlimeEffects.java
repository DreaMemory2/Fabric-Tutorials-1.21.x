package com.crystal.bluecore.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.math.Vec3d;

/**
 * <p>Climbing Effect by SameDifferent：<a href="https://github.com/samedifferent/TrickOrTreat/blob/master/LICENSE">LICENSE</a></p>
 * <p>MIT License！</p>
 * @author Modding By Kaupenjoe
 */
public class SlimeEffects extends StatusEffect {

    public SlimeEffects(StatusEffectCategory category, int color) {
        super(category, color);
    }

    /**
     * 是否应用效果
     * @param entity 玩家或者生物
     * @param amplifier 放大器
     * @return 是否应用效果
     */
    @Override
    public boolean applyUpdateEffect(LivingEntity entity, int amplifier) {
        // 如果实体有水平碰撞，如果真，则玩家正在与墙面相碰
        if (entity.horizontalCollision) {
            // 获取玩家初速度
            Vec3d initialVelocity = entity.getVelocity();
            // 获取玩家攀爬向量，将纵轴沿向量方向向上
            Vec3d climbVec = new Vec3d(initialVelocity.x, 0.2D, initialVelocity.z);
            // 玩家向上攀升，速度要乘以96
            entity.setVelocity(climbVec.multiply(0.96D));
            return true;
        }
        return super.applyUpdateEffect(entity, amplifier);
    }

    /**
     * 是否可以应用效果
     * @param duration 效果期间
     * @param amplifier 放大器
     * @return 是否可以应用效果
     */
    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}
