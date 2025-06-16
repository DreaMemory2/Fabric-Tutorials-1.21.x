package com.crystal.bluecore.registry;

import com.crystal.bluecore.BlueCore;
import com.crystal.bluecore.block.entity.BasicFluidTankBlockEntity;
import com.crystal.bluecore.block.entity.OakChestBlockEntity;
import com.crystal.bluecore.block.entity.PigGeneratorBlockEntity;
import com.crystal.bluecore.block.entity.VerticalExcavationProcessorBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModBlockEntityType {
    // 注册方块实体
    private static <T extends BlockEntity> BlockEntityType<T> create(String name, BlockEntityType.Builder<T> builder) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, BlueCore.of(name), builder.build());
    }    public static final BlockEntityType<VerticalExcavationProcessorBlockEntity> VERTICAL_EXCAVATION_PROCESSOR_BLOCK_ENTITY = create("vertical_excavation_processor_block_entity",
            BlockEntityType.Builder.create(VerticalExcavationProcessorBlockEntity::new, ModBlocks.VERTICAL_EXCAVATION_PROCESSOR));
    public static final BlockEntityType<PigGeneratorBlockEntity> PIG_GENERATOR_BLOCK_ENTITY = create("pig_entity",
            BlockEntityType.Builder.create(PigGeneratorBlockEntity::new, ModBlocks.PIG_GENERATOR));
    public static final BlockEntityType<OakChestBlockEntity> OAK_CHEST = create("oak_chest",
            BlockEntityType.Builder.create(OakChestBlockEntity::new, ModBlocks.OAK_CHEST));
    public static final BlockEntityType<BasicFluidTankBlockEntity> BASIC_FLUID_TANK_BLOCK_ENTITY = create("basic_fluid_tank_entity",
            BlockEntityType.Builder.create(BasicFluidTankBlockEntity::new, ModBlocks.BASIC_FLUID_TANK));



    public static void registerBlockEntityInfo() {
        // 发送注册方块实体成功信息
        BlueCore.LOGGER.info(BlueCore.MOD_ID + ": Registered Mod Block Entity Success");
    }
}
