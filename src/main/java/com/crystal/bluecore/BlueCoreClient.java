package com.crystal.bluecore;

import com.crystal.bluecore.model.MantisModel;
import com.crystal.bluecore.model.OakChestModel;
import com.crystal.bluecore.particle.PinkFlameParticle;
import com.crystal.bluecore.registry.*;
import com.crystal.bluecore.renderer.BasicFluidTankRenderer;
import com.crystal.bluecore.renderer.MantisEntityRenderer;
import com.crystal.bluecore.renderer.OakChestRenderer;
import com.crystal.bluecore.renderer.SpearEntityRenderer;
import com.crystal.bluecore.screen.BasicFluidTankScreen;
import com.crystal.bluecore.screen.OakChestBlockScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
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
        EntityModelLayerRegistry.registerModelLayer(MantisModel.MANTIS, MantisModel::getTexturedModelData);

        // Bind Screens to Handlers 屏幕和处理器绑定
        HandledScreens.register(ModScreenHandlerTypes.OAK_CHEST_SCREEN_HANDLER, OakChestBlockScreen::new);
        HandledScreens.register(ModScreenHandlerTypes.BASIC_FLUID_TANK_SCREEN_HANDLER, BasicFluidTankScreen::new);

        // Block Entity Render 方块实体渲染
        BlockEntityRendererFactories.register(ModBlockEntityType.OAK_CHEST, OakChestRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntityType.BASIC_FLUID_TANK_BLOCK_ENTITY, BasicFluidTankRenderer::new);
        // Entity Renders 实体渲染
        EntityRendererRegistry.register(ModEntity.NETHERITE_SPEAR, SpearEntityRenderer::new);
        EntityRendererRegistry.register(ModEntity.MANTIS, MantisEntityRenderer::new);
        // 渲染粉红色火焰粒子
        ParticleFactoryRegistry.getInstance().register(ModParticleTypes.PINK_FLAME, PinkFlameParticle.Factory::new);
        // 渲染液体材质文件
        FluidRenderHandlerRegistry.INSTANCE.register(ModFluids.GELID_CRYOTHEUM, ModFluids.FLOWING_GELID_CRYOTHEUM, new SimpleFluidRenderHandler(
                BlueCore.of("block/fluid/gelid_cryotheum_still"), // 静态液体材质
                BlueCore.of("block/fluid/gelid_cryotheum_flow") // 动态流体材质
        ));
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

        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(),
                ModBlocks.FROZEN_GRASS,
                ModBlocks.FROZEN_ROSE,
                ModBlocks.FANBRUSH,
                ModBlocks.FIR_LEAVES,
                ModBlocks.FIR_SAPLING,
                ModBlocks.FROZEN_SAPLING,
                ModBlocks.FROZEN_LEAVES,
                ModBlocks.FROZEN_DANDELION,
                ModBlocks.HONEY_BERRY_BUSH
        );

        // 渲染液体效果，半透明材质
        BlockRenderLayerMap.INSTANCE.putFluid(ModFluids.FLOWING_GELID_CRYOTHEUM, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putFluid(ModFluids.GELID_CRYOTHEUM, RenderLayer.getTranslucent());
    }
}
