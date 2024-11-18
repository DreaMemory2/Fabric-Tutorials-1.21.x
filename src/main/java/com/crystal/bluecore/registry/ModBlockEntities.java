package com.crystal.bluecore.registry;

import com.crystal.bluecore.BlueCore;
import com.crystal.bluecore.block.entity.BasicFluidTankBlockEntity;
import com.crystal.bluecore.block.entity.OakChestBlockEntity;
import com.crystal.bluecore.block.entity.PigGeneratorBlockEntity;
import com.crystal.bluecore.block.entity.VerticalExcavationProcessorBlockEntity;
import com.crystal.bluecore.entity.SpearProjectileEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public class ModBlockEntities {
    /**
     * CHEST = create("chest", ChestBlockEntity::new, Blocks.CHEST);
     */
    // 注册矛实体
    public static final EntityType<SpearProjectileEntity> SPEAR = register("spear",
            EntityType.Builder.<SpearProjectileEntity>create(SpearProjectileEntity::new, SpawnGroup.MISC)
                    .dropsNothing()
                    .dimensions(0.5F, 0.5F)
                    .eyeHeight(0.13F)
                    .maxTrackingRange(4)
                    .trackingTickInterval(128));
// 注册方块实体
    private static <T extends BlockEntity> BlockEntityType<T> registerBlockEntity(String id, FabricBlockEntityTypeBuilder.Factory<BlockEntity> factory, Block block) {
        return (BlockEntityType) Registry.register(Registries.BLOCK_ENTITY_TYPE, BlueCore.of(id), FabricBlockEntityTypeBuilder.create(factory, block).build());
    }public static final BlockEntityType<VerticalExcavationProcessorBlockEntity> VERTICAL_EXCAVATION_PROCESSOR_BLOCK_ENTITY = registerBlockEntity("vertical_excavation_processor_block_entity",
            VerticalExcavationProcessorBlockEntity::new, ModBlocks.VERTICAL_EXCAVATION_PROCESSOR);

    // 注册实体
    private static <T extends Entity> EntityType<T> register(String id, EntityType.Builder<T> type) {
        return Registry.register(Registries.ENTITY_TYPE,
                RegistryKey.of(RegistryKeys.ENTITY_TYPE, BlueCore.of(id)),
                type.build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, BlueCore.of(id))));
    }    public static final BlockEntityType<PigGeneratorBlockEntity> PIG_GENERATOR_BLOCK_ENTITY = registerBlockEntity("pig_entity",
            PigGeneratorBlockEntity::new, ModBlocks.PIG_GENERATOR);

        public static final BlockEntityType<OakChestBlockEntity> OAK_CHEST_BLOCK_ENTITY = registerBlockEntity("oak_chest_block_entity",
            OakChestBlockEntity::new, ModBlocks.OAK_CHEST);
    public static final BlockEntityType<BasicFluidTankBlockEntity> BASIC_FLUID_TANK_BLOCK_ENTITY = registerBlockEntity("basic_fluid_tank_entity",
            BasicFluidTankBlockEntity::new, ModBlocks.BASIC_FLUID_TANK);







    public static void registerBlockEntityInfo() {
        // 发送注册方块实体成功信息
        BlueCore.LOGGER.info(BlueCore.MOD_ID + ": Registered Mod Block Entity Success");
    }
}
