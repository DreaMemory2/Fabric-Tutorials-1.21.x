package com.crystal.bluecore.renderer;

import com.crystal.bluecore.entity.SpearProjectileEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.TridentEntityModel;
import net.minecraft.client.render.entity.state.TridentEntityRenderState;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;

public class SpearProjectileEntityRenderer extends EntityRenderer<SpearProjectileEntity, TridentEntityRenderState> {
    public static final Identifier TEXTURE = Identifier.ofVanilla("textures/entity/trident.png");
    private final ItemRenderer itemRenderer;
    private final float scale;
    private final boolean lit;
    private final TridentEntityModel model;

    public SpearProjectileEntityRenderer(EntityRendererFactory.Context dispatcher, ItemRenderer itemRenderer, float scale, boolean lit) {
        super(dispatcher);
        this.itemRenderer = itemRenderer;
        this.scale = scale;
        this.lit = lit;
        this.model = new TridentEntityModel(dispatcher.getPart(EntityModelLayers.TRIDENT));
    }

    public SpearProjectileEntityRenderer(EntityRendererFactory.Context dispatcher, ItemRenderer itemRenderer) {
        this(dispatcher, itemRenderer, 1.0F, false);
    }

    @Override
    protected int getBlockLight(SpearProjectileEntity entity, BlockPos blockPos) {
        return this.lit ? 15 : super.getBlockLight(entity, blockPos);
    }

    @Override
    public TridentEntityRenderState createRenderState() {
        return null;
    }

    @Override
    public void render(TridentEntityRenderState state, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        if (state.age >= 2 || this.dispatcher.camera.getFocusedEntity().squaredDistanceTo(state.positionOffset) >= 12.25D) {
            matrices.push();
            matrices.scale(1.5F, 1.5F, 1.5F);

            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(state.yaw - 90.0F));
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(state.pitch + 45.0F));
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180));

            VertexConsumer vertexConsumer = ItemRenderer.getItemGlintConsumer(vertexConsumers, this.model.getLayer(TEXTURE), false, state.enchanted);
            this.model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV);

            matrices.pop();
            super.render(state, matrices, vertexConsumers, light);
        }
    }

    public Identifier getTexture(SpearProjectileEntity entity) {
        return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE;
    }
}
