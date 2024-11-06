package com.crystal.bluecore;

import com.crystal.bluecore.model.OakChestModel;
import com.crystal.bluecore.particle.PinkFlameParticle;
import com.crystal.bluecore.registry.ModBlockEntities;
import com.crystal.bluecore.registry.ModBlocks;
import com.crystal.bluecore.registry.ModParticleTypes;
import com.crystal.bluecore.registry.ModScreenHandlerTypes;
import com.crystal.bluecore.renderer.BasicFluidTankRenderer;
import com.crystal.bluecore.renderer.OakChestInventoryRender;
import com.crystal.bluecore.renderer.SpearProjectileEntityRenderer;
import com.crystal.bluecore.screen.BasicFluidTankScreen;
import com.crystal.bluecore.screen.OakChestInventoryBlockScreen;
import com.crystal.bluecore.util.ModModelPredicates;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class BlueCoreClient implements ClientModInitializer {
    /**
     * 在客户端环境中运行模块初始化程序。
     */
    @Override
    public void onInitializeClient() {
        // 渲染图层
        renderLayers();
        // 初始化模型预测器
        ModModelPredicates.registerModelPredicates();
        // Moder Layers 模型图层
        EntityModelLayerRegistry.registerModelLayer(OakChestModel.LAYER_LOCATION, OakChestModel::getTexturedModelData);

        // Bind Screens to Handlers 屏幕和处理器绑定
        HandledScreens.register(ModScreenHandlerTypes.OAK_CHEST_INVENTORY_SCREEN_HANDLER, OakChestInventoryBlockScreen::new);
        HandledScreens.register(ModScreenHandlerTypes.BASIC_FLUID_TANK_SCREEN_HANDLER, BasicFluidTankScreen::new);

        // Block Entity Render 方块实体渲染
        BlockEntityRendererFactories.register(ModBlockEntities.OAK_CHEST_BLOCK_ENTITY, OakChestInventoryRender::new);
        BlockEntityRendererFactories.register(ModBlockEntities.BASIC_FLUID_TANK_BLOCK_ENTITY, BasicFluidTankRenderer::new);
        // Entity Renders 钻石矛实体渲染
        EntityRendererRegistry.register(ModBlockEntities.SPEAR, dispatcher -> new SpearProjectileEntityRenderer(dispatcher, MinecraftClient.getInstance().getItemRenderer()));
        // 渲染粉红色火焰粒子
        ParticleFactoryRegistry.getInstance().register(ModParticleTypes.PINK_FLAME, PinkFlameParticle.Factory::new);
    }

    public void renderLayers() {
        // 渲染粉红色宝石门和活把门的空白纹理
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.PINK_GEMSTONE_DOOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.PINK_GEMSTONE_TRAPDOOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BASIC_FLUID_TANK, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.PINK_TORCH, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WALL_PINK_TORCH, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.MAPLE_SAPLING, RenderLayer.getCutout());
        // 渲染晶体材质
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SMALL_FROST_BUD, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.MEDIUM_FROST_BUD, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LARGE_FROST_BUD, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.FROST_CLUSTER, RenderLayer.getCutout());
    }
}
