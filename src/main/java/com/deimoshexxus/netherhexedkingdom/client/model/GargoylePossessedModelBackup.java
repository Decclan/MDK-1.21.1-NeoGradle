//package com.deimoshexxus.netherhexedkingdom.client.model;
//
//import com.deimoshexxus.netherhexedkingdom.NetherHexedKingdom;
//import com.mojang.blaze3d.vertex.PoseStack;
//import com.mojang.blaze3d.vertex.VertexConsumer;
//import net.minecraft.client.model.EntityModel;
//import net.minecraft.client.model.geom.ModelPart;
//import net.minecraft.client.model.geom.ModelLayerLocation;
//import net.minecraft.client.model.geom.PartPose;
//import net.minecraft.client.model.geom.builders.*;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.entity.Entity;
//import net.neoforged.api.distmarker.Dist;
//import net.neoforged.api.distmarker.OnlyIn;
//
//@OnlyIn(Dist.CLIENT)
//public class GargoylePossessedModel<T extends Entity> extends EntityModel<T> {
//
//    public static final ModelLayerLocation LAYER_LOCATION =
//            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(NetherHexedKingdom.MODID, "gargoyle_possessed"), "main");
//
//    private final ModelPart gargoyle_model;
//    private final ModelPart body;
//    private final ModelPart head;
//    private final ModelPart eyes;
//    private final ModelPart teeth;
//    private final ModelPart frontLeftLeg;
//    private final ModelPart frontRightLeg;
//    private final ModelPart backLeftLeg;
//    private final ModelPart backRightLeg;
//    private final ModelPart tail;
//    private final ModelPart rightWing;
//    private final ModelPart leftWing;
//
//    public GargoylePossessedModel(ModelPart root) {
//        this.gargoyle_model = root.getChild("gargoyle_model");
//        this.body = this.gargoyle_model.getChild("body");
//        this.head = this.body.getChild("head");
//        this.eyes = this.head.getChild("eyes");
//        this.teeth = this.head.getChild("teeth");
//        this.frontLeftLeg = this.gargoyle_model.getChild("frontLeftLeg");
//        this.frontRightLeg = this.gargoyle_model.getChild("frontRightLeg");
//        this.backLeftLeg = this.gargoyle_model.getChild("backLeftLeg");
//        this.backRightLeg = this.gargoyle_model.getChild("backRightLeg");
//        this.tail = this.gargoyle_model.getChild("tail");
//        this.rightWing = this.gargoyle_model.getChild("rightWing");
//        this.leftWing = this.gargoyle_model.getChild("leftWing");
//    }
//
//    public static LayerDefinition createBodyLayer() {
//        MeshDefinition meshdefinition = new MeshDefinition();
//        PartDefinition partdefinition = meshdefinition.getRoot();
//
//        PartDefinition gargoyle_model = partdefinition.addOrReplaceChild("gargoyle_model", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
//
//        PartDefinition body = gargoyle_model.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0033F, -8.0165F, 1.0588F, -0.7854F, 0.0F, 0.0F));
//
//        body.addOrReplaceChild("neck_r1", CubeListBuilder.create().texOffs(14, 0).addBox(-1.9F, -2.0F, -2.5F, 3.8F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.4912F, -3.952F, 2.138F, 0.0F, 0.0F));
//        body.addOrReplaceChild("chest_r1", CubeListBuilder.create().texOffs(10, 1).addBox(-3.9F, -1.5F, -1.5F, 7.8F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.0264F, -2.3665F, 0.5236F, 0.0F, 0.0F));
//        body.addOrReplaceChild("torso_r1", CubeListBuilder.create().texOffs(0, 11).addBox(-2.0F, -4.5F, -2.5F, 4.0F, 9.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.5175F, 0.9657F, -1.1781F, 0.0F, 0.0F));
//
//        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.015F, 3.2087F, -5.4709F, 1.0036F, 0.0F, 0.0F));
//        head.addOrReplaceChild("earLeft_r1", CubeListBuilder.create().texOffs(10, 26).addBox(-0.5F, -1.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
//                .texOffs(10, 26).mirror().addBox(2.5F, -1.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.485F, -4.1233F, -1.1367F, 0.7854F, 0.0F, 0.0F));
//        head.addOrReplaceChild("hornLeft_r1", CubeListBuilder.create().texOffs(15, 17).mirror().addBox(-0.25F, -0.75F, -0.25F, 0.5F, 1.5F, 0.5F, new CubeDeformation(0.0F)).mirror(false)
//                .texOffs(15, 17).addBox(0.75F, -0.75F, -0.25F, 0.5F, 1.5F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.535F, -4.0456F, -1.2676F, -0.3927F, 0.0F, 0.0F));
//        head.addOrReplaceChild("snout_r1", CubeListBuilder.create().texOffs(18, 14).addBox(-1.5F, -1.0F, -2.0F, 3.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.015F, -2.3529F, -3.2696F, 0.3927F, 0.0F, 0.0F));
//        head.addOrReplaceChild("chin_r1", CubeListBuilder.create().texOffs(18, 5).addBox(-1.1F, -0.5F, -1.0F, 2.2F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.015F, 0.9242F, -3.1548F, 0.3927F, 0.0F, 0.0F));
//        head.addOrReplaceChild("jaw_r1", CubeListBuilder.create().texOffs(22, 4).addBox(-1.5F, -0.5F, -1.0F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.015F, 0.0483F, -1.8438F, 0.7854F, 0.0F, 0.0F));
//        head.addOrReplaceChild("head_r1", CubeListBuilder.create().texOffs(16, 0).addBox(-1.8F, -2.0F, -1.25F, 3.6F, 4.0F, 2.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.015F, -1.8256F, -0.677F, 0.7854F, 0.0F, 0.0F));
//
//        head.addOrReplaceChild("eyes", CubeListBuilder.create(), PartPose.offset(0.015F, -2.6215F, -3.1099F))
//                .addOrReplaceChild("eyeRight_r1", CubeListBuilder.create().texOffs(30, 31).addBox(-0.25F, -0.25F, -0.25F, 0.5F, 0.5F, 0.5F, new CubeDeformation(0.0F))
//                        .texOffs(30, 31).addBox(-3.15F, -0.25F, -0.25F, 0.5F, 0.5F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.45F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));
//
//        head.addOrReplaceChild("teeth", CubeListBuilder.create(), PartPose.offset(-0.035F, -0.0755F, 14.5515F))
//                .addOrReplaceChild("toothTopLeft_r1", CubeListBuilder.create().texOffs(18, 16).addBox(-0.25F, -0.3F, -0.25F, 0.5F, 0.6F, 0.5F, new CubeDeformation(0.0F))
//                        .texOffs(18, 16).addBox(0.75F, -0.3F, -0.25F, 0.5F, 0.6F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, -0.5543F, -18.9708F, 0.3927F, 0.0F, 0.0F))
//                .addOrReplaceChild("toothBottomRight_r1", CubeListBuilder.create().texOffs(18, 16).addBox(-0.25F, -0.3F, -0.25F, 0.5F, 0.6F, 0.5F, new CubeDeformation(0.0F))
//                        .texOffs(18, 16).addBox(-1.25F, -0.3F, -0.25F, 0.5F, 0.6F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 0.5543F, -18.5115F, 0.3927F, 0.0F, 0.0F));
//
//        PartDefinition frontLeftLeg = gargoyle_model.addOrReplaceChild("frontLeftLeg", CubeListBuilder.create().texOffs(1, 23).addBox(-1.5F, 5.4858F, -3.3487F, 3.0F, 1.0F, 3.5F, new CubeDeformation(0.0F)), PartPose.offset(-2.4967F, -6.4858F, -3.7617F));
//        frontLeftLeg.addOrReplaceChild("upperarmLeft_r1", CubeListBuilder.create().texOffs(6, 19).mirror().addBox(-1.2F, -2.0F, -1.0F, 2.4F, 4.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 1.9425F, 1.4474F, 0.3491F, 0.0F, 0.0F));
//        frontLeftLeg.addOrReplaceChild("fofrontmLeft_r1", CubeListBuilder.create().texOffs(17, 24).mirror().addBox(-1.0F, -3.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 4.0716F, 0.1513F, -0.7854F, 0.0F, 0.0F));
//
//        PartDefinition frontRightLeg = gargoyle_model.addOrReplaceChild("frontRightLeg", CubeListBuilder.create().texOffs(1, 23).mirror().addBox(-1.5F, 5.4858F, -3.3487F, 3.0F, 1.0F, 3.5F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(2.5033F, -6.4858F, -3.7617F));
//        frontRightLeg.addOrReplaceChild("upperarmRight_r1", CubeListBuilder.create().texOffs(6, 19).addBox(-1.2F, -2.0F, -1.0F, 2.4F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.9425F, 1.4474F, 0.3491F, 0.0F, 0.0F));
//        frontRightLeg.addOrReplaceChild("fofrontmRight_r1", CubeListBuilder.create().texOffs(17, 24).addBox(-1.0F, -3.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.0716F, 0.1513F, -0.7854F, 0.0F, 0.0F));
//
//        PartDefinition backLeftLeg = gargoyle_model.addOrReplaceChild("backLeftLeg", CubeListBuilder.create().texOffs(16, 7).addBox(-1.5833F, 5.3976F, -4.0025F, 3.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.4133F, -6.3976F, 4.3921F));
//        backLeftLeg.addOrReplaceChild("upperlegLeft_r1", CubeListBuilder.create().texOffs(15, 2).mirror().addBox(-1.75F, -1.5F, -2.5F, 3.5F, 3.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.1667F, -0.0608F, -0.498F, 0.6545F, 0.0F, 0.0F));
//        backLeftLeg.addOrReplaceChild("lowerlegLeft_r1", CubeListBuilder.create().texOffs(8, 21).addBox(-1.0F, -1.5F, -3.0F, 2.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0833F, 3.1633F, -0.9995F, -0.829F, 0.0F, 0.0F));
//
//        PartDefinition backRightLeg = gargoyle_model.addOrReplaceChild("backRightLeg", CubeListBuilder.create().texOffs(16, 7).mirror().addBox(-1.4167F, 5.3976F, -4.0025F, 3.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(3.42F, -6.3976F, 4.3921F));
//        backRightLeg.addOrReplaceChild("lowerlegRight_r1", CubeListBuilder.create().texOffs(8, 21).mirror().addBox(-1.0F, -1.5F, -3.0F, 2.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0833F, 3.1633F, -0.9995F, -0.829F, 0.0F, 0.0F));
//        backRightLeg.addOrReplaceChild("upperlegRight_r1", CubeListBuilder.create().texOffs(15, 2).addBox(-1.75F, -1.5F, -2.5F, 3.5F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1667F, -0.0608F, -0.498F, 0.6545F, 0.0F, 0.0F));
//
//        PartDefinition tail = gargoyle_model.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(26, 20).addBox(-0.5F, -5.828F, 3.7789F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0033F, -6.3646F, 5.5908F, -1.6144F, 0.0F, 0.0F));
//        tail.addOrReplaceChild("tailCenter_r1", CubeListBuilder.create().texOffs(15, 0).addBox(-0.8F, -0.9F, -2.0F, 1.6F, 1.8F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.315F, 3.3587F, 0.7854F, 0.0F, 0.0F));
//        tail.addOrReplaceChild("tailBase_r1", CubeListBuilder.create().texOffs(1, 19).addBox(-1.0F, -1.0F, -2.0F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.2411F, 0.4612F, 0.3927F, 0.0F, 0.0F));
//
//        PartDefinition rightWing = gargoyle_model.addOrReplaceChild("rightWing", CubeListBuilder.create(), PartPose.offsetAndRotation(0.9883F, -12.125F, -0.6469F, 1.2217F, 0.0F, 0.0F));
//        rightWing.addOrReplaceChild("wingTipRight_r1", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0F, -6.0F, -0.5F, 2.0F, 12.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(5.4899F, 4.125F, 0.6829F, 0.0F, 0.7854F, 0.0F));
//        rightWing.addOrReplaceChild("wingSpanTwoTipRight_r1", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0F, -5.5F, -0.5F, 2.0F, 11.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(4.1302F, 2.625F, 1.1157F, 0.0F, 0.3927F, 0.0F));
//        rightWing.addOrReplaceChild("wingSpanOneBaseRight_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -5.0F, -0.5F, 3.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.2095F, 1.125F, 1.0724F, 0.0F, -0.3927F, 0.0F));
//        rightWing.addOrReplaceChild("wingBaseRight_r1", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-0.5F, -3.0F, -2.0F, 1.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.1704F, 0.125F, -0.6961F, 0.0F, 0.3927F, 0.0F));
//
//        PartDefinition leftWing = gargoyle_model.addOrReplaceChild("leftWing", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.9817F, -12.125F, -0.6469F, 1.2217F, 0.0F, 0.0F));
//        leftWing.addOrReplaceChild("wingTipLeft_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -6.0F, -0.5F, 2.0F, 12.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.4899F, 4.125F, 0.6829F, 0.0F, -0.7854F, 0.0F));
//        leftWing.addOrReplaceChild("wingSpanTwoTipLeft_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -5.5F, -0.5F, 2.0F, 11.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.1302F, 2.625F, 1.1157F, 0.0F, -0.3927F, 0.0F));
//        leftWing.addOrReplaceChild("wingSpanOneBaseLeft_r1", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.5F, -5.0F, -0.5F, 3.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.2095F, 1.125F, 1.0724F, 0.0F, 0.3927F, 0.0F));
//        leftWing.addOrReplaceChild("wingBaseLeft_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -3.0F, -2.0F, 1.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1704F, 0.125F, -0.6961F, 0.0F, -0.3927F, 0.0F));
//
//        return LayerDefinition.create(meshdefinition, 32, 32);
//    }
//
//    @Override
//    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
//        // fill in your animation logic here (use T if you need entity-specific checks)
//    }
//
//    @Override
//    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
//        poseStack.pushPose();
//        this.gargoyle_model.render(poseStack, vertexConsumer, packedLight, packedOverlay);
//        poseStack.popPose();
//    }
//}
