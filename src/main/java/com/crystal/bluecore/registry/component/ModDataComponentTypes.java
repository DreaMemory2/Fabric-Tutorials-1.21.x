package com.crystal.bluecore.registry.component;

import com.crystal.bluecore.BlueCore;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.function.UnaryOperator;

public class ModDataComponentTypes {
    // 创建Coordinates组件
    public static final ComponentType<BlockPos> COORDINATES =
            register("coordinates", builder -> builder.codec(BlockPos.CODEC));

    /**
     * 注册组件，自定义组件
     */
    public static <T> ComponentType<T> register(String name, UnaryOperator<ComponentType.Builder<T>> builder) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(BlueCore.MOD_ID, name),
                builder.apply(ComponentType.builder()).build());
    }

    public static void registerDataComponentTypesInfo() {
        BlueCore.LOGGER.info(BlueCore.MOD_ID + ": Registering Component Types Success!");
    }
}
