package com.crystal.bluecore;

import com.crystal.bluecore.registry.ModBlocks;
import com.crystal.bluecore.registry.ModScreenHandlerTypes;
import com.crystal.bluecore.registry.ModSounds;
import com.crystal.bluecore.screen.OakChestInventoryBlockScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;

public class BlueCoreClient implements ClientModInitializer {
    /**
     * 在客户端环境中运行模块初始化程序。
     */
    @Override
    public void onInitializeClient() {
        RegistryBuilder builder = new RegistryBuilder();

        builder.addRegistry(RegistryKeys.JUKEBOX_SONG, ModSounds::bootstrap);

        // Bind Screens to Handlers
        // 屏幕和处理器绑定
        HandledScreens.register(ModScreenHandlerTypes.OAK_CHEST_INVENTORY_SCREEN_HANDLER, OakChestInventoryBlockScreen::new);

        // 渲染粉红色宝石门和活把门的空白纹理
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.PINK_GEMSTONE_DOOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.PINK_GEMSTONE_TRAPDOOR, RenderLayer.getCutout());
    }
}
