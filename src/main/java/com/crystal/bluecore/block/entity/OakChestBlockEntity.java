package com.crystal.bluecore.block.entity;

import com.crystal.bluecore.registry.ModBlockEntityType;
import com.crystal.bluecore.screenhandler.OakChestScreenHandler;
import com.crystal.bluecore.util.BlockPosPayload;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ViewerCountManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;


/**
 * <p>创建箱子方块实体</p>
 * @see <a href="https://youtu.be/vAocMCfX0hc?si=iFY_iz4NJu_6qCP8">Custom Block Entity Inventory</a>
 * @author TurtyWurty
 */
public class OakChestBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory<BlockPosPayload> {
    // 箱子盖子旋转角度，且仅限在客户端
    @Environment(EnvType.CLIENT)
    public float lidAngle;
    /* 箱子声音播放器 */
    private final ViewerCountManager stateManager = new ViewerCountManager() {
        @Override
        protected void onContainerOpen(World world, BlockPos pos, BlockState state) {
            OakChestBlockEntity.playSound(world, pos, SoundEvents.BLOCK_CHEST_OPEN);
        }

        @Override
        protected void onContainerClose(World world, BlockPos pos, BlockState state) {
            OakChestBlockEntity.playSound(world, pos, SoundEvents.BLOCK_CHEST_CLOSE);
        }

        @Override
        protected void onViewerCountUpdate(World world, BlockPos pos, BlockState state, int oldViewerCount, int newViewerCount) {
        }

        @Override
        protected boolean isPlayerViewing(PlayerEntity player) {
            return false;
        }
    };
    /* 是否有玩家打开箱子 */
    private int isPlayersOpen;
    /* 一个箱子拥有27个物品槽位，默认每个物品槽位为空 */
    private final SimpleInventory inventory = new SimpleInventory(36) {
        /**
         * 当橡木箱子打开时，则播放声音
         * @param player 玩家
         */
        @Override
        public void onOpen(PlayerEntity player) {
            super.onOpen(player);
            OakChestBlockEntity entity = OakChestBlockEntity.this;
            entity.stateManager.openContainer(player, entity.getWorld(), entity.getPos(), entity.getCachedState());
            isPlayersOpen++;
            update();
        }

        /**
         * 当橡木箱子关闭时，则播放声音
         * @param player 玩家
         */
        @Override
        public void onClose(PlayerEntity player) {
            super.onClose(player);
            OakChestBlockEntity entity = OakChestBlockEntity.this;
            entity.stateManager.openContainer(player, entity.getWorld(), entity.getPos(), entity.getCachedState());
            isPlayersOpen--;
            update();
        }

        @Override
        public void markDirty() {
            super.markDirty();
            update();
        }
    };

    public OakChestBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntityType.OAK_CHEST, blockPos, blockState);
    }

    /**
     * <p>箱子开启、关闭基础声音</p>
     * @param world 世界
     * @param pos 方块的位置
     * @param soundEvent 声音事件
     */
    private static void playSound(World world, BlockPos pos, SoundEvent soundEvent) {
        double d = pos.getX() + 0.5;
        double e = pos.getY() + 0.5;
        double f = pos.getZ() + 0.5;
        world.playSound(null, d, e, f, soundEvent, SoundCategory.BLOCKS, 0.5F, world.random.nextFloat() * 0.1F + 0.9F);
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        /*return GenericContainerScreenHandler.createGeneric9x3(syncId, playerInventory, this);*/
        return new OakChestScreenHandler(syncId, playerInventory, this);
    }

    public void update() {
        // 用于渲染物品纹理（材质）
        markDirty();
        if (world != null) {
            // 获取缓存物品（显示打开箱子里的物品）
            world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_ALL);
        }
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        Inventories.readNbt(nbt, inventory.getHeldStacks(), registryLookup);

        if (nbt.contains("isPlayersOpen", isPlayersOpen)) {
            isPlayersOpen = nbt.getInt("isPlayersOpen");
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        Inventories.writeNbt(nbt, inventory.getHeldStacks(), registryLookup);
    }

    @Override
    public BlockPosPayload getScreenOpeningData(ServerPlayerEntity player) {
        return new BlockPosPayload(pos);
    }

    @Override
    public Text getDisplayName() {
        // 容器名称
        return Text.translatable("container.bluecore.oak_chest");
    }

    public SimpleInventory getInventory() {
        return inventory;
    }

    public float getLidAngle() {
        return lidAngle;
    }

    public int isPlayersOpen() {
        return isPlayersOpen;
    }
}
