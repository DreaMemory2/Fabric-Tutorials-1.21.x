package com.crystal.bluecore.registry;

import com.crystal.bluecore.BlueCore;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

/**
 * <p>通过谓词</p>
 */
// 模型检测器，实现凿子检测功能
public class ModModelPredicates {
    /**
     * 注册模型谓词
     * <p>如果使用这个特定变量，则改变物品纹理</p>
     */
    public static void registerModelPredicates() {
        // 添加凿子预测方块功能，获取coordinates组件来检测该方块，成功则返回1f信号
        ModelPredicateProviderRegistry.register(ModItems.CHISEL, Identifier.of(BlueCore.MOD_ID, "used"),
                (stack, world, entity, seed) -> stack.get(ModDataComponentTypes.COORDINATES) != null ? 1f : 0f);
        // 添加拉弓动作模型
        registerCustomBow(ModItems.PINK_GEMSTONE_BOW);
    }

    public static void registerCustomBow(Item item) {
        // 引用拉弓的动作模型
        ModelPredicateProviderRegistry.register(item, Identifier.ofVanilla("pull"), (stack, world, entity, seed) -> {
            if (entity == null) return 0.0f;
            else return entity.getActiveItem() != stack ? 0.0f : stack.getMaxUseTime(entity) - entity.getItemUseTimeLeft() / 20.0f;
        });
        // 判断是否有拉弓的动作，1.0已经拉弓，1.0F没有拉弓
        ModelPredicateProviderRegistry.register(item, Identifier.ofVanilla("pulling"), (stack, world, entity, seed) ->
            entity != null && entity.isUsingItem() && entity.getActiveItem() == stack ? 1.0f : 0.0F
        );
    }
}
