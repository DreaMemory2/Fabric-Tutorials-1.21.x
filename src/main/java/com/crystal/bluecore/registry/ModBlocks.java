package com.crystal.bluecore.registry;

import com.crystal.bluecore.BlueCore;
import com.crystal.bluecore.block.ModSaplingGenerator;
import com.crystal.bluecore.block.custom.*;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock.Settings;
import net.minecraft.block.*;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class ModBlocks {
    // 方块 Blocks
    public static final Block PINK_GEMSTONE_BLOCK = registerBlock("pink_gemstone_block", new Block(Settings.copy(Blocks.DIAMOND_BLOCK)));
    public static final Block PINK_GEMSTONE_ORE = registerBlock("pink_gemstone_ore", new Block(Settings.copy(Blocks.DIAMOND_ORE)));
    public static final Block DEEPSLATE_PINK_GEMSTONE_ORE = registerBlock("deepslate_pink_gemstone_ore", new Block(Settings.copy(Blocks.DIAMOND_ORE)));
    public static final Block RAW_PINK_GEMSTONE_BLOCK = registerBlock("raw_pink_gemstone_block", new Block(Settings.copy(Blocks.RAW_IRON_BLOCK)));
    public static final Block MAGIC_BLOCK = registerBlock("magic_block", new MagicBlock(Settings.copy(Blocks.IRON_BLOCK).sounds(ModSounds.MAGIC_BLOCK_SOUNDS)));
    public static final Block PIG_GENERATOR = registerBlock("pig_generator", new PigGenerator(
            Settings.create().strength(1.5F, 6.0F).requiresTool()));
    public static final Block VERTICAL_EXCAVATION_PROCESSOR = registerBlock("vertical_excavation_processor",
            new VerticalExcavationProcessor(Settings.create().strength(1.5F, 6.0F).requiresTool()));
    public static final Block WHITE_STONE_BRICK = registerBlock("white_stone_bricks", new Block(Settings.copy(Blocks.STONE_BRICKS)));
    public static final Block FROST_ICE = registerBlock("frost_ice", new Block(Settings.copy(Blocks.SMOOTH_BASALT)));
    public static final Block DENSE_SNOW = registerBlock("dense_snow", new Block(Settings.copy(Blocks.CALCITE)));
    public static final Block BUDDING_FROST = registerBlock("budding_frost", new BuddingFrostBlock(Settings.copy(Blocks.BUDDING_AMETHYST)));
    public static final Block FROST_GRASS_BLOCK = registerBlock("frost_grass_block", new FrostGrassBlock(Settings.copy(Blocks.GRASS_BLOCK)));
    public static final Block FROZEN_DIRT = registerBlock("frozen_dirt", new Block(Settings.copy(Blocks.DIRT)));
    public static final Block FROZEN_STONE = registerBlock("frozen_stone", new Block(Settings.copy(Blocks.STONE)));
    // 一些装饰方块，例如：楼梯，半砖，按钮，压力板，栅栏，栅栏门，石墙，铁门，活把门
    public static final Block PINK_GEMSTONE_STAIRS = registerBlock("pink_gemstone_stair",
            new StairsBlock(ModBlocks.PINK_GEMSTONE_BLOCK.getDefaultState(), Settings.create().strength(2f).requiresTool()));
    public static final Block PINK_GEMSTONE_SLAB = registerBlock("pink_gemstone_slab",
            new SlabBlock(Settings.copy(ModBlocks.PINK_GEMSTONE_STAIRS)));
    public static final Block PINK_GEMSTONE_BUTTON = registerBlock("pink_gemstone_button",
            new ButtonBlock(BlockSetType.IRON, 2, Settings.copy(ModBlocks.PINK_GEMSTONE_STAIRS).noCollision()));
    public static final Block PINK_GEMSTONE_PRESSURE_PLATE = registerBlock("pink_gemstone_pressure_plate",
            new PressurePlateBlock(BlockSetType.IRON, Settings.copy(ModBlocks.PINK_GEMSTONE_STAIRS)));
    public static final Block PINK_GEMSTONE_FENCE = registerBlock("pink_gemstone_fence",
            new FenceBlock(Settings.copy(ModBlocks.PINK_GEMSTONE_STAIRS)));
    public static final Block PINK_GEMSTONE_FENCE_GATE = registerBlock("pink_gemstone_fence_gate",
            new FenceGateBlock(WoodType.OAK, Settings.copy(ModBlocks.PINK_GEMSTONE_STAIRS)));
    public static final Block PINK_GEMSTONE_WALL = registerBlock("pink_gemstone_wall",
            new WallBlock(Settings.copy(ModBlocks.PINK_GEMSTONE_STAIRS)));
    public static final Block PINK_GEMSTONE_DOOR = registerBlock("pink_gemstone_door",
            new DoorBlock(BlockSetType.IRON, Settings.copy(ModBlocks.PINK_GEMSTONE_STAIRS).nonOpaque()));
    public static final Block PINK_GEMSTONE_TRAPDOOR = registerBlock("pink_gemstone_trapdoor",
            new TrapdoorBlock(BlockSetType.IRON, Settings.copy(ModBlocks.PINK_GEMSTONE_STAIRS).nonOpaque()));
    // 自定义树需要方块：原木，树叶和树苗
    public static final Block MAPLE_LOG = registerBlock("maple_log", new PillarBlock(Settings.copy(Blocks.OAK_LOG)));
    public static final Block MAPLE_LEAVES = registerBlock("maple_leaves", new LeavesBlock(Settings.copy(Blocks.OAK_LEAVES)));
    public static final Block MAPLE_SAPLING = registerBlock("maple_sapling", new SaplingBlock(ModSaplingGenerator.MAPLE, Settings.copy(Blocks.OAK_SAPLING)));
    public static final Block FROZEN_LEAVES = registerBlock("frozen_leaves", new PillarBlock(Settings.copy(Blocks.OAK_LEAVES)));
    public static final Block FIR_LEAVES = registerBlock("fir_leaves", new LeavesBlock(Settings.copy(Blocks.SPRUCE_LEAVES)));
    public static final Block FIR_LOG = registerBlock("fir_log", new PillarBlock(Settings.copy(Blocks.SPRUCE_LOG)));
    public static final Block FROZEN_SAPLING = registerBlock("frozen_sapling", new SaplingBlock(ModSaplingGenerator.FROZEN, Settings.copy(Blocks.SPRUCE_SAPLING)));
    public static final Block FIR_SAPLING = registerBlock("fir_sapling", new SaplingBlock(ModSaplingGenerator.FIR, Settings.copy(Blocks.OAK_SAPLING)));
    // 粉红色红石灯
    public static final Block PINK_GEMSTONE_LAMP = registerBlock("pink_gemstone_lamp",
            new PinkGemStoneLamp(Settings.copy(Blocks.REDSTONE_LAMP)
                    // 设置粉色宝石灯的亮度（当粉色宝石灯被点击时），最大为15，最小为0
                    .luminance(state -> state.get(PinkGemStoneLamp.CLICKED) ? 15 : 0)));
    // <!-- 不规则物品 -->
    // 橡木箱子（Size: 14 * 14 * 14）
    public static final Block OAK_CHEST = registerBlock("oak_chest", new OakChestBlock(Settings.copy(Blocks.CHEST)));
    // 基础液体储罐（Size: 12 * 12 * 16）
    public static final Block BASIC_FLUID_TANK = registerBlock("basic_fluid_tank", new BasicFluidTank(Settings.copy(Blocks.IRON_BLOCK)));
    public static final Block EBONY_CRAFTING_TABLE = registerBlock("ebony_crafting_table", new Block(Settings.copy(Blocks.CRAFTING_TABLE)));
    // 火把模型
    public static final Block PINK_TORCH = registerBlock("pink_torch", new TorchBlock(ModParticleTypes.PINK_FLAME,
            Settings.create().noCollision().breakInstantly().luminance(state -> 14).sounds(BlockSoundGroup.WOOD).pistonBehavior(PistonBehavior.DESTROY)));
    public static final Block WALL_PINK_TORCH = registerWithoutBlockItem("wall_pink_torch", new WallTorchBlock(ModParticleTypes.PINK_FLAME,
            Settings.create().noCollision().breakInstantly().luminance(state -> 14).sounds(BlockSoundGroup.WOOD).dropsLike(PINK_TORCH).pistonBehavior(PistonBehavior.DESTROY)));
    // 练习onSteppedOn()方法
    public static final Block CONVERSION_TABLE = registerBlock("conversion_table", new ConversionTableBlock(Settings.copy(Blocks.IRON_BLOCK)));
    // 自定义液体
    public static final Block GELID_CRYOTHEUM = registerWithoutBlockItem("gelid_cryotheum", new FluidBlock(ModFluids.GELID_CRYOTHEUM,
            Settings.create().replaceable().noCollision().strength(100.0F).pistonBehavior(PistonBehavior.DESTROY).dropsNothing().liquid().sounds(BlockSoundGroup.INTENTIONALLY_EMPTY)));
    // 晶洞中的晶体芽
    public static final Block SMALL_FROST_BUD = registerBlock("small_frost_bud", new FrostClusterBlock(3.0F, 4.0F, Settings.copy(Blocks.SMALL_AMETHYST_BUD)));
    public static final Block MEDIUM_FROST_BUD = registerBlock("medium_frost_bud", new FrostClusterBlock(4.0F, 3.0F, Settings.copy(Blocks.MEDIUM_AMETHYST_BUD)));
    public static final Block LARGE_FROST_BUD = registerBlock("large_frost_bud", new FrostClusterBlock(5.0F, 3.0F, Settings.copy(Blocks.LARGE_AMETHYST_BUD)));
    public static final Block FROST_CLUSTER = registerBlock("frost_cluster", new FrostClusterBlock(7.0F, 3.0F, Settings.copy(Blocks.AMETHYST_CLUSTER)));
    // 植物
    public static final Block FROZEN_ROSE = registerBlock("frozen_rose", new FlowerBlock(StatusEffects.NIGHT_VISION, 5.0F,  Settings.copy(Blocks.POPPY)));
    public static final Block FROZEN_DANDELION = registerBlock("frozen_dandelion", new FlowerBlock(StatusEffects.SATURATION, 0.35F, Settings.copy(Blocks.DANDELION)));
    public static final Block FROZEN_GRASS = registerBlock("frozen_grass", new ShortPlantBlock(Settings.copy(Blocks.SHORT_GRASS)));
    public static final Block FANBRUSH = registerBlock("fanbrush", new ShortPlantBlock(Settings.copy(Blocks.FERN)));
    public static final Block HONEY_BERRY_BUSH = registerWithoutBlockItem("honey_berry_bush", new HoneyBerryBushBlock(Settings.copy(Blocks.SWEET_BERRY_BUSH)));

    // 用于注册方块的方法
    private static Block registerBlock(String id, Block block) {
        registerModBlockItems(id, block);
        return Registry.register(Registries.BLOCK, Identifier.of(BlueCore.MOD_ID, id), block);
    }
    // 注册没有物品形式方块
    private static Block registerWithoutBlockItem(String id, Block block) {
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
    public static void registerBlockInfo() {
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
