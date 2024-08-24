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
                    .entries(groups())
                    .build());

    public static void registerItemGroupsInfo() {
        // 发送注册物品创造标签页成功信息
        BlueCore.LOGGER.info("Register Item Groups for " + BlueCore.MOD_ID);
    }

    // 物品创造标签页：蓝色核心物品组
    public static EntryCollector groups () {
        return (displayContext, containers) -> {
            // Items
            containers.add(ModItems.RAW_PINK_GEMSTONE);
            containers.add(ModItems.PINK_GEMSTONE);
            // Blocks
            containers.add(ModBlocks.PINK_GEMSTONE_BLOCK);
            containers.add(ModBlocks.PINK_GEMSTONE_ORE);
            containers.add(ModBlocks.DEEPSLATE_PINK_GEMSTONE_ORE);
        };
    }
}
