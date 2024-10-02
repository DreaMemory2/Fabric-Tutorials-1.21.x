package com.crystal.bluecore.block.entity;

import com.crystal.bluecore.BlueCore;
import com.crystal.bluecore.registry.ModBlockEntities;
import com.crystal.bluecore.screenhandler.BasicFluidTankScreenHandler;
import com.crystal.bluecore.util.BlockPosPayload;
import com.crystal.bluecore.util.TickableBlockEntity;
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
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

public class BasicFluidTankBlockEntity extends BlockEntity implements TickableBlockEntity, ExtendedScreenHandlerFactory<BlockPosPayload> {
    public static final Text TITLE = Text.translatable("container." + BlueCore.MOD_ID + ".basic_fluid_tank");
    // 设置物品槽（输入液体I和输出液体O）
    private final SimpleInventory inventory = new SimpleInventory(1) {
        @Override
        public void markDirty() {
            super.markDirty();
            update();
        }
        /*
        @Override
        public boolean isValid(int slot, ItemStack stack) {
            return BasicFluidTankBlockEntity.this.isValid(stack, slot);
        }
        */
    };

    private final InventoryStorage inventoryStorage = InventoryStorage.of(inventory, null);
    /*
    // 设置可以放入水桶，且输入液体的物品槽的功能
    private final ContainerItemContext fluidItemContext = ContainerItemContext.ofSingleSlot(this.inventoryStorage.getSlot(0));
    // 设置单向流体存储器（存储100桶液体）
    private final SingleFluidStorage fluidStorage = SingleFluidStorage.withFixedCapacity(FluidConstants.BUCKET * 14, this::update);
     */

    public BasicFluidTankBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BASIC_FLUID_TANK_BLOCK_ENTITY, pos, state);
    }

    @Override
    public void tick() {

    }

    /*
        @Override
        public void tick() {
            if (this.world == null || this.world.isClient) return;
            if (this.inventory.isEmpty() || !isValid(this.inventory.getStack(0), 0)) return;

            // 判断该物品是否可以存储到储罐里
            Storage<FluidVariant> itemFluidStorage = this.fluidItemContext.find(FluidStorage.ITEM);
            if (itemFluidStorage == null) return;

            FluidVariant match = null;
            // 存储信息与视图
            for (StorageView<FluidVariant> storageView : itemFluidStorage.nonEmptyViews()) {
                if (storageView.isResourceBlank()) continue;
                // 将液体存入储罐中
                try (Transaction transaction = Transaction.openOuter()) {
                    if (this.fluidStorage.insert(storageView.getResource(), FluidConstants.BUCKET, transaction) > 0) {
                        match = storageView.getResource();
                        break;
                    }
                }
            }

            // 如果流体变体为空且没有没有流体，则退出该方法
            if (match == null || match.isBlank()) return;
            try (Transaction transaction = Transaction.openOuter()) {
                // 输入液体
                long inserted = this.fluidStorage.insert(match, FluidConstants.BUCKET, transaction);
                // 输出液体
                long extracted = itemFluidStorage.extract(match, inserted, transaction);
                // 向外输出液体的减少量
                if (extracted < FluidConstants.BUCKET) {
                    long extra = FluidConstants.BUCKET - extracted;
                    // Take any extra fluid
                    this.fluidStorage.extract(match, extra, transaction);
                }
                // 结束交易
                transaction.commit();
            }

            BlueCore.LOGGER.info("Fluid: {}, Amount: {}", this.fluidStorage.getResource().getRegistryEntry().getIdAsString(), this.fluidStorage.getAmount());
            // BlueCore.LOGGER.info("液体: {}, 容量: {}", this.fluidStorage.getResource().getRegistryEntry().getIdAsString(), this.fluidStorage.getAmount());
        }
    */

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        Inventories.readNbt(nbt, this.inventory.getHeldStacks(), registryLookup);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        Inventories.writeNbt(nbt, this.inventory.getHeldStacks(), registryLookup);
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new BasicFluidTankScreenHandler(syncId, playerInventory, this);
    }

    /*
    public boolean isValid(ItemStack stack, int slot) {
        // 判断物品是为空（true）,没有物品槽（false）
        if (stack.isEmpty()) return true;
        if (slot != 0) return false;
        // 判断该物品是否可以存储到储罐里
        Storage<FluidVariant> storage = ContainerItemContext.withConstant(stack).find(FluidStorage.ITEM);
        // supportsInsertion()：提供管道或其他可以传输资源的设备使用，它们是否应该与此存储进行交互
        return storage != null && storage.supportsInsertion();
    }
    */

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        var nbt = super.toInitialChunkDataNbt(registryLookup);
        writeNbt(nbt, registryLookup);
        return nbt;
    }

    /*
    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);

        if(nbt.contains("Inventory", NbtElement.COMPOUND_TYPE)) {
            Inventories.readNbt(nbt.getCompound("Inventory"), this.inventory.getHeldStacks(), registryLookup);
        }

        if (nbt.contains("FluidTank", NbtElement.COMPOUND_TYPE)) {
            this.fluidStorage.readNbt(nbt.getCompound("FluidTank"), registryLookup);
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);

        var inventoryNbt = new NbtCompound();
        Inventories.writeNbt(inventoryNbt, this.inventory.getHeldStacks(), registryLookup);
        nbt.put("Inventory", inventoryNbt);

        var fluidNbt = new NbtCompound();
        this.fluidStorage.writeNbt(fluidNbt, registryLookup);
        nbt.put("FluidTank", fluidNbt);
    }
    */

    /**
     * <p>用于同步客户端</p>
     * <p>正在将其同步到客户端，且渲染方块实体</p>
     */
    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        // 数据包更新：方块实体更新S2C数据包
        return BlockEntityUpdateS2CPacket.create(this);
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
    public Text getDisplayName() {
        return TITLE;
    }

    @Override
    public BlockPosPayload getScreenOpeningData(ServerPlayerEntity player) {
        return new BlockPosPayload(this.pos);
    }
/*
    public SingleFluidStorage getFluidTankProvider(Direction direction) {
        return this.fluidStorage;
    }
*/
    public InventoryStorage getInventoryProvider(Direction direction) {
        return this.inventoryStorage;
    }

    public SimpleInventory getInventory() {
        return this.inventory;
    }
}
