package com.deimoshexxus.netherhexedkingdom.client.renderer;

import com.deimoshexxus.netherhexedkingdom.content.entities.GrenadeProjectileEntity;
import com.deimoshexxus.netherhexedkingdom.content.entities.NetherPortalOrbEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;

public class NetherPortalOrbRenderer extends ThrownItemRenderer<NetherPortalOrbEntity> {

    public NetherPortalOrbRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
        this.shadowRadius = 0.15F; // optional
    }

    @Override
    public void render(NetherPortalOrbEntity entity, float entityYaw, float partialTicks,
                       com.mojang.blaze3d.vertex.PoseStack poseStack,
                       net.minecraft.client.renderer.MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        // optional scale
        float scale = 1.0F;
        poseStack.scale(scale, scale, scale);

        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
        poseStack.popPose();
    }
}