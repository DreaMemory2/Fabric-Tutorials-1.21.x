package com.crystal.bluecore.item.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FireBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.*;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.input.SingleStackRecipeInput;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EntropyManipulatorItem extends Item {

    public EntropyManipulatorItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext ctx) {
        ItemStack stack = ctx.getStack();
        World world = ctx.getWorld();
        BlockPos blockPos = ctx.getBlockPos();
        PlayerEntity player = ctx.getPlayer();
        Direction face = ctx.getSide();

        if (player == null) return ActionResult.FAIL;
        if (world.isClient()) return ActionResult.SUCCESS;

        // 如果对方块右键后，耐久值减一
        ctx.getStack().damage(1, (ServerWorld) world, (ServerPlayerEntity) ctx.getPlayer(),
                item -> Objects.requireNonNull(ctx.getPlayer()).sendEquipmentBreakStatus(item, EquipmentSlot.MAINHAND));

        BlockHitResult result = raycast(world, player, RaycastContext.FluidHandling.ANY);
        if (result != null && result.getType() == HitResult.Type.BLOCK) blockPos = result.getBlockPos();
        return tryApplyEffect(world, stack, blockPos, player, face) ? ActionResult.CONSUME : ActionResult.FAIL;
    }

    private boolean tryApplyEffect(World world, ItemStack stack, BlockPos pos, PlayerEntity player, Direction face) {

        if (canReplace(world, pos)) return true;
        if (tryApplySmeltingRecipes(stack, world, player, pos)) return true;
        return trySpawnFire(world, pos, face);
    }

    private boolean canReplace(World world, BlockPos pos) {
        FluidState fluidState = world.getFluidState(pos);
        int level = fluidState.getLevel();
        if (level > 0) {
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
            ItemScatterer.spawn(world, pos.getX(), pos.getY() + 1.0D, pos.getZ(), Items.SNOWBALL.getDefaultStack());
        }
        return false;
    }


    /**
     * 问题出处
     */
    private boolean tryApplySmeltingRecipes(ItemStack heldItem, World level, PlayerEntity p, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        BlockEntity be = level.getBlockEntity(pos);
        List<ItemStack> drops = Block.getDroppedStacks(state, (ServerWorld) level, pos, be, p, heldItem);

        Block outBlock = null;
        List<ItemStack> outItems = new ArrayList<>();

        for (ItemStack in : drops) {
            SingleStackRecipeInput tempInv = new SingleStackRecipeInput(in);
            var optional = level.getRecipeManager().getFirstMatch(RecipeType.SMELTING, tempInv, level);

            if (optional.isEmpty()) return false;

            ItemStack result = optional.get().value().craft(tempInv, level.getRegistryManager());

            if (result.getItem() instanceof BlockItem) {
                Block candidate = Block.getBlockFromItem(result.getItem());

                if (candidate == state.getBlock()) continue;

                if (outBlock == null) {
                    outBlock = candidate;
                    continue;
                }
            }
            outItems.add(result);
        }
        if (outBlock == null && outItems.isEmpty()) {
            return false;
        }

        level.playSound(null, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, 0.8F);

        if (outBlock == null) {
            outBlock = Blocks.AIR;
        }

        level.setBlockState(pos, outBlock.getDefaultState());
        // 生成掉落物品
        outItems.forEach(stack -> ItemScatterer.spawn(level, pos.getX() , pos.getY(), pos.getZ(), stack));
        return true;
    }

    private boolean trySpawnFire(World level, BlockPos pos, Direction side) {
        pos = pos.offset(side);
        if (!FireBlock.canPlaceAt(level, pos, side) || !level.isAir(pos)) {
            return false;
        }

        level.playSound(null, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, 0.8F);
        level.setBlockState(pos, Blocks.FIRE.getDefaultState());
        return true;
    }


    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        BlockHitResult result = raycast(world, player, RaycastContext.FluidHandling.ANY);
        if (result.getType() != HitResult.Type.BLOCK)
            return new TypedActionResult<>(ActionResult.FAIL, player.getStackInHand(hand));
        BlockPos pos = result.getBlockPos();
        BlockState state = world.getBlockState(pos);
        if (!state.getFluidState().isEmpty())
            useOnBlock(new ItemUsageContext(player, hand, result));
        return new TypedActionResult<>(ActionResult.success(world.isClient), player.getStackInHand(hand));
    }
}
