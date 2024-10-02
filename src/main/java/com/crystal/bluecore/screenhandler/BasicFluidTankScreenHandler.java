package com.crystal.bluecore.screenhandler;

import com.crystal.bluecore.block.entity.BasicFluidTankBlockEntity;
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

public class BasicFluidTankScreenHandler extends ScreenHandler {
    private final BasicFluidTankBlockEntity blockEntity;
    private final ScreenHandlerContext context;

    @SuppressWarnings("DataFlowIssue")
    public BasicFluidTankScreenHandler(int syncId, PlayerInventory playerInventory, BlockPosPayload payload) {
        this(syncId, playerInventory, (BasicFluidTankBlockEntity) playerInventory.player.getWorld().getBlockEntity(payload.pos()));
    }

    public BasicFluidTankScreenHandler(int syncId, PlayerInventory playerInventory, BasicFluidTankBlockEntity blockEntity) {
        super(ModScreenHandlerTypes.BASIC_FLUID_TANK_SCREEN_HANDLER, syncId);

        this.blockEntity = blockEntity;
        this.context = ScreenHandlerContext.create(blockEntity.getWorld(), blockEntity.getPos());

        SimpleInventory inventory = blockEntity.getInventory();
        checkSize(inventory, 1);
        inventory.onOpen(playerInventory.player);

        // 添加物品栏和快捷栏
        addPlayerHotbar(playerInventory);
        addPlayerInventory(playerInventory);
        // 添加输入口
        addInputSlot(inventory);
        // 添加输出口
        // addOutputSlot(inventory);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slotIndex) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = getSlot(slotIndex);
        if (slot != null && slot.hasStack()) {
            ItemStack inSlot = slot.getStack();
            newStack = inSlot.copy();

            if (slotIndex == 0) {
                if (!insertItem(inSlot, 0, this.slots.size(), false)) return ItemStack.EMPTY;
            } else if (!insertItem(inSlot, 0, 0, true)) return ItemStack.EMPTY;

            if (inSlot.isEmpty()) slot.setStack(ItemStack.EMPTY);
            else slot.markDirty();
        }
        return newStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        // 打开界面
        return canUse(this.context, player, ModBlocks.BASIC_FLUID_TANK);
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        this.blockEntity.getInventory().onClose(player);
    }

    private void addInputSlot(SimpleInventory inventory) {
        // TODO: Predicate to test if item is a bucket
        Slot input = new Slot(inventory, 0, 143, 18)/*{
            @Override
            public boolean canInsert(ItemStack stack) {
                return inventory.isValid(0, stack);
            }
        }*/;
        addSlot(input);
    }

    /*private void addOutputSlot(SimpleInventory inventory) {
        Slot output = new Slot(inventory, 1, 143, 49);
        addSlot(output);
    }*/

    private void addPlayerInventory(PlayerInventory playerInventory) {
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                addSlot(new Slot(playerInventory, 9 + (column + (row * 9)), 8 + (column * 18), 84 + (row * 18)));
            }
        }
    }

    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int column = 0; column < 9; column++) {
            addSlot(new Slot(playerInventory, column, 8 + (column * 18), 142));
        }
    }

    public BasicFluidTankBlockEntity getBlockEntity() {
        return this.blockEntity;
    }
}
