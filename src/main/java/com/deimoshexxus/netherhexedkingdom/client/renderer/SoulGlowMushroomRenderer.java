package com.deimoshexxus.netherhexedkingdom.client.renderer;

import com.deimoshexxus.netherhexedkingdom.NetherHexedKingdom;
import com.deimoshexxus.netherhexedkingdom.content.custom.blocks.SoulGlowMushroomBlockEntity;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;

/**
 * Renderer for SoulGlowMushroomBlockEntity with debug checks for model+texture loading.
 * Must register the standalone model in ModelEvent.RegisterAdditional (client) — see note below.
 */
public class SoulGlowMushroomRenderer implements BlockEntityRenderer<SoulGlowMushroomBlockEntity> {

    // Texture referenced by the glow model JSON (no "textures/" or ".png")
    private static final ResourceLocation GLOW_TEXTURE_RL =
            ResourceLocation.fromNamespaceAndPath(NetherHexedKingdom.MODID, "block/soul_glow_mushroom_emissive");

    // Standalone ModelResourceLocation -> corresponds to assets/<modid>/models/block/soul_glow_mushroom_glow.json
    private static final ModelResourceLocation GLOW_MODEL_RL =
            ModelResourceLocation.standalone(
                    ResourceLocation.fromNamespaceAndPath(NetherHexedKingdom.MODID, "block/soul_glow_mushroom_glow")
            );

    public static final RenderType SOUL_GLOW = RenderType.create(
            "soul_glow",
            DefaultVertexFormat.BLOCK,
            VertexFormat.Mode.QUADS,
            1536,
            false,
            true,
            RenderType.CompositeState.builder()
                    .setShaderState(RenderStateShard.RENDERTYPE_ENTITY_CUTOUT_SHADER)
                    .setTextureState(new RenderStateShard.TextureStateShard(
                            TextureAtlas.LOCATION_BLOCKS, false, false
                    ))
                    .setTransparencyState(RenderStateShard.NO_TRANSPARENCY)
                    .setDepthTestState(RenderStateShard.LEQUAL_DEPTH_TEST)
                    .setLayeringState(RenderStateShard.VIEW_OFFSET_Z_LAYERING) // ⭐ THIS IS THE FIX
                    .setCullState(RenderStateShard.NO_CULL)
                    .setLightmapState(RenderStateShard.LIGHTMAP)
                    .setOverlayState(RenderStateShard.OVERLAY)
                    .createCompositeState(true)
    );



    public SoulGlowMushroomRenderer(BlockEntityRendererProvider.Context ctx) { }

    @Override
    public void render(SoulGlowMushroomBlockEntity be,
                       float partialTicks,
                       PoseStack poseStack,
                       MultiBufferSource buffer,
                       int packedLight,
                       int packedOverlay) {

        poseStack.pushPose();

        // --- Normal base model pass (cutout) ---
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

        // --- DEBUG: check presence of glow texture resource ---
//        try {
//            Minecraft.getInstance().getResourceManager().getResource(GLOW_TEXTURE_RL);
//            NetherHexedKingdom.LOGGER.info("[SoulGlow] Glow texture FOUND: {}", GLOW_TEXTURE_RL);
//        } catch (Exception ex) {
//            NetherHexedKingdom.LOGGER.error("[SoulGlow] Glow texture MISSING: {}", GLOW_TEXTURE_RL, ex);
//        }
//
//        // --- DEBUG: check presence of glow model via ModelManager ---
        BakedModel glowModel = Minecraft.getInstance().getModelManager().getModel(GLOW_MODEL_RL);
        //BakedModel missing = Minecraft.getInstance().getModelManager().getMissingModel();

//        if (glowModel == missing) {
//            NetherHexedKingdom.LOGGER.error("[SoulGlow] Glow model MISSING: {}", GLOW_MODEL_RL);
//            glowModel = baseModel; // fallback to avoid crashes (no visible glow)
//        } else {
//            NetherHexedKingdom.LOGGER.info("[SoulGlow] Glow model loaded: {}", GLOW_MODEL_RL);
//        }

        // --- Emissive pass: render glow model with eyes RenderType (fullbright) ---
        poseStack.pushPose();
        //poseStack.scale(1.01f, 1.01f, 1.01f); // tiny scale to avoid z-fighting
        poseStack.translate(0.0005f, 0.0005f, 0.0005f);

        RenderType renderType = RenderType.cutout();

        dispatcher.getModelRenderer().renderModel(
                poseStack.last(),
                buffer.getBuffer(renderType),
                be.getBlockState(),
                glowModel,
                1f, 1f, 1f,
                0xF000F0,
                packedOverlay
        );

        poseStack.popPose(); // pop scale
        poseStack.popPose();

        // optional debug line
        //NetherHexedKingdom.LOGGER.debug("[SoulGlow] GLOW_MODEL_RL = {}", GLOW_MODEL_RL);
    }
}
