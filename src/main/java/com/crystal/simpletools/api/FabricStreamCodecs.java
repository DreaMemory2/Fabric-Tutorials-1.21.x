package com.crystal.simpletools.api;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;

/**
* ClassName: FabricStreamCodecs<br>
* Description: <br>
* Datetime: 2025/5/28 20:26<br>
* @author Crystal
* @version 1.0
* @since 1.0
*/
public class FabricStreamCodecs {

    public static <B extends PacketByteBuf, V extends Enum<V>> PacketCodec<B, V> enumCodec(Class<V> enumClass) {
        return new PacketCodec<>() {
            @Override
            public V decode(B buf) {
                return buf.readEnumConstant(enumClass);
            }

            @Override
            public void encode(B buf, V value) {
                buf.writeEnumConstant(value);
            }
        };
    }
}
