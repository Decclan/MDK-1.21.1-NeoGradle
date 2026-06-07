package com.deimoshexxus.netherhexedkingdom.content.entities;

import com.deimoshexxus.netherhexedkingdom.content.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.entity.MobSpawnType;

import javax.annotation.Nullable;

public class WitherSkeletonHorseJockeyEntity extends WitherSkeleton {

    public WitherSkeletonHorseJockeyEntity(EntityType<? extends WitherSkeleton> type, Level level) {
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
        ItemStack helmet = new ItemStack(Items.LEATHER_HELMET);

        helmet.set(DataComponents.DYED_COLOR, new DyedItemColor(0x363231, false)); //363231

        this.setItemSlot(EquipmentSlot.HEAD, helmet);

        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.STONE_AXE));

    }


    private void spawnMount(ServerLevel level) {

        WitherSkeletonHorseEntity horse = ModEntities.WITHER_SKELETON_HORSE.get().create(level);

        if (horse == null) {return;}

        horse.setTamed(true);
        horse.getInventory().setItem(0, new ItemStack(Items.SADDLE));

        ItemStack armor = new ItemStack(Items.LEATHER_HORSE_ARMOR);

        armor.set(DataComponents.DYED_COLOR, new DyedItemColor(0x363231, false));

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

    public static boolean canSpawn(EntityType<WitherSkeletonHorseJockeyEntity> type, LevelAccessor level,
                                   MobSpawnType reason, BlockPos pos, RandomSource random) {
        return level.getBlockState(pos.below()).isSolid();
    }
}
