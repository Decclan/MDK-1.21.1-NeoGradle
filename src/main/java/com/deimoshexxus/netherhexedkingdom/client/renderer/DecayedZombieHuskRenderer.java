package com.deimoshexxus.netherhexedkingdom.client.renderer;

import com.deimoshexxus.netherhexedkingdom.NetherHexedKingdom;
import com.deimoshexxus.netherhexedkingdom.content.entities.DecayedZombieHuskEntity;
import net.minecraft.client.model.ZombieModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;

public class DecayedZombieHuskRenderer extends MobRenderer<DecayedZombieHuskEntity, ZombieModel<DecayedZombieHuskEntity>> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(NetherHexedKingdom.MODID, "textures/entity/zombie/decayed_zombie_husk.png");

    public DecayedZombieHuskRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new ZombieModel<>(ctx.bakeLayer(ModelLayers.ZOMBIE)), 0.5F);

        this.addLayer(new HumanoidArmorLayer<>(
                this,
                new ZombieModel<>(ctx.bakeLayer(ModelLayers.ZOMBIE_INNER_ARMOR)),
                new ZombieModel<>(ctx.bakeLayer(ModelLayers.ZOMBIE_OUTER_ARMOR)),
                ctx.getModelManager()
        ));
    }

    @Override
    public ResourceLocation getTextureLocation(DecayedZombieHuskEntity entity) {
        return TEXTURE;
    }
}

