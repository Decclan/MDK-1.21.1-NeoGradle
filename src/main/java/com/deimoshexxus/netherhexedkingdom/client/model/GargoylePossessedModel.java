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
public class GargoylePossessedModel<T extends Entity> extends EntityModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(
                    ResourceLocation.fromNamespaceAndPath(NetherHexedKingdom.MODID, "gargoyle_possessed"),
                    "main"
            );

    private final ModelPart gargoyle_model;
    private final ModelPart frontLeftLeg;
    private final ModelPart frontRightLeg;
    private final ModelPart backLeftLeg;
    private final ModelPart backRightLeg;
    /** points to the torso_r1 child which actually contains the torso cube */
    private final ModelPart bodyTorso;
    private final ModelPart tail;

    // wings
    private final ModelPart wings;
    private final ModelPart rightWing;
    private final ModelPart leftWing;
    private final ModelPart rightWingBase;
    private final ModelPart leftWingBase;
    private final ModelPart rightWingPartOne;
    private final ModelPart rightWingPartTwo;
    private final ModelPart rightWingEnd;
    private final ModelPart leftWingPartOne;
    private final ModelPart leftWingPartTwo;
    private final ModelPart leftWingEnd;

    // store default poses so we can restore them each frame
    private final PartPose rightWingBaseDefault;
    private final PartPose leftWingBaseDefault;
    private final PartPose rightWingDefault;
    private final PartPose leftWingDefault;

    /** head parent (we rotate visible children around the neck pivot) */
    private final ModelPart head;
    private final ModelPart snout;
    private final ModelPart jaw;
    private final ModelPart chin;
    private final ModelPart eyesRoot;
    private final ModelPart teethRoot;
    private final ModelPart[] headParts;
    private final PartPose[] headDefaultPoses;

    // neck pivot pose (we read exported neck pos to use as pivot)
    private final PartPose neckDefaultPose;

    /** eye child that contains actual eye cubes (kept for compatibility) */
    private final ModelPart eyes;
    /** tooth child that contains actual tooth cubes (kept for compatibility) */
    private final ModelPart teeth;

    public GargoylePossessedModel(ModelPart root) {
        this.gargoyle_model = root.getChild("gargoyle_model");

        this.frontLeftLeg = this.gargoyle_model.getChild("frontLeftLeg");
        this.frontRightLeg = this.gargoyle_model.getChild("frontRightLeg");
        this.backLeftLeg = this.gargoyle_model.getChild("backLeftLeg");
        this.backRightLeg = this.gargoyle_model.getChild("backRightLeg");

        // body & torso
        this.bodyTorso = this.gargoyle_model.getChild("body").getChild("torso_r1");

        this.tail = this.gargoyle_model.getChild("tail");

        // wings are grouped under "wings" per your structure
        this.wings = this.gargoyle_model.getChild("wings");
        this.rightWing = this.wings.getChild("rightWing");
        this.leftWing = this.wings.getChild("leftWing");

        // after code-side reparenting (see createBodyLayer), base is the pivot and parts are nested
        this.rightWingBase = this.rightWing.getChild("wingBaseRight_r1");
        this.rightWingPartOne = this.rightWingBase.getChild("wingPartOneRight_r1");
        this.rightWingPartTwo = this.rightWingPartOne.getChild("wingPartTwoRight_r1");
        this.rightWingEnd = this.rightWingPartTwo.getChild("wingEndRight_r1");

        this.leftWingBase = this.leftWing.getChild("wingBaseLeft_r1");
        this.leftWingPartOne = this.leftWingBase.getChild("wingPartOneLeft_r1");
        this.leftWingPartTwo = this.leftWingPartOne.getChild("wingPartTwoLeft_r1");
        this.leftWingEnd = this.leftWingPartTwo.getChild("wingEndLeft_r1");

        // head and visible children
        this.head = this.gargoyle_model.getChild("head");
        this.eyes = this.head.getChild("eyes").getChild("eyeRight_r1");
        this.teeth = this.head.getChild("teeth").getChild("toothTopLeft_r1");
        this.snout = this.head.getChild("snout_r1");
        this.jaw = this.head.getChild("jaw_r1");
        this.chin = this.head.getChild("chin_r1");
        this.eyesRoot = this.head.getChild("eyes");
        this.teethRoot = this.head.getChild("teeth");

        this.headParts = new ModelPart[] { this.snout, this.jaw, this.chin, this.eyesRoot, this.teethRoot };
        this.headDefaultPoses = new PartPose[this.headParts.length];
        for (int i = 0; i < this.headParts.length; i++) {
            this.headDefaultPoses[i] = this.headParts[i].storePose();
        }

        // capture neck pose (neck_r1 is under body)
        ModelPart neckPart = this.gargoyle_model.getChild("body").getChild("neck_r1");
        this.neckDefaultPose = neckPart.storePose();

        // capture wing default poses to restore each frame
        this.rightWingBaseDefault = this.rightWingBase.storePose();
        this.leftWingBaseDefault = this.leftWingBase.storePose();
        this.rightWingDefault = this.rightWing.storePose();
        this.leftWingDefault = this.leftWing.storePose();
    }

    /**
     * Reworked createBodyLayer that preserves the same cubes you exported,
     * but parents the wing parts into a chain so the wingBase is the pivot:
     *
     * rightWing
     *  └ wingBaseRight_r1  (pivot)
     *     └ wingPartOneRight_r1
     *        └ wingPartTwoRight_r1
     *           └ wingEndRight_r1
     *
     * leftWing mirrored.
     */
    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition root = meshdefinition.getRoot();

        PartDefinition gargoyle_model = root.addOrReplaceChild(
                "gargoyle_model",
                CubeListBuilder.create(),
                PartPose.offset(3.0F, 17.0F, -2.0F)
        );

        // --- (legs, body, tail) unchanged from your original export but copied here for completeness ---
        PartDefinition frontLeftLeg = gargoyle_model.addOrReplaceChild(
                "frontLeftLeg",
                CubeListBuilder.create().texOffs(1, 23).addBox(-7.0F, 6.0F, 5.0F, 3, 1, 3, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 0.0F, 0.0F)
        );

        frontLeftLeg.addOrReplaceChild(
                "upperarmLeft_r1",
                CubeListBuilder.create().texOffs(6, 19).mirror()
                        .addBox(-7.7F, -8.0F, -1.0F, 2.4F, 4.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false),
                PartPose.offsetAndRotation(1.0F, 6.0F, 5.0F, 0.3927F, 0.0F, 0.0F)
        );

        frontLeftLeg.addOrReplaceChild(
                "forearmLeft_r1",
                CubeListBuilder.create().texOffs(17, 24)
                        .addBox(-2.5F, -4.0F, 0.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-4.0F, 6.0F, 5.0F, 0.7854F, 0.0F, 0.0F)
        );

        PartDefinition frontRightLeg = gargoyle_model.addOrReplaceChild(
                "frontRightLeg",
                CubeListBuilder.create().texOffs(1, 23).mirror()
                        .addBox(-2.0F, 6.0F, 5.0F, 3, 1, 3, new CubeDeformation(0.0F)).mirror(false),
                PartPose.offset(0.0F, 0.0F, 0.0F)
        );

        frontRightLeg.addOrReplaceChild(
                "upperarmRight_r1",
                CubeListBuilder.create().texOffs(6, 19)
                        .addBox(-2.7F, -8.0F, -1.0F, 2.4F, 4.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(1.0F, 6.0F, 5.0F, 0.3927F, 0.0F, 0.0F)
        );

        frontRightLeg.addOrReplaceChild(
                "forearmRight_r1",
                CubeListBuilder.create().texOffs(17, 24).mirror()
                        .addBox(-2.5F, -4.0F, 0.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false),
                PartPose.offsetAndRotation(1.0F, 6.0F, 5.0F, 0.7854F, 0.0F, 0.0F)
        );

        PartDefinition backLeftLeg = gargoyle_model.addOrReplaceChild(
                "backLeftLeg",
                CubeListBuilder.create().texOffs(16, 7)
                        .addBox(-8.0F, 6.0F, -3.0F, 3.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 0.0F, 0.0F)
        );

        backLeftLeg.addOrReplaceChild(
                "upperlegLeft_r1",
                CubeListBuilder.create().texOffs(15, 2).mirror()
                        .addBox(-1.0F, 0.5F, -3.3F, 3.5F, 3.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false),
                PartPose.offsetAndRotation(-7.0F, 1.0F, 0.0F, -0.3927F, 0.0F, 0.0F)
        );

        backLeftLeg.addOrReplaceChild(
                "lowerlegLeft_r1",
                CubeListBuilder.create().texOffs(8, 21)
                        .addBox(-1.5F, -2.8F, -0.3F, 2.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-6.0F, 7.0F, -3.0F, 0.3927F, 0.0F, 0.0F)
        );

        PartDefinition backRightLeg = gargoyle_model.addOrReplaceChild(
                "backRightLeg",
                CubeListBuilder.create().texOffs(16, 7).mirror()
                        .addBox(-1.0F, 6.0F, -3.0F, 3.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false),
                PartPose.offset(0.0F, 0.0F, 0.0F)
        );

        backRightLeg.addOrReplaceChild(
                "lowerlegRight_r1",
                CubeListBuilder.create().texOffs(8, 21).mirror()
                        .addBox(-1.5F, -2.8F, -0.3F, 2.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false),
                PartPose.offsetAndRotation(1.0F, 7.0F, -3.0F, 0.3927F, 0.0F, 0.0F)
        );

        backRightLeg.addOrReplaceChild(
                "upperlegRight_r1",
                CubeListBuilder.create().texOffs(15, 2)
                        .addBox(-1.5F, 0.5F, -3.3F, 3.5F, 3.0F, 5.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, -0.3927F, 0.0F, 0.0F)
        );

        PartDefinition body = gargoyle_model.addOrReplaceChild(
                "body",
                CubeListBuilder.create(),
                PartPose.offset(5.0F, 7.0F, -5.0F)
        );

        body.addOrReplaceChild(
                "shoulders_r1",
                CubeListBuilder.create().texOffs(10, 1)
                        .addBox(-5.9F, -10.0F, -1.0F, 7.8F, 3.0F, 3.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-6.0F, -3.0F, 0.0F, -0.7854F, 0.0F, 0.0F)
        );

        body.addOrReplaceChild(
                "neck_r1",
                CubeListBuilder.create().texOffs(14, 0)
                        .addBox(-3.9F, -2.1F, -5.5F, 3.8F, 4.0F, 5.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-6.0F, -9.0F, 8.0F, -0.7854F, 0.0F, 0.0F)
        );

        body.addOrReplaceChild(
                "torso_r1",
                CubeListBuilder.create().texOffs(0, 11)
                        .addBox(-4.0F, -9.0F, 0.0F, 4.0F, 9.0F, 5.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-6.0F, -3.0F, 0.0F, -0.3927F, 0.0F, 0.0F)
        );

        PartDefinition tail = gargoyle_model.addOrReplaceChild(
                "tail",
                CubeListBuilder.create().texOffs(26, 20)
                        .addBox(-0.5F, 0.8F, -12.4F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offset(-3.0F, -1.0F, 2.0F)
        );

        tail.addOrReplaceChild(
                "tailCenter_r1",
                CubeListBuilder.create().texOffs(15, 0)
                        .addBox(-1.8F, -1.4F, -5.3F, 1.6F, 1.8F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(1.0F, 6.0F, -8.0F, -0.7854F, 0.0F, 0.0F)
        );

        tail.addOrReplaceChild(
                "tailBase_r1",
                CubeListBuilder.create().texOffs(1, 19)
                        .addBox(-2.0F, -3.0F, -4.0F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(1.0F, 8.0F, -6.0F, -0.3927F, 0.0F, 0.0F)
        );

        // --- wings group (unchanged position) ---
        PartDefinition wings = gargoyle_model.addOrReplaceChild(
                "wings",
                CubeListBuilder.create(),
                PartPose.offset(-3.0F, -1.0F, 2.0F)
        );

        // Right wing: create rightWing, then make a chain: wingBase -> partOne -> partTwo -> end
        PartDefinition rightWing = wings.addOrReplaceChild(
                "rightWing",
                CubeListBuilder.create(),
                PartPose.offset(4.0F, -2.0F, -6.0F)
        );

        // wingBase (pivot)
        PartDefinition baseR = rightWing.addOrReplaceChild(
                "wingBaseRight_r1",
                CubeListBuilder.create().texOffs(0, 0).mirror()
                        .addBox(-1.0F, -6.0F, -1.0F, 1.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false),
                // same as your export
                PartPose.offsetAndRotation(-2.0F, 0.0F, 2.0F, 0.0F, -0.3927F, 0.0F)
        );

        // part one: position relative to wingBase (computed from original offsets)
        PartDefinition partOneR = baseR.addOrReplaceChild(
                "wingPartOneRight_r1",
                CubeListBuilder.create().texOffs(0, 0).mirror()
                        .addBox(0.0F, -7.0F, -1.0F, 3.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false),
                // offset relative to base: original (-2,0,1) minus base (-2,0,2) = (0,0,-1)
                PartPose.offsetAndRotation(0.0F, 0.0F, -1.0F, 0.0F, 0.3927F, 0.0F)
        );

        PartDefinition partTwoR = partOneR.addOrReplaceChild(
                "wingPartTwoRight_r1",
                CubeListBuilder.create().texOffs(0, 0).mirror()
                        .addBox(0.0F, -6.0F, -1.0F, 2.0F, 11.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false),
                // offset relative to partOne: original (0,0,0) - original partOne (-2,0,1) = (2,0,-1)
                PartPose.offsetAndRotation(2.0F, 0.0F, -1.0F, 0.0F, -0.3927F, 0.0F)
        );

        partTwoR.addOrReplaceChild(
                "wingEndRight_r1",
                CubeListBuilder.create().texOffs(0, 0).mirror()
                        .addBox(1.0F, -5.0F, -2.0F, 2.0F, 12.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false),
                // relative to partTwo: original (0,0,0) => keep (0,0,0)
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F)
        );

        // Left wing mirrored chain
        PartDefinition leftWing = wings.addOrReplaceChild(
                "leftWing",
                CubeListBuilder.create(),
                PartPose.offset(-4.0F, -2.0F, -6.0F)
        );

        PartDefinition baseL = leftWing.addOrReplaceChild(
                "wingBaseLeft_r1",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(0.0F, -6.0F, -1.0F, 1.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(2.0F, 0.0F, 2.0F, 0.0F, 0.3927F, 0.0F)
        );

        PartDefinition partOneL = baseL.addOrReplaceChild(
                "wingPartOneLeft_r1",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-3.0F, -7.0F, -1.0F, 3.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)),
                // relative to base: original (2,0,1) - base (2,0,2) = (0,0,-1)
                PartPose.offsetAndRotation(0.0F, 0.0F, -1.0F, 0.0F, -0.3927F, 0.0F)
        );

        PartDefinition partTwoL = partOneL.addOrReplaceChild(
                "wingPartTwoLeft_r1",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-2.0F, -6.0F, -1.0F, 2.0F, 11.0F, 1.0F, new CubeDeformation(0.0F)),
                // relative to partOne: original (0,0,0) - original partOne (2,0,1) = (-2,0,-1)
                PartPose.offsetAndRotation(-2.0F, 0.0F, -1.0F, 0.0F, 0.3927F, 0.0F)
        );

        partTwoL.addOrReplaceChild(
                "wingEndLeft_r1",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-3.0F, -5.0F, -2.0F, 2.0F, 12.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F)
        );

        // --- head: keep same children under "head" as exported ---
        PartDefinition head = gargoyle_model.addOrReplaceChild(
                "head",
                CubeListBuilder.create(),
                PartPose.offset(5.0F, 7.0F, -5.0F)
        );

        head.addOrReplaceChild(
                "earLeft_r1",
                CubeListBuilder.create().texOffs(10, 26)
                        .addBox(-4.0F, -2.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                        .texOffs(10, 26).mirror().addBox(-1.0F, -2.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false),
                PartPose.offsetAndRotation(-6.0F, -16.0F, 8.0F, -0.7854F, 0.0F, 0.0F)
        );

        head.addOrReplaceChild(
                "hornLeft_r1",
                CubeListBuilder.create().texOffs(15, 17).mirror()
                        .addBox(-2.8F, -2.7F, -0.7F, 0.5F, 1.5F, 0.5F, new CubeDeformation(0.0F)).mirror(false)
                        .texOffs(15, 17).addBox(-1.8F, -2.7F, -0.7F, 0.5F, 1.5F, 0.5F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-6.0F, -15.0F, 10.0F, 0.3927F, 0.0F, 0.0F)
        );

        head.addOrReplaceChild(
                "snout_r1",
                CubeListBuilder.create().texOffs(18, 14)
                        .addBox(-3.5F, -4.8F, -0.5F, 3.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-6.0F, -12.0F, 8.0F, -0.3927F, 0.0F, 0.0F)
        );

        head.addOrReplaceChild(
                "chin_r1",
                CubeListBuilder.create().texOffs(18, 5)
                        .addBox(-3.1F, -4.0F, 0.5F, 2.2F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-6.0F, -9.0F, 8.0F, -0.3927F, 0.0F, 0.0F)
        );

        head.addOrReplaceChild(
                "jaw_r1",
                CubeListBuilder.create().texOffs(17, 29)
                        .addBox(-3.5F, -4.0F, -2.5F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                        .texOffs(16, 0).addBox(-3.8F, -6.0F, -4.9F, 3.6F, 4.0F, 2.5F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-6.0F, -9.0F, 8.0F, -0.7854F, 0.0F, 0.0F)
        );

        PartDefinition eyes = head.addOrReplaceChild(
                "eyes",
                CubeListBuilder.create(),
                PartPose.offset(-6.0F, -15.0F, 10.0F)
        );

        eyes.addOrReplaceChild(
                "eyeRight_r1",
                CubeListBuilder.create().texOffs(30, 31)
                        .addBox(-0.8F, -0.7F, 0.3F, 0.5F, 0.5F, 0.5F, new CubeDeformation(0.0F))
                        .texOffs(30, 31).addBox(-3.7F, -0.7F, 0.3F, 0.5F, 0.5F, 0.5F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F)
        );

        PartDefinition teeth = head.addOrReplaceChild(
                "teeth",
                CubeListBuilder.create(),
                PartPose.offset(0.0F, 0.0F, 0.0F)
        );

        teeth.addOrReplaceChild(
                "toothTopLeft_r1",
                CubeListBuilder.create().texOffs(18, 16)
                        .addBox(-2.8F, -1.8F, 0.2F, 0.5F, 0.6F, 0.5F, new CubeDeformation(0.0F))
                        .texOffs(18, 16).addBox(-1.8F, -1.8F, 0.2F, 0.5F, 0.6F, 0.5F, new CubeDeformation(0.0F))
                        .texOffs(18, 16).addBox(-1.8F, -0.6F, 0.2F, 0.5F, 0.6F, 0.5F, new CubeDeformation(0.0F))
                        .texOffs(18, 16).addBox(-2.8F, -0.6F, 0.2F, 0.5F, 0.6F, 0.5F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-6.0F, -12.0F, 11.0F, -0.3927F, 0.0F, 0.0F)
        );

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        // Convert to radians
        float headYawRad = netHeadYaw * ((float)Math.PI / 180F);
        float headPitchRad = headPitch * ((float)Math.PI / 180F);

        // --- HEAD: rotate visible head parts around neck pivot (neckDefaultPose) ---
        // neckDefaultPose holds the exported pivot of the neck_r1 child (in head/body local coords)
        float pivotX = this.neckDefaultPose.x;
        float pivotY = this.neckDefaultPose.y;
        float pivotZ = this.neckDefaultPose.z;

        for (int i = 0; i < this.headParts.length; i++) {
            ModelPart part = this.headParts[i];
            PartPose def = this.headDefaultPoses[i];

            // vector from neck pivot to part default offset
            float vx = def.x - pivotX;
            float vy = def.y - pivotY;
            float vz = def.z - pivotZ;

            // apply yaw (Y axis)
            float cosY = (float)Math.cos(headYawRad);
            float sinY = (float)Math.sin(headYawRad);
            float rx = vx * cosY - vz * sinY;
            float rz = vx * sinY + vz * cosY;
            float ry = vy;

            // apply pitch (X axis)
            float cosX = (float)Math.cos(headPitchRad);
            float sinX = (float)Math.sin(headPitchRad);
            float ry2 = ry * cosX - rz * sinX;
            float rz2 = ry * sinX + rz * cosX;
            float rx2 = rx;

            float newX = pivotX + rx2;
            float newY = pivotY + ry2;
            float newZ = pivotZ + rz2;

            // combine rotations: keep part's default rotation and add head yaw/pitch
            float newXRot = def.xRot + headPitchRad;
            float newYRot = def.yRot + headYawRad;
            float newZRot = def.zRot;

            part.loadPose(PartPose.offsetAndRotation(newX, newY, newZ, newXRot, newYRot, newZRot));
        }

        // --- WALKING: quadruped legs (front legs should support front, so they act like shoulders) ---
        float walkCycle = 0.6662F;
        float walkSwing = Mth.cos(limbSwing * walkCycle) * 1.4F * limbSwingAmount;
        float walkSwingOpp = Mth.cos(limbSwing * walkCycle + (float)Math.PI) * 1.4F * limbSwingAmount;

        this.frontLeftLeg.xRot = walkSwing;
        this.frontRightLeg.xRot = walkSwingOpp;
        this.backLeftLeg.xRot = walkSwingOpp * 0.6F;
        this.backRightLeg.xRot = walkSwing * 0.6F;

        // --- WINGS: unified flap, now that parts are parented under wingBase ---
        boolean airborne = !entity.onGround();
        float wingSpeed = airborne ? 0.5F : 0.12F;
        float wingAmplitude = airborne ? 0.9F : 0.42F;
        float flap = Mth.sin(ageInTicks * wingSpeed) * wingAmplitude;

        // restore wing base default pose then add flap to yaw (children follow automatically)
        this.rightWingBase.loadPose(this.rightWingBaseDefault);
        this.leftWingBase.loadPose(this.leftWingBaseDefault);

        // add flap on top of exported yaw
        this.rightWingBase.yRot += flap;
        this.leftWingBase.yRot  -= flap; // mirror

        // also ensure the wing parents are at their default (avoid parent offsets accumulating)
        this.rightWing.loadPose(this.rightWingDefault);
        this.leftWing.loadPose(this.leftWingDefault);

        // Tail wag (unchanged)
        this.tail.yRot = Mth.sin(ageInTicks * 0.08F) * 0.15F;

        // Sitting pose: fold and tuck
        if (entity instanceof GargoylePossessedEntity gp && gp.isOrderedToSit()) {
            this.frontLeftLeg.xRot = -0.6F;
            this.frontRightLeg.xRot = -0.6F;
            this.backLeftLeg.xRot = -0.8F;
            this.backRightLeg.xRot = -0.8F;

            // tuck wings: restore then offset base yaw
            this.rightWingBase.loadPose(this.rightWingBaseDefault);
            this.leftWingBase.loadPose(this.leftWingBaseDefault);
            this.rightWingBase.yRot += -0.2F;
            this.leftWingBase.yRot  += 0.2F;

            // lower head slightly (restore each head piece then nudge)
            for (int i = 0; i < this.headParts.length; i++) {
                ModelPart part = this.headParts[i];
                PartPose def = this.headDefaultPoses[i];
                part.loadPose(PartPose.offsetAndRotation(def.x, def.y + 0.1F, def.z, def.xRot + 0.1F, def.yRot, def.zRot));
            }

            this.tail.yRot = 0.0F;
        }
    }

    /**
     * renderToBuffer signature for Mojang mappings / 1.21.1:
     * renderToBuffer(PoseStack, VertexConsumer, int light, int overlay, int color)
     */
    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        poseStack.pushPose();
        // flip 180° so Blockbench south -> in-game forward (tweak if your model still faces wrong way)
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        this.gargoyle_model.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        poseStack.popPose();
    }
}
