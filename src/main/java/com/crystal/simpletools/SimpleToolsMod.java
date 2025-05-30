package com.crystal.simpletools;

import com.crystal.simpletools.block.ModBlocks;
import com.crystal.simpletools.item.ModItems;
import com.crystal.simpletools.recipe.ModRecipes;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleToolsMod implements ModInitializer {
	public static final String MOD_ID = "simpletools";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		/* 直接引用方块注册类和物品注册类即可 */
		ModBlocks.init();
		ModItems.init();

		ModRecipes.init();

		LOGGER.info("Hello Fabric world!");
	}

	public static Identifier of(String path) {
		return Identifier.of(MOD_ID, path);
	}
}