//// Made with Blockbench 5.0.4
//// Exported for Minecraft version 1.17 or later with Mojang mappings
//// Paste this class into your mod and generate all required imports
//
//
//public class gargoyle_possessed_entity<T extends Entity> extends EntityModel<T> {
//	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
//	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "gargoyle_possessed_entity"), "main");
//	private final ModelPart gargoyle_model;
//	private final ModelPart frontLeftLeg;
//	private final ModelPart frontRightLeg;
//	private final ModelPart backLeftLeg;
//	private final ModelPart backRightLeg;
//	private final ModelPart body;
//	private final ModelPart tail;
//	private final ModelPart wings;
//	private final ModelPart rightWing;
//	private final ModelPart leftWing;
//	private final ModelPart head;
//	private final ModelPart eyes;
//	private final ModelPart teeth;
//
//	public gargoyle_possessed_entity(ModelPart root) {
//		this.gargoyle_model = root.getChild("gargoyle_model");
//		this.frontLeftLeg = this.gargoyle_model.getChild("frontLeftLeg");
//		this.frontRightLeg = this.gargoyle_model.getChild("frontRightLeg");
//		this.backLeftLeg = this.gargoyle_model.getChild("backLeftLeg");
//		this.backRightLeg = this.gargoyle_model.getChild("backRightLeg");
//		this.body = this.gargoyle_model.getChild("body");
//		this.tail = this.gargoyle_model.getChild("tail");
//		this.wings = this.gargoyle_model.getChild("wings");
//		this.rightWing = this.wings.getChild("rightWing");
//		this.leftWing = this.wings.getChild("leftWing");
//		this.head = this.gargoyle_model.getChild("head");
//		this.eyes = this.head.getChild("eyes");
//		this.teeth = this.head.getChild("teeth");
//	}
//
//	public static LayerDefinition createBodyLayer() {
//		MeshDefinition meshdefinition = new MeshDefinition();
//		PartDefinition partdefinition = meshdefinition.getRoot();
//
//		PartDefinition gargoyle_model = partdefinition.addOrReplaceChild("gargoyle_model", CubeListBuilder.create(), PartPose.offset(3.0F, 17.0F, -2.0F));
//
//		PartDefinition frontLeftLeg = gargoyle_model.addOrReplaceChild("frontLeftLeg", CubeListBuilder.create().texOffs(1, 23).addBox(-7.0F, 6.0F, 5.0F, 3.0F, 1.0F, 3.5F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
//
//		PartDefinition upperarmLeft_r1 = frontLeftLeg.addOrReplaceChild("upperarmLeft_r1", CubeListBuilder.create().texOffs(6, 19).mirror().addBox(-7.7F, -8.0F, -1.0F, 2.4F, 4.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(1.0F, 6.0F, 5.0F, 0.3927F, 0.0F, 0.0F));
//
//		PartDefinition forearmLeft_r1 = frontLeftLeg.addOrReplaceChild("forearmLeft_r1", CubeListBuilder.create().texOffs(17, 24).addBox(-2.5F, -4.0F, 0.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, 6.0F, 5.0F, 0.7854F, 0.0F, 0.0F));
//
//		PartDefinition frontRightLeg = gargoyle_model.addOrReplaceChild("frontRightLeg", CubeListBuilder.create().texOffs(1, 23).mirror().addBox(-2.0F, 6.0F, 5.0F, 3.0F, 1.0F, 3.5F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));
//
//		PartDefinition upperarmRight_r1 = frontRightLeg.addOrReplaceChild("upperarmRight_r1", CubeListBuilder.create().texOffs(6, 19).addBox(-2.7F, -8.0F, -1.0F, 2.4F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 6.0F, 5.0F, 0.3927F, 0.0F, 0.0F));
//
//		PartDefinition forearmRight_r1 = frontRightLeg.addOrReplaceChild("forearmRight_r1", CubeListBuilder.create().texOffs(17, 24).mirror().addBox(-2.5F, -4.0F, 0.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(1.0F, 6.0F, 5.0F, 0.7854F, 0.0F, 0.0F));
//
//		PartDefinition backLeftLeg = gargoyle_model.addOrReplaceChild("backLeftLeg", CubeListBuilder.create().texOffs(16, 7).addBox(-8.0F, 6.0F, -3.0F, 3.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
//
//		PartDefinition upperlegLeft_r1 = backLeftLeg.addOrReplaceChild("upperlegLeft_r1", CubeListBuilder.create().texOffs(15, 2).mirror().addBox(-1.0F, 0.5F, -3.3F, 3.5F, 3.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-7.0F, 1.0F, 0.0F, -0.3927F, 0.0F, 0.0F));
//
//		PartDefinition lowerlegLeft_r1 = backLeftLeg.addOrReplaceChild("lowerlegLeft_r1", CubeListBuilder.create().texOffs(8, 21).addBox(-1.5F, -2.8F, -0.3F, 2.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.0F, 7.0F, -3.0F, 0.3927F, 0.0F, 0.0F));
//
//		PartDefinition backRightLeg = gargoyle_model.addOrReplaceChild("backRightLeg", CubeListBuilder.create().texOffs(16, 7).mirror().addBox(-1.0F, 6.0F, -3.0F, 3.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));
//
//		PartDefinition lowerlegRight_r1 = backRightLeg.addOrReplaceChild("lowerlegRight_r1", CubeListBuilder.create().texOffs(8, 21).mirror().addBox(-1.5F, -2.8F, -0.3F, 2.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(1.0F, 7.0F, -3.0F, 0.3927F, 0.0F, 0.0F));
//
//		PartDefinition upperlegRight_r1 = backRightLeg.addOrReplaceChild("upperlegRight_r1", CubeListBuilder.create().texOffs(15, 2).addBox(-1.5F, 0.5F, -3.3F, 3.5F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, -0.3927F, 0.0F, 0.0F));
//
//		PartDefinition body = gargoyle_model.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(5.0F, 7.0F, -5.0F));
//
//		PartDefinition shoulders_r1 = body.addOrReplaceChild("shoulders_r1", CubeListBuilder.create().texOffs(10, 1).addBox(-5.9F, -10.0F, -1.0F, 7.8F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.0F, -3.0F, 0.0F, -0.7854F, 0.0F, 0.0F));
//
//		PartDefinition neck_r1 = body.addOrReplaceChild("neck_r1", CubeListBuilder.create().texOffs(14, 0).addBox(-3.9F, -2.1F, -5.5F, 3.8F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.0F, -9.0F, 8.0F, -0.7854F, 0.0F, 0.0F));
//
//		PartDefinition torso_r1 = body.addOrReplaceChild("torso_r1", CubeListBuilder.create().texOffs(0, 11).addBox(-4.0F, -9.0F, 0.0F, 4.0F, 9.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.0F, -3.0F, 0.0F, -0.3927F, 0.0F, 0.0F));
//
//		PartDefinition tail = gargoyle_model.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(26, 20).addBox(-0.5F, 0.8F, -12.4F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, -1.0F, 2.0F));
//
//		PartDefinition tailCenter_r1 = tail.addOrReplaceChild("tailCenter_r1", CubeListBuilder.create().texOffs(15, 0).addBox(-1.8F, -1.4F, -5.3F, 1.6F, 1.8F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 6.0F, -8.0F, -0.7854F, 0.0F, 0.0F));
//
//		PartDefinition tailBase_r1 = tail.addOrReplaceChild("tailBase_r1", CubeListBuilder.create().texOffs(1, 19).addBox(-2.0F, -3.0F, -4.0F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 8.0F, -6.0F, -0.3927F, 0.0F, 0.0F));
//
//		PartDefinition wings = gargoyle_model.addOrReplaceChild("wings", CubeListBuilder.create(), PartPose.offset(-3.0F, -1.0F, 2.0F));
//
//		PartDefinition rightWing = wings.addOrReplaceChild("rightWing", CubeListBuilder.create(), PartPose.offset(4.0F, -2.0F, -6.0F));
//
//		PartDefinition wingEndRight_r1 = rightWing.addOrReplaceChild("wingEndRight_r1", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(1.0F, -5.0F, -2.0F, 2.0F, 12.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));
//
//		PartDefinition wingPartTwoRight_r1 = rightWing.addOrReplaceChild("wingPartTwoRight_r1", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(0.0F, -6.0F, -1.0F, 2.0F, 11.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.3927F, 0.0F));
//
//		PartDefinition wingPartOneRight_r1 = rightWing.addOrReplaceChild("wingPartOneRight_r1", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(0.0F, -7.0F, -1.0F, 3.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.0F, 0.0F, 1.0F, 0.0F, 0.3927F, 0.0F));
//
//		PartDefinition wingBaseRight_r1 = rightWing.addOrReplaceChild("wingBaseRight_r1", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0F, -6.0F, -1.0F, 1.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.0F, 0.0F, 2.0F, 0.0F, -0.3927F, 0.0F));
//
//		PartDefinition leftWing = wings.addOrReplaceChild("leftWing", CubeListBuilder.create(), PartPose.offset(-4.0F, -2.0F, -6.0F));
//
//		PartDefinition wingEndLeft_r1 = leftWing.addOrReplaceChild("wingEndLeft_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -5.0F, -2.0F, 2.0F, 12.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));
//
//		PartDefinition wingPartTwoLeft_r1 = leftWing.addOrReplaceChild("wingPartTwoLeft_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -6.0F, -1.0F, 2.0F, 11.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.3927F, 0.0F));
//
//		PartDefinition wingPartOneLeft_r1 = leftWing.addOrReplaceChild("wingPartOneLeft_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -7.0F, -1.0F, 3.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 0.0F, 1.0F, 0.0F, -0.3927F, 0.0F));
//
//		PartDefinition wingBaseLeft_r1 = leftWing.addOrReplaceChild("wingBaseLeft_r1", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -6.0F, -1.0F, 1.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 0.0F, 2.0F, 0.0F, 0.3927F, 0.0F));
//
//		PartDefinition head = gargoyle_model.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(5.0F, 7.0F, -5.0F));
//
//		PartDefinition earLeft_r1 = head.addOrReplaceChild("earLeft_r1", CubeListBuilder.create().texOffs(10, 26).addBox(-4.0F, -2.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
//		.texOffs(10, 26).mirror().addBox(-1.0F, -2.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-6.0F, -16.0F, 8.0F, -0.7854F, 0.0F, 0.0F));
//
//		PartDefinition hornLeft_r1 = head.addOrReplaceChild("hornLeft_r1", CubeListBuilder.create().texOffs(15, 17).mirror().addBox(-2.8F, -2.7F, -0.7F, 0.5F, 1.5F, 0.5F, new CubeDeformation(0.0F)).mirror(false)
//		.texOffs(15, 17).addBox(-1.8F, -2.7F, -0.7F, 0.5F, 1.5F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.0F, -15.0F, 10.0F, 0.3927F, 0.0F, 0.0F));
//
//		PartDefinition snout_r1 = head.addOrReplaceChild("snout_r1", CubeListBuilder.create().texOffs(18, 14).addBox(-3.5F, -4.8F, -0.5F, 3.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.0F, -12.0F, 8.0F, -0.3927F, 0.0F, 0.0F));
//
//		PartDefinition chin_r1 = head.addOrReplaceChild("chin_r1", CubeListBuilder.create().texOffs(18, 5).addBox(-3.1F, -4.0F, 0.5F, 2.2F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.0F, -9.0F, 8.0F, -0.3927F, 0.0F, 0.0F));
//
//		PartDefinition jaw_r1 = head.addOrReplaceChild("jaw_r1", CubeListBuilder.create().texOffs(17, 29).addBox(-3.5F, -4.0F, -2.5F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
//		.texOffs(16, 0).addBox(-3.8F, -6.0F, -4.9F, 3.6F, 4.0F, 2.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.0F, -9.0F, 8.0F, -0.7854F, 0.0F, 0.0F));
//
//		PartDefinition eyes = head.addOrReplaceChild("eyes", CubeListBuilder.create(), PartPose.offset(-6.0F, -15.0F, 10.0F));
//
//		PartDefinition eyeRight_r1 = eyes.addOrReplaceChild("eyeRight_r1", CubeListBuilder.create().texOffs(30, 31).addBox(-0.8F, -0.7F, 0.3F, 0.5F, 0.5F, 0.5F, new CubeDeformation(0.0F))
//		.texOffs(30, 31).addBox(-3.7F, -0.7F, 0.3F, 0.5F, 0.5F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));
//
//		PartDefinition teeth = head.addOrReplaceChild("teeth", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
//
//		PartDefinition toothTopLeft_r1 = teeth.addOrReplaceChild("toothTopLeft_r1", CubeListBuilder.create().texOffs(18, 16).addBox(-2.8F, -1.8F, 0.2F, 0.5F, 0.6F, 0.5F, new CubeDeformation(0.0F))
//		.texOffs(18, 16).addBox(-1.8F, -1.8F, 0.2F, 0.5F, 0.6F, 0.5F, new CubeDeformation(0.0F))
//		.texOffs(18, 16).addBox(-1.8F, -0.6F, 0.2F, 0.5F, 0.6F, 0.5F, new CubeDeformation(0.0F))
//		.texOffs(18, 16).addBox(-2.8F, -0.6F, 0.2F, 0.5F, 0.6F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.0F, -12.0F, 11.0F, -0.3927F, 0.0F, 0.0F));
//
//		return LayerDefinition.create(meshdefinition, 32, 32);
//	}
//
//	@Override
//	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
//
//	}
//
//	@Override
//	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
//		gargoyle_model.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
//	}
//}