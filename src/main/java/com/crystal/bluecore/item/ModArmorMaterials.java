package com.crystal.bluecore.item;

import com.crystal.bluecore.BlueCore;
import com.crystal.bluecore.registry.ModItems;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;

public class ModArmorMaterials {
    // 自定义盔甲：例如头盔、胸甲、护腿、靴子
    public static final RegistryEntry<ArmorMaterial> PINK_GEMSTONE_ARMOR = register("pink_gemstone", () ->
        new ArmorMaterial(Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
            map.put(ArmorItem.Type.HELMET, 2);
            map.put(ArmorItem.Type.CHESTPLATE, 6);
            map.put(ArmorItem.Type.LEGGINGS, 6);
            map.put(ArmorItem.Type.BOOTS, 4);
        }), 20, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, () -> Ingredient.ofItems(ModItems.PINK_GEMSTONE),
                List.of(new ArmorMaterial.Layer(Identifier.of(BlueCore.MOD_ID, "pink_gemstone"))), 0, 0));

    public static RegistryEntry<ArmorMaterial> register(String id, Supplier<ArmorMaterial> material) {
        return Registry.registerReference(Registries.ARMOR_MATERIAL, Identifier.of(BlueCore.MOD_ID, id), material.get());
    }
}
