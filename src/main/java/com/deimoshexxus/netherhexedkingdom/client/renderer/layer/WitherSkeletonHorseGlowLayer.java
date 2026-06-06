package com.deimoshexxus.netherhexedkingdom.client.renderer.layer;

import com.deimoshexxus.netherhexedkingdom.NetherHexedKingdom;
import com.deimoshexxus.netherhexedkingdom.content.entities.WitherSkeletonHorseEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.HorseModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class WitherSkeletonHorseGlowLayer extends RenderLayer<
        WitherSkeletonHorseEntity,
        HorseModel<WitherSkeletonHorseEntity>> {

    private static final ResourceLocation GLOW_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(
                    NetherHexedKingdom.MODID,
                    "textures/entity/horse/wither_skeleton_horse_redeyes.png"
            );

    public WitherSkeletonHorseGlowLayer(
            RenderLayerParent<
                    WitherSkeletonHorseEntity,
                    HorseModel<WitherSkeletonHorseEntity>> parent) {

        super(parent);
    }

    @Override
    public void render(
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            int packedLight,
            WitherSkeletonHorseEntity entity,
            float limbSwing,
            float limbSwingAmount,
            float partialTick,
            float ageInTicks,
            float netHeadYaw,
            float headPitch) {

        if (!entity.isTamed()) {
            return;
        }

        VertexConsumer vertexConsumer =
                bufferSource.getBuffer(RenderType.eyes(GLOW_TEXTURE));

        this.getParentModel().renderToBuffer(
                poseStack,
                vertexConsumer,
                15728640,
                OverlayTexture.NO_OVERLAY
        );
    }
}