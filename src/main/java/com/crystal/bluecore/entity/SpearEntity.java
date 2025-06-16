package com.crystal.bluecore.entity;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * @author Draylar
 * @see <a href="https://www.mcmod.cn/item/587118.html">Diamond Spear</a>
 */
public abstract class SpearEntity extends PersistentProjectileEntity {
    private static final TrackedData<Byte> LOYALTY = DataTracker.registerData(SpearEntity.class, TrackedDataHandlerRegistry.BYTE);
    private static final TrackedData<Boolean> ENCHANTED = DataTracker.registerData(SpearEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<ItemStack> STACK = DataTracker.registerData(SpearEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);
    public int returnTimer;
    private final ItemStack stack;
    private boolean dealtDamage;


    public SpearEntity(EntityType<? extends SpearEntity> entityType, World world) {
        super(entityType, world);
        stack = new ItemStack(Items.TRIDENT);
    }

    public SpearEntity(EntityType<? extends SpearEntity> entityType, World world, LivingEntity owner, ItemStack stack) {
        super(entityType, owner, world, stack, null);

        this.stack = stack.copy();
        dataTracker.set(LOYALTY, getLoyalty(stack));
        dataTracker.set(ENCHANTED, stack.hasGlint());
    }

    @Override
    public void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);

        builder.add(LOYALTY, (byte) 0);
        builder.add(ENCHANTED, false);
        builder.add(STACK, ItemStack.EMPTY);
    }

    @Override
    public void tick() {
        /* 表示矛是否已经攻击过实体或落地超过4游戏刻（0.2秒） */
        if (inGroundTime > 4) {
            // 如果此值为true，则此三叉戟不会与其他实体进行碰撞检测，三叉戟忠诚魔咒在这时才会生效
            dealtDamage = true;
        }

        /* 该弹射物的发射者 */
        Entity owner = getOwner();
        /* 数据追踪器：有附魔忠诚的弹射物 */
        int i = dataTracker.get(LOYALTY);
        if (i > 0 && (dealtDamage || isNoClip()) && owner != null) {
            /* 如果不是你的，则不会回收捡起 */
            if (!isOwnerAlive()) {
                if (!getWorld().isClient && pickupType == PersistentProjectileEntity.PickupPermission.ALLOWED) {
                    dropStack(asItemStack(), 0.1F);
                }

                discard();
            } else {
                setNoClip(true);
                Vec3d vec3d = owner.getEyePos().subtract(getPos());
                setPos(getX(), getY() + vec3d.y * 0.015D * (double) i, getZ());
                if (getWorld().isClient) {
                    lastRenderY = getY();
                }

                /* 三叉戟的攻击速度是1.1，需要0.95秒来恢复。 */
                double d = 0.05D * (double) i;
                setVelocity(getVelocity().multiply(0.95D).add(vec3d.normalize().multiply(d)));
                if (returnTimer == 0) {
                    playSound(SoundEvents.ITEM_TRIDENT_RETURN, 10.0F, 1.0F);
                }

                returnTimer++;
            }
        }

        super.tick();
    }

    @Override
    public ItemStack asItemStack() {
        return stack.copy();
    }

    /**
     * @return the {@link ItemStack} tracked by this {@link SpearEntity}
     */
    public ItemStack getStack() {
        return getWorld().isClient ? dataTracker.get(STACK) : stack;
    }

    private byte getLoyalty(ItemStack stack) {
        if (getWorld() instanceof ServerWorld serverWorld) {
            return (byte)MathHelper.clamp(EnchantmentHelper.getTridentReturnAcceleration(serverWorld, stack, this), 0, 127);
        }
        return 0;
    }

    @Override
    public void onEntityHit(EntityHitResult entityHitResult) {
        if (!getWorld().isClient()) return;
        // 目标实体
        Entity target = entityHitResult.getEntity();
        // 自己实体
        Entity owner = getOwner();
        float damage = 8.0F;

        // Calculate damage bonuses for enchantments (Sharpness, Bane, Smite, etc.)
        DamageSource damageSource = getDamageSources().trident(this, (owner == null ? this : owner));
        if (getWorld() instanceof ServerWorld world && getWeaponStack() != null) {
            damage = EnchantmentHelper.getDamage(world, getWeaponStack(), target, damageSource, damage);
        }


        dealtDamage = true;
        if (target.damage(damageSource, damage)) {
            if (target.getType() == EntityType.ENDERMAN) return;

            if (getWorld() instanceof ServerWorld world) {
                EnchantmentHelper.onTargetDamaged(world, target, damageSource, getWeaponStack());
            }

            /* 击中目标实体，且有击退效果 */
            if (target instanceof  LivingEntity livingEntity) {
                knockback(livingEntity, damageSource);
                onHit(livingEntity);
            }

            setVelocity(getVelocity().multiply(-0.01, -0.1, -0.01));
            playSound(SoundEvents.ITEM_TRIDENT_HIT, 1.0F, 1.0F);
        }
    }

    @Override
    protected void onBlockHitEnchantmentEffects(ServerWorld world, BlockHitResult blockHitResult, ItemStack weaponStack) {
        Vec3d vec3d = blockHitResult.getBlockPos().clampToWithin(blockHitResult.getPos());
        EnchantmentHelper.onHitBlock(
                world,
                weaponStack,
                getOwner() instanceof LivingEntity livingEntity ? livingEntity : null,
                this,
                null,
                vec3d,
                world.getBlockState(blockHitResult.getBlockPos()),
                item -> kill()
        );
    }

    /**
     * @param player 玩家
     * @return 尝试捡起矛
     */
    @Override
    protected boolean tryPickup(PlayerEntity player) {
        return super.tryPickup(player) || isNoClip() && isOwner(player) && player.getInventory().insertStack(asItemStack());
    }

    @Override
    public void onPlayerCollision(PlayerEntity player) {
        Entity entity = getOwner();
        if (entity == null || entity.getUuid() == player.getUuid()) {
            super.onPlayerCollision(player);
        }
    }

    @Override
    public void age() {
        int i = dataTracker.get(LOYALTY);
        if (pickupType != PersistentProjectileEntity.PickupPermission.ALLOWED || i <= 0) {
            super.age();
        }
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound tag) {
        super.readCustomDataFromNbt(tag);

        dealtDamage = tag.getBoolean("DealtDamage");
        dataTracker.set(LOYALTY, getLoyalty(stack));
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound tag) {
        super.writeCustomDataToNbt(tag);

        tag.putBoolean("DealtDamage", dealtDamage);
    }

    @Override
    public SoundEvent getHitSound() {
        return SoundEvents.ITEM_TRIDENT_HIT_GROUND;
    }

    @Override
    public ItemStack getWeaponStack() {
        return getItemStack();
    }

    /**
     * @return 水下发射速度，受到液体阻力
     */
    @Override
    public float getDragInWater() {
        return 0.99F;
    }

    /**
     * @param currentPosition 目前碰撞箱
     * @param nextPosition 下次碰撞箱
     * @return 设置获取实体碰撞箱
     */
    @Override
    public EntityHitResult getEntityCollision(Vec3d currentPosition, Vec3d nextPosition) {
        return dealtDamage ? null : super.getEntityCollision(currentPosition, nextPosition);
    }

    private boolean isOwnerAlive() {
        Entity owner = getOwner();
        if (owner != null && owner.isAlive()) {
            return !(owner instanceof ServerPlayerEntity) || !owner.isSpectator();
        } else {
            return false;
        }
    }
}
