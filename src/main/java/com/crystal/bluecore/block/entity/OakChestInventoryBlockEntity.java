package com.crystal.bluecore.block.entity;

import com.crystal.bluecore.BlueCore;
import com.crystal.bluecore.registry.ModBlockEntities;
import com.crystal.bluecore.screenhandler.OakChestInventoryScreenHandler;
import com.crystal.bluecore.util.BlockPosPayload;
import com.crystal.bluecore.util.SoundStateManger;
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
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

/**
 * <p>GUI页面小设计二，箱子页面以及存储物品（橡木箱子）</p>
 */
public class OakChestInventoryBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory<BlockPosPayload> {
    // 箱子页面的标题
    public static final Text TITLE = Text.translatable("container." + BlueCore.MOD_ID + ".oak_chest_inventory");
    // 声音状态管理器
    private final SoundStateManger stateManager = new SoundStateManger() {
        // 箱子打开时播放声音
        @Override
        protected void onContainerOpen(World world, BlockPos pos, BlockState state) {
            OakChestInventoryBlockEntity.playSound(world, pos, SoundEvents.BLOCK_CHEST_OPEN);
        }

        // 箱子关闭时播放声音
        @Override
        protected void onContainerClose(World world, BlockPos pos, BlockState state) {
            OakChestInventoryBlockEntity.playSound(world, pos, SoundEvents.BLOCK_CHEST_CLOSE);
        }
    };
    // 箱子盖子旋转角度
    public float lidAngle;
    private int numPlayersOpen;
    // 创建简单库存，大小36物品格
    private final SimpleInventory inventory = new SimpleInventory(36) {
        /** 用于渲染物品纹理（材质） */
        @Override
        public void markDirty() {
            super.markDirty();
            update();
        }

        /**
         * 当橡木箱子打开时，并播放声音
         * @param player 玩家
         */
        @Override
        public void onOpen(PlayerEntity player) {
            super.onOpen(player);
            OakChestInventoryBlockEntity entity = OakChestInventoryBlockEntity.this;
            entity.stateManager.openContainer(player, entity.getWorld(), entity.getPos(), entity.getCachedState());
            entity.numPlayersOpen++;
            update();
        }

        /**
         * 当橡木箱子关闭时，并播放声音
         * @param player 玩家
         */
        @Override
        public void onClose(PlayerEntity player) {
            super.onClose(player);
            OakChestInventoryBlockEntity entity = OakChestInventoryBlockEntity.this;
            entity.stateManager.closeContainer(player, entity.getWorld(), entity.getPos(), entity.getCachedState());
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
     * <p>播放箱子被打开或关闭声音</p>
     *
     * @param world      世界位置
     * @param pos        方块位置
     * @param soundEvent 声音事件
     */
    public static void playSound(World world, BlockPos pos, SoundEvent soundEvent) {
        double d = pos.getX() + 0.5;
        double e = pos.getY() + 0.5;
        double f = pos.getZ() + 0.5;
        world.playSound(null, d, e, f, soundEvent, SoundCategory.BLOCKS, 0.5F,
                world.random.nextFloat() * 0.1F + 0.9F);
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

    /* <-- NBT标签 --> */
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

    /* GUI界面的标题 */
    @Override
    public Text getDisplayName() {
        return TITLE;
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
