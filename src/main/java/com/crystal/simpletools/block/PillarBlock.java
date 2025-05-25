package com.crystal.simpletools.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.Direction;

/**
* ClassName: PillarBlock<br>
* Description: 柱状方块<br>
* Datetime: 2025/5/25 13:40<br>
* @author Crystal
* @version 1.0
* @since 1.0
*/
public class PillarBlock extends Block {
    /* 轴线 */
    public static final EnumProperty<Direction.Axis> AXIS = Properties.AXIS;

    public PillarBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(AXIS, Direction.Axis.Y));
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        switch (state.get(AXIS)) {
            case X -> state.with(AXIS, Direction.Axis.Z);
            case Z -> state.with(AXIS, Direction.Axis.X);
        }
        return state;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return getDefaultState().with(AXIS, ctx.getSide().getAxis());
    }

    @Override
    public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        /* 构建方块状态 */
        builder.add(AXIS);
    }
}
