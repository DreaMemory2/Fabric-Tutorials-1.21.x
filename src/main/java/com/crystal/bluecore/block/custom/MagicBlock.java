package com.crystal.bluecore.block.custom;

import com.crystal.bluecore.util.ModTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class MagicBlock extends Block {

    public MagicBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        // 播放方块成功转变的音效（玩家与紫水晶互动时播放的轻音效）
        world.playSound(player, pos, SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, SoundCategory.BLOCKS, 1f, 1f);
        return ActionResult.SUCCESS;
    }

    /**
     * 向该方块扔进物品时，发生转变
     * <p>例如：将属于粉红色宝石标签的物品转换为钻石({@link net.minecraft.block.Blocks Diamond})</p>
     */
    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        if (entity instanceof ItemEntity itemEntity) {
            if (isValidItem(itemEntity.getStack()))
                itemEntity.setStack(new ItemStack(Items.DIAMOND, itemEntity.getStack().getCount()));
        }
        super.onSteppedOn(world, pos, state, entity);
    }

    /**
     * 使用标签，实现多个物品进行物质转换。
     * @param stack 可以多个物品转换
     * @return 如果可以进行物品转换，则返回成功，否则相反。
     */
    private boolean isValidItem(ItemStack stack) {
        return stack.isIn(ModTags.Items.GEMSTONE_ITEM);
    }

    /**
     * 添加方块的文本描述信息
     */
    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
        tooltip.add(Text.translatable("tooltip.bluecore.magic_block.tooltip"));
        super.appendTooltip(stack, context, tooltip, options);
    }
}
