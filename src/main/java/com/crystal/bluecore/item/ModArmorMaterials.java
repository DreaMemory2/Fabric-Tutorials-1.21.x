package com.crystal.bluecore.item;

import com.crystal.bluecore.BlueCore;
import com.crystal.bluecore.util.ModTags;
import net.minecraft.item.equipment.ArmorMaterial;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Util;

import java.util.EnumMap;

public class ModArmorMaterials {
    public static final ArmorMaterial PINK_GEMSTONE_ARMOR = new ArmorMaterial(
            500,
            Util.make(new EnumMap<>(EquipmentType.class), map -> {
                map.put(EquipmentType.BOOTS, 2);
                map.put(EquipmentType.LEGGINGS, 4);
                map.put(EquipmentType.CHESTPLATE, 6);
                map.put(EquipmentType.HELMET, 2);
                map.put(EquipmentType.BODY, 4);
            }),
            20,
            SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND,
            0,
            0,
            ModTags.Items.PINK_GEMSTONE_REPAIR,
            BlueCore.of("pink_gemstone"));
}
