package com.crystal.bluecore.item.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FireBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.input.SingleStackRecipeInput;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
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
    private static final String CUR_POWER = "curPower";
    private static final int BAR_COLOR = MathHelper.hsvToRgb(1.0F / 3.0F, 1.0F, 1.0F);

    public EntropyManipulatorItem(Settings settings) {
        super(settings);
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        return BAR_COLOR;
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        double filled = getPower(stack) / (double) MAX_POWER;
        return MathHelper.clamp((int)filled * 13, 0, 13);
    }

    private int getPower(ItemStack stack) {
        stack.setDamage(50);
        return MAX_POWER;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext ctx) {
        ItemStack stack = ctx.getStack();
        World world = ctx.getWorld();
        BlockPos blockPos = ctx.getBlockPos();
        PlayerEntity player = ctx.getPlayer();
        Direction face = ctx.getSide();
        NbtCompound nbt = new NbtCompound();
        int power = getPower(stack);
        if (power == 0) return ActionResult.PASS;
        if (player == null) return ActionResult.FAIL;
        BlockHitResult result = raycast(world, player, RaycastContext.FluidHandling.ANY);
        if (result != null && result.getType() == HitResult.Type.BLOCK) blockPos = result.getBlockPos();
        if (world.isClient()) return ActionResult.SUCCESS;
        return tryApplyEffect(nbt, world, stack, blockPos, player, face) ? ActionResult.CONSUME : ActionResult.FAIL;
    }

    private boolean tryApplyEffect(NbtCompound nbt, World world, ItemStack stack, BlockPos pos, PlayerEntity player, Direction face) {
        if (tryApplySmeltingRecipes(nbt, stack, world, player, pos)) return true;
        return trySpawnFire(nbt, world, stack, pos, face);
    }

    private boolean tryApplySmeltingRecipes(NbtCompound nbt, ItemStack heldItem, World level, PlayerEntity p, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        BlockEntity be = level.getBlockEntity(pos);
        List<ItemStack> drops = Block.getDroppedStacks(state, (ServerWorld) level, pos, be, p, heldItem);

        Block outBlock = null;
        List<ItemStack> outItems = new ArrayList<>();

        SimpleInventory tempInv = new SimpleInventory(1);
        for (ItemStack in : drops) {
            tempInv.setStack(0, in);
            var optional = level.getRecipeManager().getFirstMatch(RecipeType.SMELTING, new SingleStackRecipeInput(tempInv.getStack(0)), level);
            if (optional.isEmpty()) return false;
            // TODO 物品凭空消失不见，部分功能已经完善
            ItemStack result = optional.get().value().craft(new SingleStackRecipeInput(tempInv.getStack(0)), level.getRegistryManager());
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
        extractPower(nbt, heldItem);
        level.playSound(null, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, 0.8F);

        if (outBlock == null) {
            outBlock = Blocks.AIR;
        }

        level.setBlockState(pos, outBlock.getDefaultState());
        return true;
    }

    private boolean trySpawnFire(NbtCompound nbt, World level, ItemStack item, BlockPos pos, Direction side) {
        pos = pos.offset(side);
        if (!FireBlock.canPlaceAt(level, pos, side) || !level.isAir(pos)) {
            return false;
        }
        extractPower(nbt, item);
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

    private void setPower(NbtCompound nbt, ItemStack stack, int p) {
        stack.setDamage(p);
        nbt.putInt(CUR_POWER, p);
    }

    private void extractPower(NbtCompound nbt, ItemStack stack) {
        setPower(nbt, stack, getPower(stack) - 1);
    }
}
