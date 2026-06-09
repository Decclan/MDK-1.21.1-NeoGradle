package com.deimoshexxus.netherhexedkingdom.content.entities;

import com.deimoshexxus.netherhexedkingdom.content.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class HexedZombieHorseJockeyEntity extends Skeleton {

    public HexedZombieHorseJockeyEntity(EntityType<? extends Skeleton> type, Level level) {
        super(type, level);
    }

    @Override
    public SpawnGroupData finalizeSpawn(
            ServerLevelAccessor level,
            DifficultyInstance difficulty,
            MobSpawnType spawnReason,
            @Nullable SpawnGroupData spawnGroupData
    ) {

        SpawnGroupData data = super.finalizeSpawn(
                level,
                difficulty,
                spawnReason,
                spawnGroupData
        );

        if (!this.isPassenger()
                && level instanceof ServerLevel serverLevel) {

            spawnMount(serverLevel);
        }

        return data;
    }


    @Override
    protected void populateDefaultEquipmentSlots(
            RandomSource random,
            DifficultyInstance difficulty
    ) {
        // Chainmail chest
        this.setItemSlot(
                EquipmentSlot.CHEST,
                new ItemStack(Items.CHAINMAIL_CHESTPLATE)
        );

        // Red leather cap
        ItemStack helmet =
                new ItemStack(Items.LEATHER_HELMET);

        helmet.set(
                DataComponents.DYED_COLOR,
                new DyedItemColor(0xAA0000, false)
        );

        this.setItemSlot(
                EquipmentSlot.HEAD,
                helmet
        );

        switch (random.nextInt(3)) {

            case 0 ->
                    this.setItemSlot(
                            EquipmentSlot.MAINHAND,
                            new ItemStack(Items.IRON_SWORD)
                    );

            case 1 ->
                    this.setItemSlot(
                            EquipmentSlot.MAINHAND,
                            new ItemStack(Items.GOLDEN_AXE)
                    );

            case 2 ->
                    this.setItemSlot(
                            EquipmentSlot.MAINHAND,
                            new ItemStack(Items.BOW)
                    );
        }
    }

    @Override
    public EntityDimensions getDefaultDimensions(Pose pose) {
        return EntityType.SKELETON.getDimensions();
    }

//    @Override
//    protected EntityAttachments.Builder getAttachmentsBuilder() {
//        return EntityAttachments.builder()
//                .attach(EntityAttachment.VEHICLE, 0.0F, 0.7F, 0.0F);
//    }


    private void spawnMount(ServerLevel level) {
        HexedZombieHorseEntity horse =
                ModEntities.HEXED_ZOMBIE_HORSE.get().create(level);

        if (horse == null) {
            return;
        }

        horse.setTamed(true);
        horse.getInventory().setItem(0, new ItemStack(Items.SADDLE));

        horse.moveTo(
                this.getX(),
                this.getY(),
                this.getZ(),
                this.getYRot(),
                this.getXRot()
        );

        level.addFreshEntity(horse);

        this.startRiding(horse, true);
        System.out.println(getClass());
        System.out.println(getType());
        System.out.println(getBbHeight());
        System.out.println(getEyeHeight());
    }

    public static boolean canSpawn(EntityType<HexedZombieHorseJockeyEntity> type, LevelAccessor level,
                                   MobSpawnType reason, BlockPos pos, RandomSource random) {
        return level.getBlockState(pos.below()).isSolid();
    }

    @Override
    public void tick() {
        super.tick();

        if (tickCount == 20) {
//            System.out.println("Type dimensions = " + getType().getDimensions());
//            System.out.println("Current dimensions = " + getDimensions(getPose()));
//            System.out.println("Eye height = " + getEyeHeight());
//
//            System.out.println(EntityType.SKELETON.getDimensions());
//            System.out.println(
//                    ModEntities.HEXED_ZOMBIE_HORSE_JOCKEY.get().getDimensions()
//            );
//
//            System.out.println(
//                    EntityType.SKELETON.getDimensions().attachments()
//            );
//
//            System.out.println(
//                    this.getType().getDimensions().attachments()
//            );
//
//            EntityDimensions vanilla = EntityType.SKELETON.getDimensions();
//            EntityDimensions custom = getType().getDimensions();

            System.out.println(
                    "Custom PASSENGER = " +
                            ModEntities.HEXED_ZOMBIE_HORSE_JOCKEY.get()
                                    .getDimensions()
                                    .attachments()
                                    .get(EntityAttachment.PASSENGER, 0, 0)
            );

            System.out.println(
                    "Custom VEHICLE = " +
                            ModEntities.HEXED_ZOMBIE_HORSE_JOCKEY.get()
                                    .getDimensions()
                                    .attachments()
                                    .get(EntityAttachment.VEHICLE, 0, 0)
            );

            System.out.println(
                    "Vanilla PASSENGER = " +
                            EntityType.SKELETON.getDimensions()
                                    .attachments()
                                    .get(EntityAttachment.PASSENGER, 0, 0)
            );

            System.out.println(
                    "Vanilla VEHICLE = " +
                            EntityType.SKELETON.getDimensions()
                                    .attachments()
                                    .get(EntityAttachment.VEHICLE, 0, 0)
            );

//            System.out.println(
//                    EntityType.SKELETON
//                            .getDimensions()
//                            .attachments()
//                            .get(EntityAttachment.VEHICLE, 0, 0.0F)
//            );
//
//            System.out.println(
//                    ModEntities.HEXED_ZOMBIE_HORSE_JOCKEY.get()
//                            .getDimensions()
//                            .attachments()
//                            .get(EntityAttachment.VEHICLE, 0, 0.0F)
//            );

//            EntityType.HORSE.getDimensions()
//                    .attachments()
//                    .get(EntityAttachment.PASSENGER, 0, 0);
//
//            EntityType.ZOMBIE_HORSE.getDimensions()
//                    .attachments()
//                    .get(EntityAttachment.PASSENGER, 0, 0);
//
//            ModEntities.HEXED_ZOMBIE_HORSE.get()
//                    .getDimensions()
//                    .attachments()
//                    .get(EntityAttachment.PASSENGER, 0, 0);

//            EntityAttachment.PASSENGER
//            EntityAttachment.NAME_TAG
//            EntityAttachment.WARDEN_CHEST

        }


    }
}
