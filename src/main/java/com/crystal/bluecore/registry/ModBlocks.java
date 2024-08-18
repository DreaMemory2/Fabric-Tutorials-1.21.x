package com.crystal.bluecore.registry;

import com.crystal.bluecore.BlueCore;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock.Settings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlocks {
    // 方块 Blocks
    public static final Block PINK_GEMSTONE_BLOCK = registerModBlocks("pink_gemstone_block", new Block(Settings.copy(Blocks.DIAMOND_BLOCK)));
    public static final Block PINK_GEMSTONE_ORE = registerModBlocks("pink_gemstone_ore", new Block(Settings.copy(Blocks.DIAMOND_ORE)));
    public static final Block PINK_GEMSTONE_DEEPSLATE_ORE = registerModBlocks("pink_gemstone_deepslate_ore", new Block(Settings.copy(Blocks.DIAMOND_ORE)));

    // 用于注册方块的方法
    private static Block registerModBlocks(String id, Block block) {
        registerModBlockItems(id, block);
        return Registry.register(Registries.BLOCK, Identifier.of(BlueCore.MOD_ID, id), block);
    }

    // 用于注册方块物品的方法
    private static void registerModBlockItems(String id, Block block) {
        // 方块物品是用于表示方块的一类物品。可以放置为世界中的方块
        // 游戏会在物品和方块之间建立映射关系。一个方块物品只与一个方块对应。
        // 所以首先注册方块物品。
        Registry.register(Registries.ITEM, Identifier.of(BlueCore.MOD_ID, id),
                new BlockItem(block, new Item.Settings()));
    }

    /**
     * @see ModItems#registerModItemsInfo() 原版创造标签页具体汉译
     */
    public static void registerModBlocksInfo() {
        // 发送注册物品成功信息
        BlueCore.LOGGER.info(BlueCore.MOD_ID + ": Registered Mod Blocks Success");
        // 方块组是创造模式物品栏内存储物品的标签页。
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(entries -> {
            // 添加你的方块到创造模式物品栏内
            entries.add(PINK_GEMSTONE_BLOCK);
            entries.add(PINK_GEMSTONE_ORE);
            entries.add(PINK_GEMSTONE_DEEPSLATE_ORE);
        });
    }
}
