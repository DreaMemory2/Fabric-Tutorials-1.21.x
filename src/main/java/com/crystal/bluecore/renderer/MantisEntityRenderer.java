package com.crystal.bluecore.renderer;

import com.crystal.bluecore.BlueCore;
import com.crystal.bluecore.entity.MantisEntity;
import com.crystal.bluecore.entity.custom.MantisVariant;
import com.crystal.bluecore.model.MantisModel;
import com.google.common.collect.Maps;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.Map;

/**
 * @author Crystal
 * @version 1.0
 * @since 1.0
 */
public class MantisEntityRenderer extends MobEntityRenderer<MantisEntity, MantisModel<MantisEntity>> {
    /* 螳螂颜色多样性，含有变种螳螂的图层地址 */
    private static final Map<MantisVariant, Identifier> TEXTURES = Util.make(Maps.newEnumMap(MantisVariant.class), map -> {
        map.put(MantisVariant.DEFAULT, BlueCore.of("textures/entity/mantis/mantis.png"));
        map.put(MantisVariant.ORCHID, BlueCore.of("textures/entity/mantis/mantis_orchid.png"));
    });
    /**
     * f: 阴影半径
     * @param context 上下文
     */
    public MantisEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new MantisModel<>(context.getPart(MantisModel.MANTIS)), 0.75f);
    }

    @Override
    public Identifier getTexture(MantisEntity entity) {
        return TEXTURES.get(entity.getVariant());
    }

    @Override
    public void render(MantisEntity livingEntity, float f, float g, MatrixStack matrixStack,
                       VertexConsumerProvider vertexConsumerProvider, int i) {
        // 生物幼体渲染尺寸
        if(livingEntity.isBaby()) {
            matrixStack.scale(0.5f, 0.5f, 0.5f);
        } else {
            matrixStack.scale(1f, 1f, 1f);
        }

        super.render(livingEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }
}
