package com.crystal.tutorial;

import com.crystal.tutorial.block.ModBlocks;
import com.crystal.tutorial.item.ModItems;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TutorialMod implements ModInitializer {
	public static final String MOD_ID = "tutorial";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		/* 直接引用方块注册类和物品注册类即可 */
		ModBlocks.init();
		ModItems.init();

		LOGGER.info("Hello Fabric world!");
	}

	public static Identifier of(String path) {
		return Identifier.of(MOD_ID, path);
	}
}