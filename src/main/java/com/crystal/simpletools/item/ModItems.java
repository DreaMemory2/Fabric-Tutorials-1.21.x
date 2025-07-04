package com.crystal.simpletools.item;

import com.crystal.simpletools.SimpleToolsMod;
import com.crystal.simpletools.item.custom.DiamondAppleItem;
import com.crystal.simpletools.item.custom.EntropyManipulatorItem;
import com.crystal.simpletools.item.tool.*;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Settings;
import net.minecraft.item.Items;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

import java.util.function.Function;

/**
 * @author Crystal
 * @version 1.0
 * @since 1.0
 */
public class ModItems {

    public static final Item DIAMOND_APPLE_CORE = register("diamond_apple_core", new Settings());
    public static final Item DIAMOND_APPLE = register("diamond_apple", DiamondAppleItem::new, new Settings());
    public static final Item ENTROPY_MANIPULATOR = register("entropy_manipulator", EntropyManipulatorItem::new, new Settings());
    public static final Item FLUIX_CRYSTAL = register("fluix_crystal", Item::new, new Settings());
    /* Tool */
    public static final Item FLUIX_SWORD = register("fluix_sword", FluixSwordItem::new, new Settings());
    public static final Item FLUIX_SHOVEL = register("fluix_shovel", FluixSpadeItem::new, new Settings());
    public static final Item FLUIX_PICKAXE = register("fluix_pickaxe", FluixPickaxeItem::new, new Settings());
    public static final Item FLUIX_AXE = register("fluix_axe", FluixAxeItem::new, new Settings());
    public static final Item FLUIX_HOE = register("fluix_hoe", FluixHoeItem::new, new Settings());
    /* Armor */
    public static final Item AMETHYST_HELMET = register("amethyst_helmet", new Settings().armor(ModArmorMaterials.AMETHYST, EquipmentType.HELMET));
    public static final Item AMETHYST_CHESTPLATE = register("amethyst_chestplate", new Settings().armor(ModArmorMaterials.AMETHYST, EquipmentType.CHESTPLATE));
    public static final Item AMETHYST_LEGGINGS = register("amethyst_leggings", new Settings().armor(ModArmorMaterials.AMETHYST, EquipmentType.LEGGINGS));
    public static final Item AMETHYST_BOOTS = register("amethyst_boots", new Settings().armor(ModArmorMaterials.AMETHYST, EquipmentType.BOOTS));

    /**
     * 如果是简单物品，不用新建物品类，直接采用Item物品注册即可
     */
    public static Item register(String name, Settings settings) {
        return register(name, Item::new, settings);
    }

    public static Item register(String name, Function<Settings, Item> factory, Settings settings) {
        RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, SimpleToolsMod.of(name));
        return Items.register(key, factory, settings);
    }

    public static void init() {

    }
}
