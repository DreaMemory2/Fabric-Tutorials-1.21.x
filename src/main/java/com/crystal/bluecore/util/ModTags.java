package com.crystal.bluecore.util;

import com.crystal.bluecore.BlueCore;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ModTags {
    public static class Blocks {
        private static TagKey<Block> createItemTags(String name) {
            // 注册方块标签
            return TagKey.of(RegistryKeys.BLOCK, Identifier.of(BlueCore.MOD_ID, name));
        }
    }
    // 物品标签
    public static class Items {
        public static final TagKey<Item> GEMSTONE_ITEM = createItemTags("gemstone_item");
        private static TagKey<Item> createItemTags(String name) {
            // 注册物品标签
            return TagKey.of(RegistryKeys.ITEM, Identifier.of(BlueCore.MOD_ID, name));
        }
    }
}
