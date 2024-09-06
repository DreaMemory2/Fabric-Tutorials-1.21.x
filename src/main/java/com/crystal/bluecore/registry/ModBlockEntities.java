package com.crystal.bluecore.registry;

import com.crystal.bluecore.BlueCore;
import com.crystal.bluecore.block.custom.PigGenerator;
import com.crystal.bluecore.block.entity.PigGeneratorBlockEntity;
import com.crystal.bluecore.block.entity.VerticalExcavationProcessorBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {
    public static final BlockEntityType<VerticalExcavationProcessorBlockEntity> VERTICAL_EXCAVATION_PROCESSOR_BLOCK_ENTITY = registerBlockEntity("vertical_excavation_processor_block_entity",
            BlockEntityType.Builder.create(VerticalExcavationProcessorBlockEntity::new, ModBlocks.VERTICAL_EXCAVATION_PROCESSOR).build());
    public static final BlockEntityType<PigGeneratorBlockEntity> PIG_GENERATOR_BLOCK_ENTITY = registerBlockEntity("pig_entity",
            BlockEntityType.Builder.create(PigGeneratorBlockEntity::new, ModBlocks.PIG_GENERATOR).build());

    // 注册方块实体
    private static <T extends BlockEntity> BlockEntityType<T> registerBlockEntity(String name, BlockEntityType<T> type) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(BlueCore.MOD_ID, name), type);
    }

    public static void registerBlockEntityInfo() {
        // 发送注册方块实体成功信息
        BlueCore.LOGGER.info(BlueCore.MOD_ID + ": Registered Mod Block Entity Success");
    }
}
