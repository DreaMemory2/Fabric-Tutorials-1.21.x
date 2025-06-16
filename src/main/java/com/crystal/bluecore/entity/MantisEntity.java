package com.crystal.bluecore.entity;

import com.crystal.bluecore.entity.custom.MantisVariant;
import com.crystal.bluecore.registry.ModEntity;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Util;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

/**
 * 螳螂实体, 继承动物实体(抽象类)
 * @author Crystal
 * @version 1.0
 * @since 1.0
 */
public class MantisEntity extends AnimalEntity {
    /* 设置变种实体数据信息：效果自动和客户端或服务端变种实体信息同步，不需要格外网络发包配置 */
    private static final TrackedData<Integer> DATA_ID_TYPE_VARIANT =
            DataTracker.registerData(MantisEntity.class, TrackedDataHandlerRegistry.INTEGER);
    /** 空闲动画(Idle Animation): 实体的非运动时间（空间）状态 */
    public AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeOut = 0;

    public MantisEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    /**
     * <p>GENERIC_MAX_HEALTH: Generic Max Health 最大生命值</p>
     * <p>GENERIC_MOVEMENT_SPEED: Generic Movement Speed 移动速度</p>
     * <p>GENERIC_ATTACK_DAMAGE: Generic Attack Damage 攻击伤害</p>
     * <p>GENERIC_FOLLOW_RANGE: Generic Follow Range 随机跟随范围</p>
     * <p>攻击伤害需要攻击目标的实现，请参考僵尸实体源代码</p>
     * <p>随机跟随范围：指的是已驯化实体，需要跟随时长，需要跟随的父级实体</p>
     * @return 创建默认实体属性
     */
    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 18)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 1)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 20);
    }

    /**
     * <p>初始化目标行为设置</p>
     * <p>priority: 优先级, 最高为零; 优先级越高，最先执行目标</p>
     * <p>先执行0目标，完成之后，再执行1目标，依此类推</p>
     * <p>goal: 目标, 选择执行的目标</p>
     */
    @Override
    public void initGoals() {
        // 游泳目标
        goalSelector.add(0, new SwimGoal(this));
        // 交配目标
        goalSelector.add(1, new AnimalMateGoal(this, 1.15D));
        // 诱惑目标(小麦物品)
        goalSelector.add(2, new TemptGoal(this, 1.25D, Ingredient.ofItems(Items.WHEAT), false));
        // 跟随父母目标
        goalSelector.add(3, new FollowParentGoal(this, 1.1D));
        // 徘徊目标
        goalSelector.add(4, new WanderAroundFarGoal(this, 1.0D));
        // 寻找实体目标(寻找玩家目标)
        goalSelector.add(5, new LookAtEntityGoal(this, PlayerEntity.class, 4.0F));
        // 寻找同类目标
        goalSelector.add(6, new LookAroundGoal(this));
    }

    /**
     * <p>动物交配繁育，生成幼年实体</p>
     * @param world 世界
     * @param other 其他动物实体
     */
    @Override
    public void breed(ServerWorld world, AnimalEntity other) {
        super.breed(world, other);
    }

    /**
     * <p>从零开始播放动画，每过40次记为一次循环</p>
     * <p>设置每40次循环播放空闲动画方法</p>
     * <p>由于空闲动画时长为2秒，换算成40tick</p>
     */
    private void setupAnimationStates() {
        if (idleAnimationTimeOut <= 0) {
            idleAnimationTimeOut = 40;
            idleAnimationState.start(age);
        } else {
            --idleAnimationTimeOut;
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (getWorld().isClient()) {
            // 设置空闲动画播放时长，一秒二十次 (20次/秒)
            setupAnimationStates();
        }
    }

    /**
     *
     * @param stack 物品(小麦)
     * @return 是否能够繁育下一代
     */
    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.isOf(Items.WHEAT);
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        /* 提供变种幼体实体 */
        MantisEntity baby = ModEntity.MANTIS.create(world);
        MantisVariant variant = Util.getRandom(MantisVariant.values(), random);
        if (baby != null) baby.setVariant(variant);
        // 生成幼体实体
        return baby;
    }

    /* VARIANT 变种实体 */

    /**
     * <p>初始化数据追踪，数据管理</p>
     * @param builder 构建
     */
    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(DATA_ID_TYPE_VARIANT, 0);
    }

    /**
     * {@return 获取变种实体}
     */
    public MantisVariant getVariant() {
        return MantisVariant.getVariant(getTypeVariant());
    }

    /**
     * <p>设计螳螂变体</p>
     * @param variant 螳螂变体
     */
    private void setVariant(MantisVariant variant) {
        dataTracker.set(DATA_ID_TYPE_VARIANT, variant.getId() & 255);
    }

    /**
     * {@return 获取变体类型，数据跟踪器}
     */
    private int getTypeVariant() {
        return dataTracker.get(DATA_ID_TYPE_VARIANT);
    }

    /* 实体NBT标签 */

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("Variant", getTypeVariant());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        dataTracker.set(DATA_ID_TYPE_VARIANT, nbt.getInt("Variant"));
    }

    /**
     * <p>初始化变种实体，随机生成变种实体</p>
     * @param world 世界
     * @param difficulty 生成阻碍
     * @param spawnReason 生成原因
     * @param entityData 实体数据
     * @return 实体数据
     */
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
        /* 设置随机实体螳螂变种 */
        MantisVariant variant = Util.getRandom(MantisVariant.values(), random);
        /* 设计、设置螳螂变种 */
        setVariant(variant);

        return super.initialize(world, difficulty, spawnReason, entityData);
    }
}
