package com.crystal.tutorial.block;

import com.crystal.tutorial.TutorialMod;
import net.minecraft.block.AbstractBlock.Settings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

import java.util.function.Function;

/**
 * @author Crystal
 * @version 1.0
 * @since 1.0
 */
public class ModBlocks {

    public static final Block CRUCIATE_STONE = register("cruciate_stone", Settings.copy(Blocks.STONE));

    /**
     * 如果是简单方块，不用新建方块类，直接采用Block方块注册即可
     */
    public static Block register(String name, Settings settings) {
        return register(name, Block::new, settings);
    }

    public static Block register(String name, Function<Settings, Block> factory, Settings settings) {
        RegistryKey<Block> key = RegistryKey.of(RegistryKeys.BLOCK, TutorialMod.of(name));
        Block block = Blocks.register(key, factory, settings);
        Items.register(block);
        return block;
    }

    public static void init() {

    }
}
