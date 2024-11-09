package com.crystal.bluecore.screenhandler;

import com.crystal.bluecore.block.entity.OakChestBlockEntity;
import com.crystal.bluecore.registry.ModBlocks;
import com.crystal.bluecore.registry.ModScreenHandlerTypes;
import com.crystal.bluecore.util.BlockPosPayload;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;

/**
 * <p>原名为：OakChestInventoryScreenHandler</p>
 * @author TurtyWurty
 */
public class OakChestScreenHandler extends ScreenHandler {
    private final OakChestBlockEntity blockEntity;
    private final ScreenHandlerContext context;

    // Client Constructor
    public OakChestScreenHandler(int syncId, PlayerInventory playerInventory, BlockPosPayload payload) {
        this(syncId, playerInventory, (OakChestBlockEntity) playerInventory.player.getWorld().getBlockEntity(payload.pos()));
    }

    // Main Constructor - (Directly called from server)
    public OakChestScreenHandler(int syncId, PlayerInventory playerInventory, OakChestBlockEntity blockEntity) {
        super(ModScreenHandlerTypes.OAK_CHEST_INVENTORY_SCREEN_HANDLER, syncId);

        // 屏幕处理器的实体
        this.blockEntity = blockEntity;
        // 屏幕处理器上下文（获取世界和位置）
        this.context = ScreenHandlerContext.create(this.blockEntity.getWorld(), this.blockEntity.getPos());

        SimpleInventory inventory = this.blockEntity.getInventory();
        // 36 being replaced by the number of slots in your block entity's inventory
        checkSize(inventory, 36);
        // 当玩家打开箱子时
        inventory.onOpen(playerInventory.player);

        // 添加玩家物品栏
        addPlayerInventory(playerInventory);
        // 添加玩家快捷栏
        addPlayerHotbar(playerInventory);
        // 添加方块库存（箱子库存）
        addBlockInventory(inventory);
    }

    /**
     * <p>快速将物品填充到箱子中去</p>
     * @param player 玩家
     * @param slotIndex the index of the slot to quick-move from
     * @return 箱子内的物品
     */
    @Override
    public ItemStack quickMove(PlayerEntity player, int slotIndex) {
        // 初始化一个没有物品的空物品槽
        ItemStack newStack = ItemStack.EMPTY;
        // 获取箱子物品格
        Slot slot = getSlot(slotIndex);
        if (slot != null && slot.hasStack()) {
            ItemStack inSlot = slot.getStack();
            // 箱子内的物品复制到空物品栏（空箱子）里
            newStack = inSlot.copy();
            // 判断箱子是否填满（物品槽，或者是物品格）
            if (slotIndex < 36) {
                // 如果没有填满，则继续添加物品
                // 如果已经填满，则紧张添加物品
                if (!insertItem(inSlot, 36, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                } else if (!insertItem(inSlot, 0, 36, false)) {
                    return ItemStack.EMPTY;
                }
            }
            // 如果箱子为空，则设置物品槽为空
            if (inSlot.isEmpty()) slot.setStack(ItemStack.EMPTY);
            else slot.markDirty();
        }
        return newStack;
    }

    /**
     * <p>添加方块库存（橡木箱子库存）</p>
     */
    public void addBlockInventory(SimpleInventory inventory) {
        for (int row = 0; row < 4; row++) {
            for (int column = 0; column < 9; column++) {
                // 创建实际库存槽36格 x和y表示获取第一格存储槽的位置(23, 33)
                addSlot(new Slot(inventory, column + (row * 9), 8 + (column * 18), 18 + (row * 18)));
            }
        }
    }

    /**
     * <p>快捷栏（Hotbar）是游戏中界面最下方的一条物品栏，玩家可以使用数字键1-9或鼠标滚轮来切换快捷栏中选定的槽位</p>
     *
     * @param playerInventory 玩家物品栏
     * @see <a href="https://www.mcmod.cn/item/282495.html">快捷栏 (Hotbar)</a>
     */
    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int column = 0; column < 9; column++) {
            addSlot(new Slot(playerInventory, column, 8 + (column * 18), 160));
        }
    }

    /**
     * <p>物品栏 (Inventory)</p>
     *
     * @param playerInventory 玩家物品栏
     * @see <a href="https://www.mcmod.cn/item/596967.html">物品栏 (Inventory)</a>
     */
    private void addPlayerInventory(PlayerInventory playerInventory) {
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                addSlot(new Slot(playerInventory, 9 + (column + (row * 9)), 8 + (column * 18), 102 + (row * 18)));
            }
        }
    }

    /**
     * <p>打开橡木箱子</p>
     */
    @Override
    public boolean canUse(PlayerEntity player) {
        // 通过上下文和玩家来打开橡木箱子
        return canUse(this.context, player, ModBlocks.OAK_CHEST);
    }

    /**
     * <p>关闭GUI屏幕（GUI物品栏），关闭玩家库存</p>
     * <p>关闭箱子</p>
     * @param player 玩家
     */
    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        this.blockEntity.getInventory().onClose(player);
    }
}
