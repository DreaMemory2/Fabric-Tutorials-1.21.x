package com.crystal.bluecore.registry;

import com.crystal.bluecore.BlueCore;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ModTags {
    public static class Blocks {
        public static final TagKey<Block> NEEDS_PINK_GEMSTONE = createBlockTags("needs_pink_gemstone");
        public static final TagKey<Block> INCORRECT_FOR_PINK_GEMSTONE_TOOL = createBlockTags("incorrect_for_pink_gemstone_tool");

        private static TagKey<Block> createBlockTags(String name) {
            // 注册方块标签
            return TagKey.of(RegistryKeys.BLOCK, Identifier.of(BlueCore.MOD_ID, name));
        }
    }
    // 物品标签
    public static class Items {
        public static final TagKey<Item> GEMSTONE_ITEM = createItemTags("gemstone_item");
        public static final TagKey<Item> HAMMER = createItemTags("hammer");

        private static TagKey<Item> createItemTags(String name) {
            // 注册物品标签
            return TagKey.of(RegistryKeys.ITEM, Identifier.of(BlueCore.MOD_ID, name));
        }
    }
}
