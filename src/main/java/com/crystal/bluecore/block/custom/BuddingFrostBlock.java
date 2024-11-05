package com.crystal.bluecore.block.custom;

import com.crystal.bluecore.registry.ModBlocks;
import net.minecraft.block.AmethystBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluids;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;

public class BuddingFrostBlock extends AmethystBlock {
    public BuddingFrostBlock(Settings settings) {
        super(settings);
    }

    /**
     * <p>晶体可以生长在空气或者等级为8的静态水里</p>
     * @param state 方块状态
     * @return 可以生长
     */
    public static boolean canGrowIn(BlockState state) {
        return state.isAir() || state.isOf(Blocks.WATER) && state.getFluidState().getLevel() == 8;
    }

    @Override
    protected void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        // 随机生长状态：0~5
        if (random.nextInt(5) == 0) {
            // 生长方向四面八方
            Direction direction = DIRECTIONS[random.nextInt(DIRECTIONS.length)];
            // 方块位置和状态
            BlockPos blockPos = pos.offset(direction);
            BlockState blockState = world.getBlockState(blockPos);
            Block block = null;
            // 可以生长，则冒出晶体芽，并开始生长
            if (canGrowIn(blockState))
                block = ModBlocks.SMALL_FROST_BUD;
            else if (blockState.isOf(ModBlocks.SMALL_FROST_BUD) && blockState.get(FrostClusterBlock.FACING) == direction)
                block = ModBlocks.MEDIUM_FROST_BUD;
            else if (blockState.isOf(ModBlocks.MEDIUM_FROST_BUD) && blockState.get(FrostClusterBlock.FACING) == direction)
                block = ModBlocks.LARGE_FROST_BUD;
            else if (blockState.isOf(ModBlocks.LARGE_FROST_BUD) && blockState.get(FrostClusterBlock.FACING) == direction)
                block = ModBlocks.FROST_CLUSTER;
            // 如果方块为寒霜晶簇，则停止生长
            if (block != null) {
                BlockState frostCluster = block.getDefaultState()
                        .with(FrostClusterBlock.FACING, direction)
                        .with(FrostClusterBlock.WATERLOGGED, blockState.getFluidState().getFluid() == Fluids.WATER);
                world.setBlockState(blockPos, frostCluster);
            }
        }
    }
}
