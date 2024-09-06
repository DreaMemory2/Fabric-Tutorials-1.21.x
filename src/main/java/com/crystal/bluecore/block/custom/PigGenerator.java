package com.crystal.bluecore.block.custom;

import com.crystal.bluecore.block.entity.PigGeneratorBlockEntity;
import com.crystal.bluecore.registry.ModBlockEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class PigGenerator extends Block implements BlockEntityProvider {
    public PigGenerator(Settings settings) {
        super(settings);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!world.isClient) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof PigGeneratorBlockEntity exampleBlockEntity && player != null) {
                if(!player.isSneaking()) {
                    exampleBlockEntity.incrementCounter();
                }

                player.sendMessage(Text.of(exampleBlockEntity.getCounter() + ""), true);
            }
        }

        return ActionResult.success(world.isClient);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntities.PIG_GENERATOR_BLOCK_ENTITY.instantiate(pos, state);
    }
}
