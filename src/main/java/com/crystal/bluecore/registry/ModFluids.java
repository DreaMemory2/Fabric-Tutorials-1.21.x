package com.crystal.bluecore.registry;

import com.crystal.bluecore.BlueCore;
import com.crystal.bluecore.fluid.GelidCryotheumFluid;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModFluids {

    public static final FlowableFluid FLOWING_GELID_CRYOTHEUM = register("flowing_gelid_cryotheum", new GelidCryotheumFluid.Flowing());
    public static final FlowableFluid GELID_CRYOTHEUM = register("gelid_cryotheum", new GelidCryotheumFluid.Still());

    /**
     * <p>用于注册可流动液体的方法</p>
     * <p>注册命名空间为：fluid.bluecore.flowable_fluid</p>
     * @param id 液体命名空间
     * @param value 可流动的液体
     * @return 注册可流动液体
     */
    private static FlowableFluid register(String id, FlowableFluid value) {
        return Registry.register(Registries.FLUID, BlueCore.of(id), value);
    }

    public static void registerFluidInfo() {
        BlueCore.LOGGER.info("Register Fluid Information: " + BlueCore.MOD_ID);
    }
}
