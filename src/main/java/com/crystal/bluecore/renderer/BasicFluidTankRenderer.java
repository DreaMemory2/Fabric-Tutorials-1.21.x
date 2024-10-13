package com.crystal.bluecore.renderer;

import com.crystal.bluecore.block.entity.BasicFluidTankBlockEntity;
import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.base.SingleFluidStorage;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

public class BasicFluidTankRenderer implements BlockEntityRenderer<BasicFluidTankBlockEntity> {
    private final BlockEntityRendererFactory.Context context;

    public BasicFluidTankRenderer(BlockEntityRendererFactory.Context context) {
        this.context = context;
    }

    private static void drawQuadSide(VertexConsumer vertexConsumer, MatrixStack.Entry entry,
                                     float x1, float y1, float z1,
                                     float x2, float y2, float z2,
                                     float minU, float minV, float maxU, float maxV,
                                     int color, int light, int overlay
                                 ) {
        vertexConsumer.vertex(entry, x1, y1, z1)
                .color(color)
                .texture(minU, minV)
                .light(light)
                .overlay(overlay)
                .normal(0.0f, 1.0f, 0.0f);
        vertexConsumer.vertex(entry, x1, y2, z1)
                .color(color)
                .texture(minU, maxV)
                .light(light)
                .overlay(overlay)
                .normal(0.0f, 1.0f, 0.0f);
        vertexConsumer.vertex(entry, x2, y2, z2)
                .color(color)
                .texture(maxU, maxV)
                .light(light)
                .overlay(overlay)
                .normal(0.0f, 1.0f, 0.0f);
        vertexConsumer.vertex(entry, x2, y1, z2)
                .color(color)
                .texture(maxU, minV)
                .light(light)
                .overlay(overlay)
                .normal(0.0f, 1.0f, 0.0f);
    }

    /* 渲染液体的方法 */
    @Override
    public void render(BasicFluidTankBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        // Draw fluid quads
        // 获取液体
        SingleFluidStorage fluidTank = entity.getFluidTank();
        // 如果储罐里的液体为空则不渲染
        if (fluidTank.isResourceBlank() || fluidTank.amount <= 0) {
            return;
        }

        // 获取液体类型、数量和容量
        FluidVariant fluidVariant = fluidTank.getResource();
        long amount = fluidTank.getAmount();
        long capacity = fluidTank.getCapacity();
        // 获取储罐填充的半分比
        float fillPercentage = (float) amount / capacity;
        // 获取最大填充量
        fillPercentage = MathHelper.clamp(fillPercentage, 0, 1);
        // 获取液体的颜色
        int color = FluidVariantRendering.getColor(fluidVariant, entity.getWorld(), entity.getPos());
        // 获取液体渲染图层（默认状态，动态静止状态）
        Sprite sprite = FluidVariantRendering.getSprites(fluidVariant)[0];
        // 渲染图层单面问题（渲染图层为单面）
        RenderLayer renderLayer = RenderLayer.getEntityTranslucent(sprite.getAtlasId()); // 改为渲染图层为双面
        // RenderLayer renderLayer = RenderLayers.getFluidLayer(fluidVariant.getFluid().getDefaultState());
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(renderLayer);
        // 渲染位置
        float y1 = 1f / 16f;
        float y2 = ((fillPercentage * 14f) / 16f) + y1;

        float minU = sprite.getFrameU(3f / 16f);
        float maxU = sprite.getFrameU(13f / 16f);
        float minV = sprite.getFrameV(y1);
        float maxV = sprite.getFrameV(y2);
        // 可见的
        MatrixStack.Entry entry = matrices.peek();
        // 渲染出矩形
        // Font Face
        drawQuadSide(vertexConsumer, entry,
                3f / 16f, y1, 3f / 16f,
                13f/ 16f, y2, 3f / 16f,
                minU, minV, maxU, maxV, color, light, overlay);
        // Back Face
        // 解决图层重叠问题和
        drawQuadSide(vertexConsumer, entry,
                3f / 16f, y1, 13f / 16f,
                13f/ 16f, y2, 13f / 16f,
                minU, minV, maxU, maxV, color, light, overlay);

        // Left face
        drawQuadSide(vertexConsumer, entry,
                3f / 16f, y1, 3f / 16f,
                3f/ 16f, y2, 13f / 16f,
                minU, minV, maxU, maxV, color, light, overlay);

        // Right face
        drawQuadSide(vertexConsumer, entry,
                13f / 16f, y1, 3f / 16f,
                13f/ 16f, y2, 13f / 16f,
                minU, minV, maxU, maxV, color, light, overlay);

        // Top face
        // 如果液体储罐没有填满
        if (fillPercentage < 1f) {
            minU= sprite.getFrameU(3f /16f);
            minV = sprite.getFrameV(3f / 16f);
            maxU = sprite.getFrameU(13f/ 16f);
            maxV = sprite.getFrameV(13f/ 16f);

            vertexConsumer.vertex(entry, 3f / 16f, y2, 3f / 16f)
                    .color(color)
                    .texture(minU, minV)
                    .light(light)
                    .overlay(overlay)
                    .normal(0.0f, 1.0f, 0.0f);
            vertexConsumer.vertex(entry, 3f / 16f, y2, 13f / 16f)
                    .color(color)
                    .texture(minU, maxV)
                    .light(light)
                    .overlay(overlay)
                    .normal(0.0f, 1.0f, 0.0f);
            vertexConsumer.vertex(entry, 13f / 16f, y2, 13f / 16f)
                    .color(color)
                    .texture(maxU, maxV)
                    .light(light)
                    .overlay(overlay)
                    .normal(0.0f, 1.0f, 0.0f);
            vertexConsumer.vertex(entry, 13f / 16f, y2, 3f / 16f)
                    .color(color)
                    .texture(maxU, minV)
                    .light(light)
                    .overlay(overlay)
                    .normal(0.0f, 1.0f, 0.0f);

        }
    }
}
