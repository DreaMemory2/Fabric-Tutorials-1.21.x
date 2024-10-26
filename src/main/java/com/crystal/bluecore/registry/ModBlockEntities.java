package com.crystal.bluecore.registry;

import com.crystal.bluecore.BlueCore;
import com.crystal.bluecore.block.entity.BasicFluidTankBlockEntity;
import com.crystal.bluecore.block.entity.OakChestInventoryBlockEntity;
import com.crystal.bluecore.block.entity.PigGeneratorBlockEntity;
import com.crystal.bluecore.block.entity.VerticalExcavationProcessorBlockEntity;
import com.crystal.bluecore.entity.SpearProjectileEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {
    public static final BlockEntityType<VerticalExcavationProcessorBlockEntity> VERTICAL_EXCAVATION_PROCESSOR_BLOCK_ENTITY = registerBlockEntity("vertical_excavation_processor_block_entity",
            BlockEntityType.Builder.create(VerticalExcavationProcessorBlockEntity::new, ModBlocks.VERTICAL_EXCAVATION_PROCESSOR).build());
    public static final BlockEntityType<PigGeneratorBlockEntity> PIG_GENERATOR_BLOCK_ENTITY = registerBlockEntity("pig_entity",
            BlockEntityType.Builder.create(PigGeneratorBlockEntity::new, ModBlocks.PIG_GENERATOR).build());
    public static final BlockEntityType<OakChestInventoryBlockEntity> OAK_CHEST_BLOCK_ENTITY = registerBlockEntity("oak_chest_block_entity",
            BlockEntityType.Builder.create(OakChestInventoryBlockEntity::new, ModBlocks.OAK_CHEST).build());
    public static final BlockEntityType<BasicFluidTankBlockEntity> BASIC_FLUID_TANK_BLOCK_ENTITY = registerBlockEntity("basic_fluid_tank_entity",
            BlockEntityType.Builder.create(BasicFluidTankBlockEntity::new, ModBlocks.BASIC_FLUID_TANK).build());
    // 注册矛实体
    public static final EntityType<SpearProjectileEntity> SPEAR = register("spear",
            EntityType.Builder.<SpearProjectileEntity>create(SpearProjectileEntity::new, SpawnGroup.MISC)
                    .dimensions(0.5F, 0.5F)
                    .eyeHeight(0.13F)
                    .maxTrackingRange(4)
                    .trackingTickInterval(128).build());

    // 注册方块实体
    private static <T extends BlockEntity> BlockEntityType<T> registerBlockEntity(String name, BlockEntityType<T> type) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(BlueCore.MOD_ID, name), type);
    }

    // 注册实体
    private static <T extends Entity> EntityType<T> register(String name, EntityType<T> entity) {
        return Registry.register(Registries.ENTITY_TYPE, Identifier.of(BlueCore.MOD_ID, name), entity);
    }

    public static void registerBlockEntityInfo() {
        // 发送注册方块实体成功信息
        BlueCore.LOGGER.info(BlueCore.MOD_ID + ": Registered Mod Block Entity Success");
    }
}
