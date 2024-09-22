package com.crystal.bluecore.registry;

import com.crystal.bluecore.BlueCore;
import com.crystal.bluecore.block.custom.MagicBlock;
import com.crystal.bluecore.block.custom.PigGenerator;
import com.crystal.bluecore.block.custom.PinkGemStoneLamp;
import com.crystal.bluecore.block.custom.VerticalExcavationProcessor;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock.Settings;
import net.minecraft.block.*;
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
    public static final Block DEEPSLATE_PINK_GEMSTONE_ORE = registerModBlocks("deepslate_pink_gemstone_ore", new Block(Settings.copy(Blocks.DIAMOND_ORE)));
    public static final Block RAW_PINK_GEMSTONE_BLOCK = registerModBlocks("raw_pink_gemstone_block", new Block(Settings.copy(Blocks.RAW_IRON_BLOCK)));
    public static final Block MAGIC_BLOCK = registerModBlocks("magic_block", new MagicBlock(Settings.copy(Blocks.IRON_BLOCK)));
    public static final Block PIG_GENERATOR = registerModBlocks("pig_generator", new PigGenerator(
            Settings.create().strength(1.5F, 6.0F).requiresTool()));
    public static final Block VERTICAL_EXCAVATION_PROCESSOR = registerModBlocks("vertical_excavation_processor",
            new VerticalExcavationProcessor(Settings.create().strength(1.5F, 6.0F).requiresTool()));
    // 一些装饰方块，例如：楼梯，半砖，按钮，压力板，栅栏，栅栏门，石墙，铁门，活把门
    public static final Block PINK_GEMSTONE_STAIRS = registerModBlocks("pink_gemstone_stair",
            new StairsBlock(ModBlocks.PINK_GEMSTONE_BLOCK.getDefaultState(), Settings.create().strength(2f).requiresTool()));
    public static final Block PINK_GEMSTONE_SLAB = registerModBlocks("pink_gemstone_slab",
            new SlabBlock(Settings.copy(ModBlocks.PINK_GEMSTONE_STAIRS)));
    public static final Block PINK_GEMSTONE_BUTTON = registerModBlocks("pink_gemstone_button",
            new ButtonBlock(BlockSetType.IRON, 2, Settings.copy(ModBlocks.PINK_GEMSTONE_STAIRS).noCollision()));
    public static final Block PINK_GEMSTONE_PRESSURE_PLATE = registerModBlocks("pink_gemstone_pressure_plate",
            new PressurePlateBlock(BlockSetType.IRON, Settings.copy(ModBlocks.PINK_GEMSTONE_STAIRS)));
    public static final Block PINK_GEMSTONE_FENCE = registerModBlocks("pink_gemstone_fence",
            new FenceBlock(Settings.copy(ModBlocks.PINK_GEMSTONE_STAIRS)));
    public static final Block PINK_GEMSTONE_FENCE_GATE = registerModBlocks("pink_gemstone_fence_gate",
            new FenceGateBlock(WoodType.OAK, Settings.copy(ModBlocks.PINK_GEMSTONE_STAIRS)));
    public static final Block PINK_GEMSTONE_WALL = registerModBlocks("pink_gemstone_wall",
            new WallBlock(Settings.copy(ModBlocks.PINK_GEMSTONE_STAIRS)));
    public static final Block PINK_GEMSTONE_DOOR = registerModBlocks("pink_gemstone_door",
            new DoorBlock(BlockSetType.IRON, Settings.copy(ModBlocks.PINK_GEMSTONE_STAIRS).nonOpaque()));
    public static final Block PINK_GEMSTONE_TRAPDOOR = registerModBlocks("pink_gemstone_trapdoor",
            new TrapdoorBlock(BlockSetType.IRON, Settings.copy(ModBlocks.PINK_GEMSTONE_STAIRS).nonOpaque()));
    public static final Block PINK_GEMSTONE_LAMP = registerModBlocks("pink_gemstone_lamp",
            new PinkGemStoneLamp(Settings.copy(Blocks.REDSTONE_LAMP)
                    // 设置粉色宝石灯的亮度（当粉色宝石灯被点击时），最大为15，最小为0
                    .luminance(state -> state.get(PinkGemStoneLamp.CLICKED) ? 15 : 0)));

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
        // 发送注册方块成功信息
        BlueCore.LOGGER.info(BlueCore.MOD_ID + ": Registered Mod Blocks Success");
        // 方块组是创造模式物品栏内存储物品的标签页。
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(entries -> {
            // 添加你的方块到创造模式物品栏内
            entries.add(PINK_GEMSTONE_BLOCK);
            entries.add(PINK_GEMSTONE_ORE);
            entries.add(DEEPSLATE_PINK_GEMSTONE_ORE);
        });
    }
}
