package com.crystal.bluecore.block.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.conversion.EntityConversionContext;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ConversionTableBlock extends Block {

    public ConversionTableBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        // Instance Creeper strike lighting to make Charged Creeper
        // 实现苦力怕（爬行者）转换为高压苦力怕
        if (!world.isClient() && entity instanceof CreeperEntity creeper) {
            // 是否苦力怕用于闪电图层
            // 生物触发
            EntityType.LIGHTNING_BOLT.spawn((ServerWorld) world, pos, SpawnReason.TRIGGERED);
            // 恢复生命值
            creeper.heal(1000);
        }
        // Instance Skeleton convert to Wither Skeleton
        // 实现骷髅转换为凋零骷髅
        if (entity instanceof SkeletonEntity skeleton)
            skeleton.convertTo(EntityType.WITHER_SKELETON,
                    EntityConversionContext.create(skeleton, false, false),
                    convertedEntity -> {});
        // Instance Zombie setBaby
        // 实现僵尸转换为小僵尸
        if (entity instanceof ZombieEntity zombie) zombie.setBaby(true);
        // Instance Player kill
        // 实现清除玩家
        if (entity instanceof PlayerEntity player) player.kill((ServerWorld) world);

        super.onSteppedOn(world, pos, state, entity);
    }
}
