package com.crystal.bluecore.block.entity;

import com.crystal.bluecore.BlueCore;
import com.crystal.bluecore.registry.ModBlockEntityType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class PigGeneratorBlockEntity extends BlockEntity {
    // 记数器
    private int counter;

    public PigGeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityType.PIG_GENERATOR_BLOCK_ENTITY, pos, state);
    }

    /**
     * <p>当玩家不潜行时</p>
     */
    public void incrementCounter() {
        // 增加记数，自+1
        counter++;
        // 目的是：知道数据已经改变，需要保存这个数据。通过这个方法，让它保存并读取一个整数
        // 如何实现保存或读取整数数据呢？需要用NBT来实现数据保存或读取
        markDirty();
        // 进行世界检查是否为服务端
        if (!(world instanceof ServerWorld serverWorld)) return;
        // 如果数字能被10整除，说明记数器是10的倍数，可以生成一只猪
        if(counter % 10 == 0) {
            EntityType.PIG.spawn(serverWorld, pos.up(), SpawnReason.TRIGGERED);
        }
        // 如果数字能被100整除，说明记数器是100的倍数，可以生成一道闪电
        if(counter % 100 == 0) {
            EntityType.LIGHTNING_BOLT.spawn(serverWorld, pos.up(), SpawnReason.TRIGGERED);
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        // 防止其他模组的NBT数据干扰记数器NBT数据
        // 保证数据安全性和唯一性
        var data = new NbtCompound();
        // 保存数据功能（整数型）
        data.putInt("counter", counter);
        // 含有模组后缀的NBT
        nbt.put(BlueCore.MOD_ID, data);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        // 安全读取数据（整数型）
        if (nbt.contains(BlueCore.MOD_ID, NbtElement.COMPOUND_TYPE)) {
            // 通过模组ID获取NBT计数器的数据
            var data = nbt.getCompound(BlueCore.MOD_ID);
            counter = data.contains("counter", NbtElement.INT_TYPE) ? data.getInt("counter") : 0;
        }
    }

    public int getCounter() {
        return counter;
    }
}
