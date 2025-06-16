package com.crystal.bluecore.renderer;

import com.crystal.bluecore.block.custom.OakChestBlock;
import com.crystal.bluecore.block.entity.OakChestBlockEntity;
import com.crystal.bluecore.model.OakChestModel;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

/**
 * @author Crystal
 * @version 1.0
 * @since 1.0
 */
public class OakChestRenderer implements BlockEntityRenderer<OakChestBlockEntity> {
    // 箱子模型
    private final OakChestModel model;

    public OakChestRenderer(BlockEntityRendererFactory.Context context) {
        // 获取模型图层的位置
        model = new OakChestModel(context.getLayerModelPart(OakChestModel.LAYER_LOCATION));
    }

    @Override
    public void render(OakChestBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        // 向栈压入一个新的矩阵
        matrices.push();

        /* 矫正箱子的位置 */
        matrices.translate(0.5, 0, 0.5);

        /* 玩家是否打开箱子 */
        int isPlayersOpen = entity.isPlayersOpen();
        /* 箱子模型的盖子旋转角度 */
        float lidAngle = entity.getLidAngle();
        /* 箱子模型的盖子 */
        ModelPart lid = model.getLid();
        /* 箱子模型的盖子默认角度 */
        float defaultLidAngle = lid.pitch;
        /* 箱子模型的盖子最大旋转90度 */
        double maxAngle = Math.toRadians(90);

        /* 箱子旋转和朝向问题 */
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(switch (entity.getCachedState().get(OakChestBlock.FACING)) {
            case EAST -> 270;
            case SOUTH -> 180;
            case WEST -> 90;
            default -> 0;
        }));

        /* 执行箱子盖子动画效果 */
        if (isPlayersOpen > 0 && lidAngle < maxAngle) {
            // 当玩家右键点击箱子模型，则打开盖子
            lid.pitch = MathHelper.lerp(tickDelta / 4, lidAngle, (float) maxAngle);
        } else if (isPlayersOpen == 0 && lidAngle > defaultLidAngle) {
            // 当玩家推出箱子界面，则合上盖子
            lid.pitch = MathHelper.lerp(tickDelta / 4, lidAngle, defaultLidAngle);
        }
        entity.lidAngle = lid.pitch;

        /* 获取方块纹理 */
        model.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntitySolid(OakChestModel.TEXTURE)), light, overlay);
        /* 初始化箱子的盖子 */
        lid.pitch = defaultLidAngle;

        // 从栈中弹出一个矩阵
        matrices.pop();
    }
}
