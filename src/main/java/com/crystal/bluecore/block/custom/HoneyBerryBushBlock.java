package com.crystal.bluecore.block.custom;

import com.crystal.bluecore.registry.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SweetBerryBushBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.event.GameEvent;

/**
 * <p>创建甜蜜浆果丛方块</p>
 * @author Crystal
 */
public class HoneyBerryBushBlock extends SweetBerryBushBlock {

    public HoneyBerryBushBlock(Settings settings) {
        super(settings);
    }

    /*
    * <p>获取甜蜜浆果物品</p>
    * */
    @Override
    public ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state) {
        return new ItemStack(ModItems.HONEY_BERRIES);
    }

    /*
    * <p>右键点击灌木丛获取甜蜜浆果</p>
    * */
    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        // 浆果生长阶段：最大为3
        int i = state.get(AGE);
        /* 当浆果到生长阶段为3时 */
        boolean bl = (i == 3);
        if (i > 1) {
            /* 则一定获取浆果一个，可能获取格外浆果数量为1或2个 */
            int j = 1 + world.random.nextInt(2);
            dropStack(world, pos, new ItemStack(ModItems.HONEY_BERRIES, j + (bl ? 1 : 0)));
            /* 播放玩家采摘甜浆果的声音 */
            world.playSound(null, pos, SoundEvents.BLOCK_SWEET_BERRY_BUSH_PICK_BERRIES, SoundCategory.BLOCKS, 0.5f, 0.5f);
            /* 采收后浆果生长阶段回到1(没有浆果的甜浆果丛); NOTIFY_ALL: 设置方块的默认行为 */
            BlockState blockState = state.with(AGE, 1);
            world.setBlockState(pos, blockState, Block.NOTIFY_ALL);
            /* 更新游戏事件：该位置的方块状态发生改变 */
            world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(player, blockState));
            return ActionResult.success(world.isClient);
        } else {
            return super.onUse(state, world, pos, player, hit);
        }
    }
}
