package com.crystal.bluecore.mixin;

import com.crystal.bluecore.registry.ModItems;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

// With help from https://github.com/Globox1997/MedievalWeapons/blob/1.21/src/main/java/net/medievalweapons/mixin/client/AbstractClientPlayerEntityMixin.java
// Under MIT License
@Mixin(AbstractClientPlayerEntity.class)
public abstract class AbstractClientPlayerEntityMixin extends PlayerEntity {
    public AbstractClientPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    /**
     * <p>因为实现拉弓操作是硬编码，所以使用Mixin调用并修改getFovMultiplier中的代码</p>
     */
    @Inject(method = "getFovMultiplier", at = @At(value = "TAIL"), locals = LocalCapture.CAPTURE_FAILSOFT, cancellable = true)
    private void getFovMultiplier(CallbackInfoReturnable<Float> info, float f) {
        ItemStack itemStack = this.getActiveItem();
        if (this.isUsingItem() && itemStack.isOf(ModItems.PINK_GEMSTONE_BOW)) {
            int i = this.getItemUseTime();
            float g = (float) i / 20.0f;
            g = g > 1.0f ? 1.0f : g * g;
            f *= 1.0f - g * 0.15f;
            info.setReturnValue(MathHelper.lerp(MinecraftClient.getInstance().options.getFovEffectScale().getValue().floatValue(), 1.0f, f));
        }
    }
}
