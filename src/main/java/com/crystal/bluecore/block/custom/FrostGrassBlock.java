package com.crystal.bluecore.block.custom;

import com.crystal.bluecore.registry.ModBlocks;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SnowBlock;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldView;
import net.minecraft.world.chunk.light.ChunkLightProvider;

/**
 * @see net.minecraft.block.GrassBlock GrassBlock
 * @author Crystal
 */
public class FrostGrassBlock extends Block {
    private static final MapCodec<FrostGrassBlock> CODEC = createCodec(FrostGrassBlock::new);

    public FrostGrassBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends Block> getCodec() {
        return CODEC;
    }

    private static boolean canSpread(BlockState state, WorldView world, BlockPos pos) {
        BlockPos blockPos = pos.up();
        // 是否可以进行传播
        return canSurvive(state, world, pos) && !world.getFluidState(blockPos).isIn(FluidTags.WATER);
    }

    private static boolean canSurvive(BlockState state, WorldView world, BlockPos pos) {
        // 获取方块的位置和状态
        BlockPos blockPos = pos.up();
        BlockState blockState = world.getBlockState(blockPos);
        // 如果方块上方有一层雪，则可以传播，如果液体等级为8，则不可以传播，
        if (blockState.isOf(Blocks.SNOW) && blockState.get(SnowBlock.LAYERS) == 1) {
            return true;
        } else if (blockState.getFluidState().getLevel() == 8) {
            // 当水方块等级为8时，则返回false，如果水方块等级不为8时，则返回true
            return false;
        } else {
            // 获取区块亮度等级，如果区块小于最大亮度等级，则返回true
            int i = ChunkLightProvider.getRealisticOpacity(state, blockState, Direction.UP, blockState.getOpacity());
            return i < 15;
        }
    }

    @Override
    protected void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        // 判断是否满足传播条件
        if (!canSurvive(state, world, pos)) {
            world.setBlockState(pos, ModBlocks.FROZEN_DIRT.getDefaultState());
        } else {
            if (world.getLightLevel(pos.up()) >= 9) {
                // 判断世界亮度等级是否小于9
                BlockState blockState = this.getDefaultState();

                for (int i = 0; i < 4; i++) {
                    // 得到随机刻时，草方块会以下方的方块于为扩散中心，在该中心3×5×3的范围内随机挑三次方块尝试传播
                    BlockPos blockPos = pos.add(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);
                    if (world.getBlockState(blockPos).isOf(ModBlocks.FROZEN_DIRT) && canSpread(blockState, world, blockPos)) {
                        // 如果可以传播对应的泥土，则设置方块状态为草方块
                        world.setBlockState(blockPos, blockState);
                    }
                }
            }
        }
    }
}
