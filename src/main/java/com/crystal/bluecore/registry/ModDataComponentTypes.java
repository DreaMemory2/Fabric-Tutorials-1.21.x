package com.crystal.bluecore.registry;

import com.crystal.bluecore.BlueCore;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.math.BlockPos;

import java.util.function.UnaryOperator;

public class ModDataComponentTypes {
    // 创建Coordinates组件：3*3位置组件
    public static final ComponentType<BlockPos> COORDINATES =
            register("coordinates", builder -> builder.codec(BlockPos.CODEC));

    /**
     * 注册组件，自定义组件
     */
    public static <T> ComponentType<T> register(String name, UnaryOperator<ComponentType.Builder<T>> builder) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, BlueCore.of(name),
                builder.apply(ComponentType.builder()).build());
    }

    public static void registerDataComponentTypesInfo() {
        BlueCore.LOGGER.info(BlueCore.MOD_ID + ": Registering Component Types Success!");
    }
}
