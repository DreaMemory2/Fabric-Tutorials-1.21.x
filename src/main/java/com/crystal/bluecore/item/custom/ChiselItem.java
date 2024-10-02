package com.crystal.bluecore.item.custom;

import com.crystal.bluecore.registry.ModBlocks;
import com.crystal.bluecore.registry.component.ModDataComponentTypes;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ChiselItem extends Item {
    // 转变配方
    private static final Map<Block, Block> CHISEL_MAP = Map.of(
            Blocks.STONE, Blocks.STONE_BRICKS,
            Blocks.END_STONE, Blocks.END_STONE_BRICKS,
            Blocks.NETHERRACK, Blocks.NETHER_BRICKS,
            ModBlocks.RAW_PINK_GEMSTONE_BLOCK, ModBlocks.PINK_GEMSTONE_BLOCK
    );

    public ChiselItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        // 获取世界信息
        World world = context.getWorld();
        // 被点击的方块（获取方块状态，方块位置）
        Block clickBlock = world.getBlockState(context.getBlockPos()).getBlock();

        if (CHISEL_MAP.containsKey(clickBlock)) {
            // 在服务端上，将被点击的方块转换为CHISEL_MAP上的方块
            if (!world.isClient) {
                world.setBlockState(context.getBlockPos(), CHISEL_MAP.get(clickBlock).getDefaultState());
                // 配置装备栏，位于主手位置
                /*
                    damage：设置耐久值，使用一次耐久减1
                    amount：显示数量 （默认为1）
                    EquipmentSlot：决定了物品所处物品栏位置提供的属性，形参为装备位置，一般包括装备栏、左手、右手，用以进行加成
                 */
                context.getStack().damage(1, (ServerWorld) world, (ServerPlayerEntity) context.getPlayer(),
                        item -> Objects.requireNonNull(context.getPlayer()).sendEquipmentBreakStatus(item, EquipmentSlot.MAINHAND));
                // 使用成功的音效（使用砂轮），播放类型为方块
                world.playSound(null, context.getBlockPos(), SoundEvents.BLOCK_GRINDSTONE_USE, SoundCategory.BLOCKS);
                // 添加组件：根据组件来获取方块位置信息
                context.getStack().set(ModDataComponentTypes.COORDINATES, context.getBlockPos());
            }

        }
        return ActionResult.SUCCESS;
    }

    /**
     * 获取使用方式
     * @param stack 物品栏中的物品
     * @return 更改使用方式
     */
    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BLOCK;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        if (Screen.hasShiftDown()) {
            // 需要按下Shift键，可以展示更多信息
            tooltip.add(Text.translatable("tooltip.bluecore.chisel.shift_down"));
        } else {
            tooltip.add(Text.translatable("tooltip.bluecore.chisel.text"));
        }

        if (stack.get(ModDataComponentTypes.COORDINATES) != null) {
            // 添加组件文本信息（最后一个方块更改位置）
            tooltip.add(Text.literal("Last Block Changed at " + stack.get(ModDataComponentTypes.COORDINATES)));
        }
        super.appendTooltip(stack, context, tooltip, type);
    }
}
