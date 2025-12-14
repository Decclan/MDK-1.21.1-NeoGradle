package com.deimoshexxus.netherhexedkingdom.client.renderer;

import com.deimoshexxus.netherhexedkingdom.NetherHexedKingdom;
import com.deimoshexxus.netherhexedkingdom.content.entities.DecayedZombifiedPiglinEntity;
import net.minecraft.client.model.PiglinModel;
import net.minecraft.client.model.ZombieModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.ZombifiedPiglin;

public class DecayedZombifiedPiglinRenderer extends MobRenderer<DecayedZombifiedPiglinEntity, PiglinModel<DecayedZombifiedPiglinEntity>> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(NetherHexedKingdom.MODID, "textures/entity/zombie/decayed_zombified_piglin.png");

    public DecayedZombifiedPiglinRenderer(EntityRendererProvider.Context context) {
        super(
                context,
                new PiglinModel<>(
                        context.bakeLayer(ModelLayers.ZOMBIFIED_PIGLIN)
                ),
                0.5F // shadow size
        );
    }

    @Override
    public ResourceLocation getTextureLocation(DecayedZombifiedPiglinEntity entity) {
        return TEXTURE;
    }
}

