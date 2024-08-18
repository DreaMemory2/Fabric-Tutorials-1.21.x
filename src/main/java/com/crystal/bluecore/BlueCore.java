package com.crystal.bluecore;

import com.crystal.bluecore.registry.ModBlocks;
import com.crystal.bluecore.registry.ModItems;
import net.fabricmc.api.ModInitializer;

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
	}
}