package com.crystal.bluecore.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.MathHelper;

/**
 * 粉红色火焰粒子
 * @author <a href="https://www.youtube.com/@RomeoSnowblitz">RomeoSnowblitz</a>
 * @see net.minecraft.client.particle.FlameParticle FlameParticle 和
 * <a href="https://youtu.be/OT-z_eG16q4?si=TiF-FJYVJEO0BbM8">Custom Torches & Custom Particles</a>
 */
@Environment(EnvType.CLIENT)
public class PinkFlameParticle extends AbstractSlowingParticle {

    public PinkFlameParticle(ClientWorld clientWorld, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        super(clientWorld, x, y, z, velocityX, velocityY, velocityZ);
    }

    /**
     * {@return 这个粒子在渲染类别下的渲染方式}
     * <p>要了解每个粒子可用的属性和类型，请访问ParticleTextureSheet</p>
     */
    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
    }

    /**
     * <p>通过指定的增量移动这个粒子，重新定位边界框并调整移动以避免与世界的碰撞。</p>
     * @param dx the delta x to move this particle by
     * @param dy the delta y to move this particle by
     * @param dz the delta z to move this particle by
     */
    @Override
    public void move(double dx, double dy, double dz) {
        this.setBoundingBox(this.getBoundingBox().offset(dx, dy, dz));
        this.repositionFromBoundingBox();
    }

    /**
     * {@return 在 {@link #buildGeometry} 渲染时使用的这个粒子的绘制比例}
     */
    @Override
    public float getSize(float tickDelta) {
        float f = ((float) this.age + tickDelta) / (float) this.maxAge;
        return this.scale * (1.0F - f * f * 0.5F);
    }

    /**
     * {@return 这个粒子应该渲染的填充亮度级别}
     *
     * @see net.minecraft.client.render.LightmapTextureManager LightmapTextureManager
     */
    @Override
    public int getBrightness(float tint) {
        float f = ((float) this.age + tint) / (float) this.maxAge;
        f = MathHelper.clamp(f, 0.0F, 1.0F);
        int i = super.getBrightness(tint);
        int j = i & 0xFF;
        int k = i >> 16 & 0xFF;
        j += (int) (f * 15.0F * 16.0F);
        if (j > 240) {
            j = 240;
        }

        return j | k << 16;
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<SimpleParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        /**
         * @param simpleParticleType 简单粒子类型
         * @param clientWorld 世界
         * @param x X轴
         * @param y Y轴
         * @param z Z轴
         * @param velocityX X轴速率
         * @param velocityY Y轴速率
         * @param velocityZ Z轴速率
         * @return 创建粒子的方法
         */
        public Particle createParticle(SimpleParticleType simpleParticleType, ClientWorld clientWorld, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            PinkFlameParticle flameParticle = new PinkFlameParticle(clientWorld, x, y, z, velocityX, velocityY, velocityZ);
            flameParticle.setSprite(this.spriteProvider);
            return flameParticle;
        }
    }
}
