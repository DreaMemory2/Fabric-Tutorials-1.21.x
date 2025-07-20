package com.crystal.simpletools.recipe.entropy;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringIdentifiable;

/**
 * ClassName: Entropy Mode<br>
 * Description: 熵变机械臂的工作模式，分为加热模式和冷却模式<br>
 * Datetime: 2025/5/27 19:21<br>
 * @author Crystal
 * @version 1.0
 * @since 1.0
 */
public enum EntropyMode implements StringIdentifiable {
    /* 加热模式 */
    HEAT("heat"),
    /* 冷却模式 */
    COOL("cool");

    public static final Codec<EntropyMode> CODEC = StringIdentifiable.createCodec(EntropyMode::values);

    private final String serializedName;

    /**
     * <p>如果是加热模式，则序列化为："mode": "heat"</p>
     * <p>如果是冷却模式。则序列化为："mode": "cool"</p>
     * @param serializedName 可序列化的名称
     */
    EntropyMode(String serializedName) {
        this.serializedName = serializedName;
    }

    @Override
    public String asString() {
        return serializedName;
    }
}
