package com.crystal.bluecore.renderer;

import com.crystal.bluecore.block.custom.OakChest;
import com.crystal.bluecore.block.entity.OakChestInventoryBlockEntity;
import com.crystal.bluecore.model.OakChestModel;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

// 完整类名：OakChestInventoryBlockEntityRenderer
public class OakChestInventoryRender implements BlockEntityRenderer<OakChestInventoryBlockEntity> {
    private final OakChestModel model;

    public OakChestInventoryRender(BlockEntityRendererFactory.Context context) {
        // 获取模型图层
        this.model = new OakChestModel(context.getLayerModelPart(OakChestModel.LAYER_LOCATION));
    }

    @Override
    public void render(OakChestInventoryBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();
        // 解决渲染模型的位置
        matrices.translate(0.5f, -0.625f, 0.5f);
        // <-- 箱子盖子旋转效果 -->
        int numPlayersOpen = entity.getNumPlayersOpen();
        float lidAngle = entity.getLidAngle();
        ModelPart lid = this.model.getLid();
        // 默认箱子盖子角度（初始化为0）
        float defaultLidAngle = lid.pitch;
        // 最大角度
        double maxAngle = Math.toRadians(90);

        if (numPlayersOpen > 0 && lidAngle < maxAngle) {
            lid.pitch = MathHelper.lerp(tickDelta / 4, lidAngle, (float) maxAngle);
        } else if (numPlayersOpen == 0 && lidAngle > defaultLidAngle) {
            lid.pitch = MathHelper.lerp(tickDelta / 4, lidAngle, defaultLidAngle);
        }
        entity.lidAngle = lid.pitch;

        // 处理旋转角度，出现多个锁头问题
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(switch (entity.getCachedState().get(OakChest.FACING)) {
            case EAST -> 270;
            case SOUTH -> 180;
            case WEST -> 90;
            default -> 0;
        }));

        // 渲染纹理
        this.model.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntitySolid(OakChestModel.TEXTURE)), light, overlay);
        lid.pitch = defaultLidAngle;

        matrices.pop();
    }
}
