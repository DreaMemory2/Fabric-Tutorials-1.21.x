package com.crystal.simpletools.registry;

import com.crystal.simpletools.SimpleToolsMod;
import com.crystal.simpletools.block.ModBlocks;
import com.crystal.simpletools.item.ModItems;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;

/**
* ClassName: Mod Item Group<br>
* Description: <br>
* Datetime: 2025/6/3 12:58<br>
* @author Crystal
* @version 1.0
* @since 1.0
*/
public class ModItemGroups {

    public static final ItemGroup SIMPLE_TOOL = Registry.register(Registries.ITEM_GROUP,
            SimpleToolsMod.of("simple_tool_groups"), FabricItemGroup.builder()
                    .icon(() -> new ItemStack(ModItems.ENTROPY_MANIPULATOR))
                    .displayName(Text.translatable("itemgroup.simpletools.simple_groups"))
                    .entries(groups()).build());

    private static ItemGroup.EntryCollector groups() {
        return (displayContext, containers) -> {
            /* Blocks */
            containers.add(ModBlocks.AZALEA_LEAVES);
            containers.add(ModBlocks.AZALEA_LOG);
            containers.add(ModBlocks.BLAST_FURNACE);

            /* Items */
            containers.add(ModItems.ENTROPY_MANIPULATOR);
            containers.add(ModItems.DIAMOND_APPLE_CORE);
            containers.add(ModItems.DIAMOND_APPLE);
        };
    }

    public static void init() {

    }
}
