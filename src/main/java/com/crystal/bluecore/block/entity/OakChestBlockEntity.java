package com.crystal.bluecore.block.entity;

import com.crystal.bluecore.registry.ModBlockEntities;
import com.crystal.bluecore.screenhandler.OakChestScreenHandler;
import com.crystal.bluecore.util.BlockPosPayload;
import com.crystal.bluecore.util.SoundStateManger;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
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
 * @see <a href="https://youtu.be/vAocMCfX0hc?si=iFY_iz4NJu_6qCP8">Custom Block Entity Inventory</a>
 * @author TurtyWurty
 */
public class OakChestBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory<BlockPosPayload> {
    // 箱子页面的标题
    public static final Text TITLE = Text.translatable("container.bluecore.oak_chest_inventory");
    // 声音状态管理器
    private final SoundStateManger stateManager = new SoundStateManger() {
        // 箱子打开时播放声音
        @Override
        protected void onContainerOpen(World world, BlockPos pos, BlockState state) {
            OakChestBlockEntity.playSound(world, pos, SoundEvents.BLOCK_CHEST_OPEN);
        }

        // 箱子关闭时播放声音
        @Override
        protected void onContainerClose(World world, BlockPos pos, BlockState state) {
            OakChestBlockEntity.playSound(world, pos, SoundEvents.BLOCK_CHEST_CLOSE);
        }
    };
    // 箱子盖子旋转角度，且仅限在客户端
    @Environment(EnvType.CLIENT)
    public float lidAngle;
    // 打开箱子的玩家数量
    private int numPlayersOpen;
    // 创建简单库存，容量为36库存槽
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
            OakChestBlockEntity entity = OakChestBlockEntity.this;
            entity.stateManager.openContainer(player, entity.getWorld(), entity.getPos(), entity.getCachedState());
            // 确保箱子的NBT数据更新，且同步到客户端上
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
            OakChestBlockEntity entity = OakChestBlockEntity.this;
            entity.stateManager.closeContainer(player, entity.getWorld(), entity.getPos(), entity.getCachedState());
            // 与打开箱子同理
            entity.numPlayersOpen--;
            update();
        }
    };
    // 创建每个面，每个方向都能访问的库存存储器
    private final InventoryStorage inventoryStorage = InventoryStorage.of(inventory, null);

    public OakChestBlockEntity(BlockPos pos, BlockState state) {
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
        double x = pos.getX() + 0.5;
        double y= pos.getY() + 0.5;
        double z = pos.getZ() + 0.5;
        world.playSound(null, x, y, z, soundEvent, SoundCategory.BLOCKS, 0.5F,
                world.random.nextFloat() * 0.1F + 0.9F);
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new OakChestScreenHandler(syncId, playerInventory, this);
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
        // 当两个初始内部进行方块数据传递时，进行数据初始化处理，保存玩家数量（整数）
        var nbt = super.toInitialChunkDataNbt(registryLookup);
        writeNbt(nbt, registryLookup);
        nbt.putInt("NumPlayersOpen", this.numPlayersOpen);
        return nbt;
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        // 读取方块内的物品（物品栏内的物品），并获取物品列表
        Inventories.readNbt(nbt, this.inventory.getHeldStacks(), registryLookup);

        if (nbt.contains("NumPlayersOpen", NbtElement.INT_TYPE)) {
            this.numPlayersOpen = nbt.getInt("NumPlayersOpen");
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        // 同理，将方块内的物品（物品栏内的物品）写入到NBT数据中，并保存物品列表
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

    /* 使用屏幕处理程序时，要获取简单库存槽 */
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
