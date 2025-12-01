package com.deimoshexxus.netherhexedkingdom.client.model;

import com.deimoshexxus.netherhexedkingdom.NetherHexedKingdom;
import com.deimoshexxus.netherhexedkingdom.content.entities.GargoylePossessedEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
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
 * GargoylePossessedModel — fixed wing parenting + neck-based head pivot.
 *
 * Compatible with NeoForge 21.1.215 / Minecraft 1.21.1 (Mojang mappings).
 *
 * Notes:
 *  - I adjusted createBodyLayer to keep the same cube geometry but reparent wing segments
 *    so the wingBase is the rotating joint and the rest of the wing follows.
 *  - Head rotations are computed around the neck_r1 pose (exported) so the head won't orbit.
 */
@OnlyIn(Dist.CLIENT)
public class WarpedParabolModel<T extends Entity> extends EntityModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(
                    ResourceLocation.fromNamespaceAndPath(NetherHexedKingdom.MODID, "gargoyle_possessed"),
                    "main"
            );

    private final ModelPart parabol_model;
    private final ModelPart tail;
    private final ModelPart rightWing;
    private final ModelPart leftWing;
    private final ModelPart head;
    private final ModelPart eyes;
    private final ModelPart teeth;
    private final ModelPart frontLeftLeg;
    private final ModelPart frontRightLeg;
    private final ModelPart backLeftLeg;
    private final ModelPart backRightLeg;
    private final ModelPart body;


    public WarpedParabolModel(ModelPart root) {
        this.parabol_model = root.getChild("parabol_model");
        this.frontLeftLeg = this.parabol_model.getChild("frontLeftLeg");
        this.frontRightLeg = this.parabol_model.getChild("frontRightLeg");
        this.backLeftLeg = this.parabol_model.getChild("backLeftLeg");
        this.backRightLeg = this.parabol_model.getChild("backRightLeg");
        this.body = this.parabol_model.getChild("body");
        this.tail = this.parabol_model.getChild("tail");
        this.rightWing = this.parabol_model.getChild("rightWing");
        this.leftWing = this.parabol_model.getChild("leftWing");
        this.head = this.parabol_model.getChild("head");
        this.eyes = this.parabol_model.getChild("eyes");
        this.teeth = this.parabol_model.getChild("teeth");
    }


    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();


        PartDefinition parabol_model = partdefinition.addOrReplaceChild("parabol_model", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));


        PartDefinition frontLeftLeg = parabol_model.addOrReplaceChild("frontLeftLeg", CubeListBuilder.create().texOffs(17, 24).addBox(-5.000F, -4.000F, 4.000F, 2.400F, 4.000F, 2.000F, new CubeDeformation(0.0F)).texOffs(1, 23).addBox(-7.000F, 6.000F, 5.000F, 3.000F, 1.000F, 3.500F, new CubeDeformation(0.0F)).texOffs(16, 7).addBox(-6.000F, 8.000F, 6.000F, 2.000F, 1.000F, 2.000F, new CubeDeformation(0.0F)), PartPose.offset(-1.000F, -23.000F, -1.000F));
        PartDefinition frontRightLeg = parabol_model.addOrReplaceChild("frontRightLeg", CubeListBuilder.create().texOffs(17, 24).addBox(1.000F, -4.000F, 4.000F, 2.000F, 6.000F, 2.000F, new CubeDeformation(0.0F)).texOffs(1, 23).addBox(-2.000F, 6.000F, 5.000F, 3.000F, 1.000F, 3.500F, new CubeDeformation(0.0F)).texOffs(16, 7).addBox(-1.000F, 8.000F, 6.000F, 2.000F, 1.000F, 2.000F, new CubeDeformation(0.0F)), PartPose.offset(-10.000F, -25.000F, -11.000F));
        PartDefinition backLeftLeg = parabol_model.addOrReplaceChild("backLeftLeg", CubeListBuilder.create().texOffs(15, 2).addBox(-1.000F, 0.500F, -3.300F, 3.500F, 3.000F, 5.000F, new CubeDeformation(0.0F)).texOffs(8, 21).addBox(-1.500F, -2.800F, -0.300F, 2.000F, 3.000F, 6.000F, new CubeDeformation(0.0F)), PartPose.offset(1.000F, -24.000F, -9.000F));
        PartDefinition backRightLeg = parabol_model.addOrReplaceChild("backRightLeg", CubeListBuilder.create().texOffs(15, 2).addBox(-1.500F, 0.500F, -3.300F, 3.500F, 3.000F, 5.000F, new CubeDeformation(0.0F)).texOffs(8, 21).addBox(-1.500F, -2.800F, -0.300F, 2.000F, 3.000F, 6.000F, new CubeDeformation(0.0F)), PartPose.offset(-5.000F, -18.000F, -6.000F));
        PartDefinition body = parabol_model.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 11).addBox(-4.000F, -9.000F, 0.000F, 4.000F, 9.000F, 5.000F, new CubeDeformation(0.0F)).texOffs(10, 1).addBox(-5.900F, -10.000F, -1.000F, 7.800F, 3.000F, 3.000F, new CubeDeformation(0.0F)).texOffs(14, 0).addBox(-3.900F, -2.100F, -5.500F, 3.800F, 4.000F, 5.000F, new CubeDeformation(0.0F)), PartPose.offset(-4.000F, -21.000F, -11.000F));
        PartDefinition tail = parabol_model.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(26, 20).addBox(-0.500F, 0.800F, -12.400F, 1.000F, 2.000F, 2.000F, new CubeDeformation(0.0F)).texOffs(15, 0).addBox(-1.800F, -1.400F, -5.300F, 1.600F, 1.800F, 4.000F, new CubeDeformation(0.0F)).texOffs(1, 19).addBox(-2.000F, -3.000F, -4.000F, 2.000F, 2.000F, 4.000F, new CubeDeformation(0.0F)), PartPose.offset(-3.000F, -22.000F, -12.000F));
        PartDefinition rightWing = parabol_model.addOrReplaceChild("rightWing", CubeListBuilder.create().texOffs(0, 0).addBox(1.000F, -5.000F, -2.000F, 2.000F, 12.000F, 1.000F, new CubeDeformation(0.0F)).texOffs(0, 0).addBox(0.000F, -6.000F, -1.000F, 2.000F, 11.000F, 1.000F, new CubeDeformation(0.0F)).texOffs(0, 0).addBox(0.000F, -7.000F, -1.000F, 3.000F, 10.000F, 1.000F, new CubeDeformation(0.0F)).texOffs(0, 0).addBox(-1.000F, -6.000F, -1.000F, 1.000F, 6.000F, 4.000F, new CubeDeformation(0.0F)), PartPose.offset(-4.000F, -14.000F, -8.000F));
        PartDefinition leftWing = parabol_model.addOrReplaceChild("leftWing", CubeListBuilder.create().texOffs(0, 0).addBox(-3.000F, -5.000F, -2.000F, 2.000F, 12.000F, 1.000F, new CubeDeformation(0.0F)).texOffs(0, 0).addBox(-2.000F, -6.000F, -1.000F, 2.000F, 11.000F, 1.000F, new CubeDeformation(0.0F)).texOffs(0, 0).addBox(-3.000F, -7.000F, -1.000F, 3.000F, 10.000F, 1.000F, new CubeDeformation(0.0F)).texOffs(0, 0).addBox(0.000F, -6.000F, -1.000F, 1.000F, 6.000F, 4.000F, new CubeDeformation(0.0F)), PartPose.offset(0.000F, -14.000F, -8.000F));
        PartDefinition head = parabol_model.addOrReplaceChild("head", CubeListBuilder.create().texOffs(18, 14).addBox(-3.500F, -4.800F, -0.500F, 3.000F, 2.000F, 4.000F, new CubeDeformation(0.0F)).texOffs(18, 5).addBox(-3.100F, -4.000F, 0.500F, 2.200F, 1.000F, 2.000F, new CubeDeformation(0.0F)).texOffs(17, 29).addBox(-3.500F, -4.000F, -2.500F, 3.000F, 1.000F, 2.000F, new CubeDeformation(0.0F)).texOffs(16, 0).addBox(-3.800F, -6.000F, -4.900F, 3.600F, 4.000F, 2.500F, new CubeDeformation(0.0F)).texOffs(10, 26).addBox(-4.000F, -2.000F, -1.000F, 1.000F, 2.000F, 2.000F, new CubeDeformation(0.0F)).texOffs(10, 26).addBox(-1.000F, -2.000F, -1.000F, 1.000F, 2.000F, 2.000F, new CubeDeformation(0.0F)).texOffs(15, 17).addBox(-2.800F, -2.700F, -0.700F, 0.500F, 1.500F, 0.500F, new CubeDeformation(0.0F)).texOffs(15, 17).addBox(-1.800F, -2.700F, -0.700F, 0.500F, 1.500F, 0.500F, new CubeDeformation(0.0F)), PartPose.offset(-4.000F, -15.000F, -3.000F));
        PartDefinition eyes = parabol_model.addOrReplaceChild("eyes", CubeListBuilder.create().texOffs(30, 31).addBox(-0.800F, -0.700F, 0.300F, 0.500F, 0.500F, 0.500F, new CubeDeformation(0.0F)).texOffs(30, 31).addBox(-3.700F, -0.700F, 0.300F, 0.500F, 0.500F, 0.500F, new CubeDeformation(0.0F)), PartPose.offset(-4.000F, -9.000F, -1.000F));
        PartDefinition teeth = parabol_model.addOrReplaceChild("teeth", CubeListBuilder.create().texOffs(18, 16).addBox(-2.800F, -1.800F, 0.200F, 0.500F, 0.600F, 0.500F, new CubeDeformation(0.0F)).texOffs(18, 16).addBox(-1.800F, -1.800F, 0.200F, 0.500F, 0.600F, 0.500F, new CubeDeformation(0.0F)).texOffs(18, 16).addBox(-1.800F, -0.600F, 0.200F, 0.500F, 0.600F, 0.500F, new CubeDeformation(0.0F)).texOffs(18, 16).addBox(-2.800F, -0.600F, 0.200F, 0.500F, 0.600F, 0.500F, new CubeDeformation(0.0F)), PartPose.offset(-4.000F, -12.000F, 0.000F));


        return LayerDefinition.create(meshdefinition, 64, 64);
    }


    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
// Head look
        this.head.yRot = netHeadYaw * Mth.DEG_TO_RAD;
        this.head.xRot = headPitch * Mth.DEG_TO_RAD;


// Tail idle sway
        this.tail.yRot = Mth.cos(ageInTicks * 0.2F) * 0.1F;
        this.tail.xRot = -0.2F + Mth.sin(ageInTicks * 0.15F) * 0.05F;


// Wing subtle breathing
        this.rightWing.zRot = (Mth.sin(ageInTicks * 0.1F) * 0.05F) - 0.2F;
        this.leftWing.zRot = -this.rightWing.zRot;


// Legs walking cycle around a crouched baseline
        float walkSpeed = 0.6F;
        float walkDegree = 0.8F * limbSwingAmount;


        this.frontLeftLeg.xRot = -0.8F + Mth.cos(limbSwing * walkSpeed) * walkDegree;
        this.frontRightLeg.xRot = -0.8F + Mth.cos(limbSwing * walkSpeed + (float)Math.PI) * walkDegree;


        this.backLeftLeg.xRot = -0.4F + Mth.cos(limbSwing * walkSpeed + (float)Math.PI) * walkDegree;
        this.backRightLeg.xRot = -0.4F + Mth.cos(limbSwing * walkSpeed) * walkDegree;


// Body breathing/crouch bob
        this.body.xRot = -0.35F + Mth.sin(ageInTicks * 0.1F) * 0.02F;
    }

//    @Override
//    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
//        // Convert to radians
//        float headYawRad = netHeadYaw * ((float)Math.PI / 180F);
//        float headPitchRad = headPitch * ((float)Math.PI / 180F);
//
//        // --- HEAD: rotate visible head parts around neck pivot (neckDefaultPose) ---
//        // neckDefaultPose holds the exported pivot of the neck_r1 child (in head/body local coords)
//        float pivotX = this.neckDefaultPose.x;
//        float pivotY = this.neckDefaultPose.y;
//        float pivotZ = this.neckDefaultPose.z;
//
//        for (int i = 0; i < this.headParts.length; i++) {
//            ModelPart part = this.headParts[i];
//            PartPose def = this.headDefaultPoses[i];
//
//            // vector from neck pivot to part default offset
//            float vx = def.x - pivotX;
//            float vy = def.y - pivotY;
//            float vz = def.z - pivotZ;
//
//            // apply yaw (Y axis)
//            float cosY = (float)Math.cos(headYawRad);
//            float sinY = (float)Math.sin(headYawRad);
//            float rx = vx * cosY - vz * sinY;
//            float rz = vx * sinY + vz * cosY;
//            float ry = vy;
//
//            // apply pitch (X axis)
//            float cosX = (float)Math.cos(headPitchRad);
//            float sinX = (float)Math.sin(headPitchRad);
//            float ry2 = ry * cosX - rz * sinX;
//            float rz2 = ry * sinX + rz * cosX;
//            float rx2 = rx;
//
//            float newX = pivotX + rx2;
//            float newY = pivotY + ry2;
//            float newZ = pivotZ + rz2;
//
//            // combine rotations: keep part's default rotation and add head yaw/pitch
//            float newXRot = def.xRot + headPitchRad;
//            float newYRot = def.yRot + headYawRad;
//            float newZRot = def.zRot;
//
//            part.loadPose(PartPose.offsetAndRotation(newX, newY, newZ, newXRot, newYRot, newZRot));
//        }
//
//        // --- WALKING: quadruped legs (front legs should support front, so they act like shoulders) ---
//        float walkCycle = 0.6662F;
//        float walkSwing = Mth.cos(limbSwing * walkCycle) * 1.4F * limbSwingAmount;
//        float walkSwingOpp = Mth.cos(limbSwing * walkCycle + (float)Math.PI) * 1.4F * limbSwingAmount;
//
//        this.frontLeftLeg.xRot = walkSwing;
//        this.frontRightLeg.xRot = walkSwingOpp;
//        this.backLeftLeg.xRot = walkSwingOpp * 0.6F;
//        this.backRightLeg.xRot = walkSwing * 0.6F;
//
//        // --- WINGS: unified flap, now that parts are parented under wingBase ---
//        boolean airborne = !entity.onGround();
//        float wingSpeed = airborne ? 0.5F : 0.12F;
//        float wingAmplitude = airborne ? 0.9F : 0.42F;
//        float flap = Mth.sin(ageInTicks * wingSpeed) * wingAmplitude;
//
//        // restore wing base default pose then add flap to yaw (children follow automatically)
//        this.rightWingBase.loadPose(this.rightWingBaseDefault);
//        this.leftWingBase.loadPose(this.leftWingBaseDefault);
//
//        // add flap on top of exported yaw
//        this.rightWingBase.yRot += flap;
//        this.leftWingBase.yRot  -= flap; // mirror
//
//        // also ensure the wing parents are at their default (avoid parent offsets accumulating)
//        this.rightWing.loadPose(this.rightWingDefault);
//        this.leftWing.loadPose(this.leftWingDefault);
//
//        // Tail wag (unchanged)
//        this.tail.yRot = Mth.sin(ageInTicks * 0.08F) * 0.15F;
//
//        // Sitting pose: fold and tuck
//        if (entity instanceof GargoylePossessedEntity gp && gp.isOrderedToSit()) {
//            this.frontLeftLeg.xRot = -0.6F;
//            this.frontRightLeg.xRot = -0.6F;
//            this.backLeftLeg.xRot = -0.8F;
//            this.backRightLeg.xRot = -0.8F;
//
//            // tuck wings: restore then offset base yaw
//            this.rightWingBase.loadPose(this.rightWingBaseDefault);
//            this.leftWingBase.loadPose(this.leftWingBaseDefault);
//            this.rightWingBase.yRot += -0.2F;
//            this.leftWingBase.yRot  += 0.2F;
//
//            // lower head slightly (restore each head piece then nudge)
//            for (int i = 0; i < this.headParts.length; i++) {
//                ModelPart part = this.headParts[i];
//                PartPose def = this.headDefaultPoses[i];
//                part.loadPose(PartPose.offsetAndRotation(def.x, def.y + 0.1F, def.z, def.xRot + 0.1F, def.yRot, def.zRot));
//            }
//
//            this.tail.yRot = 0.0F;
//        }
//    }

    /**
     * renderToBuffer signature for Mojang mappings / 1.21.1:
     * renderToBuffer(PoseStack, VertexConsumer, int light, int overlay, int color)
     */
    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        poseStack.pushPose();
        // flip 180° so Blockbench south -> in-game forward (tweak if your model still faces wrong way)
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        this.parabol_model.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        poseStack.popPose();
    }
}
