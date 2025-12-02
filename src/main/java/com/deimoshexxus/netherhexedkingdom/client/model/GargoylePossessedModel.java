package com.deimoshexxus.netherhexedkingdom.client.model;

import com.deimoshexxus.netherhexedkingdom.NetherHexedKingdom;
import com.deimoshexxus.netherhexedkingdom.content.entities.GargoylePossessedEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * GargoylePossessedModel — Blockbench geometry kept intact, with corrected teeth pivot.
 *
 * Compatible with NeoForge 21.1.215 / Minecraft 1.21.1 (Mojang mappings).
 */
@OnlyIn(Dist.CLIENT)
public class GargoylePossessedModel<T extends Entity> extends EntityModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(
                    ResourceLocation.fromNamespaceAndPath(NetherHexedKingdom.MODID, "gargoyle_possessed"),
                    "main"
            );

    private final ModelPart gargoyle_model;
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart eyes;
    private final ModelPart teeth;
    private final ModelPart frontLeftLeg;
    private final ModelPart frontRightLeg;
    private final ModelPart backLeftLeg;
    private final ModelPart backRightLeg;
    private final ModelPart tail;
    private final ModelPart rightWing;
    private final ModelPart leftWing;

    public GargoylePossessedModel(ModelPart root) {
        this.gargoyle_model = root.getChild("gargoyle_model");
        this.body = this.gargoyle_model.getChild("body");
//        this.bodyDefaultX = this.body.xRot;
        this.head = this.body.getChild("head");
        this.eyes = this.head.getChild("eyes");
        this.teeth = this.head.getChild("teeth");
        this.frontLeftLeg = this.gargoyle_model.getChild("frontLeftLeg");
        this.frontRightLeg = this.gargoyle_model.getChild("frontRightLeg");
        this.backLeftLeg = this.gargoyle_model.getChild("backLeftLeg");
        this.backRightLeg = this.gargoyle_model.getChild("backRightLeg");
        this.tail = this.gargoyle_model.getChild("tail");
        this.rightWing = this.gargoyle_model.getChild("rightWing");
        this.leftWing = this.gargoyle_model.getChild("leftWing");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition gargoyle_model = partdefinition.addOrReplaceChild("gargoyle_model", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition body = gargoyle_model.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0033F, -8.0165F, 1.0588F));

        PartDefinition neck_r1 = body.addOrReplaceChild("neck_r1", CubeListBuilder.create().texOffs(14, 0).addBox(-1.9F, -2.0F, -2.6069F, 3.8F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.5088F, -4.952F, 1.3963F, 0.0F, 0.0F));

        PartDefinition chest_r1 = body.addOrReplaceChild("chest_r1", CubeListBuilder.create().texOffs(10, 1).addBox(-3.9F, -1.5F, -1.5F, 7.8F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0264F, -4.3665F, -0.2182F, 0.0F, 0.0F));

        PartDefinition torso_r1 = body.addOrReplaceChild("torso_r1", CubeListBuilder.create().texOffs(0, 11).addBox(-2.0F, -4.5F, -2.5F, 4.0F, 9.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.4825F, 0.9657F, -1.9635F, 0.0F, 0.0F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(-0.015F, -2.98F, -8.5409F));

        PartDefinition earLeft_r1 = head.addOrReplaceChild("earLeft_r1", CubeListBuilder.create().texOffs(10, 26).addBox(-2.0F, -1.9115F, 1.6577F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(10, 26).mirror().addBox(1.0F, -1.9115F, 1.6577F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(22, 4).addBox(-1.5F, 1.0383F, -1.7921F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(16, 0).addBox(-1.8F, -0.9617F, 0.1079F, 3.6F, 5.0F, 2.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.015F, 0.5892F, -0.3014F, 0.7854F, 0.0F, 0.0F));

        PartDefinition hornLeft_r1 = head.addOrReplaceChild("hornLeft_r1", CubeListBuilder.create().texOffs(15, 17).mirror().addBox(-0.8F, -3.4322F, -0.1663F, 0.5F, 1.5F, 0.5F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(15, 17).addBox(0.2F, -3.4322F, -0.1663F, 0.5F, 1.5F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.015F, 0.5892F, -0.3014F, -0.3927F, 0.0F, 0.0F));

        PartDefinition snout_r1 = head.addOrReplaceChild("snout_r1", CubeListBuilder.create().texOffs(18, 14).addBox(-1.5F, -2.0398F, -2.5415F, 3.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(18, 5).addBox(-1.1F, 1.5319F, -2.6895F, 2.2F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.015F, 0.5892F, -0.3014F, 0.3927F, 0.0F, 0.0F));

        PartDefinition eyes = head.addOrReplaceChild("eyes", CubeListBuilder.create(), PartPose.offset(0.015F, -0.4328F, -1.0399F));

        PartDefinition eyeRight_r1 = eyes.addOrReplaceChild("eyeRight_r1", CubeListBuilder.create().texOffs(30, 31).addBox(1.2F, -1.4768F, -0.5412F, 0.5F, 0.5F, 0.5F, new CubeDeformation(0.0F))
                .texOffs(30, 31).addBox(-1.7F, -1.4768F, -0.5412F, 0.5F, 0.5F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0219F, 0.7385F, 0.3927F, 0.0F, 0.0F));

        PartDefinition teeth = head.addOrReplaceChild("teeth", CubeListBuilder.create(), PartPose.offset(-0.035F, 2.1132F, -2.1197F));

        PartDefinition toothTopLeft_r1 = teeth.addOrReplaceChild("toothTopLeft_r1", CubeListBuilder.create().texOffs(18, 16).addBox(-0.8F, -0.1878F, -2.5131F, 0.5F, 0.6F, 0.5F, new CubeDeformation(0.0F))
                .texOffs(18, 16).addBox(0.2F, -0.1878F, -2.5131F, 0.5F, 0.6F, 0.5F, new CubeDeformation(0.0F))
                .texOffs(18, 16).addBox(0.2F, 1.0122F, -2.5131F, 0.5F, 0.6F, 0.5F, new CubeDeformation(0.0F))
                .texOffs(18, 16).addBox(-0.8F, 1.0122F, -2.5131F, 0.5F, 0.6F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.05F, -1.524F, 1.8183F, 0.3927F, 0.0F, 0.0F));

        PartDefinition frontLeftLeg = gargoyle_model.addOrReplaceChild("frontLeftLeg", CubeListBuilder.create().texOffs(1, 23).addBox(-1.5F, 5.4858F, -3.3487F, 3.0F, 1.0F, 3.5F, new CubeDeformation(0.0F)), PartPose.offset(-2.4967F, -6.4858F, -3.7617F));

        PartDefinition upperarmLeft_r1 = frontLeftLeg.addOrReplaceChild("upperarmLeft_r1", CubeListBuilder.create().texOffs(6, 19).mirror().addBox(-1.2F, 0.2214F, -0.7443F, 2.4F, 4.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -0.0575F, 0.4474F, 0.3491F, 0.0F, 0.0F));

        PartDefinition forearmLeft_r1 = frontLeftLeg.addOrReplaceChild("forearmLeft_r1", CubeListBuilder.create().texOffs(17, 24).mirror().addBox(-1.0F, -3.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 4.0716F, 0.1513F, -0.7854F, 0.0F, 0.0F));

        PartDefinition frontRightLeg = gargoyle_model.addOrReplaceChild("frontRightLeg", CubeListBuilder.create().texOffs(1, 23).mirror().addBox(-1.5F, 5.4858F, -3.3487F, 3.0F, 1.0F, 3.5F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(2.5033F, -6.4858F, -3.7617F));

        PartDefinition upperarmRight_r1 = frontRightLeg.addOrReplaceChild("upperarmRight_r1", CubeListBuilder.create().texOffs(6, 19).addBox(-1.2F, 0.2214F, -0.7443F, 2.4F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.0575F, 0.4474F, 0.3491F, 0.0F, 0.0F));

        PartDefinition forearmRight_r1 = frontRightLeg.addOrReplaceChild("forearmRight_r1", CubeListBuilder.create().texOffs(17, 24).addBox(-1.0F, -3.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.0716F, 0.1513F, -0.7854F, 0.0F, 0.0F));

        PartDefinition backLeftLeg = gargoyle_model.addOrReplaceChild("backLeftLeg", CubeListBuilder.create().texOffs(16, 7).addBox(-1.5833F, 5.3976F, -4.0025F, 3.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.4133F, -6.3976F, 4.3921F));

        PartDefinition upperlegLeft_r1 = backLeftLeg.addOrReplaceChild("upperlegLeft_r1", CubeListBuilder.create().texOffs(15, 2).mirror().addBox(-1.75F, -1.5F, -2.5F, 3.5F, 3.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.1667F, -0.0608F, -0.498F, 0.6545F, 0.0F, 0.0F));

        PartDefinition lowerlegLeft_r1 = backLeftLeg.addOrReplaceChild("lowerlegLeft_r1", CubeListBuilder.create().texOffs(8, 21).addBox(-1.0F, -1.5F, -3.0F, 2.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0833F, 3.1633F, -0.9995F, -0.829F, 0.0F, 0.0F));

        PartDefinition backRightLeg = gargoyle_model.addOrReplaceChild("backRightLeg", CubeListBuilder.create().texOffs(16, 7).mirror().addBox(-1.4167F, 5.3976F, -4.0025F, 3.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(3.42F, -6.3976F, 4.3921F));

        PartDefinition lowerlegRight_r1 = backRightLeg.addOrReplaceChild("lowerlegRight_r1", CubeListBuilder.create().texOffs(8, 21).mirror().addBox(-1.0F, -1.5F, -3.0F, 2.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0833F, 3.1633F, -0.9995F, -0.829F, 0.0F, 0.0F));

        PartDefinition upperlegRight_r1 = backRightLeg.addOrReplaceChild("upperlegRight_r1", CubeListBuilder.create().texOffs(15, 2).addBox(-1.75F, -1.5F, -2.5F, 3.5F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1667F, -0.0608F, -0.498F, 0.6545F, 0.0F, 0.0F));

        PartDefinition tail = gargoyle_model.addOrReplaceChild("tail", CubeListBuilder.create(), PartPose.offset(0.0033F, -6.3646F, 5.5908F));

        PartDefinition tailCenter_r1 = tail.addOrReplaceChild("tailCenter_r1", CubeListBuilder.create().texOffs(15, 0).addBox(-0.8F, -0.9617F, -0.5871F, 1.6F, 1.8F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.813F, 2.4925F, -0.829F, 0.0F, 0.0F));

        PartDefinition tailBase_r1 = tail.addOrReplaceChild("tailBase_r1", CubeListBuilder.create().texOffs(0, 18).addBox(-1.0F, -1.0611F, -0.7648F, 2.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.1131F, 0.595F, -1.1345F, 0.0F, 0.0F));

        PartDefinition tailTip_r1 = tail.addOrReplaceChild("tailTip_r1", CubeListBuilder.create().texOffs(26, 20).addBox(-0.5F, -1.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 5.3F, 4.9126F, -1.6581F, 0.0F, 0.0F));

        PartDefinition rightWing = gargoyle_model.addOrReplaceChild("rightWing", CubeListBuilder.create(), PartPose.offset(0.9883F, -12.125F, -0.6469F));

        PartDefinition wingTipRight_r1 = rightWing.addOrReplaceChild("wingTipRight_r1", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0F, -6.0F, -0.5F, 2.0F, 12.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(5.4899F, 1.125F, 3.6829F, 1.3189F, 0.2443F, 0.7543F));

        PartDefinition wingSpanTwoTipRight_r1 = rightWing.addOrReplaceChild("wingSpanTwoTipRight_r1", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0F, -5.5F, -0.5F, 2.0F, 11.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(4.1302F, -0.375F, 2.1157F, 1.2464F, 0.1313F, 0.3712F));

        PartDefinition wingSpanOneBaseRight_r1 = rightWing.addOrReplaceChild("wingSpanOneBaseRight_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -5.0F, -0.5F, 3.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.2095F, -0.875F, 1.0724F, 1.2464F, -0.1313F, -0.3712F));

        PartDefinition wingBaseRight_r1 = rightWing.addOrReplaceChild("wingBaseRight_r1", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-0.5F, -3.0F, -2.0F, 1.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.1704F, 0.125F, -0.6961F, 1.2464F, 0.1313F, 0.3712F));

        PartDefinition leftWing = gargoyle_model.addOrReplaceChild("leftWing", CubeListBuilder.create(), PartPose.offset(-0.9817F, -12.125F, -0.6469F));

        PartDefinition wingTipLeft_r1 = leftWing.addOrReplaceChild("wingTipLeft_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -6.0F, -0.5F, 2.0F, 12.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.4899F, 1.125F, 3.6829F, 1.3189F, -0.2443F, -0.7543F));

        PartDefinition wingSpanTwoTipLeft_r1 = leftWing.addOrReplaceChild("wingSpanTwoTipLeft_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -5.5F, -0.5F, 2.0F, 11.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.1302F, -0.375F, 2.1157F, 1.2464F, -0.1313F, -0.3712F));

        PartDefinition wingSpanOneBaseLeft_r1 = leftWing.addOrReplaceChild("wingSpanOneBaseLeft_r1", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.5F, -5.0F, -0.5F, 3.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.2095F, -0.875F, 1.0724F, 1.2464F, 0.1313F, 0.3712F));

        PartDefinition wingBaseLeft_r1 = leftWing.addOrReplaceChild("wingBaseLeft_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -3.0F, -2.0F, 1.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1704F, 0.125F, -0.6961F, 1.2464F, -0.1313F, -0.3712F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks,
                          float netHeadYaw, float headPitch) {

        // 1) Reset to base pose (must exist and restore Blockbench base each frame)
        resetPose();

        // 2) Head look + idle
        // Convert degrees (from vanilla head yaw/pitch) to radians because model rotations use radians
        final float DEG_TO_RAD = (float) Math.PI / 180F;
        // Horizontal head rotation (left/right)
        float yawRad = netHeadYaw * DEG_TO_RAD;
        // Vertical head rotation (up/down)
        float pitchRad = headPitch * DEG_TO_RAD;
        // Limit the pitch so the head can't snap unrealistically far up/down
        pitchRad = Mth.clamp(pitchRad, -0.75F, 0.75F);
        // Apply vanilla-style head look rotations
        this.head.yRot = yawRad;
        this.head.xRot = pitchRad;
        // Add subtle idle movement (breathing / twitching):
        // - small sinusoidal up/down motion over time
        this.head.xRot += Mth.sin(ageInTicks * 0.067F) * 0.06F;
        // - slight left/right drift to avoid robotic motion
        this.head.yRot += Mth.sin(ageInTicks * 0.09F) * 0.04F;


        // 3) Determine sitting state
        boolean sitting = false;
        if (entity instanceof GargoylePossessedEntity gargoyle) {
            sitting = gargoyle.isInSittingPose();
        }

        if (sitting) {
            // ====== HIND-LEG SITTING POSE ======
            //
            // Intent:
            //  - hind legs folded under the body to support weight
            //  - front legs nearly straight, planted on the ground
            //  - body lowered and leaned back slightly so hindquarters touch ground
            //  - tail relaxed down/back; wings relaxed
            //
            // TUNABLES: adjust these numbers if a limb bends the wrong way or the sit is too deep.
            // If a leg bends the wrong direction, invert the sign on that limb's rotation.

            // Lower the whole model so hips contact the ground (default root y = 24)
            this.gargoyle_model.y = 24.5F; // was 21.5

            // Slight lean BACK so the hindquarters take the weight
            this.body.xRot = -0.30F; // negative = lean back (if it leans forward, flip sign)

            // FRONT LEGS: mostly vertical (small inward/outward lean possible)
            // Negative small value tends to put them under the chest (adjust if needed)
            this.frontLeftLeg.xRot  = -0.25F;
            this.frontRightLeg.xRot = -0.25F;

            // HIND LEGS: folded under the body to support — larger positive rotation
            // If these end up pointing the wrong way, change sign to negative.
            this.backLeftLeg.xRot   =  -1.05F;
            this.backRightLeg.xRot  =  -1.05F;

            // Tail relaxed and slightly under the body to help visual balance
            this.tail.xRot = 0.25F; //was -0.25

            // Wings relaxed (tucked), small inward rotation
            this.leftWing.zRot  = -0.25F;
            this.rightWing.zRot =  0.25F;

        } else {
            // ====== STANDING / WALKING ======
            float speed = 1.5F;
            float degree = 1.0F;

            // Simple walk cycle (quadrupedal phase offsets)
            this.frontLeftLeg.xRot  = Mth.cos(limbSwing * speed) * degree * limbSwingAmount;
            this.frontRightLeg.xRot = Mth.cos(limbSwing * speed + (float) Math.PI) * degree * limbSwingAmount;
            this.backLeftLeg.xRot   = Mth.cos(limbSwing * speed + (float) Math.PI) * degree * limbSwingAmount;
            this.backRightLeg.xRot  = Mth.cos(limbSwing * speed) * degree * limbSwingAmount;

            // Wing idle when in air (optional)
            if (!entity.onGround()) {
                this.leftWing.zRot  = Mth.sin(ageInTicks * 0.6F) * 0.45F;
                this.rightWing.zRot = -Mth.sin(ageInTicks * 0.6F) * 0.45F;
            }
        }
    }



    private void resetPose() {
        this.gargoyle_model.y = 24.0F;

        this.body.xRot = 0.0F;

        this.frontLeftLeg.xRot = 0.0F;
        this.frontRightLeg.xRot = 0.0F;
        this.backLeftLeg.xRot = 0.0F;
        this.backRightLeg.xRot = 0.0F;

        this.tail.xRot = 0.0F;

        this.leftWing.zRot = 0.0F;
        this.rightWing.zRot = 0.0F;

        this.head.xRot = 0.0F;
        this.head.yRot = 0.0F;
    }


    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        poseStack.pushPose();
        this.gargoyle_model.render(poseStack, vertexConsumer, packedLight, packedOverlay);
        poseStack.popPose();
    }
}