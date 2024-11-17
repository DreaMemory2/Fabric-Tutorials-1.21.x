package com.crystal.bluecore.registry;

import com.crystal.bluecore.BlueCore;
import com.crystal.bluecore.screenhandler.BasicFluidTankScreenHandler;
import com.crystal.bluecore.screenhandler.OakChestScreenHandler;
import com.crystal.bluecore.util.BlockPosPayload;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class ModScreenHandlerTypes {
    /**
     * <p>注册屏幕处理器</p>
     * <p>将扩展屏幕处理器{@link OakChestScreenHandler}，通过扩展屏幕处理器工厂生产对应屏幕处理器</p>
     *
     * @param id      屏幕处理器的ID
     * @param factory 用于生产扩展屏幕处理器的工厂
     * @param codec   打包类型的编解码器
     * @param <T>     屏幕处理器
     * @param <D>     自定义有效载荷（对于接收者有用的数据）
     * @return 注册成功
     */
    private static <T extends ScreenHandler, D extends CustomPayload> ExtendedScreenHandlerType<T, D> register(String id, ExtendedScreenHandlerType.ExtendedFactory<T, D> factory, PacketCodec<? super RegistryByteBuf, D> codec) {
        return Registry.register(Registries.SCREEN_HANDLER, Identifier.of(BlueCore.MOD_ID, id), new ExtendedScreenHandlerType<>(factory, codec));
    }    public static final ScreenHandlerType<OakChestScreenHandler> OAK_CHEST_INVENTORY_SCREEN_HANDLER =
            register("oak_chest_inventory", OakChestScreenHandler::new, BlockPosPayload.PACKET_CODEC);
    public static final ScreenHandlerType<BasicFluidTankScreenHandler> BASIC_FLUID_TANK_SCREEN_HANDLER =
            register("basic_fluid_tank", BasicFluidTankScreenHandler::new, BlockPosPayload.PACKET_CODEC);



    public static void registerModScreenHandlerTypesInfo() {
        BlueCore.LOGGER.info(BlueCore.MOD_ID + ": Registered Mod Screen Handler Type Success");
    }


}
