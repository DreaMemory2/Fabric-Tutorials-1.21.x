package com.crystal.simpletools.block;

import com.crystal.simpletools.SimpleToolsMod;
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

    public static final Block BLAST_FURNACE = register("blast_furnace", Settings.copy(Blocks.STONE));
    /* 参考：丰富生态 https://www.mcmod.cn/class/6198.html */
    public static final Block AZALEA_LOG = register("azalea_log", PillarBlock::new, Settings.copy(Blocks.OAK_LOG));
    public static final Block AZALEA_LEAVES = register("azalea_leaves", LeavesBlock::new, Settings.copy(Blocks.AZALEA_LEAVES));

    /**
     * 如果是简单方块，不用新建方块类，直接采用Block方块注册即可
     */
    public static Block register(String name, Settings settings) {
        return register(name, Block::new, settings);
    }

    public static Block register(String name, Function<Settings, Block> factory, Settings settings) {
        RegistryKey<Block> key = RegistryKey.of(RegistryKeys.BLOCK, SimpleToolsMod.of(name));
        Block block = Blocks.register(key, factory, settings);
        Items.register(block);
        return block;
    }

    public static void init() {

    }
}
