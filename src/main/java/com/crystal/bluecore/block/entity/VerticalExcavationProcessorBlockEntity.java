package com.crystal.bluecore.block.entity;

import com.crystal.bluecore.api.TickableBlockEntity;
import com.crystal.bluecore.registry.ModBlockEntities;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;
import java.util.List;

public class VerticalExcavationProcessorBlockEntity extends BlockEntity implements TickableBlockEntity {
    // Tick时间，游戏刻
    private int ticks = 0;
    // 挖掘方块的位置（始终在该方块以下的位置）
    private BlockPos miningPos = this.pos.down();

    public VerticalExcavationProcessorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.VERTICAL_EXCAVATION_PROCESSOR_BLOCK_ENTITY, pos, state);
    }

    /**
     * <p>如果有存储空间，则从箱子底部开始存储被挖掘的方块</p>
     * @param world 世界
     * @param pos 方块位置
     * @return 将掉落物存储在箱子里
     */
    private static Storage<ItemVariant> findItemStorage(ServerWorld world, BlockPos pos) {
        return ItemStorage.SIDED.find(world, pos, Direction.DOWN);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        nbt.putInt("Ticks", this.ticks);
        nbt.putLong("MiningPos", this.miningPos.asLong());
    }

    private static void insertDrops(List<ItemStack> drops, Storage<ItemVariant> aboveStorage) {
        for (ItemStack drop : drops) {
            // 创建一个事务，用于有效存储物品，成功后，提交事务
            // 如果事务停止或失败了，不会影响存储空间中的实际物品
            try (Transaction transaction = Transaction.openOuter()) {
                // 最大存储数量(64为一组)填充箱子
                long inserted = aboveStorage.insert(ItemVariant.of(drop), drop.getCount(), transaction);
                // 如果没有达到64，则可以继续填充被挖掘方块物品
                if (inserted > 0) {
                    drop.decrement((int) inserted);
                    transaction.commit();
                }
            }
        }
        // 如果存储空间满了，则删除物品列表（不在向箱子里填充物品）
        drops.removeIf(ItemStack::isEmpty);
    }

    private static void spawnDrops(List<ItemStack> drops, ServerWorld world, BlockPos pos) {
        for (ItemStack drop : drops){
            // 喷出被挖掘的方块
            ItemScatterer.spawn(world, pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 1.0D, drop);
        }
    }

    /**
     * <p>读取NBT数据，如Tick和最低方块的位置的数据</p>
     * <p>实现向下一个一个挖掘方块，如果方块处理器上方有箱子，则把挖掘的物品放入箱子中</p>
     * @param nbt NBT数据
     * @param registryLookup 注册表
     */
    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        this.ticks = nbt.getInt("Ticks");
        this.miningPos = BlockPos.fromLong(nbt.getLong("MiningPos"));
    }

    @Override
    public void tick() {
        // 首先检查世界是否为服务端
        if(this.world == null || this.world.isClient) return;
        // 当Tick达到20的倍数时，可以继续挖掘（挖掘一次方块有20 Tick的冷却时间）
        // 我的世界中一秒钟等于20 Tick（20个游戏刻）
        if(this.ticks++ % 20 == 0) {
            // 判断是否为世界底部，如果挖掘的方块的位置小于等于世界最底部，则重新在顶部开始挖掘
            // 重新在该方块的底部（pos.down()）开始Tick挖掘
            if(this.miningPos.getY() <= this.world.getBottomY()) this.miningPos = this.pos.down();
            // 获取方块状态
            // 检查是否为空气，或者有无法破环方块（例如：基岩[硬度为-1]），如果是，则跳过这个方块，不挖掘这个方块
            BlockState state = this.world.getBlockState(this.miningPos);
            if(state.isAir() || state.getHardness(this.world, this.miningPos) < 0) {
                this.miningPos = this.miningPos.down();
                return;
            }
            // 构建一个战利品表，设置被挖掘的方块列表（例如：石头，矿物等）
            // 获取掉落物方块列表
            List<ItemStack> drops = new ArrayList<>(state.getDroppedStacks(
                    // 构建一个战利品上下文参数列表，获取两个重要参数，一个是用工具挖掘的方块，一个是在原始位置采集方块
                    new LootContextParameterSet.Builder((ServerWorld) this.world)
                            // 给予一个类似钻石镐的挖掘方块效果
                            .add(LootContextParameters.TOOL, Items.DIAMOND_PICKAXE.getDefaultStack())
                            // 提供挖掘方块的原始位置
                            .add(LootContextParameters.ORIGIN, this.miningPos.toCenterPos())
                            // 挖掘方块后，该方块实体释放（那个被挖掘方块位置）方块
                            .addOptional(LootContextParameters.BLOCK_ENTITY, this)));
            // 该被挖掘的方块是否成功删除，如果成功删除，则继续向下挖掘方块
            this.world.breakBlock(this.miningPos, false);
            // 判断该方块的上面是否有存储空间，如果有（例如箱子），则将掉落物放入箱子中
            Storage<ItemVariant> aboveStorage = findItemStorage((ServerWorld) this.world, this.pos.up());
            // 判断存储空间是否支持存储物品
            if(aboveStorage != null && aboveStorage.supportsInsertion()) insertDrops(drops, aboveStorage);
            // 释放那个被挖掘方块位置中的方块
            if(!drops.isEmpty()) spawnDrops(drops, (ServerWorld) this.world, this.pos);
            this.miningPos = this.miningPos.down();
        }
    }

    public BlockPos getMiningPos() {
        return this.miningPos;
    }
}
