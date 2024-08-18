package com.crystal.bluecore.registry;

import com.crystal.bluecore.BlueCore;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {

    public static final Item ITEM = new Item(new Item.Settings());
    public static final Item RAW_PINK_GEMSTONE = registerModItems("raw_pink_gemstone", ITEM);
    public static final Item PINK_GEMSTONE = registerModItems("pink_gemstone", ITEM);

    // 提供注册你的物品方法（物品ID，物品类）
    private static Item registerModItems(String id, Item item) {
        // 注册物品的ID和模组命名空间“Bluecore”
        return Registry.register(Registries.ITEM, Identifier.of(BlueCore.MOD_ID, id), item);
    }

    // 发送注册物品成功信息

    /**
     * -- 以下是原版创造标签页 --
     * 1. Hotbar 已保存的快捷栏
     * 2. Item Search 搜索栏
     * 3. Building Blocks 建筑方块
     * 4. Colored Blocks 染色方块
     * 5. Natural Blocks 自然方块
     * 6. Functional Blocks 功能方块
     * 7. Redstone Blocks 红石方块
     * 8. Tools And Utilities工具与实用物品
     * 9. Combat 战斗用品
     * 10. Food And Drinks 食物与饮品
     * 11. Ingredients 原材料
     * 12. Spawn Eggs 刷怪蛋
     * 13. Op Blocks 管理员用品
     * 14. Inventory 生存模式物品栏
     */
    public static void registerModItemsInfo() {
        // 物品组是创造模式物品栏内存储物品的标签页。
        // 这个物品组添加事件处理器，类型于给原版物品组添加物品的方式。
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            // 添加你的物品到创造模式物品栏内
            entries.add(RAW_PINK_GEMSTONE);
            entries.add(PINK_GEMSTONE);
        });
        BlueCore.LOGGER.info("Registered Mod Items Success");
    }
}
