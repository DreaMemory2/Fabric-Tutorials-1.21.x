package com.crystal.bluecore.registry;

import com.crystal.bluecore.BlueCore;
import com.crystal.bluecore.item.ModArmorItem;
import com.crystal.bluecore.item.ModArmorMaterials;
import com.crystal.bluecore.item.ModToolMaterials;
import com.crystal.bluecore.item.custom.ChiselItem;
import com.crystal.bluecore.item.custom.EntropyManipulatorItem;
import com.crystal.bluecore.item.custom.HammerItem;
import com.crystal.bluecore.item.custom.SpearItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.*;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.Direction;

import java.util.List;

public class ModItems {
    // 物品 Items
    public static final Item RAW_PINK_GEMSTONE = registerModItems("raw_pink_gemstone",  new Item(new Item.Settings()));
    public static final Item PINK_GEMSTONE = registerModItems("pink_gemstone",  new Item(new Item.Settings()));
    public static final Item CHISEL = registerModItems("chisel", new ChiselItem(new Item.Settings().maxDamage(32)));
    public static final Item ONESIES_DISC = registerModItems("onesies_disc", new Item(
            new Item.Settings().maxCount(1).rarity(Rarity.RARE).jukeboxPlayable(ModSounds.ONESIES)));
    public static final Item OCTOPUS_DISC = registerModItems("octopus_disc", new Item(
            new Item.Settings().maxCount(1).rarity(Rarity.RARE).jukeboxPlayable(ModSounds.OCTOPUS)));
    public static final Item BLUEY_THEME_DISC = registerModItems("bluey_theme_disc", new Item(
            new Item.Settings().maxCount(1).rarity(Rarity.RARE).jukeboxPlayable(ModSounds.BLUEY_THEME)));
    public static final Item CAULIFLOWER = registerModItems("cauliflower", new Item(new Item.Settings().food(ModFoodComponents.CAULIFLOWER)) {
        // 给物品添加文本信息
        @Override
        public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
            tooltip.add(Text.translatable("tooltip.bluecore.cauliflower.tooltip"));
            super.appendTooltip(stack, context, tooltip, type);
        }
    });
    public static final Item STARLIGHT_ASHES = registerModItems("starlight_ashes", new Item(new Item.Settings()));
    // 添加武器与工具，例如：宝剑、镐子、斧子、铲子、锄头、或者是锤子
    public static final Item PINK_GEMSTONE_SWORD = registerModItems("pink_gemstone_sword", new SwordItem(ModToolMaterials.PINK_GEMSTONE,
            new Item.Settings().attributeModifiers(SwordItem.createAttributeModifiers(ModToolMaterials.PINK_GEMSTONE, 3, -2.4f))));
    public static final Item PINK_GEMSTONE_PICKAXE = registerModItems("pink_gemstone_pickaxe", new PickaxeItem(ModToolMaterials.PINK_GEMSTONE,
            new Item.Settings().attributeModifiers(PickaxeItem.createAttributeModifiers(ModToolMaterials.PINK_GEMSTONE, 1, -2.8f))));
    public static final Item PINK_GEMSTONE_AXE = registerModItems("pink_gemstone_axe", new AxeItem(ModToolMaterials.PINK_GEMSTONE,
            new Item.Settings().attributeModifiers(AxeItem.createAttributeModifiers(ModToolMaterials.PINK_GEMSTONE, 6, -3.2f))));
    public static final Item PINK_GEMSTONE_SHOVEL = registerModItems("pink_gemstone_shovel", new ShovelItem(ModToolMaterials.PINK_GEMSTONE,
            new Item.Settings().attributeModifiers(ShovelItem.createAttributeModifiers(ModToolMaterials.PINK_GEMSTONE, 1.5f, -3.0f))));
    public static final Item PINK_GEMSTONE_HOE = registerModItems("pink_gemstone_hoe", new HoeItem(ModToolMaterials.PINK_GEMSTONE,
            new Item.Settings().attributeModifiers(HoeItem.createAttributeModifiers(ModToolMaterials.PINK_GEMSTONE, 0, -3f))));
    public static final Item PINK_GEMSTONE_HAMMER = registerModItems("pink_gemstone_hammer", new HammerItem(ModToolMaterials.PINK_GEMSTONE,
            new Item.Settings().attributeModifiers(PickaxeItem.createAttributeModifiers(ModToolMaterials.PINK_GEMSTONE, 7, -3.4f))));
    // 添加装备：头盔、胸甲、护腿、靴子
    public static final Item PINK_GEMSTONE_HELMET = registerModItems("pink_gemstone_helmet", new ModArmorItem(ModArmorMaterials.PINK_GEMSTONE_ARMOR,
            ArmorItem.Type.HELMET, new Item.Settings().maxDamage(ArmorItem.Type.HELMET.getMaxDamage(15))));
    public static final Item PINK_GEMSTONE_CHESTPLATE = registerModItems("pink_gemstone_chestplate", new ArmorItem(ModArmorMaterials.PINK_GEMSTONE_ARMOR,
            ArmorItem.Type.CHESTPLATE, new Item.Settings().maxDamage(ArmorItem.Type.CHESTPLATE.getMaxDamage(15))));
    public static final Item PINK_GEMSTONE_LEGGING = registerModItems("pink_gemstone_leggings", new ArmorItem(ModArmorMaterials.PINK_GEMSTONE_ARMOR,
            ArmorItem.Type.LEGGINGS, new Item.Settings().maxDamage(ArmorItem.Type.LEGGINGS.getMaxDamage(15))));
    public static final Item PINK_GEMSTONE_BOOST = registerModItems("pink_gemstone_boots", new ArmorItem(ModArmorMaterials.PINK_GEMSTONE_ARMOR,
            ArmorItem.Type.BOOTS, new Item.Settings().maxDamage(ArmorItem.Type.BOOTS.getMaxDamage(15))));
    // 马铠(最大堆叠为1)
    public static final Item PINK_GEMSTONE_HORSE_ARMOR = registerModItems("pink_gemstone_horse_armor", new AnimalArmorItem(ModArmorMaterials.PINK_GEMSTONE_ARMOR,
            AnimalArmorItem.Type.EQUESTRIAN, false, new Item.Settings().maxCount(1)));
    // 盔甲纹饰锻造模板
    public static final Item PINK_SMITHING_TEMPLATE = registerModItems("pink_armor_trim_smithing_template",
            SmithingTemplateItem.of(Identifier.of(BlueCore.MOD_ID, "pink"), FeatureFlags.VANILLA));
    // 粉红色宝石弓
    public static final Item PINK_GEMSTONE_BOW = registerModItems("pink_gemstone_bow", new BowItem(new Item.Settings().maxDamage(10)));
    // 熵变机械臂
    public static final Item ENTROPY_MANIPULATOR = registerModItems("entropy_manipulator", new EntropyManipulatorItem(new Item.Settings()));
    // 自定义流体容器：极寒之凛冰桶：使用配方剩余则返回空桶，最大堆叠数量为1
    public static final Item GELID_CRYOTHEUM_BUCKET = registerModItems("gelid_cryotheum_bucket", new BucketItem(ModFluids.GELID_CRYOTHEUM, new Item.Settings()
            .recipeRemainder(Items.BUCKET).maxCount(1)));

    public static final Item PINK_TORCH = registerModItems("pink_torch",
            new VerticallyAttachableBlockItem(ModBlocks.PINK_TORCH, ModBlocks.WALL_PINK_TORCH, new Item.Settings(), Direction.DOWN));
    public static final Item DIAMOND_SPEAR = registerModItems("diamond_spear", new SpearItem(ToolMaterials.DIAMOND, 7.0f, 1.1f, new Item.Settings().maxCount(1)));
    // 提供注册你的物品方法（物品ID，物品类）
    private static Item registerModItems(String id, Item item) {
        // 注册物品的ID和模组命名空间“Bluecore”
        return Registry.register(Registries.ITEM, Identifier.of(BlueCore.MOD_ID, id), item);
    }

    /**
     * -- 以下是原版创造标签页 --
     * <p>1. Hotbar 已保存的快捷栏</p>
     * <p>2. Item Search 搜索栏</p>
     * <p>3. Building Blocks 建筑方块</p>
     * <p>4. Colored Blocks 染色方块</p>
     * <p>5. Natural Blocks 自然方块</p>
     * <p>6. Functional Blocks 功能方块</p>
     * <p>7. Redstone Blocks 红石方块</p>
     * <p>8. Tools And Utilities工具与实用物品</p>
     * <p>9. Combat 战斗用品</p>
     * <p>10. Food And Drinks 食物与饮品</p>
     * <p>11. Ingredients 原材料</p>
     * <p>12. Spawn Eggs 刷怪蛋</p>
     * <p>13. Op Blocks 管理员用品</p>
     * <p>14. Inventory 生存模式物品栏</p>
     */
    public static void registerModItemsInfo() {
        // 发送注册物品成功信息
        BlueCore.LOGGER.info(BlueCore.MOD_ID + ": Registered Mod Items Success");
        // 物品组是创造模式物品栏内存储物品的标签页。
        // 这个物品组添加事件处理器，类型于给原版物品组添加物品的方式。
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            // 添加你的物品到创造模式物品栏内
            entries.add(RAW_PINK_GEMSTONE);
            entries.add(PINK_GEMSTONE);
        });
    }
}
