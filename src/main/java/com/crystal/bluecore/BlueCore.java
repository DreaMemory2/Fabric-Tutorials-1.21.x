package com.crystal.bluecore;

import com.crystal.bluecore.block.entity.BasicFluidTankBlockEntity;
import com.crystal.bluecore.block.entity.OakChestInventoryBlockEntity;
import com.crystal.bluecore.registry.*;
import com.crystal.bluecore.registry.component.ModDataComponentTypes;
import com.crystal.bluecore.util.HammerUsageEvent;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BlueCore implements ModInitializer {
	public static final String MOD_ID = "bluecore";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// 物品与方块的初始化
		ModItems.registerModItemsInfo();
		ModBlocks.registerModBlocksInfo();
		// 方块实体类型的初始化
		ModBlockEntities.registerBlockEntityInfo();

		// 物品组或创造标签页的初始化
		ModItemGroups.registerItemGroupsInfo();
		// 音效和唱片初始化
		ModSounds.registerSoundsInfo();
		// 屏幕处理器初始化
		ModScreenHandlerTypes.registerModScreenHandlerTypesInfo();
		// 数据组件初始化
		ModDataComponentTypes.registerDataComponentTypesInfo();

		// 注册物品燃料（物品，燃烧时间）
		FuelRegistry.INSTANCE.add(ModItems.STARLIGHT_ASHES, 600);
		// 注册方块实体的存储系统
		ItemStorage.SIDED.registerForBlockEntity(OakChestInventoryBlockEntity::getInventoryProvider, ModBlockEntities.OAK_CHEST_BLOCK_ENTITY);
		ItemStorage.SIDED.registerForBlockEntity(BasicFluidTankBlockEntity::getInventoryProvider, ModBlockEntities.BASIC_FLUID_TANK_BLOCK_ENTITY);
		// 注册方块实体的流体系统
		FluidStorage.SIDED.registerForBlockEntity(BasicFluidTankBlockEntity::getFluidTankProvider, ModBlockEntities.BASIC_FLUID_TANK_BLOCK_ENTITY);
		// 注册事件
		PlayerBlockBreakEvents.BEFORE.register(new HammerUsageEvent());
	}
}