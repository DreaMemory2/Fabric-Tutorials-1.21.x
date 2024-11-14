package com.crystal.bluecore.item.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FireBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.*;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.input.SingleStackRecipeInput;
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
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class EntropyManipulatorItem extends Item {
    public static final int MAX_POWER = 50;
    private static final int BAR_COLOR = MathHelper.hsvToRgb(1.0F / 3.0F, 1.0F, 1.0F);

    public EntropyManipulatorItem(Settings settings) {
        super(settings);
    }

    /**
     * 是否可以显示耐久度条
     * @param stack 物品
     * @return 是否可以显示耐久度条
     */
    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return true;
    }

    /**
     * 设置耐久度条颜色
     * @param stack 物品
     * @return 设置耐久度条颜色
     */
    @Override
    public int getItemBarColor(ItemStack stack) {
        return BAR_COLOR;
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        double filled = getPower(stack) / (double) MAX_POWER;
        return MathHelper.clamp((int)filled * 13, 0, 13);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext ctx) {
        ItemStack stack = ctx.getStack();
        World world = ctx.getWorld();
        BlockPos blockPos = ctx.getBlockPos();
        PlayerEntity player = ctx.getPlayer();
        Direction face = ctx.getSide();
        int power = getPower(stack);

        if (power == 0) return ActionResult.PASS;
        if (player == null) return ActionResult.FAIL;
        if (world.isClient()) return ActionResult.SUCCESS;

        BlockHitResult result = raycast(world, player, RaycastContext.FluidHandling.ANY);
        if (result != null && result.getType() == HitResult.Type.BLOCK) blockPos = result.getBlockPos();
        return tryApplyEffect(world, stack, blockPos, player, face) ? ActionResult.CONSUME : ActionResult.FAIL;
    }

    private boolean tryApplyEffect(World world, ItemStack stack, BlockPos pos, PlayerEntity player, Direction face) {

        if (canReplace(stack, world, pos)) return true;
        if (tryApplySmeltingRecipes(stack, world, player, pos)) return true;
        return trySpawnFire(world, stack, pos, face);
    }

    private boolean canReplace(ItemStack stack, World world, BlockPos pos) {
        extractPower(stack);
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
        // 耐久值-1
        extractPower(heldItem);
        level.playSound(null, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, 0.8F);

        if (outBlock == null) {
            outBlock = Blocks.AIR;
        }

        level.setBlockState(pos, outBlock.getDefaultState());
        // 生成掉落物品
        outItems.forEach(stack -> ItemScatterer.spawn(level, pos.getX() , pos.getY(), pos.getZ(), stack));
        return true;
    }

    private boolean trySpawnFire(World level, ItemStack item, BlockPos pos, Direction side) {
        pos = pos.offset(side);
        if (!FireBlock.canPlaceAt(level, pos, side) || !level.isAir(pos)) {
            return false;
        }
        extractPower(item);
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

    private void setPower(ItemStack stack, int power) {
        stack.setDamage(power);
    }

    private int getPower(ItemStack stack) {
        stack.setDamage(MAX_POWER);
        return MAX_POWER;
    }

    private void extractPower(ItemStack stack) {
        setPower(stack, getPower(stack) - 1);
    }
}
