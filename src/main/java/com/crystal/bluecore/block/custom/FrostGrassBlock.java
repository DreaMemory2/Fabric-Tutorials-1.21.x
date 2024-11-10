package com.crystal.bluecore.block.custom;

import com.crystal.bluecore.registry.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldView;

public class FrostGrassBlock extends Block {

    public FrostGrassBlock(Settings settings) {
        super(settings);
    }

    private static boolean canSpread(WorldView world, BlockPos pos) {
        BlockPos blockPos = pos.up();
        return canSurvive(world, pos) && !world.getFluidState(blockPos).isIn(FluidTags.WATER);
    }

    private static boolean canSurvive(WorldView world, BlockPos pos) {
        // 获取方块的位置和状态
        BlockPos blockPos = pos.up();
        BlockState blockState = world.getBlockState(blockPos);
        // 如果方块上方有一层雪，则可以传播，如果液体等级为8，则不可以传播，
        if (blockState.isOf(Blocks.SNOW)) return true;
        // 当水方块等级为8时，则返回false，如果水方块等级不为8时，则返回true
        else return blockState.getFluidState().getLevel() != 8;
    }

    @Override
    protected void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        // 判断是否满足传播条件
        if (!canSurvive(world, pos)) {
            world.setBlockState(pos, ModBlocks.FROZEN_DIRT.getDefaultState());
        } else {
            BlockState blockState = this.getDefaultState();
            for (int i = 0; i < 4; i++) {
                // 得到随机刻时，草方块会以下方的方块于为扩散中心，在该中心3×5×3的范围内随机挑三次方块尝试传播
                pos.add(random.nextInt() - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);
                if (world.getBlockState(pos).isOf(Blocks.DIRT) && canSpread(world, pos)) {
                    world.setBlockState(pos, blockState.with(Properties.SNOWY, world.getBlockState(pos.up()).isOf(Blocks.SNOW)));
                }
            }
        }
    }
}
