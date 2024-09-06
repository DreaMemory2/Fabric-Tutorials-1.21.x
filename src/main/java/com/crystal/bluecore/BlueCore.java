package com.crystal.bluecore;

import com.crystal.bluecore.registry.*;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.registry.FuelRegistry;
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

		// 注册物品燃料（物品，燃烧时间）
		FuelRegistry.INSTANCE.add(ModItems.STARLIGHT_ASHES, 600);
	}
}