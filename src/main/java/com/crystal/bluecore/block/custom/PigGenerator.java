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

/**
 * @see <a href="https://youtu.be/1SHiPygYPD0?si=CMliXYG3UqYjCTrx">Custom Block Entities</a>
 * @author TurtyWurty
 */
public class PigGenerator extends Block implements BlockEntityProvider {

    public PigGenerator(Settings settings) {
        super(settings);
    }

    /**
     * <p>当玩家右键该方块时，调用此方法</p>
     * <p>当玩家使用物品时，或者空手右键方块也可以调用此方法</p>
     * <p>该方块提供一个计数器（保存整数）。当玩家每次点击该方块时，计数器加一，</p>
     * <p>并且当数字达到10的倍数时，在方块上方生成猪{@link net.minecraft.entity.passive.PigEntity PigEntity}</p>
     * @param state 方块状态
     * @param world 世界
     * @param pos 方块位置
     * @param player 玩家
     * @param hit 敲击
     * @return 产生效果
     */
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
        // 绑定实体
        return ModBlockEntities.PIG_GENERATOR_BLOCK_ENTITY.instantiate(pos, state);
    }
}
