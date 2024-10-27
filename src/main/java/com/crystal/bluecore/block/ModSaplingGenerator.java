package com.crystal.bluecore.block;

import com.crystal.bluecore.BlueCore;
import com.crystal.bluecore.registry.worldgen.ModConfigureFeatures;
import net.minecraft.block.SaplingGenerator;
import net.minecraft.util.Identifier;

import java.util.Optional;

/**
 * 树苗构造器：说明树苗成长为树木的变体哪些
 */
public class ModSaplingGenerator {
    public static final SaplingGenerator MAPLE = new SaplingGenerator(
            Identifier.of(BlueCore.MOD_ID, "maple").toString(),
            0.1F, /* 罕见机会概率 */
            Optional.empty(), /* 巨型变体 */
            Optional.empty(), /* 罕见巨型变体 */
            Optional.of(ModConfigureFeatures.MAPLE_TREE), /* 常规变体 */
            Optional.empty(), /* 罕见常规变体 */
            Optional.empty(), /* 带有蜂巢变体 */
            Optional.empty() /* 罕见带有蜂巢变体 */
    );
}
