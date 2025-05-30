package com.crystal.simpletools.recipe.entropy;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringIdentifiable;

/**
 * @author Crystal
 * @version 1.0
 * @since 1.0
 */
public enum EntropyMode implements StringIdentifiable {
    HEAT("heat"),
    COOL("cool");

    public static final Codec<EntropyMode> CODEC = StringIdentifiable.createCodec(EntropyMode::values);

    private final String serializedName;

    EntropyMode(String serializedName) {
        this.serializedName = serializedName;
    }

    @Override
    public String asString() {
        return serializedName;
    }
}
