package com.crystal.bluecore.model;

import com.crystal.bluecore.BlueCore;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class OakChestModel extends Model {
	// 实体图层
	public static final EntityModelLayer LAYER_LOCATION = new EntityModelLayer(Identifier.of(BlueCore.MOD_ID, "oak_chest"), "main");
	// 材质纹理
	public static final Identifier TEXTURE = Identifier.of(BlueCore.MOD_ID, "textures/block/custom/oak_chest.png");

	private final ModelPart main;
	private final ModelPart lid;

	public OakChestModel(ModelPart root) {
		super(RenderLayer::getEntitySolid);
		this.main = root.getChild("main");
		this.lid = this.main.getChild("lid");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();

		ModelPartData main = modelPartData.addChild("main", ModelPartBuilder.create()
						.uv(0, 19)
						.cuboid(-7.0F, -9.0F, -7.0F, 14.0F, 10.0F, 14.0F,
								new Dilation(0.0F)),
				ModelTransform.pivot(0.0F, 19.0F, 0.0F));

		ModelPartData lid = main.addChild("lid", ModelPartBuilder.create()
						.uv(0, 0)
						.cuboid(-9.0F, 0.0F, -14.0F, 14.0F, 5.0F, 14.0F,
								new Dilation(0.0F))
						.uv(0, 0)
						.cuboid(-3.0F, -2.0F, -15.0F, 2.0F, 4.0F, 1.0F,
								new Dilation(0.0F)),
				ModelTransform.pivot(2.0F, 0.0F, 7.0F));
		return TexturedModelData.of(modelData, 64, 64);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, int color) {
		main.render(matrices, vertexConsumer, light, overlay, color);
	}

	public ModelPart getLid() {
		return this.lid;
	}
}