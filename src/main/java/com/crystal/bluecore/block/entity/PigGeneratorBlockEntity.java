package com.crystal.bluecore.block.entity;

import com.crystal.bluecore.registry.ModBlockEntities;
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
    private int counter;

    public PigGeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.PIG_GENERATOR_BLOCK_ENTITY, pos, state);
    }

    public int getCounter() {
        return this.counter;
    }

    public void incrementCounter() {
        this.counter++;
        // 保存更新到世界
        this.markDirty();

        if (!(this.world instanceof ServerWorld serverWorld)) return;

        if(this.counter % 10 == 0) {
            EntityType.PIG.spawn(serverWorld, this.pos.up(), SpawnReason.TRIGGERED);
        }

        if(this.counter % 100 == 0) {
            EntityType.PIG.spawn(serverWorld, this.pos.up(), SpawnReason.TRIGGERED);
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        nbt.putInt("counter", this.counter);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        this.counter = nbt.contains("counter", NbtElement.INT_TYPE) ? nbt.getInt("counter") : 0;
    }
}
