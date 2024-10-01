package com.crystal.bluecore.block.custom;

import com.crystal.bluecore.block.entity.OakChestInventoryBlockEntity;
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
    // 方向属性：facing
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    /* 设置碰撞箱和轮廓 */
    private static final VoxelShape DEFAULT_SHAPE = VoxelShapes.union(
            VoxelShapes.cuboid(0.0625, 0, 0.0625, 0.9375, 0.625, 0.9375),
            VoxelShapes.cuboid(0.0625, 0.5625, 0.0625, 0.9375, 0.875, 0.9375),
            VoxelShapes.cuboid(0.4375, 0.4375, 0, 0.5625, 0.6875, 0.0625).simplify());
    private static final Map<Direction, VoxelShape> SHAPES = new HashMap<>();

    public OakChest(Settings settings) {
        super(settings);
        // 默认状态下，箱子面朝向背面
        setDefaultState(getDefaultState().with(FACING, Direction.NORTH));
        // 方块旋转时，形状和状态也会发生改变
        for (Direction direction : Direction.values()) {
            SHAPES.put(direction, calculateShapes(direction, DEFAULT_SHAPE));
        }
    }

    /* 计算形状 */
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

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!world.isClient) {
            if (world.getBlockEntity(pos) instanceof OakChestInventoryBlockEntity inventoryBlockEntity) {
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
     * <p>方块的旋转方向</p>
     */
    @Override
    protected BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    /**
     * <p>方块的镜像方向</p>
     */
    @Override
    protected BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    /**
     * <p>添加方向属性</p>
     */
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACING);
    }

    /**
     * 将渲染材质（纹理）绑定到方块上（解决方块材质丢失或者为黑紫色方块）
     * @param state 方块状态
     */
    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        // 解决渲染模型为黑色问题
        return SHAPES.get(state.get(FACING));
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

}
