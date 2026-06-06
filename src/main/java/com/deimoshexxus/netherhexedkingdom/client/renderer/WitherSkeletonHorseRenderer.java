package com.deimoshexxus.netherhexedkingdom.client.renderer;

import com.deimoshexxus.netherhexedkingdom.NetherHexedKingdom;
import com.deimoshexxus.netherhexedkingdom.client.renderer.layer.HexedHorseArmorLayer;
import com.deimoshexxus.netherhexedkingdom.content.entities.WitherSkeletonHorseEntity;
import com.deimoshexxus.netherhexedkingdom.client.renderer.layer.WitherSkeletonHorseGlowLayer;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.AbstractHorseRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class WitherSkeletonHorseRenderer extends AbstractHorseRenderer<
        WitherSkeletonHorseEntity,
        HorseModel<WitherSkeletonHorseEntity>> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(
                    NetherHexedKingdom.MODID,
                    "textures/entity/horse/wither_skeleton_horse.png"
            );

    public WitherSkeletonHorseRenderer(EntityRendererProvider.Context context) {
        super(
                context,
                new HorseModel<>(context.bakeLayer(ModelLayers.HORSE)),
                1.20F
        );

        this.addLayer(new WitherSkeletonHorseGlowLayer(this));

        this.addLayer(
                new HexedHorseArmorLayer<>(
                        this,
                        context.getModelSet()
                )
        );
    }

    @Override
    public ResourceLocation getTextureLocation(WitherSkeletonHorseEntity entity) {
        return TEXTURE;
    }
}
