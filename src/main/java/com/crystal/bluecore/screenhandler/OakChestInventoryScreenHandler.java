package com.crystal.bluecore.screenhandler;

import com.crystal.bluecore.block.entity.OakChestInventoryBlockEntity;
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

public class OakChestInventoryScreenHandler extends ScreenHandler {
    private final OakChestInventoryBlockEntity blockEntity;
    private final ScreenHandlerContext context;

    // Client Constructor
    public OakChestInventoryScreenHandler(int syncId, PlayerInventory playerInventory, BlockPosPayload payload) {
        this(syncId, playerInventory, (OakChestInventoryBlockEntity) playerInventory.player.getWorld().getBlockEntity(payload.pos()));
    }

    // Main Constructor - (Directly called from server)
    public OakChestInventoryScreenHandler(int syncId, PlayerInventory playerInventory, OakChestInventoryBlockEntity blockEntity) {
        super(ModScreenHandlerTypes.OAK_CHEST_INVENTORY_SCREEN_HANDLER, syncId);

        // 屏幕处理器的实体
        this.blockEntity = blockEntity;
        // 屏幕处理器上下文（获取世界和位置）
        this.context = ScreenHandlerContext.create(this.blockEntity.getWorld(), this.blockEntity.getPos());

        SimpleInventory inventory = this.blockEntity.getInventory();
        checkSize(inventory, 36);
        inventory.onOpen(playerInventory.player);

        // 添加玩家物品栏
        addPlayerInventory(playerInventory);
        // 添加玩家快捷栏
        addPlayerHotbar(playerInventory);
        // 添加方块库存（箱子库存）
        addBlockInventory(inventory);
    }

    /**
     * <p>当箱子被玩家破环，移动时，掉落箱子内的物品</p>
     *
     * @param player    玩家
     * @param slotIndex the index of the slot to quick-move from
     * @return 箱子内的物品
     */
    @Override
    public ItemStack quickMove(PlayerEntity player, int slotIndex) {
        // 清空箱子内的物品
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = getSlot(slotIndex);
        if (slot != null && slot.hasStack()) {
            ItemStack inSlot = slot.getStack();
            // 箱子内的物品
            newStack = inSlot.copy();
            // 判断箱子是否填满（物品槽，或者是物品格）
            if (slotIndex < 36) {
                if (!insertItem(inSlot, 36, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                } else if (!insertItem(inSlot, 0, 36, false)) {
                    return ItemStack.EMPTY;
                }
            }
            if (inSlot.isEmpty())
                slot.setStack(ItemStack.EMPTY);
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
                // 创建物品框（大小）
                addSlot(new Slot(inventory, column + (row * 9), 8 + (column * 18), 18 + (row * 18)));
            }
        }
    }

    /**
     * <p>快捷栏（Hotbar）是游戏中界面最下方的一条物品栏，玩家可以使用数字键1-9或鼠标滚轮来切换快捷栏中选定的槽位</p>
     *
     * @param PlayerInventory 玩家物品栏
     * @see <a href="https://www.mcmod.cn/item/282495.html">快捷栏 (Hotbar)</a>
     */
    private void addPlayerHotbar(PlayerInventory PlayerInventory) {
        for (int column = 0; column < 9; column++) {
            addSlot(new Slot(PlayerInventory, column, 8 + (column * 18), 160));
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
     * 关闭GUI屏幕（GUI物品栏）
     *
     * @param player 玩家
     */
    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        this.blockEntity.getInventory().onClose(player);
    }
}
