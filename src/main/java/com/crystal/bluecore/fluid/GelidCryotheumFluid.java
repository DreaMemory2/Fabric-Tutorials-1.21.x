package com.crystal.bluecore.fluid;

import com.crystal.bluecore.registry.ModBlocks;
import com.crystal.bluecore.registry.ModFluids;
import com.crystal.bluecore.registry.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class GelidCryotheumFluid extends FlowableFluid {

    /**
     * @return 获取液体流动状态
     */
    @Override
    public Fluid getFlowing() {
        return ModFluids.FLOWING_GELID_CRYOTHEUM;
    }

    /**
     * @return 获取液体静止状态
     */
    @Override
    public Fluid getStill() {
        return ModFluids.GELID_CRYOTHEUM;
    }

    /**
     * @return 获取液体桶的物品形式
     */
    @Override
    public Item getBucketItem() {
        return ModItems.GELID_CRYOTHEUM_BUCKET;
    }

    /**
     * <p>设置液体水平方向流向最远距离为15，例如：Water</p>
     * @param state 液体状态
     * @return 设置液体状态值
     */
    @Override
    protected BlockState toBlockState(FluidState state) {
        return ModBlocks.GELID_CRYOTHEUM.getDefaultState().with(FluidBlock.LEVEL, getBlockStateLevel(state));
    }

    /**
     * <p>比较液体类型，判断是静态还是动态</p>
     * <p>设置液体可以流入那些液体中</p>
     * @param fluid 液体
     * @return 比较液体类型
     */
    @Override
    public boolean matchesType(Fluid fluid) {
        return fluid == ModFluids.GELID_CRYOTHEUM || fluid == ModFluids.FLOWING_GELID_CRYOTHEUM;
    }

    /**
     * <p>判断是否是液体无限的</p>
     * <p>例如：水体是无限的，岩浆是有限的</p>
     * @param world 世界
     * @return 判断是否是液体无限的
     */
    @Override
    protected boolean isInfinite(World world) {
        return false;
    }

    /**
     * <p>例如石灰乳滴下的水滴粒子效果</p>
     * <p>当玩家在液体走动时，生成水滴粒子效果</p>
     * <p>当玩家掉入液体时，生成水滴粒子效果</p>
     * @return 设置液体粒子效果
     */
    @Nullable
    @Override
    protected ParticleEffect getParticle() {
        return ParticleTypes.DRIPPING_WATER;
    }

    /**
     * @param world 世界
     * @param pos 方块位置
     * @param state 方块状态
     * @param random 随机数
     */
    @Override
    protected void randomDisplayTick(World world, BlockPos pos, FluidState state, Random random) {
        if (!state.isStill() && !(Boolean)state.get(FALLING)) {
            if (random.nextInt(64) == 0) {
                world.playSound((double)pos.getX() + 0.5D,
                        (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D,
                        SoundEvents.BLOCK_HONEY_BLOCK_SLIDE, SoundCategory.BLOCKS,
                        random.nextFloat() * 0.25F + 0.75F, random.nextFloat() + 0.5F,
                        false);
            }
        } else if (random.nextInt(10) == 0) {
            world.addParticle(ParticleTypes.UNDERWATER, (double)pos.getX() + random.nextDouble(),
                    (double)pos.getY() + random.nextDouble(),
                    (double)pos.getZ() + random.nextDouble(),
                    0.0D, 0.0D, 0.0D);
        }
    }

    /**
     * <p>传播的水将会熄灭火以及冲走在其传播路径上的一些特定方块</p>
     * @param world 世界
     * @param pos 方块位置
     * @param state 方块状态
     */
    @Override
    protected void beforeBreakingBlock(WorldAccess world, BlockPos pos, BlockState state) {
        BlockEntity blockEntity = state.hasBlockEntity() ? world.getBlockEntity(pos) : null;
        // 掉落物
        Block.dropStacks(state, world, pos, blockEntity);
    }

    /**
     * @param world 世界
     * @return 获取流淌最大距离为4
     */
    @Override
    protected int getMaxFlowDistance(WorldView world) {
        return 4;
    }

    /**
     * @param world 世界
     * @return <p>获得每个区块的级别降低</p>
     */
    @Override
    protected int getLevelDecreasePerBlock(WorldView world) {
        return 1;
    }

    @Override
    protected boolean canBeReplacedWith(FluidState state, BlockView world, BlockPos pos, Fluid fluid, Direction direction) {
        return direction == Direction.DOWN && !fluid.isIn(FluidTags.WATER);
    }

    /**
     * 水的传播速度是每5游戏刻一方块，即每秒4方块
     * @param world 世界
     * @return 获取传播速度
     */
    @Override
    public int getTickRate(WorldView world) {
        return 5;
    }

    /**
     * @return 获取抗爆性
     */
    @Override
    protected float getBlastResistance() {
        return 100.0F;
    }

    /**
     * @param state 液体状态
     * @return 判断是否流体为静态
     */
    @Override
    public boolean isStill(FluidState state) {
        return false;
    }

    /**
     * @param state 液体状态
     * @return 获取液体水平方向流体等级
     */
    @Override
    public int getLevel(FluidState state) {
        return 8;
    }

    /**
     * @return 获取空桶填满的声音
     */
    @Override
    public Optional<SoundEvent> getBucketFillSound() {
        return Optional.of(SoundEvents.ITEM_BUCKET_FILL);
    }

    /**
     * <p>设置流动液体</p>
     */
    public static class Flowing extends GelidCryotheumFluid {
        @Override
        protected void appendProperties(StateManager.Builder<Fluid, FluidState> builder) {
            super.appendProperties(builder);
            builder.add(LEVEL);
        }

        /**
         * <p>设置水平方向流动液体状态值，最远距离为8</p>
         * <p>可以流淌范围为8</p>
         * @param state 液体状态
         * @return 设置液体状态值
         */
        @Override
        public int getLevel(FluidState state) {
            return state.get(LEVEL);
        }

        @Override
        public boolean isStill(FluidState state) {
            return super.isStill(state);
        }
    }

    /**
     * <p>设置静止液体</p>
     */
    public static class Still extends GelidCryotheumFluid {
        /**
         * <p>设置静止液体状态值为8</p>
         * @param state 液体状态
         * @return 设置液体状态值
         */
        @Override
        public int getLevel(FluidState state) {
            return 8;
        }

        @Override
        public boolean isStill(FluidState state) {
            return true;
        }
    }
}
