package com.crystal.simpletools.item;

import com.crystal.simpletools.SimpleToolsMod;
import net.minecraft.item.equipment.EquipmentAsset;
import net.minecraft.registry.RegistryKey;

/**
* ClassName: Equipment Asset Keys<br>
* Description: <br>
* Datetime: 2025/6/5 18:34<br>
* @author Crystal
* @version 1.0
* @since 1.0
*/
public interface EquipmentAssetKeys {

    RegistryKey<EquipmentAsset> AMETHYST = register("amethyst");

    static RegistryKey<EquipmentAsset> register(String name) {
        return RegistryKey.of(net.minecraft.item.equipment.EquipmentAssetKeys.REGISTRY_KEY, SimpleToolsMod.of(name));
    }
}
