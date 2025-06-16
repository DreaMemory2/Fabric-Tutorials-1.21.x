package com.crystal.bluecore.registry.trim;

import com.crystal.bluecore.BlueCore;
import com.crystal.bluecore.registry.ModItems;
import net.minecraft.item.Item;
import net.minecraft.item.trim.ArmorTrimMaterial;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.Map;

public class ModTrimMaterials {
    // 纹饰名称
    public static final RegistryKey<ArmorTrimMaterial> PINK_GEMSTONE = RegistryKey.of(RegistryKeys.TRIM_MATERIAL, Identifier.of(BlueCore.MOD_ID, "pink_gemstone"));

    /**
     *
     * @param registerable 注册接口
     * @param armorTrimKey 盔甲纹饰材料
     * @param item 模板材质
     * @param style 样式
     * @param itemModelIndex 模型
     */
    private static void register(Registerable<ArmorTrimMaterial> registerable, RegistryKey<ArmorTrimMaterial> armorTrimKey,
                                 RegistryEntry<Item> item, Style style, float itemModelIndex) {
        ArmorTrimMaterial trimMaterial = new ArmorTrimMaterial(armorTrimKey.getValue().getPath(), item, itemModelIndex, Map.of(),
                Text.translatable(Util.createTranslationKey("trim_material", armorTrimKey.getValue())).fillStyle(style));

        registerable.register(armorTrimKey, trimMaterial);
    }

    public static void bootstrap(Registerable<ArmorTrimMaterial> register) {
        // 粉红色盔甲纹饰；粉红色宝石物品；样式格式，文本颜色为#FF51F4，物品模型谓词下标
        register(register, PINK_GEMSTONE, Registries.ITEM.getEntry(ModItems.PINK_GEMSTONE),
                Style.EMPTY.withColor(TextColor.parse("#FF51F4").getOrThrow()), 1.0f);
    }
}
