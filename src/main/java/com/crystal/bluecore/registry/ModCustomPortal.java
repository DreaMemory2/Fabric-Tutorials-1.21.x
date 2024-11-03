package com.crystal.bluecore.registry;

import com.crystal.bluecore.BlueCore;
import net.kyrptonaught.customportalapi.api.CustomPortalBuilder;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

public class ModCustomPortal {
    /**
     * <p>注册传送门</p>
     * <ol>
     *     <li>frameBlock：传送门框架</li>
     *     <li>lightWithItem：点燃传说门物品</li>
     *     <li>destDimID：前往的维度</li>
     *     <li>tintColor：传送门方块的颜色</li>
     * </ol>
     */
    public static void registerPortal() {
        CustomPortalBuilder.beginPortal()
                .frameBlock(ModBlocks.WHITE_STONE_BRICK)
                .lightWithFluid(Fluids.WATER)
                .destDimID(Identifier.of(BlueCore.MOD_ID, "snowflake_crystal"))
                .tintColor(0xD1F1FF)
                .registerPortal();
    }
}
