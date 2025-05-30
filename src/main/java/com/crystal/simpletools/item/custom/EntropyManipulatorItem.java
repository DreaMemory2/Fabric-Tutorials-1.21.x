package com.crystal.simpletools.item.custom;

import com.crystal.simpletools.recipe.entropy.EntropyMode;
import com.crystal.simpletools.recipe.entropy.EntropyRecipe;
import net.fabricmc.fabric.api.entity.FakePlayer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FireBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.ServerRecipeManager;
import net.minecraft.recipe.SmeltingRecipe;
import net.minecraft.recipe.input.SingleStackRecipeInput;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
* ClassName: Entropy Manipulator Item<br>
* Description: <br>
* Datetime: 2025/5/27 12:10<br>
* @author Crystal
* @version 1.0
* @since 1.0
*/
public class EntropyManipulatorItem extends Item {

    public EntropyManipulatorItem(Settings settings) {
        super(settings);
    }

    // Overridden to allow use of the item on WATER and LAVA which are otherwise not considered for onItemUse
    @Override
    public ActionResult use(World world, PlayerEntity player, Hand hand) {
        BlockHitResult target = raycast(world, player, RaycastContext.FluidHandling.ANY);

        if (target.getType() != HitResult.Type.BLOCK) {
            return ActionResult.FAIL;
        } else {
            BlockPos pos = target.getBlockPos();
            BlockState state = world.getBlockState(pos);
            if (!state.getFluidState().isEmpty()) {
                ItemUsageContext context = new ItemUsageContext(player, hand, target);
                useOnBlock(context);
            }
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext ctx) {
        ItemStack stack = ctx.getStack();
        World level = ctx.getWorld();
        BlockPos blockPos = ctx.getBlockPos();
        PlayerEntity player = ctx.getPlayer();
        Direction face = ctx.getSide();

        if (level.isClient()) return ActionResult.SUCCESS;

        if (player == null) {
            // Fake players cannot crouch, and we cannot communicate whether they want to heat or cool
            player = FakePlayer.get((ServerWorld) level);
        }

        // Correct pos for fluids as these are normally not taken into account.
        BlockHitResult target = raycast(level, player, RaycastContext.FluidHandling.ANY);
        if (target.getType() == HitResult.Type.BLOCK) {
            blockPos = target.getBlockPos();
        }

        return tryApplyEffect(level, stack, blockPos, face, player) ? ActionResult.CONSUME : ActionResult.FAIL;
    }

    private boolean tryApplyEffect(World world, ItemStack stack, BlockPos pos, Direction face, PlayerEntity player) {
        BlockState blockState = world.getBlockState(pos);
        FluidState fluidState = world.getFluidState(pos);

        if (player.isSneaking()) {
            EntropyRecipe heatRecipe = findRecipe(world, EntropyMode.COOL, blockState, fluidState);
            if (heatRecipe != null) {
                applyRecipe(heatRecipe, world, pos, blockState, fluidState);
                return true;
            }
        }

        EntropyRecipe heatRecipe = findRecipe(world, EntropyMode.HEAT, blockState, fluidState);
        if (heatRecipe != null) {
            applyRecipe(heatRecipe, world, pos, blockState, fluidState);
            return true;
        }

        if (tryApplySmeltingRecipes(stack, world, player, pos)) {
            return true;
        }

        return trySpawnFire(world, pos, face);
    }

    /**
     * The entropy manipulator in heat-mode can directly smelt in-level blocks and drop the smelted results, but only if
     * all drops of the block have been smelting recipes.
     */
    private boolean tryApplySmeltingRecipes(ItemStack heldItem, World level, PlayerEntity player, BlockPos pos) {
        BlockState blockState = level.getBlockState(pos);
        BlockEntity blockEntity = level.getBlockEntity(pos);

        List<ItemStack> drops = Block.getDroppedStacks(blockState, (ServerWorld) level, pos, blockEntity, player, heldItem);

        Block outBlock = null;
        List<ItemStack> outItems = new ArrayList<>();

        for (ItemStack in : drops) {

            SingleStackRecipeInput tempInv = new SingleStackRecipeInput(in);
            Optional<RecipeEntry<SmeltingRecipe>> recipe = ((ServerRecipeManager)level.getRecipeManager()).getFirstMatch(RecipeType.SMELTING, tempInv, level);
            if (recipe.isEmpty()) {
                return false;
            }

            ItemStack result = recipe.get().value().craft(tempInv, level.getRegistryManager());

            if (result.getItem() instanceof BlockItem) {

                Block candidate = Block.getBlockFromItem(result.getItem());
                if (candidate == blockState.getBlock()) continue;

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

    /* Recipes */
    private static EntropyRecipe findRecipe(World level, EntropyMode mode, BlockState blockState, FluidState fluidState) {
        for (var recipe : ((ServerWorld)level).getRecipeManager().getAllOfType(EntropyRecipe.Type.INSTANCE)) {
            if (recipe.value().matches(mode, blockState, fluidState)) {
                return recipe.value();
            }
        }
        return null;
    }

    private static void applyRecipe(EntropyRecipe recipe, World level, BlockPos pos, BlockState blockState, FluidState fluidState) {
        BlockState outputBlockState = recipe.getOutputBlockState(blockState);

        if (outputBlockState != null) {
            level.setBlockState(pos, outputBlockState, 3);
        } else {
            FluidState outputFluidState = recipe.getOutputFluidState(fluidState);
            if (outputFluidState != null) {
                level.setBlockState(pos, outputFluidState.getBlockState(), 3);
            } else {
                level.setBlockState(pos, Blocks.AIR.getDefaultState());
            }
        }

        if (!level.isClient) {
            for (var drop : recipe.getDrops()) {
                ItemScatterer.spawn(level, pos.getX(), pos.getY(), pos.getZ(), drop.copy());
            }
        }

        if (recipe.getMode() == EntropyMode.HEAT && !level.isClient()) {
            // Same effect as emptying a water bucket in the nether (see BucketItem)
            level.playSound(null, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F,
                    2.6F + (level.random.nextFloat() - level.random.nextFloat()) * 0.8F);
            for (int l = 0; l < 8; ++l) {
                level.addParticleClient(ParticleTypes.LARGE_SMOKE, pos.getX() + Math.random(),
                        pos.getY() + Math.random(), pos.getZ() + Math.random(), 0.0D, 0.0D, 0.0D);
            }
        }
    }
}
