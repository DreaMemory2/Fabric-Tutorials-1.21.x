package com.crystal.bluecore.registry;

import com.crystal.bluecore.BlueCore;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroup.EntryCollector;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {
    /**
     * 创建物品创造标签页：模组的ID、命名空间、物品堆({@link ItemStack})
     */
    public static final ItemGroup BLUE_CORE_GROUPS = Registry.register(Registries.ITEM_GROUP,
            Identifier.of(BlueCore.MOD_ID, "blue_core_groups"), FabricItemGroup.builder()
                    // 物品堆：显示为“粉红色宝石块”
                    .icon(() -> new ItemStack(ModBlocks.PINK_GEMSTONE_BLOCK.asItem()))
                    // 信息文本显示
                    .displayName(Text.translatable("itemgroup.bluecore.blue_core_groups"))
                    // 物品内容
                    .entries(groups()).build());
    public static final ItemGroup BLUE_TUTORIAL_GROUPS = Registry.register(Registries.ITEM_GROUP,
            Identifier.of(BlueCore.MOD_ID, "blue_tutorial_groups"), FabricItemGroup.builder()
                    .icon(() -> new ItemStack(ModBlocks.BASIC_FLUID_TANK.asItem()))
                    .displayName(Text.translatable("itemgroup.blue_tutorial_groups"))
                    .entries(tutorial()).build());

    public static void registerItemGroupsInfo() {
        // 发送注册物品创造标签页成功信息
        BlueCore.LOGGER.info("Register Item Groups for " + BlueCore.MOD_ID);
    }

    // 物品创造标签页：蓝色核心物品组
    public static EntryCollector groups() {
        return (displayContext, containers) -> {
            // Items
            containers.add(ModItems.RAW_PINK_GEMSTONE);
            containers.add(ModItems.PINK_GEMSTONE);
            containers.add(ModItems.PINK_TORCH);
            containers.add(ModItems.CHISEL);
            containers.add(ModItems.ONESIES_DISC);
            containers.add(ModItems.OCTOPUS_DISC);
            containers.add(ModItems.BLUEY_THEME_DISC);
            containers.add(ModItems.CAULIFLOWER);
            containers.add(ModItems.STARLIGHT_ASHES);
            containers.add(ModItems.PINK_SMITHING_TEMPLATE);
            containers.add(ModItems.HONEY_BERRIES);
            containers.add(ModItems.MANTIS_SPAWN_EGG);

            containers.add(ModItems.PINK_GEMSTONE_SWORD);
            containers.add(ModItems.PINK_GEMSTONE_PICKAXE);
            containers.add(ModItems.PINK_GEMSTONE_SHOVEL);
            containers.add(ModItems.PINK_GEMSTONE_AXE);
            containers.add(ModItems.PINK_GEMSTONE_HOE);
            containers.add(ModItems.PINK_GEMSTONE_HAMMER);
            containers.add(ModItems.PINK_GEMSTONE_BOW);

            containers.add(ModItems.PINK_GEMSTONE_HELMET);
            containers.add(ModItems.PINK_GEMSTONE_CHESTPLATE);
            containers.add(ModItems.PINK_GEMSTONE_LEGGING);
            containers.add(ModItems.PINK_GEMSTONE_BOOST);
            containers.add(ModItems.PINK_GEMSTONE_HORSE_ARMOR);
            // Blocks
            containers.add(ModBlocks.PINK_GEMSTONE_BLOCK);
            containers.add(ModBlocks.PINK_GEMSTONE_ORE);
            containers.add(ModBlocks.DEEPSLATE_PINK_GEMSTONE_ORE);
            containers.add(ModBlocks.RAW_PINK_GEMSTONE_BLOCK);
            containers.add(ModBlocks.MAGIC_BLOCK);
            containers.add(ModBlocks.PINK_GEMSTONE_LAMP);

            containers.add(ModBlocks.PINK_GEMSTONE_STAIRS);
            containers.add(ModBlocks.PINK_GEMSTONE_SLAB);
            containers.add(ModBlocks.PINK_GEMSTONE_BUTTON);
            containers.add(ModBlocks.PINK_GEMSTONE_PRESSURE_PLATE);
            containers.add(ModBlocks.PINK_GEMSTONE_FENCE);
            containers.add(ModBlocks.PINK_GEMSTONE_FENCE_GATE);
            containers.add(ModBlocks.PINK_GEMSTONE_WALL);
            containers.add(ModBlocks.PINK_GEMSTONE_DOOR);
            containers.add(ModBlocks.PINK_GEMSTONE_TRAPDOOR);
        };
    }

    public static EntryCollector tutorial() {
        return ((displayContext, containers) -> {
            containers.add(ModBlocks.PIG_GENERATOR);
            containers.add(ModBlocks.VERTICAL_EXCAVATION_PROCESSOR);
            containers.add(ModBlocks.OAK_CHEST);
            containers.add(ModBlocks.BASIC_FLUID_TANK);
            containers.add(ModBlocks.EBONY_CRAFTING_TABLE);
            containers.add(ModBlocks.CONVERSION_TABLE);
            // 树木系列
            containers.add(ModBlocks.MAPLE_LOG);
            containers.add(ModBlocks.MAPLE_LEAVES);
            containers.add(ModBlocks.FROZEN_LEAVES);
            containers.add(ModBlocks.FIR_LOG);
            containers.add(ModBlocks.FIR_LEAVES);
            containers.add(ModBlocks.FROZEN_SAPLING);
            containers.add(ModBlocks.FIR_SAPLING);
            // 白色系列
            containers.add(ModBlocks.WHITE_STONE_BRICK);
            containers.add(ModBlocks.FROZEN_DIRT);
            containers.add(ModBlocks.FROST_GRASS_BLOCK);
            containers.add(ModBlocks.FROZEN_STONE);
            // 晶洞系列
            containers.add(ModBlocks.FROST_ICE);
            containers.add(ModBlocks.DENSE_SNOW);
            containers.add(ModBlocks.BUDDING_FROST);
            containers.add(ModBlocks.SMALL_FROST_BUD);
            containers.add(ModBlocks.MEDIUM_FROST_BUD);
            containers.add(ModBlocks.LARGE_FROST_BUD);
            containers.add(ModBlocks.FROST_CLUSTER);

            containers.add(ModItems.NETHERITE_SPEAR);
            containers.add(ModBlocks.MAPLE_SAPLING);
            containers.add(ModItems.ENTROPY_MANIPULATOR);
            containers.add(ModItems.GELID_CRYOTHEUM_BUCKET);
            // 植物
            containers.add(ModBlocks.FROZEN_ROSE);
            containers.add(ModBlocks.FROZEN_DANDELION);
            containers.add(ModBlocks.FROZEN_GRASS);
            containers.add(ModBlocks.FANBRUSH);
        });
    }

}
