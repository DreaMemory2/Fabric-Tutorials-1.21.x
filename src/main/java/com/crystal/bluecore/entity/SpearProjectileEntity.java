package com.crystal.bluecore.entity;

import com.crystal.bluecore.registry.ModBlockEntities;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
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
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

/**
 * @author Draylar
 * @see <a href="https://www.mcmod.cn/item/587118.html">Diamond Spear</a>
 */
public class SpearProjectileEntity extends PersistentProjectileEntity {
    private static final TrackedData<Byte> LOYALTY = DataTracker.registerData(SpearProjectileEntity.class, TrackedDataHandlerRegistry.BYTE);
    private static final TrackedData<Boolean> ENCHANTED = DataTracker.registerData(SpearProjectileEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<ItemStack> STACK = DataTracker.registerData(SpearProjectileEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);
    public int returnTimer;
    private ItemStack stack = ItemStack.EMPTY;
    private boolean dealtDamage;


    public SpearProjectileEntity(EntityType<? extends SpearProjectileEntity> entityType, World world) {
        super(entityType, world);
        this.stack = new ItemStack(Items.TRIDENT);
    }

    public SpearProjectileEntity(World world, LivingEntity owner, ItemStack stack) {
        super(ModBlockEntities.SPEAR, owner, world, stack, null);
        this.stack = stack.copy();

        this.dataTracker.set(LOYALTY, this.getLoyalty(stack));
        this.dataTracker.set(ENCHANTED, stack.hasGlint());
        this.dataTracker.set(STACK, stack);
    }

    @Environment(EnvType.CLIENT)
    public SpearProjectileEntity(World world, double x, double y, double z, ItemStack stack) {
        super(ModBlockEntities.SPEAR, x, y, z, world, stack, stack);

        this.updatePosition(x, y, z);
        this.updateTrackedPosition(x, y, z);
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
        if (this.inGroundTime > 4) {
            this.dealtDamage = true;
        }

        Entity entity = this.getOwner();
        if ((this.dealtDamage || this.isNoClip()) && entity != null) {
            int i = this.dataTracker.get(LOYALTY);
            if (i > 0 && !this.isOwnerAlive()) {
                if (!this.getWorld().isClient && this.pickupType == PersistentProjectileEntity.PickupPermission.ALLOWED) {
                    this.dropStack(this.asItemStack(), 0.1F);
                }

                this.remove(RemovalReason.DISCARDED);
            } else if (i > 0) {
                this.setNoClip(true);
                Vec3d vec3d = new Vec3d(entity.getX() - this.getX(), entity.getEyeY() - this.getY(), entity.getZ() - this.getZ());
                this.setPos(this.getX(), this.getY() + vec3d.y * 0.015D * (double) i, this.getZ());
                if (this.getWorld().isClient) {
                    this.lastRenderY = this.getY();
                }

                double d = 0.05D * (double) i;
                this.setVelocity(this.getVelocity().multiply(0.95D).add(vec3d.normalize().multiply(d)));
                if (this.returnTimer == 0) {
                    this.playSound(SoundEvents.ITEM_TRIDENT_RETURN, 10.0F, 1.0F);
                }

                ++this.returnTimer;
            }
        }

        super.tick();
    }

    private boolean isOwnerAlive() {
        Entity entity = this.getOwner();
        if (entity != null && entity.isAlive()) {
            return !(entity instanceof ServerPlayerEntity) || !entity.isSpectator();
        } else {
            return false;
        }
    }

    @Override
    public ItemStack asItemStack() {
        return this.stack.copy();
    }

    /**
     * @return the {@link ItemStack} tracked by this {@link SpearProjectileEntity}
     */
    public ItemStack getStack() {
        return getWorld().isClient ? dataTracker.get(STACK) : stack;
    }

    @Environment(EnvType.CLIENT)
    public boolean isEnchanted() {
        return this.dataTracker.get(ENCHANTED);
    }

    @Override
    public EntityHitResult getEntityCollision(Vec3d currentPosition, Vec3d nextPosition) {
        return this.dealtDamage ? null : super.getEntityCollision(currentPosition, nextPosition);
    }

    @Override
    public void onEntityHit(EntityHitResult entityHitResult) {
        if (!this.getWorld().isClient()) {
            return;
        }
        Entity target = entityHitResult.getEntity();
        float damage = 8.0F;

        // Calculate damage bonuses for enchantments (Sharpness, Bane, Smite, etc.)
        if (target instanceof LivingEntity) {
            // damage += EnchantmentHelper.getAttackDamage(this.stack, livingEntity.getGroup());
            damage += EnchantmentHelper.getItemDamage((ServerWorld) this.getWorld(), this.stack, 8);
        }

        Entity spearOwner = this.getOwner();
        DamageSource damageSource = getDamageSources().trident(this, (spearOwner == null ? this : spearOwner));
        this.dealtDamage = true;
        SoundEvent hitSound = SoundEvents.ITEM_TRIDENT_HIT;
        if (target.damage(damageSource, damage)) {
            if (target.getType() == EntityType.ENDERMAN) {
                return;
            }

            if (target instanceof LivingEntity livingTarget) {
                if (spearOwner instanceof LivingEntity) {
                    // EnchantmentHelper.onUserDamage(livingTarget, spearOwner);
                    EnchantmentHelper.onTargetDamaged((ServerWorld) this.getWorld(), target, damageSource, this.getWeaponStack());
                    // EnchantmentHelper.onTargetDamage(spearOwner, livingTarget);
                }

                this.onHit(livingTarget);
            }
        }

        // apply a fire aspect to targets if valid
        // int fireAspectLevel = EnchantmentHelper.getLevel(Enchantments.FIRE_ASPECT, stack);
        target.setOnFireFor(2 * 40);

        setVelocity(getVelocity().multiply(-0.01D, 0.1D, -0.01D));
        float impactVolume = 1.0F;

        // Handle channeling if the channeling effect summons lighting.
        // We adjust the spear's collision-hit sound to the trident thunder SFX.
        if (getWorld() instanceof ServerWorld && getWorld().isThundering() && EnchantmentHelper.canHaveEnchantments(this.stack)) {
            BlockPos blockPos = target.getBlockPos();
            if (getWorld().isSkyVisible(blockPos)) {
                @Nullable LightningEntity lightning = EntityType.LIGHTNING_BOLT.create(getWorld());
                if (lightning != null) {
                    lightning.refreshPositionAfterTeleport(Vec3d.ofBottomCenter(blockPos));
                    lightning.setChanneler(spearOwner instanceof ServerPlayerEntity ? (ServerPlayerEntity) spearOwner : null);
                    getWorld().spawnEntity(lightning);
                    hitSound = SoundEvents.ITEM_TRIDENT_THUNDER.value();
                    impactVolume = 5.0F;
                }
            }
        }

        playSound(hitSound, impactVolume, 1.0F);
    }

    @Override
    public SoundEvent getHitSound() {
        return SoundEvents.ITEM_TRIDENT_HIT_GROUND;
    }

    @Override
    public void onPlayerCollision(PlayerEntity player) {
        Entity entity = this.getOwner();
        if (entity == null || entity.getUuid() == player.getUuid()) {
            super.onPlayerCollision(player);
        }
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound tag) {
        super.readCustomDataFromNbt(tag);

        this.dealtDamage = tag.getBoolean("DealtDamage");
        this.dataTracker.set(LOYALTY, this.getLoyalty(this.stack));
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound tag) {
        super.writeCustomDataToNbt(tag);

        // tag.put("Stack", this.stack.writeNbt(new NbtCompound()));
        tag.putBoolean("DealtDamage", this.dealtDamage);
    }

    private byte getLoyalty(ItemStack stack) {
        return this.getWorld() instanceof ServerWorld serverWorld
                ? (byte) MathHelper.clamp(EnchantmentHelper.getTridentReturnAcceleration(serverWorld, stack, this), 0, 127)
                : 0;
    }

    @Override
    public void age() {
        int i = this.dataTracker.get(LOYALTY);
        if (this.pickupType != PersistentProjectileEntity.PickupPermission.ALLOWED || i <= 0) {
            super.age();
        }

    }

    @Override
    public float getDragInWater() {
        return 0.99F;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public boolean shouldRender(double cameraX, double cameraY, double cameraZ) {
        return true;
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return new ItemStack(Items.TRIDENT);
    }
}
