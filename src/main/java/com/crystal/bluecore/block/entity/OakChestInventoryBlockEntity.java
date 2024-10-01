package com.crystal.bluecore.block.entity;

import com.crystal.bluecore.BlueCore;
import com.crystal.bluecore.registry.ModBlockEntities;
import com.crystal.bluecore.screenhandler.OakChestInventoryScreenHandler;
import com.crystal.bluecore.util.BlockPosPayload;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

/**
 * <p>GUI页面小设计二，箱子页面以及存储物品（橡木箱子）</p>
 */
public class OakChestInventoryBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory<BlockPosPayload> {
    public float lidAngle;
    private int numPlayersOpen;
    public static final Text TITLE = Text.translatable("container." + BlueCore.MOD_ID + ".oak_chest_inventory");
    // 创建简单库存，大小36物品格
    private final SimpleInventory inventory = new SimpleInventory(36) {
        /** 用于渲染物品纹理（材质） */
        @Override
        public void markDirty() {
            super.markDirty();
            update();
        }

        /**
         * 当橡木箱子打开时
         * @param player 玩家
         */
        @Override
        public void onOpen(PlayerEntity player) {
            super.onOpen(player);
            OakChestInventoryBlockEntity.this.numPlayersOpen++;
            update();
        }

        /**
         * 当橡木箱子关闭时
         * @param player 玩家
         */
        @Override
        public void onClose(PlayerEntity player) {
            super.onClose(player);
            OakChestInventoryBlockEntity.this.numPlayersOpen--;
            update();
        }
    };
    // 创建每个方向的库存存储器（例如：可以使用漏斗对方块进行存储）
    private final InventoryStorage inventoryStorage = InventoryStorage.of(inventory, null);

    public OakChestInventoryBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.OAK_CHEST_BLOCK_ENTITY, pos, state);
    }

    /**
     * GUI界面的标题
     */
    @Override
    public Text getDisplayName() {
        return TITLE;
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new OakChestInventoryScreenHandler(syncId, playerInventory, this);
    }

    /**
     * 纹理（材质）更新
     */
    public void update() {
        // 用于渲染物品纹理（材质）
        markDirty();
        if (world != null) {
            // 获取缓存物品（显示打开箱子里的物品）
            world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_ALL);
        }
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        var nbt = super.toInitialChunkDataNbt(registryLookup);
        writeNbt(nbt, registryLookup);
        nbt.putInt("NumPlayersOpen", this.numPlayersOpen);
        return nbt;
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        Inventories.readNbt(nbt, this.inventory.getHeldStacks(), registryLookup);

        if (nbt.contains("NumPlayersOpen", NbtElement.INT_TYPE)) {
            this.numPlayersOpen = nbt.getInt("NumPlayersOpen");
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        Inventories.writeNbt(nbt, this.inventory.getHeldStacks(), registryLookup);
        super.writeNbt(nbt, registryLookup);
    }

    /* 获取库存提供者（获取库存共享状态） */
    public InventoryStorage getInventoryProvider(Direction direction) {
        return inventoryStorage;
    }

    /* 获取打开屏幕的数据 */
    @Override
    public BlockPosPayload getScreenOpeningData(ServerPlayerEntity player) {
        return new BlockPosPayload(this.pos);
    }

    public SimpleInventory getInventory() {
        return this.inventory;
    }

    public float getLidAngle() {
        return this.lidAngle;
    }

    public int getNumPlayersOpen() {
        return this.numPlayersOpen;
    }
}
