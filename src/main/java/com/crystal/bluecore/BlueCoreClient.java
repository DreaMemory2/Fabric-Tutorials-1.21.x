package com.crystal.bluecore;

import com.crystal.bluecore.registry.ModSounds;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;

public class BlueCoreClient implements ClientModInitializer {
    /**
     * 在客户端环境中运行模块初始化程序。
     */
    @Override
    public void onInitializeClient() {
        new RegistryBuilder().addRegistry(RegistryKeys.JUKEBOX_SONG, ModSounds::bootstrap);
    }
}
