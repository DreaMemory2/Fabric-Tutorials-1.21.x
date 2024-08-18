package com.crystal.bluecore;

import com.crystal.bluecore.registry.ModItems;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Very important comment
public class BlueCore implements ModInitializer {

	public static final String MOD_ID = "bluecore";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	// 主要初始化入口
	@Override
	public void onInitialize() {
		ModItems.registerModItemsInfo();
	}
}