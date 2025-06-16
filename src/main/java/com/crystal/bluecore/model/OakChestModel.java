package com.crystal.bluecore.model;

import com.crystal.bluecore.BlueCore;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class OakChestModel extends Model {
	// 实体模型图层，定义实体模型图层的名称为模型的名称
	public final static EntityModelLayer LAYER_LOCATION = new EntityModelLayer(BlueCore.of("main"), "oak_chest");
	// 获取纹理文件
	public static final Identifier TEXTURE = BlueCore.of("textures/entity/chest/oak_chest.png");
	private final ModelPart main;
	private final ModelPart lid;

	public OakChestModel(ModelPart root) {
		// 渲染图层
		super(RenderLayer::getEntitySolid);
		main = root.getChild("main");
		lid = main.getChild("lid");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData main = modelPartData.addChild("main", ModelPartBuilder.create()
				.uv(0, 19)
				.cuboid(-7.0F, 0.0F, -7.0F, 14.0F, 10.0F, 14.0F,
						new Dilation(0.0F)),
				ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		main.addChild("lid", ModelPartBuilder.create()
				.uv(0, 0)
				.cuboid(-7.0F, -1.25F, -14.0F, 14.0F, 5.0F, 14.0F,
						new Dilation(0.0F))
				.uv(0, 0)
				.cuboid(-1.0F, -3.25F, -15.0F, 2.0F, 4.0F, 1.0F,
						new Dilation(0.0F)),
				ModelTransform.pivot(0.0F, 10.25F, 7.0F));
		return TexturedModelData.of(modelData, 64, 64);
	}

	/**
	 * @param matrices 矩阵
	 * @param vertexConsumer 顶点
	 * @param light the lightmap coordinates used for this model rendering
	 * @param overlay 图层
	 * @param color 颜色
	 */
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, int color) {
		main.render(matrices, vertexConsumer, light, overlay, color);
	}

	/**
	 * <p>只让箱子的盖子旋转90度</p>
	 * @return 箱子的盖子
	 */
	public ModelPart getLid() {
		return lid;
	}

	/**
	 * <p>可以让箱子让中心旋转</p>
	 * @return 箱子的箱体（整个箱子）
	 */
	public ModelPart getBottom() {
		return main;
	}
}