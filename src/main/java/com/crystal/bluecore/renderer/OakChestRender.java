package com.crystal.bluecore.renderer;

import com.crystal.bluecore.block.custom.OakChest;
import com.crystal.bluecore.block.entity.OakChestBlockEntity;
import com.crystal.bluecore.model.OakChestModel;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ModelTransformationMode;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

// 完整类名：OakChestInventoryBlockEntityRenderer
public class OakChestRender implements BlockEntityRenderer<OakChestBlockEntity> {
    // 物品位置和旋转角度
    private static final List<ItemTransformation> TRANSFORMATIONS = new ArrayList<>();

    static {
        // 实现物品随机摆放，通过静态上下文表示
        // 当每次调用render方法，则重新随机渲染物品方块，且每一帧都在变化
        Random random = ThreadLocalRandom.current();
        // 所以预先计算物品的位置和旋转
        for (int index = 0; index < 36; index++) {
            /*
            <!-- 0.4375D的由来 -->
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

    // 箱子模型
    private final OakChestModel model;
    // 通过上下文获取物品
    private final BlockEntityRendererFactory.Context context;

    public OakChestRender(BlockEntityRendererFactory.Context context) {
        this.context = context;
        // 获取模型图层的位置
        this.model = new OakChestModel(context.getLayerModelPart(OakChestModel.LAYER_LOCATION));
    }

    @Override
    public void render(OakChestBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        // 创建矩阵堆栈
        matrices.push();
        // 解决渲染模型的位置
        matrices.translate(0.5f, 0.0f, 0.5f);

        // <!-- 箱子盖子旋转效果 -->
        // 获取箱子中的玩家数量，盖子角度和盖子模型
        int numPlayersOpen = entity.getNumPlayersOpen();
        float lidAngle = entity.getLidAngle();
        ModelPart lid = this.model.getLid();
        // 默认箱子盖子角度（初始化为0）
        float defaultLidAngle = lid.pitch;
        // 旋转角度，设置盖子旋转为90度
        // 如果存在盖子反复开或合，则是精度问题导致的
        double maxAngle = Math.toRadians(90);

        // 箱子的盖子的旋转速度（角速度）
        if (numPlayersOpen > 0 && lidAngle < maxAngle) {
            // 打开盖子，其中tickDelta / 4，为了减慢开盖子的速度
            lid.pitch = MathHelper.lerp(tickDelta / 4, lidAngle, (float) maxAngle);
        } else if (numPlayersOpen == 0 && lidAngle > defaultLidAngle) {
            // 合上盖子，如果玩家为0（表示没有玩家正在使用箱子）
            lid.pitch = MathHelper.lerp(tickDelta / 4, lidAngle, defaultLidAngle);
        }
        entity.lidAngle = lid.pitch;

        // 处理方块旋转后，锁头多个问题
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(switch (entity.getCachedState().get(OakChest.FACING)) {
            case EAST -> 270;
            case SOUTH -> 180;
            case WEST -> 90;
            default -> 0;
        }));

        // <!-- Block Entity Item Rendering -->
        // 用来渲染箱子里方块和物品
        // 当打开一定角度时，渲染出物品（当大于零），如果盖子角度大于0时，会存在精度问题
        if (entity.getLidAngle() > 0.1D) {
            // 获取世界和库存
            SimpleInventory inventory = entity.getInventory();
            World world = entity.getWorld();
            // 添加For循环，可以渲染出箱子内所有的物品
            for (int index = 0; index < inventory.getHeldStacks().size(); index++) {
                // 获取物品堆栈
                ItemStack stack = inventory.getStack(index);
                if (stack.isEmpty()) continue;
                // 从物品槽中获取物品
                ItemTransformation transformation = TRANSFORMATIONS.get(index);

                matrices.push();
                // 将物品渲染在箱子中间
                matrices.translate(transformation.x(), 0.65D, transformation.z());
                // 缩小物品形状和大小
                matrices.scale(0.325f, 0.325f, 0.325f);
                // 物品随机旋转且静止存放
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(transformation.rotation()));
                // 渲染随机物品显示图，且物品保持静止状态
                this.context.getItemRenderer().renderItem(stack, ModelTransformationMode.FIXED,
                        light, overlay, matrices, vertexConsumers, world, 0);
                // 当物品位置变换时，物品会基于箱子，并应用到新的局部缩放比例空间
                // 如果箱子先旋转后平移，则transformation受现在的XYZ轴共同影响
                matrices.pop();
            }
        }

        // 获取纹理（节点）
        this.model.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntitySolid(OakChestModel.TEXTURE)), light, overlay);
        // 将盖子归零，目的是渲染器是共享所有的渲染器的方块实体，因此将这些完全相同的实例（对象），将要被清除
        lid.pitch = defaultLidAngle;
        // 弹出矩阵堆栈
        matrices.pop();
    }

    /**
     * 物品随机旋转记录类
     * @param x X轴
     * @param z Z轴
     * @param rotation 沿XZ旋转角度
     */
    public record ItemTransformation(double x, double z, int rotation) {

    }
}
