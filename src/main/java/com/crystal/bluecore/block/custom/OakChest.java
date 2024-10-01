package com.crystal.bluecore.block.custom;

import com.crystal.bluecore.block.entity.OakChestInventoryBlockEntity;
import com.crystal.bluecore.registry.ModBlockEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class OakChest extends Block implements BlockEntityProvider {
    public OakChest(Settings settings) {
        super(settings);
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
}
