package com.crystal.bluecore.renderer;

import com.crystal.bluecore.block.custom.OakChest;
import com.crystal.bluecore.block.entity.OakChestInventoryBlockEntity;
import com.crystal.bluecore.model.OakChestModel;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

// 完整类名：OakChestInventoryBlockEntityRenderer
public class OakChestInventoryRender implements BlockEntityRenderer<OakChestInventoryBlockEntity> {
    private static final List<ItemTransformation> TRANSFORMATIONS = new ArrayList<>();

    static {
        Random random = ThreadLocalRandom.current();
        for (int index = 0; index < 36; index++) {
            /*
            1 / 16 = 0.625;
            0.5 = width / 2;
            0.5 - (1 / 16) = 0.4375D
            */
            TRANSFORMATIONS.add(
                    new ItemTransformation(
                            (random.nextDouble() - 0.5d) * 0.4375D,
                            (random.nextDouble() - 0.5d) * 0.4375D,
                            random.nextInt(360))
            );
        }
    }
    private final OakChestModel model;

    private final BlockEntityRendererFactory.Context context;

    public OakChestInventoryRender(BlockEntityRendererFactory.Context context) {
        this.context = context;
        // 获取模型图层
        this.model = new OakChestModel(context.getLayerModelPart(OakChestModel.LAYER_LOCATION));
    }

    @Override
    public void render(OakChestInventoryBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        // 开将顶部条目的副本推送到该堆栈中
        matrices.push();
        // 解决渲染模型的位置
        matrices.translate(0.5f, -0.625f, 0.5f);

        // <!-- 箱子盖子旋转效果 -->
        int numPlayersOpen = entity.getNumPlayersOpen();
        float lidAngle = entity.getLidAngle();
        ModelPart lid = this.model.getLid();
        // 默认箱子盖子角度（初始化为0）
        float defaultLidAngle = lid.pitch;
        // 最大角度
        double maxAngle = Math.toRadians(90);

        // 箱子的盖子的旋转速度（角速度）
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

        // <!-- Block Entity Item Rendering -->
        // 用来渲染箱子里方块和物品
        if (entity.lidAngle > 0.1D) {
            SimpleInventory inventory = entity.getInventory();
            World world = entity.getWorld();

            for (int index = 0; index < inventory.getHeldStacks().size(); index++) {
                ItemStack stack = inventory.getStack(index);
                if (stack.isEmpty()) continue;

                ItemTransformation transformation = TRANSFORMATIONS.get(index);
                matrices.push();
                // 物品扁平化
                matrices.translate(transformation.x(), 0.0D, transformation.z());
                // 物品和方块最小化
                matrices.scale(0.325f, 0.325f, 0.325f);
                // 物品随机旋转且静止存放
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(transformation.rotation()));
                // 渲染物品
                this.context.getItemRenderer().renderItem(stack, ModelTransformationMode.FIXED,
                        light, overlay, matrices, vertexConsumers, world, 0);
                matrices.pop();
            }
        }

        // 渲染纹理
        this.model.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntitySolid(OakChestModel.TEXTURE)), light, overlay);
        lid.pitch = defaultLidAngle;
        // 删除此堆栈顶部的条目
        matrices.pop();
    }

    /**
     * 物品转换记录类
     */
    public record ItemTransformation(double x, double z, int rotation) {}
}
