package com.crystal.bluecore.block.custom;

import net.minecraft.block.AmethystBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

/**
 * @see net.minecraft.block.AmethystClusterBlock 紫水晶簇
 */
public class FrostClusterBlock extends AmethystBlock {
    // 设置是否可以没入水中
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    // 设置方向属性
    public static final DirectionProperty FACING = Properties.FACING;
    // 生长方向和偏转位移，例如北面、南面、东面、西面、上面、下面
    private final VoxelShape northShape;
    private final VoxelShape southShape;
    private final VoxelShape eastShape;
    private final VoxelShape westShape;
    private final VoxelShape upShape;
    private final VoxelShape downShape;

    public FrostClusterBlock(float height, float xzOffset, Settings settings) {
        super(settings);
        // 默认状态
        this.setDefaultState(this.getDefaultState().with(WATERLOGGED, Boolean.FALSE).with(FACING, Direction.UP));
        // 方向形状
        this.upShape = Block.createCuboidShape(xzOffset, 0.0, xzOffset, 16.0F - xzOffset, height, 16.0F - xzOffset);
        this.downShape = Block.createCuboidShape(xzOffset, 16.0F - height, xzOffset, 16.0F - xzOffset, 16.0, 16.0F - xzOffset);
        this.northShape = Block.createCuboidShape(xzOffset, xzOffset, 16.0F - height, 16.0F - xzOffset, 16.0F - xzOffset, 16.0);
        this.southShape = Block.createCuboidShape(xzOffset, xzOffset, 0.0, 16.0F - xzOffset, 16.0F - xzOffset, height);
        this.eastShape = Block.createCuboidShape(0.0, xzOffset, xzOffset, height, 16.0F - xzOffset, 16.0F - xzOffset);
        this.westShape = Block.createCuboidShape(16.0F - height, xzOffset, xzOffset, 16.0, 16.0F - xzOffset, 16.0F - xzOffset);
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction direction = state.get(FACING);
        return switch (direction) {
            case NORTH -> this.northShape;
            case SOUTH -> this.southShape;
            case EAST -> this.eastShape;
            case WEST -> this.westShape;
            case DOWN -> this.downShape;
            default -> this.upShape;
        };
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED, FACING);
        super.appendProperties(builder);
    }
}
