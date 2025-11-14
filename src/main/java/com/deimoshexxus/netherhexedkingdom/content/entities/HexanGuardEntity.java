package com.deimoshexxus.netherhexedkingdom.content.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.List;

public class HexanGuardEntity extends AbstractSkeleton {

    // ---------------------------
    // Variant Enum
    // ---------------------------
    public enum Variant {
        MELEE,
        RANGED;

        public static Variant fromId(int id) {
            return values()[id % values().length];
        }

        public int getId() {
            return this.ordinal();
        }
    }

    // Synced variant field
    private static final EntityDataAccessor<Integer> DATA_VARIANT =
            SynchedEntityData.defineId(HexanGuardEntity.class, EntityDataSerializers.INT);

    public HexanGuardEntity(EntityType<? extends AbstractSkeleton> type, Level level) {
        super(type, level);
    }

    // ---------------------------------
    // Synched Data
    // ---------------------------------

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_VARIANT, Variant.MELEE.getId());
    }


    public Variant getVariant() {
        return Variant.fromId(this.entityData.get(DATA_VARIANT));
    }

    public void setVariant(Variant v) {
        this.entityData.set(DATA_VARIANT, v.getId());
    }

    // ---------------------------------
    // NBT Save/Load
    // ---------------------------------

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Variant", this.getVariant().getId());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setVariant(Variant.fromId(tag.getInt("Variant")));
    }

    // ---------------------------------
    // Spawning + variant assignment
    // ---------------------------------

    // Call externally by: hexanGuard.setVariant(HexanGuardEntity.Variant.MELEE);
    @Override
    public SpawnGroupData finalizeSpawn(
            ServerLevelAccessor level,
            DifficultyInstance difficulty,
            MobSpawnType reason,
            @Nullable SpawnGroupData spawnData
    ) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, spawnData);

        // Random variant if none set
        this.setVariant(this.random.nextBoolean() ? Variant.MELEE : Variant.RANGED);

        // Equip based on the variant
        applyVariantEquipment();

        // Goals based on variant
        setupGoalsForVariant();

        return data;
    }

    // Equip weapons/armor based on variant
    private void applyVariantEquipment() {
        RandomSource r = this.random;

        switch (this.getVariant()) {

            case MELEE -> {
                // --- Armor ---
                this.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.LEATHER_CHESTPLATE));
                this.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.LEATHER_LEGGINGS));

                if (r.nextFloat() < 0.70F) {
                    this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
                }

                // --- Shield ---
                if (r.nextFloat() < 0.70F) {
                    this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(Items.SHIELD));
                }

                // --- Weapon: stone sword / stone axe / gold sword (equal chance) ---
                int pick = r.nextInt(3); // 0,1,2
                ItemStack weapon = switch (pick) {
                    case 0 -> new ItemStack(Items.STONE_SWORD);
                    case 1 -> new ItemStack(Items.IRON_AXE);
                    default -> new ItemStack(Items.GOLDEN_SWORD);
                };
                this.setItemSlot(EquipmentSlot.MAINHAND, weapon);
            }

            case RANGED -> {
                // --- Armor ---
                this.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.LEATHER_CHESTPLATE));
                this.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.LEATHER_LEGGINGS));

                if (r.nextFloat() < 0.30F) {
                    this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
                }

                // --- Weapon ---
                this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
            }
        }

        // prevent natural drops from giving excessive gear
        this.setDropChance(EquipmentSlot.MAINHAND, 0.0F);
        this.setDropChance(EquipmentSlot.OFFHAND, 0.0F);
        this.setDropChance(EquipmentSlot.HEAD, 0.0F);
        this.setDropChance(EquipmentSlot.CHEST, 0.0F);
        this.setDropChance(EquipmentSlot.LEGS, 0.0F);
    }

    // ---------------------------------
    // Variant-Aware Goals
    // ---------------------------------

    private void setupGoalsForVariant() {
        // wipe goals
        this.goalSelector.getAvailableGoals().forEach(g -> this.goalSelector.removeGoal(g.getGoal()));
        this.targetSelector.getAvailableGoals().forEach(g -> this.targetSelector.removeGoal(g.getGoal()));

        // Shared Targeting
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Piglin.class, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, PiglinBrute.class, true));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));

        // Variant-specific logic
        if (this.getVariant() == Variant.MELEE) {

            this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2D, false));

        } else {

            this.goalSelector.addGoal(1, new RangedBowAttackGoal<>(this, 1.0D, 20, 15.0F));

        }

        // Shared navigation
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));

        // Avoids
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Cat.class, 8.0F, 1.0, 1.2));
        this.goalSelector.addGoal(4, new AvoidEntityGoal<>(this, ZombifiedPiglin.class, 6.0F, 1.0, 1.2));
    }

    @Override
    protected void registerGoals() {
        // Override to replace AbstractSkeleton from injecting bow goals.
    }

    // ---------------------------------
    // Sounds
    // ---------------------------------

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SKELETON_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.SKELETON_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SKELETON_DEATH;
    }

    @Override
    protected SoundEvent getStepSound() {
        return SoundEvents.SKELETON_STEP;
    }

    // ---------------------------------
    // Attributes
    // ---------------------------------

    public static AttributeSupplier.Builder createAttributes() {
        return AbstractSkeleton.createAttributes()
                .add(Attributes.MAX_HEALTH, 24.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.24D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D)
                .add(Attributes.FOLLOW_RANGE, 32.0D);
    }
}
