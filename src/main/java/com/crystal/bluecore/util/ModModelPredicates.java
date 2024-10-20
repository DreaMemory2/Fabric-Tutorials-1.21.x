package com.crystal.bluecore.util;

import com.crystal.bluecore.BlueCore;
import com.crystal.bluecore.registry.ModItems;
import com.crystal.bluecore.registry.component.ModDataComponentTypes;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
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
    }
}
