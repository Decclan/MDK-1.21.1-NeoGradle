package com.deimoshexxus.netherhexedkingdom.client.renderer;

import com.deimoshexxus.netherhexedkingdom.NetherHexedKingdom;
import com.deimoshexxus.netherhexedkingdom.client.model.GargoylePossessedModel;
import com.deimoshexxus.netherhexedkingdom.content.entities.GargoylePossessedEntity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class GargoylePossessedRenderer
        extends MobRenderer<GargoylePossessedEntity, GargoylePossessedModel<GargoylePossessedEntity>> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(
                    NetherHexedKingdom.MODID,
                    "textures/entity/gargoyle/gargoyle_possessed.png"
            );

    public GargoylePossessedRenderer(EntityRendererProvider.Context ctx) {
        super(
                ctx,
                new GargoylePossessedModel<>(ctx.bakeLayer(GargoylePossessedModel.LAYER_LOCATION)),
                0.5F // shadow size
        );
    }

    @Override
    public ResourceLocation getTextureLocation(GargoylePossessedEntity entity) {
        return TEXTURE;
    }
}
