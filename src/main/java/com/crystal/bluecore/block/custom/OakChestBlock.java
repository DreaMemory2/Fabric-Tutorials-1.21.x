package com.crystal.bluecore.block.custom;

import com.crystal.bluecore.block.entity.OakChestBlockEntity;
import com.crystal.bluecore.registry.ModBlockEntityType;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.stat.Stat;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Crystal
 * @version 1.0
 * @since 1.0
 */
public class OakChestBlock extends Block implements Waterloggable, BlockEntityProvider {
    /* 设置方块朝向属性 */
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
    /* 设置方块碰撞箱 */
    private static final VoxelShape SHAPE = Block.createCuboidShape(1.0, 0.0, 1.0, 15.0, 14.0, 15.0);
    /* 设置方块含水特征 */
    private static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    /* 设置方块的方向 */
    private static final Map<Direction, VoxelShape> DIRECTION_SHAPE = new HashMap<>();

    public OakChestBlock(Settings settings) {
        super(settings);
        /* 设置默认朝向属性，默认朝向北; 默认不含水状态 */
        setDefaultState(stateManager.getDefaultState()
                .with(FACING, Direction.NORTH)
                .with(WATERLOGGED, false));

        for (Direction direction : Direction.values()) {
            DIRECTION_SHAPE.put(direction, calculateShapes(direction));
        }
    }

    /**
     *
     * @param world 世界
     * @param pos 方块的位置
     * @return 箱子在上方置有红石导体或固体方块时，或猫坐在其上方时，不能开启。
     */
    public static boolean isChestBlocked(WorldAccess world, BlockPos pos) {
        return hasBlockOnTop(world, pos) || hasCatOnTop(world, pos);
    }

    /**
     *
     * @param world 世界
     * @param pos 方块的位置
     * @return 箱子在上方置有红石导体或固体方块时，则不能开启。
     */
    private static boolean hasBlockOnTop(BlockView world, BlockPos pos) {
        BlockPos blockPos = pos.up();
        return world.getBlockState(blockPos).isSolidBlock(world, blockPos);
    }

    /**
     * 没有坐下的猫会在水平4格半径方形水平范围内的当前Y轴下尝试跳到箱子、床的下半部分和燃烧的熔炉上，
     * 并且会在没有玩家命令的情况下立刻坐下。
     */
    private static boolean hasCatOnTop(WorldAccess world, BlockPos pos) {
        // 非旁观者实体(Cat Entity)
        List<CatEntity> cats = world.getNonSpectatingEntities(CatEntity.class,
                new Box(pos.getX(), pos.getY() + 1, pos.getZ(), pos.getX() + 1, pos.getY() + 2, pos.getZ() + 1));
        if (!cats.isEmpty()) {
            for (CatEntity cat : cats) {
                // 猫的姿势
                if (cat.isInSittingPose()) return true;
            }
        }
        return false;
    }

    private static VoxelShape calculateShapes(Direction direction) {
        final VoxelShape[] buffer = {SHAPE, VoxelShapes.empty()};

        final int times = (direction.getHorizontal() - Direction.NORTH.getHorizontal() + 4) % 4;
        for (int i = 0; i < times; i++) {
            buffer[0].forEachBox((minX, minY, minZ, maxX, maxY, maxZ) ->
                    buffer[1] = VoxelShapes.union(buffer[1],
                            VoxelShapes.cuboid(1 - maxZ, minY, minX, 1 - minZ, maxY, maxX)));
            buffer[0] = buffer[1];
            buffer[1] = VoxelShapes.empty();
        }

        return buffer[0];
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (world.isClient) return ActionResult.SUCCESS;
        if (world.getBlockEntity(pos) instanceof OakChestBlockEntity blockEntity) {
            if(!isChestBlocked(world, pos)) {
                /* 打开箱子容器界面 */
                player.openHandledScreen(blockEntity);
                // 玩家打开箱子的次数增量统计
                player.incrementStat(getOpenStat());
                // 在非和平难度下，成年猪灵会与附近打开或破坏箱子的玩家敌对。
                PiglinBrain.onGuardedBlockInteracted(player, true);
            }
        }
        return ActionResult.CONSUME;
    }

    /**
     * {@return 玩家打开箱子的次数统计方法}
     */
    protected Stat<Identifier> getOpenStat() {
        return Stats.CUSTOM.getOrCreateStat(Stats.OPEN_CHEST);
    }

    /**
     * <p>箱子总是朝向玩家放置的方向</p>
     */
    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        Direction direction = ctx.getHorizontalPlayerFacing().getOpposite();
        FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
        return getDefaultState().with(FACING, direction).with(WATERLOGGED, (fluidState.getFluid() == Fluids.WATER));
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntityType.OAK_CHEST.instantiate(pos, state);
    }

    @Override
    protected BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return DIRECTION_SHAPE.get(state.get(FACING));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED);
    }
}
