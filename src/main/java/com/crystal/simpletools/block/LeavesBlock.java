package com.crystal.simpletools.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Waterloggable;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;

import java.util.OptionalInt;

/**
* ClassName: LeavesBlock<br>
* Description: <br>
* Datetime: 2025/5/25 16:22<br>
* @author Crystal
* @version 1.0
* @since 1.0
*/
public class LeavesBlock extends Block implements Waterloggable {
    /* 树叶距离最近的原木的距离 */
    public static final IntProperty DISTANCE = Properties.DISTANCE_1_7;
    /* 树叶是否可以持久存在 */
    public static final BooleanProperty PERSISTENT = Properties.PERSISTENT;
    /** 使得方块可以含水属性 */
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    /* 含水状态 */
    /**
     * <p>设置默认状态</p>
     * <p>树叶默认状态不含水</p>
     */
    public LeavesBlock(Settings settings) {
        super(settings);
        setDefaultState(stateManager.getDefaultState()
                .with(DISTANCE, 7)
                .with(PERSISTENT, false)
                .with(WATERLOGGED, false));
    }

    /**
     * <p>ture：如果树叶保持含水，则方块内的水源方块不会从方块内侧向外流淌</p>
     * <p>false: 如果树叶没有含水，则方块内没有水源方块</p>
     * @param state 方块状态
     * @return 判断液体是否四周流淌
     */
    @Override
    public FluidState getFluidState(BlockState state) {
        // getStill(boolean) 获取向下流动的水状态值（布尔类型）
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    /* 叶木间距离 */
    /**
     * <p>树叶枯萎方法</p>
     * <p>如果树叶距离原木小于7，且树叶不能持久存在，那么该方块处于介稳状态</p>
     * <p>处于介稳状态方块，可以长久存在，且不会枯萎，但是只要该方块六个面发生方块更新则树叶立即消失</p>
     * <p>无论方块是否含水，都不会影响方块的枯萎</p>
     * @param state 方块状态
     * @param world 世界
     * @param pos 方块位置
     * @param random 随机刻
     */
    @Override
    protected void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        /* 如果两个条件都为真，结果都为真 */
        /* 当树叶距离原木为7且能够不能持续存在时，树叶可以枯萎 */
        boolean isDecay = state.get(DISTANCE) == 7 && !(Boolean) state.get(PERSISTENT);
        // 移除树叶
        if (isDecay) {
            dropStacks(state, world, pos);
            world.removeBlock(pos, false);
        }
    }

    /**
     * <p>第一步：执行getStateForNeighborUpdate方法，判断该方块状态值</p>
     */
    @Override
    protected BlockState getStateForNeighborUpdate(
            BlockState state,
            WorldView world,
            ScheduledTickView tickView,
            BlockPos pos,
            Direction direction,
            BlockPos neighborPos,
            BlockState neighborState,
            Random random
    ) {
        /* 判断该方块是否含水 */
        if (state.get(WATERLOGGED)) {
            tickView.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }

        /* 树叶距离原木的距离值 */
        int i = getDistanceFromLog(neighborState) + 1;
        if (i != 1 || state.get(DISTANCE) != i) {
            tickView.scheduleBlockTick(pos, this, 1);
        }

        return state;
    }

    /**
     * <p>检测并方块更新</p>
     * <p>第二步：检测四周方块的状态和方块位置</p>
     * <p>树叶对自身添加延迟为1游戏刻（0.05秒）的计划刻，执行时检查当前距离原木的距离</p>
     */
    @Override
    protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        world.setBlockState(pos, updateDistanceFromLogs(state, world, pos), Block.NOTIFY_ALL);
    }

    /**
     *
     * @param state 方块状态
     * @param world 世界
     * @param pos 方块位置
     * @return 方块更新——PP跟新
     */
    private static BlockState updateDistanceFromLogs(BlockState state, WorldAccess world, BlockPos pos) {
        int i = 7;
        /* 获取动态方块位置信息（方块位置可变化） */
        BlockPos.Mutable mutable = new BlockPos.Mutable();

        for (Direction direction : Direction.values()) {
            mutable.set(pos, direction);
            /* 每一次游戏刻，都在检测树叶四周是否有原木：结果取最小值 */
            i = Math.min(i, getDistanceFromLog(world.getBlockState(mutable)) + i);
            /* 如果树叶间距离为1时，直接跳过本次循环 */
            if (i == 1) break;
        }

        return state.with(DISTANCE, i);
    }

    private static int getDistanceFromLog(BlockState state) {
        return getOptionalDistanceFromLog(state).orElse(7);
    }

    /**
     * <p>若当前距离与方块状态中的距离不对应，则修改方块状态</p>
     * <p>1. 详细来说，当树叶检测到原木时，更改之前距离值，重新计算距离原木的距离值，并设置为新的距离值</p>
     * <p>其中persistent不影响距离值，只会影响树叶的枯萎</p>
     * <p>2. 如果原木破环消失，则树叶直接修改为默认状态</p>
     * @param state 方块状态
     */
    private static OptionalInt getOptionalDistanceFromLog(BlockState state) {
        /* 这里是检测原木标签，不是原木方块；如果即使原木不在标签里，树叶永远不会检测到原木，所以需要玩家手动添加原木标签 */
        if (state.isIn(BlockTags.LOGS)) {
            return OptionalInt.of(0);
        } else {
            /* 没有检测到原木标签中原木方块，则进入下一次方块更新循环 */
            return state.contains(DISTANCE) ? OptionalInt.of(state.get(DISTANCE)) : OptionalInt.empty();
        }
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        /* 判断液体是否为水 */
        boolean fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos()).getFluid() == Fluids.WATER;
        /* 方块状态，persistent=true: 树叶可以无视距离持久存在；waterlogged: 即可以含水，又可以无水 */
        BlockState blockState = this.getDefaultState().with(PERSISTENT, true).with(WATERLOGGED, fluidState);
        return updateDistanceFromLogs(blockState, ctx.getWorld(), ctx.getBlockPos());
    }

    @Override
    public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED, PERSISTENT, DISTANCE);
    }
}
