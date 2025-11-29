package com.deimoshexxus.netherhexedkingdom.client.model;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Mob;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class HexanGuardModel<T extends Mob> extends HumanoidModel<T> {

    public HexanGuardModel(ModelPart root) {
        super(root);
    }

    /** Standard body layer (no deformation). */
    public static LayerDefinition createBodyLayer() {
        return createLayer(0.0F);
    }

    /** Armor layers with deformation (inner: 0.5F, outer: 1.0F). */
    public static LayerDefinition createArmorLayer(float deformation) {
        return createLayer(deformation);
    }

    private static LayerDefinition createLayer(float deformation) {
        CubeDeformation def = new CubeDeformation(deformation);

        // Pass deformation to createMesh
        MeshDefinition mesh = HumanoidModel.createMesh(def, deformation);
        PartDefinition root = mesh.getRoot();

        // =========================================================
        // HEAD + HORNS
        // =========================================================
        PartDefinition head = root.addOrReplaceChild(
                "head",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, def), // <-- use def here
                PartPose.ZERO
        );

        // Vanilla "hat" layer (required for armor)
        head.addOrReplaceChild(
                "hat",
                CubeListBuilder.create()
                        .texOffs(32, 0)
                        .addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, def), // already used def (kept)
                PartPose.ZERO
        );

        // Horns (usually not inflated - keep these without def if you don't want armor to alter horns)
        head.addOrReplaceChild(
                "horn_right",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(4.0F, -10.0F, -1.0F, 1, 4, 1),
                PartPose.ZERO
        );
        head.addOrReplaceChild(
                "horn_left",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .mirror()
                        .addBox(-5.0F, -10.0F, -1.0F, 1, 4, 1),
                PartPose.ZERO
        );

        // =========================================================
        // BODY
        // =========================================================
        PartDefinition body = root.addOrReplaceChild(
                "body",
                CubeListBuilder.create()
                        .texOffs(16, 16)
                        .addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, def), // <-- use def
                PartPose.ZERO
        );

        // REQUIRED: jacket – blank part used by armor layer
        body.addOrReplaceChild("jacket", CubeListBuilder.create(), PartPose.ZERO);

        // =========================================================
        // ARMS
        // =========================================================
        PartDefinition rightArm = root.addOrReplaceChild(
                "right_arm",
                CubeListBuilder.create()
                        .texOffs(40, 16)
                        .addBox(-2.0F, -2.0F, -1.0F, 3, 12, 3, def), // <-- use def
                PartPose.offset(-5.0F, 2.0F, 0.0F)
        );

        PartDefinition leftArm = root.addOrReplaceChild(
                "left_arm",
                CubeListBuilder.create()
                        .texOffs(40, 16)
                        .mirror()
                        .addBox(-1.0F, -2.0F, -1.0F, 3, 12, 3, def), // <-- use def
                PartPose.offset(5.0F, 2.0F, 0.0F)
        );

        // Item anchors for held items
        rightArm.addOrReplaceChild("rightItem", CubeListBuilder.create(), PartPose.offset(0.0F, 7.0F, 1.0F));
        leftArm.addOrReplaceChild("leftItem", CubeListBuilder.create(), PartPose.offset(1.0F, 7.0F, 1.0F));

        // REQUIRED sleeves for armor
        rightArm.addOrReplaceChild("right_sleeve", CubeListBuilder.create(), PartPose.ZERO);
        leftArm.addOrReplaceChild("left_sleeve", CubeListBuilder.create(), PartPose.ZERO);

        // =========================================================
        // LEGS
        // =========================================================
        PartDefinition rightLeg = root.addOrReplaceChild(
                "right_leg",
                CubeListBuilder.create()
                        .texOffs(0, 16)
                        .addBox(-2.0F, 0.0F, -1.0F, 3, 12, 3, def), // <-- use def
                PartPose.offset(-2.0F, 12.0F, 0.0F)
        );

        PartDefinition leftLeg = root.addOrReplaceChild(
                "left_leg",
                CubeListBuilder.create()
                        .texOffs(0, 16)
                        .mirror()
                        .addBox(-1.0F, 0.0F, -1.0F, 3, 12, 3, def), // <-- use def
                PartPose.offset(2.0F, 12.0F, 0.0F)
        );

        // REQUIRED for armor layer (pants attachments)
        rightLeg.addOrReplaceChild("right_pants", CubeListBuilder.create(), PartPose.ZERO);
        leftLeg.addOrReplaceChild("left_pants", CubeListBuilder.create(), PartPose.ZERO);

        // =========================================================

        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public void setupAnim(
            T entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }
}
