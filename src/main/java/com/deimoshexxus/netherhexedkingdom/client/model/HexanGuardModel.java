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

    /** Armor layer using CubeDeformation (pass 0.5F for inner, 1.0F for outer). */
    public static LayerDefinition createArmorLayer(float deformation) {
        return createLayer(deformation);
    }

    private static LayerDefinition createLayer(float deformation) {
        CubeDeformation def = new CubeDeformation(deformation);
        MeshDefinition mesh = HumanoidModel.createMesh(def, 0.0F);
        PartDefinition root = mesh.getRoot();

        // HEAD (main skull)
        PartDefinition head = root.addOrReplaceChild(
                "head",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8),
                PartPose.ZERO
        );

        // HAT / outer head layer (use deformation)
        head.addOrReplaceChild(
                "hat",
                CubeListBuilder.create()
                        .texOffs(32, 0)
                        .addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, def),
                PartPose.ZERO
        );

        // HORNS — map the two small boxes from Blockbench
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
                        .addBox(-5.0F, -10.0F, -1.0F, 1, 4, 1),
                PartPose.ZERO
        );

        // BODY (standard torso)
        root.addOrReplaceChild(
                "body",
                CubeListBuilder.create()
                        .texOffs(16, 16)
                        .addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4),
                PartPose.ZERO
        );

        // RIGHT ARM — return the PartDefinition so we can add item anchor children
        PartDefinition rightArm = root.addOrReplaceChild(
                "right_arm",
                CubeListBuilder.create()
                        .texOffs(40, 16)
                        .addBox(-2.0F, -2.0F, -1.0F, 3, 12, 3),
                PartPose.offset(-5.0F, 2.0F, 0.0F)
        );

        // LEFT ARM — mirrored
        PartDefinition leftArm = root.addOrReplaceChild(
                "left_arm",
                CubeListBuilder.create()
                        .texOffs(40, 16)
                        .mirror()
                        .addBox(-1.0F, -2.0F, -1.0F, 3, 12, 3),
                PartPose.offset(5.0F, 2.0F, 0.0F)
        );

        // --- ITEM ANCHORS (these are REQUIRED so held items show up correctly) ---
        // Left item anchor uses Blockbench offset from your export:
        leftArm.addOrReplaceChild(
                "leftItem",
                CubeListBuilder.create(),
                PartPose.offset(1.0F, 7.0F, 1.0F)
        );

        // Right item anchor — mirror of left. Blockbench didn't provide rightItem but we add a sensible anchor.
        rightArm.addOrReplaceChild(
                "rightItem",
                CubeListBuilder.create(),
                PartPose.offset(0.0F, 7.0F, 1.0F) // tweak to taste (try -1.0F if item sits too far in)
        );

        // Optional sleeves used by armor layer baking (keeps armor attachments consistent)
        rightArm.addOrReplaceChild("right_sleeve", CubeListBuilder.create(), PartPose.ZERO);
        leftArm.addOrReplaceChild("left_sleeve", CubeListBuilder.create(), PartPose.ZERO);

        // RIGHT LEG
        root.addOrReplaceChild(
                "right_leg",
                CubeListBuilder.create()
                        .texOffs(0, 16)
                        .addBox(-2.0F, 0.0F, -1.0F, 3, 12, 3),
                PartPose.offset(-2.0F, 12.0F, 0.0F)
        );

        // LEFT LEG — mirrored
        root.addOrReplaceChild(
                "left_leg",
                CubeListBuilder.create()
                        .texOffs(0, 16)
                        .mirror()
                        .addBox(-1.0F, 0.0F, -1.0F, 3, 12, 3),
                PartPose.offset(2.0F, 12.0F, 0.0F)
        );

        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }
}
