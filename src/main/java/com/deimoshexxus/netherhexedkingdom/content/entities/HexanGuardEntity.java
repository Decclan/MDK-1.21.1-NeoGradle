package com.deimoshexxus.netherhexedkingdom.content.entities;

import com.deimoshexxus.netherhexedkingdom.NetherHexedKingdomMain;
import com.deimoshexxus.netherhexedkingdom.content.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.util.GoalUtils;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.PathType;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;

public class HexanGuardEntity extends AbstractSkeleton {

    private static final float BREAK_DOOR_CHANCE = 0.3F;
    private static final Predicate<Difficulty> DOOR_BREAKING_PREDICATE = p_34284_ -> p_34284_ == Difficulty.NORMAL;
    private final BreakDoorGoal breakDoorGoal = new BreakDoorGoal(this, DOOR_BREAKING_PREDICATE);
    private boolean canBreakDoors;

    public enum Variant { MELEE, RANGED;
        public static Variant fromId(int id) { return values()[Math.floorMod(id, values().length)]; }
        public int getId() { return this.ordinal(); }
    }

    private static final EntityDataAccessor<Integer> DATA_VARIANT =
            SynchedEntityData.defineId(HexanGuardEntity.class, EntityDataSerializers.INT);

    public HexanGuardEntity(EntityType<? extends AbstractSkeleton> type, Level level) {
        super(type, level);
        this.setPathfindingMalus(PathType.DANGER_FIRE, 16.0F);
        this.setPathfindingMalus(PathType.DAMAGE_FIRE, -1.0F);
    }

    // safe clear helper
    private static void clearGoalsSafe(GoalSelector selector) {
        var snapshot = List.copyOf(selector.getAvailableGoals());
        for (var wrapped : snapshot) {
            if (wrapped == null) continue;
            var g = wrapped.getGoal();
            if (g == null) continue;
            selector.removeGoal(g);
        }
    }

    // ---------------------------------
    // Synched data
    // ---------------------------------
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_VARIANT, Variant.MELEE.getId());
    }

    public boolean canBreakDoors() {
        return this.canBreakDoors;
    }

    /**
     * Sets or removes EntityAIBreakDoor task
     */
    public void setCanBreakDoors(boolean canBreakDoors) {
        if (this.supportsBreakDoorGoal() && GoalUtils.hasGroundPathNavigation(this)) {
            if (this.canBreakDoors != canBreakDoors) {
                this.canBreakDoors = canBreakDoors;
                ((GroundPathNavigation)this.getNavigation()).setCanOpenDoors(canBreakDoors);
                if (canBreakDoors) {
                    this.goalSelector.addGoal(1, this.breakDoorGoal);
                } else {
                    this.goalSelector.removeGoal(this.breakDoorGoal);
                }
            }
        } else if (this.canBreakDoors) {
            this.goalSelector.removeGoal(this.breakDoorGoal);
            this.canBreakDoors = false;
        }
    }

    protected boolean supportsBreakDoorGoal() {
        return true;
    }


    public Variant getVariant() {
        return Variant.fromId(this.entityData.get(DATA_VARIANT));
    }
    public void setVariant(Variant v) { this.entityData.set(DATA_VARIANT, v.getId()); }

    // ---------------------------------
    // Save / load NBT
    // ---------------------------------
    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Variant", getVariant().getId());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("Variant")) this.setVariant(Variant.fromId(tag.getInt("Variant")));
    }

    // ---------------------------
    // Prevent vanilla auto-equipping
    // ---------------------------
    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        // Do nothing — we set equipment explicitly in finalizeSpawn
    }

    // Keep vanilla skeleton initialisation but we will replace attack goals dynamically
    @Override
    protected void registerGoals() {
        super.registerGoals();
        if (this.level() != null && !this.level().isClientSide()) {
            NetherHexedKingdomMain.LOGGER.debug("HexanGuard.registerGoals() on server for {}", this.getUUID());
        }
    }

    @Override
    public void reassessWeaponGoal() {
        // prevent vanilla skeleton from forcing its own weapon logic
    }

    @Override
    protected SoundEvent getStepSound() {
        return SoundEvents.SKELETON_STEP; // safer than returning null
    }

    // ---------------------------
    // Spawning + variant assignment
    // ---------------------------
    @Override
    public SpawnGroupData finalizeSpawn(
            ServerLevelAccessor level,
            DifficultyInstance difficulty,
            MobSpawnType reason,
            @Nullable SpawnGroupData spawnData
    ) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, spawnData);

        // ALWAYS assign variant randomly (70% melee / 30% ranged)
        this.setVariant(this.random.nextFloat() < 0.7F ? Variant.MELEE : Variant.RANGED);

        // Apply gear and goals AFTER vanilla spawn logic
        applyVariantEquipment();
        setupGoalsForVariant();

        NetherHexedKingdomMain.LOGGER.debug(
                "Finalized spawn for HexanGuard variant={} at {}",
                this.getVariant(), this.blockPosition()
        );

        return data;
    }

    // ---------------------------
    // Equipment application
    // ---------------------------
    public void applyVariantEquipment() {
        RandomSource r = this.random;

        // Clear hands first (be explicit)
        this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
        this.setItemSlot(EquipmentSlot.OFFHAND, ItemStack.EMPTY);

        switch (getVariant()) {
            case MELEE -> {
                this.setItemSlot(EquipmentSlot.CHEST, new ItemStack(ModItems.MILITUS_ALLOY_CHESTPLATE));
                this.setItemSlot(EquipmentSlot.LEGS, new ItemStack(ModItems.MILITUS_ALLOY_LEGGINGS));
                if (r.nextFloat() < 0.70F) this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(ModItems.MILITUS_ALLOY_HELMET));
                if (r.nextFloat() < 0.70F) this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(Items.SHIELD));

                int pick = r.nextInt(3);
                ItemStack weapon = switch (pick) {
                    case 0 -> new ItemStack(Items.STONE_SWORD);
                    case 1 -> new ItemStack(Items.IRON_AXE);
                    default -> new ItemStack(Items.GOLDEN_SWORD);
                };
                this.setItemSlot(EquipmentSlot.MAINHAND, weapon);
            }
            case RANGED -> {
                this.setItemSlot(EquipmentSlot.CHEST, new ItemStack(ModItems.MILITUS_ALLOY_CHESTPLATE));
                this.setItemSlot(EquipmentSlot.LEGS, new ItemStack(ModItems.MILITUS_ALLOY_LEGGINGS));
                if (r.nextFloat() < 0.30F) this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(ModItems.MILITUS_ALLOY_HELMET));
                this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
            }
        }

        // Prevent natural drops
        for (EquipmentSlot slot : EquipmentSlot.values()) this.setDropChance(slot, 0.0F);
        NetherHexedKingdomMain.LOGGER.debug("Applied equipment for HexanGuard variant={} main={} off={}",
                getVariant(), this.getItemInHand(InteractionHand.MAIN_HAND), this.getItemInHand(InteractionHand.OFF_HAND));
    }

    // ---------------------------
    // Goals (variant aware)
    // ---------------------------
    private void setupGoalsForVariant() {
        if (this.level() == null || this.level().isClientSide()) {
            NetherHexedKingdomMain.LOGGER.debug("setupGoalsForVariant on client - ignoring");
            return;
        }

        // safe clear
        clearGoalsSafe(this.goalSelector);
        clearGoalsSafe(this.targetSelector);

        NetherHexedKingdomMain.LOGGER.debug("Setting up goals for HexanGuard variant={} at {}", this.getVariant(), this.blockPosition());

        // shared targeting
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Piglin.class, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, PiglinBrute.class, true));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
        this.targetSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(6, new BreakDoorGoal(this, DOOR_BREAKING_PREDICATE));
        this.targetSelector.addGoal(6, new FollowMobGoal(this,0.0D, 0.0F, 0.0F));

        // variant attack
        if (getVariant() == Variant.MELEE) {
            this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2D, false));
        } else {
            this.goalSelector.addGoal(1, new RangedBowAttackGoal<>(this, 1.0D, 20, 15.0F));
        }

        // shared nav & avoids
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));

        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Cat.class, 8.0F, 1.0, 1.2));
        this.goalSelector.addGoal(4, new AvoidEntityGoal<>(this, ZombifiedPiglin.class, 6.0F, 1.0, 1.2));
    }

    // Sounds & attributes
    @Override protected SoundEvent getAmbientSound() { return SoundEvents.SKELETON_AMBIENT; }
    @Override protected SoundEvent getHurtSound(DamageSource d) { return SoundEvents.SKELETON_HURT; }
    @Override protected SoundEvent getDeathSound() { return SoundEvents.SKELETON_DEATH; }

    public static AttributeSupplier.Builder createAttributes() {
        return AbstractSkeleton.createAttributes()
                //.add(Attributes.SPAWN_REINFORCEMENTS_CHANCE, 0.0)
                .add(Attributes.MAX_HEALTH, 24.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.24D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D)
                .add(Attributes.FOLLOW_RANGE, 32.0D);
    }
}
