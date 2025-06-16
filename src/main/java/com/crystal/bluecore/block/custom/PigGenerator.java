package com.crystal.bluecore.block.custom;

import com.crystal.bluecore.block.entity.PigGeneratorBlockEntity;
import com.crystal.bluecore.registry.ModBlockEntityType;
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
        // 使用服务端，而不是客户端，其中服务端可以使用相同数据信息
        if (!world.isClient) {
            // 通过世界来获取方块实体（在任何位置单击这个方块，都可以成为该方块的方块实体）
            BlockEntity blockEntity = world.getBlockEntity(pos);
            // 但是这个方块的实体，不确定是否为PigGenerator方块的实体，需要进行判断这个方块实体，但是还要保证玩家存在
            if (blockEntity instanceof PigGeneratorBlockEntity pigGenerator && player != null) {
                // 判断玩家是否潜行
                if(!player.isSneaking()) {
                    // 如果玩家不潜行，则增加记数
                    pigGenerator.incrementCounter();
                }
                // 如果玩家潜行，则发送记数信息。overlay：如果为false，则向聊天栏发送信息；如果为ture，则位于方块的上方
                player.sendMessage(Text.of(Integer.toString(pigGenerator.getCounter())), true);
            }
        }
        // 只要服务器获取记数，则客户端才能完成挥手动作
        return ActionResult.success(world.isClient);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        // 创建一个方块实体
        // 或者也可以new PigGeneratorBlockEntity(pos, state);
        // 这种方法考虑模组兼容性
        return ModBlockEntityType.PIG_GENERATOR_BLOCK_ENTITY.instantiate(pos, state);
    }
}
