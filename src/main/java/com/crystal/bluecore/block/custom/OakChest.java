package com.crystal.bluecore.block.custom;

import com.crystal.bluecore.block.entity.OakChestBlockEntity;
import com.crystal.bluecore.registry.ModBlockEntities;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class OakChest extends Block implements BlockEntityProvider {
    // 方向属性：facing，箱子正面始终朝向玩家方向
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    /* 设置判定箱 ，添加实际形状，解决方块实心问题（黑色方块） */
    private static final VoxelShape SHAPE = VoxelShapes.cuboid(0.0625, 0, 0.0625, 0.9375, 0.875, 0.9375).simplify();
    // 设置真实正确方向的形状
    private static final Map<Direction, VoxelShape> SHAPE_MAP = new HashMap<>();

    public OakChest(Settings settings) {
        super(settings);
        // 默认状态下，箱子朝向北面
        setDefaultState(getDefaultState().with(FACING, Direction.NORTH));
        // 当方块朝向方向改变时，判定箱也会发生改变
        for (Direction direction : Direction.values()) {
            // 方向 + 判定箱
            SHAPE_MAP.put(direction, calculateShapes(direction, SHAPE));
        }
    }

    /**
     * @param direction 方向
     * @param shape 形状
     * @return 像素形状
     */
    private static VoxelShape calculateShapes(Direction direction, VoxelShape shape) {
        final VoxelShape[] buffer = {shape, VoxelShapes.empty()};

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

    // 右键点击打开箱子界面
    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!world.isClient) {
            if (world.getBlockEntity(pos) instanceof OakChestBlockEntity inventoryBlockEntity) {
                // 打开页面
                player.openHandledScreen(inventoryBlockEntity);
            }
        }
        return ActionResult.success(world.isClient);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntities.OAK_CHEST_BLOCK_ENTITY.instantiate(pos, state);
    }

    /**
     * <p>实现方块旋转操作</p>
     */
    @Override
    protected BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    /**
     * <p>方块的对称方向</p>
     */
    @Override
    protected BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    /**
     * <p>添加方块属性</p>
     */
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        // 朝向属性
        builder.add(FACING);
        super.appendProperties(builder);
    }

    /**
     * 将渲染材质（纹理）绑定到方块上（解决方块材质丢失，为紫红色方块）
     * @param state 方块状态
     */
    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        // 添加方块可旋转功能
        return SHAPE_MAP.get(state.get(FACING));
    }

    /**
     * 根据玩家面向方向，进行方块旋转
     * @param ctx 物品放置上下文
     * @return 方块状态
     */
    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

}
