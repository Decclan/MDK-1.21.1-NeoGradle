package com.deimoshexxus.netherhexedkingdom.client.renderer;

import com.deimoshexxus.netherhexedkingdom.NetherHexedKingdom;
import com.deimoshexxus.netherhexedkingdom.content.entities.HexedZombieHorseEntity;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.AbstractHorseRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.HorseArmorLayer;
import net.minecraft.resources.ResourceLocation;
import com.deimoshexxus.netherhexedkingdom.client.renderer.layer.HexedHorseArmorLayer;
public class HexedZombieHorseRenderer extends AbstractHorseRenderer<
        HexedZombieHorseEntity,
        HorseModel<HexedZombieHorseEntity>> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(
                    NetherHexedKingdom.MODID,
                    "textures/entity/horse/hexed_zombie_horse.png"
            );

    public HexedZombieHorseRenderer(EntityRendererProvider.Context context) {
        super(
                context,
                new HorseModel<>(context.bakeLayer(ModelLayers.HORSE)),
                1.00F
        );

        this.addLayer(
                new HexedHorseArmorLayer<>(
                        this,
                        context.getModelSet()
                )
        );
    }

    @Override
    public ResourceLocation getTextureLocation(HexedZombieHorseEntity entity) {
        return TEXTURE;
    }
}
