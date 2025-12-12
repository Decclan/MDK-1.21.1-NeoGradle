package com.deimoshexxus.netherhexedkingdom.client.renderer;

import com.deimoshexxus.netherhexedkingdom.NetherHexedKingdom;
import com.deimoshexxus.netherhexedkingdom.content.custom.blocks.SoulGlowMushroomBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.Util;

public class SoulGlowMushroomRenderer implements BlockEntityRenderer<SoulGlowMushroomBlockEntity> {

    /**
     * NOTE: This must be "modid:block/texture_name" — do NOT include "textures/" or ".png".
     * That's the same form used in model JSON ("textures": { "cross": "modid:block/texture_name" }).
     */
    // ResourceLocation for the block model JSON
    ResourceLocation GLOW_TEXTURE_RL = ResourceLocation.fromNamespaceAndPath(NetherHexedKingdom.MODID, "block/soul_glow_mushroom_glow");

    // ModelResourceLocation for the baked model lookup
    ModelResourceLocation GLOW_MODEL_RL = ModelResourceLocation.standalone(GLOW_TEXTURE_RL);



    public SoulGlowMushroomRenderer(BlockEntityRendererProvider.Context ctx) { }

    @Override
    public void render(SoulGlowMushroomBlockEntity be,
                       float partialTicks,
                       PoseStack poseStack,
                       MultiBufferSource buffer,
                       int packedLight,
                       int packedOverlay) {

        poseStack.pushPose();

        // 1) fetch base model baked from block state (what the block normally uses)
        BakedModel baseModel = Minecraft.getInstance()
                .getBlockRenderer()
                .getBlockModel(be.getBlockState());

        var dispatcher = Minecraft.getInstance().getBlockRenderer();

        dispatcher.getModelRenderer().renderModel(
                poseStack.last(),
                buffer.getBuffer(RenderType.cutout()),
                be.getBlockState(),
                baseModel,
                1f, 1f, 1f,
                packedLight,
                packedOverlay
        );

        // 2) try to fetch the separate glow model (must exist at models/block/soul_glow_mushroom_glow.json)
        BakedModel glowModel = Minecraft.getInstance().getModelManager().getModel(GLOW_MODEL_RL);

        // If the glow model is the missing model or identical to baseModel, log a warning so you can debug assets
        BakedModel missing = Minecraft.getInstance().getModelManager().getMissingModel();
        if (glowModel == missing) {
            NetherHexedKingdom.LOGGER.warn("SoulGlow: glow model missing: {}", GLOW_MODEL_RL);
            // fallback — but still attempt to render using base model (may produce no visible glow)
            glowModel = baseModel;
        } else if (glowModel == baseModel) {
            NetherHexedKingdom.LOGGER.warn("SoulGlow: glow model equals base model (check json/model names).");
        }

        // 3) Render glow model with eyes RenderType (fullbright) and tiny scale (avoid z-fight)
        poseStack.pushPose();
        poseStack.scale(1.001f, 1.001f, 1.001f);

        dispatcher.getModelRenderer().renderModel(
                poseStack.last(),
                buffer.getBuffer(RenderType.eyes(GLOW_TEXTURE_RL)),
                be.getBlockState(),
                glowModel,
                1f, 1f, 1f,
                0xF000F0, // fullbright packed light
                packedOverlay
        );

        poseStack.popPose(); // pop scale
        poseStack.popPose();
        NetherHexedKingdom.LOGGER.info("GLOW_MODEL_RL = {}", GLOW_MODEL_RL);

    }
}
