package com.crystal.bluecore.trim;

import com.crystal.bluecore.BlueCore;
import com.crystal.bluecore.registry.ModItems;
import net.minecraft.item.Item;
import net.minecraft.item.trim.ArmorTrimPattern;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

public class ModTrimPatterns {
    public static final RegistryKey<ArmorTrimPattern> PINK = RegistryKey.of(RegistryKeys.TRIM_PATTERN, Identifier.of(BlueCore.MOD_ID, "pink"));

    /**
     * <p>注册纹饰样式方法</p>
     * @param context 注册接口
     * @param item 物品：锻造模板
     * @param key 纹饰样式
     */
    private static void register(Registerable<ArmorTrimPattern> context, Item item, RegistryKey<ArmorTrimPattern> key) {
        ArmorTrimPattern trimPattern = new ArmorTrimPattern(key.getValue(), Registries.ITEM.getEntry(item),
                Text.translatable(Util.createTranslationKey("trim_pattern", key.getValue())), false);

        context.register(key, trimPattern);
    }

    public static void bootstrap(Registerable<ArmorTrimPattern> register) {
        // 注册粉红色样式纹饰
        register(register, ModItems.PINK_SMITHING_TEMPLATE, PINK);
    }
}
