package com.crystal.bluecore.block.custom;

import com.crystal.bluecore.api.TickableBlockEntity;
import com.crystal.bluecore.block.entity.VerticalExcavationProcessorBlockEntity;
import com.crystal.bluecore.registry.ModBlockEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class VerticalExcavationProcessor extends Block implements BlockEntityProvider {

    public VerticalExcavationProcessor(Settings settings) {
        super(settings);
    }

    /**
     * 方块实体：可以存储大规模数据、自主地进行时刻更新（Tick）和播放模型动画
     */
    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntities.VERTICAL_EXCAVATION_PROCESSOR_BLOCK_ENTITY.instantiate(pos, state);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!world.isClient) {
            if (world.getBlockEntity(pos) instanceof VerticalExcavationProcessorBlockEntity blockEntity)
                // 发送一个消息说明正在向下挖掘方块
                player.sendMessage(Text.of("Mining at: " + blockEntity.getMiningPos().toString()), false);
        }
        return ActionResult.SUCCESS;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return TickableBlockEntity.getTicker(world);
    }
}
