package com.deimoshexxus.netherhexedkingdom.client.renderer;

import com.deimoshexxus.netherhexedkingdom.client.ModModelLayers;
import com.deimoshexxus.netherhexedkingdom.client.model.HexanGuardModel;
import com.deimoshexxus.netherhexedkingdom.content.entities.HexanGuardEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import com.deimoshexxus.netherhexedkingdom.NetherHexedKingdomMain;

public class HexanGuardRenderer extends MobRenderer<HexanGuardEntity, HexanGuardModel<HexanGuardEntity>> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(NetherHexedKingdomMain.MODID,
                    "textures/entity/hexan_guard_entity.png");

    public HexanGuardRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new HexanGuardModel<>(ctx.bakeLayer(ModModelLayers.HEXAN_GUARD_BODY)), 0.5F);

        this.addLayer(new HumanoidArmorLayer<>(
                this,
                new HexanGuardModel<>(ctx.bakeLayer(ModModelLayers.HEXAN_GUARD_ARMOR_INNER)),
                new HexanGuardModel<>(ctx.bakeLayer(ModModelLayers.HEXAN_GUARD_ARMOR_OUTER)),
                ctx.getModelManager()
        ));
    }

    @Override
    public ResourceLocation getTextureLocation(HexanGuardEntity entity) {
        return TEXTURE;
    }
}
