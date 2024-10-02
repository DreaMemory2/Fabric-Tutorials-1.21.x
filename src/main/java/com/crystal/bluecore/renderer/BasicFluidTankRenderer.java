package com.crystal.bluecore.renderer;

import com.crystal.bluecore.block.entity.BasicFluidTankBlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;

public class BasicFluidTankRenderer implements BlockEntityRenderer<BasicFluidTankBlockEntity> {
    private final BlockEntityRendererFactory.Context context;

    public BasicFluidTankRenderer(BlockEntityRendererFactory.Context context) {
        this.context = context;
    }

    @Override
    public void render(BasicFluidTankBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();

        // TODO: Draw fluid quads

        matrices.pop();
    }
}
