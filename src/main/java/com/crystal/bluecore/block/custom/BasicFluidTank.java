package com.crystal.bluecore.block.custom;

import com.crystal.bluecore.block.entity.BasicFluidTankBlockEntity;
import com.crystal.bluecore.registry.ModBlockEntities;
import com.crystal.bluecore.util.TickableBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class BasicFluidTank extends Block implements BlockEntityProvider {
    private static final VoxelShape SHAPE = VoxelShapes.union(
            VoxelShapes.cuboid(0.125, 0, 0.125, 0.875, 0.0625, 0.875),
            VoxelShapes.cuboid(0.125, 0.9375, 0.125, 0.875, 1, 0.875),
            VoxelShapes.cuboid(0.125, 0.0625, 0.125, 0.1875, 0.9375, 0.1875),
            VoxelShapes.cuboid(0.125, 0.0625, 0.8125, 0.1875, 0.9375, 0.875),
            VoxelShapes.cuboid(0.8125, 0.0625, 0.125, 0.875, 0.9375, 0.1875),
            VoxelShapes.cuboid(0.8125, 0.0625, 0.8125, 0.875, 0.9375, 0.875),
            VoxelShapes.cuboid(0.125, 0.0625, 0.1875, 0.1875, 0.9375, 0.8125),
            VoxelShapes.cuboid(0.8125, 0.0625, 0.1875, 0.875, 0.9375, 0.8125),
            VoxelShapes.cuboid(0.1875, 0.0625, 0.8125, 0.8125, 0.9375, 0.875),
            VoxelShapes.cuboid(0.1875, 0.0625, 0.125, 0.8125, 0.9375, 0.1875)
    );

    public BasicFluidTank(Settings settings) {
        super(settings);
    }


    @Override
    protected void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        // 判断方块状态，且更新方块状态，通过方块实体来更新方块状态
        if (state.getBlock() != newState.getBlock()) {
            if (world.getBlockEntity(pos) instanceof BasicFluidTankBlockEntity blockEntity) {
                ItemScatterer.spawn(world, pos, blockEntity.getInventory());
                // 发送红色信号（表示方块状态已更新）
                world.updateComparators(pos, this);
            }
        }

        super.onStateReplaced(state, world, pos, newState, moved);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if(!world.isClient) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if(blockEntity instanceof BasicFluidTankBlockEntity fluidTank) {
                player.openHandledScreen(fluidTank);
            }
        }

        return ActionResult.success(world.isClient);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntities.BASIC_FLUID_TANK_BLOCK_ENTITY.instantiate(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return TickableBlockEntity.getTicker(world);
    }

    /*  添加碰撞体积，否则报出CrashException: Ticking entity的异常（没有碰撞体积） */
    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }
}
