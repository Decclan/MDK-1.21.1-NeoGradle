package com.deimoshexxus.netherhexedkingdom.client.renderer.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.item.AnimalArmorItem;
import net.minecraft.world.item.ItemStack;

public class HexedHorseArmorLayer<T extends Horse>
        extends RenderLayer<T, HorseModel<T>> {

    private final HorseModel<T> armorModel;

    public HexedHorseArmorLayer(
            RenderLayerParent<T, HorseModel<T>> renderer,
            EntityModelSet modelSet
    ) {
        super(renderer);
        this.armorModel =
                new HorseModel<>(modelSet.bakeLayer(ModelLayers.HORSE_ARMOR));
    }

    @Override
    public void render(
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            T horse,
            float limbSwing,
            float limbSwingAmount,
            float partialTicks,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {

        ItemStack armorStack = horse.getBodyArmorItem();

        if (!(armorStack.getItem() instanceof AnimalArmorItem armorItem)) {
            return;
        }

        if (armorItem.getBodyType() != AnimalArmorItem.BodyType.EQUESTRIAN) {
            return;
        }

        this.getParentModel().copyPropertiesTo(this.armorModel);

        this.armorModel.prepareMobModel(
                horse,
                limbSwing,
                limbSwingAmount,
                partialTicks
        );

        this.armorModel.setupAnim(
                horse,
                limbSwing,
                limbSwingAmount,
                ageInTicks,
                netHeadYaw,
                headPitch
        );

        VertexConsumer consumer =
                buffer.getBuffer(
                        RenderType.entityCutoutNoCull(
                                armorItem.getTexture()
                        )
                );

        this.armorModel.renderToBuffer(
                poseStack,
                consumer,
                packedLight,
                OverlayTexture.NO_OVERLAY,
                -1
        );
    }
}