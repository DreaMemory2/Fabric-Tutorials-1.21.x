package com.crystal.bluecore.entity.custom;

import java.util.Arrays;
import java.util.Comparator;

/**
 * <p>添加螳螂变体实体</p>
 * <p>一个是绿色螳螂，另一个是粉色螳螂</p>
 * @author Crystal
 * @version 1.0
 * @since 1.0
 */
public enum MantisVariant {
    // 默认颜色为绿色
    DEFAULT(0),
    // 变种颜色为兰花紫
    ORCHID(1);

    private static final MantisVariant[] VARIANTS = Arrays.stream(values()).sorted(Comparator
            .comparing(MantisVariant::getId)).toArray(MantisVariant[]::new);
    private final int id;

    MantisVariant(int id) {
        this.id = id;
    }

    public static MantisVariant getVariant(int id) {
        return VARIANTS[id % VARIANTS.length];
    }

    public int getId() {
        return id;
    }
}
