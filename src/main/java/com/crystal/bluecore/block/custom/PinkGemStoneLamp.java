package com.crystal.bluecore.block.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PinkGemStoneLamp extends Block {
    /**
     * 添加粉红色宝石灯的方块属性（方块状态）
     */
    public static final BooleanProperty CLICKED = BooleanProperty.of("clicked");

    public PinkGemStoneLamp(Settings settings) {
        super(settings);
        // 设置粉红色宝石灯初始未点亮（False）
        setDefaultState(this.getDefaultState().with(CLICKED, false));
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        // 右键点击方块可以点亮粉红色宝石灯
        if (!world.isClient()) {
            world.setBlockState(pos, state.cycle(CLICKED));
        }
        return ActionResult.SUCCESS;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        // 添加属性
        builder.add(CLICKED);
    }
}
