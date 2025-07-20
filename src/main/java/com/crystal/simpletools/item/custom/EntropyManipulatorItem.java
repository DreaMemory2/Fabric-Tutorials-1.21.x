package com.crystal.simpletools.item.custom;

import com.crystal.simpletools.recipe.entropy.EntropyMode;
import com.crystal.simpletools.recipe.entropy.EntropyRecipe;
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
* Description: 实现熵变机械臂的功能，优点无需能量支持<br>
* Datetime: 2025/5/27 12:10<br>
* @author Crystal
* @version 1.0
* @since 1.0
*/
public class EntropyManipulatorItem extends Item {

    public EntropyManipulatorItem(Settings settings) {
        super(settings);
    }

    /**
     * 当点击一大片液体时，由于没有点到方块，故不会触发useOn，需要修改use的逻辑
     * <p>Overridden to allow use of the item on WATER and LAVA which are otherwise not considered for onItemUse</p>
     * @param world 在此世界上，使用该物品
     * @param player 使用物品的玩家
     * @param hand 那只手用于使用物品
     */
    @Override
    public ActionResult use(World world, PlayerEntity player, Hand hand) {
        BlockHitResult target = raycast(world, player, RaycastContext.FluidHandling.ANY);

        /* 如果玩家点击的目标类型不是方块，则返回FAIL */
        if (target.getType() != HitResult.Type.BLOCK) {
            return ActionResult.FAIL;
        } else {
            // 如果玩家点击的目标类型是方块，则获取目标方块的位置和状态
            BlockPos pos = target.getBlockPos();
            BlockState state = world.getBlockState(pos);
            /* 如果方块液体不为空，则调用useOnBlock方法 */
            if (!state.getFluidState().isEmpty()) {
                useOnBlock(new ItemUsageContext(player, hand, target));
            }
        }
        return ActionResult.SUCCESS;
    }

    /**
     * 点击方块是调用该方法
     * <p>如果方块液体不为空，则调用{@link Item#useOnBlock(ItemUsageContext) useOnBlock}方法</p>
     */
    @Override
    public ActionResult useOnBlock(ItemUsageContext ctx) {
        ItemStack stack = ctx.getStack();
        World level = ctx.getWorld();
        BlockPos blockPos = ctx.getBlockPos();
        PlayerEntity player = ctx.getPlayer();
        Direction face = ctx.getSide();

        /* 如果玩家为null, 则返回FAIL */
        if (player == null) return ActionResult.FAIL;
        // 修正点击位置（使得流体可被识别）
        // Correct pos for fluids as these are normally not taken into account.
        BlockHitResult target = raycast(level, player, RaycastContext.FluidHandling.ANY);
        /* 如果目标类型是方块类型，则初始位置替换成目标方块的位置 */
        if (target.getType() == HitResult.Type.BLOCK) {
            blockPos = target.getBlockPos();
        }
        /* 玩家是否可以使用物品, 如果不能使用物品，则返回FAIL */
        if (!player.canPlaceOn(blockPos, face, stack)) return ActionResult.FAIL;
        /* 如果该世界为客户端，则返回SUCCESS */
        if (level.isClient()) return ActionResult.SUCCESS;
        // 应用效果方法，true则CONSUME, false则FAIL
        return tryApplyEffect(level, stack, blockPos, face, player) ? ActionResult.CONSUME : ActionResult.FAIL;
    }

    private boolean tryApplyEffect(World world, ItemStack stack, BlockPos pos, Direction face, PlayerEntity player) {
        /* 尝试应用Entropy配方 */
        if (tryApplyEntropyRecipes(world, pos, player)) return true;
        /* 尝试应用Smelting配方 */
        if (tryApplySmeltingRecipes(world, stack, pos, player)) return true;
        /* 尝试生成火源 */
        return trySpawnFire(world, pos, face);
    }

    private boolean tryApplyEntropyRecipes(World world, BlockPos pos, PlayerEntity player) {
        // 方块状态和液体状态
        BlockState blockState = world.getBlockState(pos);
        FluidState fluidState = world.getFluidState(pos);
        /* 如果玩家潜行右键，则启动冷却模式 */
        if (player.isSneaking()) {
            EntropyRecipe coolRecipe = findRecipe(EntropyMode.COOL, world, blockState, fluidState);
            /* 如果配方不为null，则应用此配方，返回true */
            if (coolRecipe != null) {
                /* 应用冷却模式配方 */
                apply(coolRecipe, world, pos, blockState, fluidState);
                return true;
            }
        }

        /* 如果玩家直接右键，则启动加热模式 */
        EntropyRecipe heatRecipe = findRecipe(EntropyMode.HEAT, world, blockState, fluidState);
        /* 如果配方不为null，则应用此配方，返回true */
        if (heatRecipe != null) {
            /* 应用冷却模式配方 */
            apply(heatRecipe, world, pos, blockState, fluidState);
            return true;
        }
        return false;
    }

    /**
     * The entropy manipulator in heat-mode can directly smelt in-level blocks and drop the smelted results, but only if
     * all drops of the block have been smelting recipes.
     */
    private boolean tryApplySmeltingRecipes(World level, ItemStack heldItem, BlockPos pos, PlayerEntity player) {
        /* 获取方块状态和方块实体 */
        BlockState blockState = level.getBlockState(pos);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        // 点击的方块或物品（对应的掉落物实体）
        List<ItemStack> drops = Block.getDroppedStacks(blockState, (ServerWorld) level, pos, blockEntity, player, heldItem);
        // 提供输出方块和输出物品列表
        Block outBlock = null;
        List<ItemStack> outItems = new ArrayList<>();

        for (ItemStack in : drops) {
            // 提供输入槽，将物品放入输入槽中
            SingleStackRecipeInput tempInv = new SingleStackRecipeInput(in);
            // 获取SmeltingRecipe配方，并判断是否为空
            Optional<RecipeEntry<SmeltingRecipe>> recipe = ((ServerRecipeManager)level.getRecipeManager()).getFirstMatch(RecipeType.SMELTING, tempInv, level);
            if (recipe.isEmpty()) return false;
            // 输出结果物品
            ItemStack result = recipe.get().value().craft(tempInv, level.getRegistryManager());
            /* 如果物品是方块物品，例如：沙子变玻璃、圆石变石头等 */
            if (result.getItem() instanceof BlockItem) {
                // 从方块物品中获取方块
                Block candidate = Block.getBlockFromItem(result.getItem());
                /* 如果方块状态是原来方块，则返回continue; 例如：石头变石头 */
                if (candidate == blockState.getBlock()) continue;
                /* 如果输入方块为null, 则将获取方块作为输出方块 */
                if (outBlock == null) {
                    outBlock = candidate;
                    continue;
                }
            }
            outItems.add(result);
        }

        /* 如果输出方块为null, 且输入物品为空，则返回false */
        /* 如果只是输出方块为null，则将目标方块替换为空气，例如：原木变木炭，铁矿石变为铁锭 */
        if (outBlock == null && outItems.isEmpty()) return false;
        if (outBlock == null) outBlock = Blocks.AIR;
        // 向世界播放"使用打火石点火"的声音
        level.playSound(null, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, 0.8F);
        // 生成烧炼对应方块和物品
        level.setBlockState(pos, outBlock.getDefaultState());
        outItems.forEach(stack -> ItemScatterer.spawn(level, pos.getX() , pos.getY(), pos.getZ(), stack));
        return true;
    }

    private boolean trySpawnFire(World level, BlockPos pos, Direction side) {
        pos = pos.offset(side);
        /* 判断火源生成条件：是否可以替换，是否单击位置为空气*/
        if (!FireBlock.canPlaceAt(level, pos, side) || !level.isAir(pos)) {
            return false;
        }
        // 向世界播放"使用打火石点火"的声音
        level.playSound(null, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, 0.8F);
        // 生成火源方块
        level.setBlockState(pos, Blocks.FIRE.getDefaultState());
        return true;
    }

    /* Recipes */
    private static EntropyRecipe findRecipe(EntropyMode mode, World level, BlockState blockState, FluidState fluidState) {
        /* 通过配方管理器来获取所有Entropy配方类型，遍历获取每个配方包装器 */
        for (var recipe : ((ServerWorld)level).getRecipeManager().getAllOfType(EntropyRecipe.Type.INSTANCE)) {
            /* 如果存在这个配方，则返回true */
            if (recipe.value().matches(mode, blockState, fluidState)) {
                return recipe.value(); // 返回 EntropyRecipe
            }
        }
        return null;
    }

    private static void apply(EntropyRecipe recipe, World level, BlockPos pos, BlockState blockState, FluidState fluidState) {
        /* 获取输出方块和输出液体 */
        BlockState outputBlock = recipe.getOutputBlockState(blockState);
        FluidState outputFluid = recipe.getOutputFluidState(fluidState);

        if (outputBlock != null) {
            level.setBlockState(pos, outputBlock);
        }
        if (outputFluid != null) {
            level.setBlockState(pos, outputFluid.getBlockState());
        }
        /* 如果输出方块和输出物品都为null，则将目标方块设为空气 */
        if (outputBlock == null && outputFluid == null) {
            level.setBlockState(pos, Blocks.AIR.getDefaultState());
        }
        /* 如果配方输出物品不为空，则返回物品掉落物：例如：流动水变雪球 */
        if (recipe.getDrops() != null) {
            if (!level.isClient) {
                for (var drop : recipe.getDrops()) {
                    ItemScatterer.spawn(level, pos.getX(), pos.getY(), pos.getZ(), drop.copy());
                }
            }
        }

        /* 如果熵变机械臂模式为加热模式，效果如同在下界放水桶(另见水桶) */
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
