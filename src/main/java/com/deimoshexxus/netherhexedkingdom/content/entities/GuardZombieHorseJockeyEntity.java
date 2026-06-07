package com.deimoshexxus.netherhexedkingdom.content.entities;

import com.deimoshexxus.netherhexedkingdom.content.ModEntities;
import com.deimoshexxus.netherhexedkingdom.content.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;

import javax.annotation.Nullable;

public class GuardZombieHorseJockeyEntity extends HexanGuardEntity {

    public GuardZombieHorseJockeyEntity(EntityType<? extends HexanGuardEntity> type, Level level) {
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
    public void tick() {
        super.tick();
    }


//    @Override
//    protected void populateDefaultEquipmentSlots(
//            RandomSource random,
//            DifficultyInstance difficulty
//    ) {
//        // Chainmail chest
//        this.setItemSlot(
//                EquipmentSlot.CHEST,
//                new ItemStack(Items.CHAINMAIL_CHESTPLATE)
//        );
//
//        // Red leather cap
//        ItemStack helmet =
//                new ItemStack(Items.LEATHER_HELMET);
//
//        helmet.set(
//                DataComponents.DYED_COLOR,
//                new DyedItemColor(0xAA0000, false)
//        );
//
//        this.setItemSlot(
//                EquipmentSlot.HEAD,
//                helmet
//        );
//
//        switch (random.nextInt(3)) {
//
//            case 0 ->
//                    this.setItemSlot(
//                            EquipmentSlot.MAINHAND,
//                            new ItemStack(Items.STONE_SWORD)
//                    );
//
//            case 1 ->
//                    this.setItemSlot(
//                            EquipmentSlot.MAINHAND,
//                            new ItemStack(Items.GOLDEN_AXE)
//                    );
//
//            case 2 ->
//                    this.setItemSlot(
//                            EquipmentSlot.MAINHAND,
//                            new ItemStack(Items.BOW)
//                    );
//        }
//    }


    private void spawnMount(ServerLevel level) {

        HexedZombieHorseEntity horse = ModEntities.HEXED_ZOMBIE_HORSE.get().create(level);

        if (horse == null) {return;}

        horse.setTamed(true);
        horse.getInventory().setItem(0, new ItemStack(Items.SADDLE));

        ItemStack armor = new ItemStack(ModItems.MILITUS_ALLOY_HORSE_ARMOR);

        horse.moveTo(
                this.getX(),
                this.getY(),
                this.getZ(),
                this.getYRot(),
                this.getXRot()
        );

        horse.setPersistenceRequired();

        level.addFreshEntity(horse);

        horse.setBodyArmorItem(armor);

        this.startRiding(horse, true);
    }
    public static boolean canSpawn(EntityType<GuardZombieHorseJockeyEntity> type, LevelAccessor level,
                                   MobSpawnType reason, BlockPos pos, RandomSource random) {
        return level.getBlockState(pos.below()).isSolid();
    }
}
