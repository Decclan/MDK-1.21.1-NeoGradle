package com.deimoshexxus.netherhexedkingdom.content.entities;

import com.deimoshexxus.netherhexedkingdom.content.ModItems;
import com.deimoshexxus.netherhexedkingdom.content.ModSounds;
import com.deimoshexxus.netherhexedkingdom.content.entities.ai.AlertNearbyGuardsGoal;
import com.deimoshexxus.netherhexedkingdom.content.entities.ai.FollowSquadLeaderGoal;
import com.deimoshexxus.netherhexedkingdom.content.entities.ai.ThrowGrenadeGoal;
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
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.util.GoalUtils;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.PathType;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.Entity;

import java.util.List;
import java.util.function.Predicate;


import net.minecraft.nbt.CompoundTag;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class HexanGuardEntity extends AbstractSkeleton {

    public enum Variant { MELEE, RANGED, GRENADIER;
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

    private static final EntityDataAccessor<Optional<UUID>> SQUAD_LEADER_UUID =
            SynchedEntityData.defineId(HexanGuardEntity.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Integer> SQUAD_LEADER_ENTITY_ID =
            SynchedEntityData.defineId(HexanGuardEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> IS_SQUAD_LEADER =
            SynchedEntityData.defineId(HexanGuardEntity.class, EntityDataSerializers.BOOLEAN);

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_VARIANT, Variant.MELEE.getId());
        builder.define(SQUAD_LEADER_UUID, Optional.empty());
        builder.define(SQUAD_LEADER_ENTITY_ID, -1); // -1 means "no runtime leader"
        builder.define(IS_SQUAD_LEADER, false);
    }

    public Variant getVariant() {
        return Variant.fromId(this.entityData.get(DATA_VARIANT));
    }
    public void setVariant(Variant v) { this.entityData.set(DATA_VARIANT, v.getId()); }

    // --- Accessors ---
    public Optional<UUID> getSquadLeaderUUID() {
        return this.entityData.get(SQUAD_LEADER_UUID);
    }

    /** runtime entity id for fast lookup (-1 = none) */
    public int getSquadLeaderEntityId() {
        return this.entityData.get(SQUAD_LEADER_ENTITY_ID);
    }

    public boolean isSquadLeader() {
        return this.entityData.get(IS_SQUAD_LEADER);
    }

    /** Make this entity the leader (sets both UUID and entity id) */
    public void setAsSquadLeader() {
        this.entityData.set(IS_SQUAD_LEADER, true);
        this.entityData.set(SQUAD_LEADER_UUID, Optional.of(this.getUUID()));
        this.entityData.set(SQUAD_LEADER_ENTITY_ID, this.getId());
    }

    /** Assign leader by entity instance (recommended at runtime) */
    public void setSquadLeader(HexanGuardEntity leader) {
        if (leader == null) {
            this.entityData.set(SQUAD_LEADER_UUID, Optional.empty());
            this.entityData.set(SQUAD_LEADER_ENTITY_ID, -1);
            this.entityData.set(IS_SQUAD_LEADER, false);
        } else {
            this.entityData.set(SQUAD_LEADER_UUID, Optional.of(leader.getUUID()));
            this.entityData.set(SQUAD_LEADER_ENTITY_ID, leader.getId());
            this.entityData.set(IS_SQUAD_LEADER, false);
        }
    }

    // ---------------------------------
    // Save / load NBT
    // ---------------------------------
    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        this.getSquadLeaderUUID().ifPresent(uuid -> tag.putUUID("SquadLeaderUUID", uuid));
        tag.putInt("SquadLeaderEntityId", this.getSquadLeaderEntityId());
        tag.putBoolean("IsSquadLeader", this.isSquadLeader());
        // your existing variant save...
        tag.putInt("Variant", getVariant().getId());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("SquadLeaderUUID")) {
            this.entityData.set(SQUAD_LEADER_UUID, Optional.of(tag.getUUID("SquadLeaderUUID")));
        } else {
            this.entityData.set(SQUAD_LEADER_UUID, Optional.empty());
        }
        if (tag.contains("SquadLeaderEntityId")) {
            this.entityData.set(SQUAD_LEADER_ENTITY_ID, tag.getInt("SquadLeaderEntityId"));
        } else {
            this.entityData.set(SQUAD_LEADER_ENTITY_ID, -1);
        }
        if (tag.contains("IsSquadLeader")) {
            this.entityData.set(IS_SQUAD_LEADER, tag.getBoolean("IsSquadLeader"));
        } else {
            this.entityData.set(IS_SQUAD_LEADER, false);
        }

        if (tag.contains("Variant")) this.setVariant(Variant.fromId(tag.getInt("Variant")));
    }

    // ---------------------------
    // Prevent vanilla auto-equipping
    // ---------------------------
    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        // Do nothing — we set equipment explicitly in finalizeSpawn
    }

    // Vanilla skeleton initialisation
    @Override
    protected void registerGoals() {
        super.registerGoals();
        if (this.level() != null && !this.level().isClientSide()) {
            //NetherHexedKingdom.LOGGER.debug("HexanGuard.registerGoals() on server for {}", this.getUUID());
        }
    }

    @Override
    public void reassessWeaponGoal() {
        // prevent vanilla skeleton from forcing its own weapon logic
    }

    @Override
    protected SoundEvent getStepSound() {
        return ModSounds.GUARD_STEP.get(); // safer than returning null
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

        // Assign random variant for non-leaders (50% melee / 20% ranged / 30% grenadier)
        float roll = this.random.nextFloat();
        if (roll < 0.5F)
            setVariant(Variant.MELEE);
        else if (roll < 0.7F)
            setVariant(Variant.RANGED);
        else
            setVariant(Variant.GRENADIER);

        // Try to find a leader nearby (16-block radius)
        List<HexanGuardEntity> nearby = level.getEntitiesOfClass(
                HexanGuardEntity.class,
                this.getBoundingBox().inflate(16),
                e -> e.isSquadLeader()
        );

        if (nearby.isEmpty()) {
            // No nearby leader: this entity becomes leader
            this.setAsSquadLeader();

            // Force leader to MELEE variant to prevent grenade throws
            this.setVariant(Variant.MELEE);
        } else {
            // Assign this entity to an existing leader
            HexanGuardEntity leader = nearby.get(0);
            this.setSquadLeader(leader);
        }

        // Apply equipment and goals based on final variant
        applyVariantEquipment();
        setupGoalsForVariant();

        // Leader-only buffs
        if (this.isSquadLeader()) {
            // Override head and main hand
            this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.GOLDEN_HELMET));
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.GOLDEN_AXE));

            // Apply permanent buffs - is this causing suspicious effect holder bug???
//            this.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 999999, 1)); // Strength II
//            this.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 999999, 0)); // Resistance I
//            this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 999999, 0)); // Speed I
        }
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
            case GRENADIER -> {
                this.setItemSlot(EquipmentSlot.CHEST, new ItemStack(ModItems.MILITUS_ALLOY_CHESTPLATE));
                this.setItemSlot(EquipmentSlot.LEGS, new ItemStack(ModItems.MILITUS_ALLOY_LEGGINGS));
                if (r.nextFloat() < 0.50F)
                    this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(ModItems.MILITUS_ALLOY_HELMET));

                this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.FIRE_CHARGE));
            }
        }

        // Prevent natural drops
        for (EquipmentSlot slot : EquipmentSlot.values()) this.setDropChance(slot, 0.0F);
//        NetherHexedKingdom.LOGGER.debug("Applied equipment for HexanGuard variant={} main={} off={}",
//        getVariant(), this.getItemInHand(InteractionHand.MAIN_HAND), this.getItemInHand(InteractionHand.OFF_HAND));
    }

    // ---------------------------
    // Goals
    // ---------------------------
    private void setupGoalsForVariant() {
        if (this.level() == null || this.level().isClientSide) {
            return;
        }

        // clear existing goals safely
        clearGoalsSafe(this.goalSelector);
        clearGoalsSafe(this.targetSelector);

        // -----------------------
        // Targeting (targetSelector)
        // -----------------------
        // 1 = highest priority for target selection (retaliate + alert)
        HurtByTargetGoal hurtBy = new HurtByTargetGoal(this);
        // alert same type (HexanGuardEntity) so other guards get the attacker set
        hurtBy.setAlertOthers(HexanGuardEntity.class);
        this.targetSelector.addGoal(1, hurtBy);

        // 2..n: prefer players, then hostile mobs / villagers / golems etc.
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, /*checkSight*/ true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, /*checkSight*/ true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Villager.class, /*checkSight*/ true));
        // explicit zombified-type targets or other monsters you want prioritized:
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, Piglin.class, /*checkSight*/ true));
        this.targetSelector.addGoal(6, new NearestAttackableTargetGoal<>(this, PiglinBrute.class, /*checkSight*/ true));

        // Optional: keep your custom alert goal if you want a custom radius or additional logic.
        // Run this after HurtByTarget which gives the guard a target (lower priority number = higher priority,
        // so choose a higher number so it runs later).
        this.targetSelector.addGoal(10, new AlertNearbyGuardsGoal<>(this, HexanGuardEntity.class, 10.0D));
        this.targetSelector.addGoal(11, new AlertNearbyGuardsGoal<>(this, WitherSkeleton.class, 10.0D));

        // -----------------------
        // Goals
        // -----------------------
        // Movement and actions: lower numbers = higher priority
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(2, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0D));

        // variant behavior
        if (getVariant() == Variant.MELEE) {
            this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.2D, false));
            this.goalSelector.addGoal(5, new BreakDoorGoal(this, difficulty -> difficulty != Difficulty.PEACEFUL));
        } else if (getVariant() == Variant.GRENADIER) {
            this.goalSelector.addGoal(4, new ThrowGrenadeGoal(this));
        } else { // ranged
            this.goalSelector.addGoal(4, new RangedBowAttackGoal<>(this, 1.0D, 20, 16.0F));
        }

        // Avoidance/utility near bottom so attack goals can override when needed
        this.goalSelector.addGoal(6, new AvoidEntityGoal<>(this, Cat.class, 8.0F, 1.0, 1.2));
        this.goalSelector.addGoal(7, new AvoidEntityGoal<>(this, ZombifiedPiglin.class, 6.0F, 1.0, 1.2));
        // Squad following (if you have leaders)
        this.goalSelector.addGoal(8, new FollowSquadLeaderGoal(this, 1.0D, 3.0F));
    }

    // Sounds & attributes
    @Override protected SoundEvent getAmbientSound() { return ModSounds.GUARD_AMBIENT.get(); }
    @Override protected SoundEvent getHurtSound(DamageSource d) { return ModSounds.GUARD_HURT.get(); }
    @Override protected SoundEvent getDeathSound() { return ModSounds.GUARD_DEATH.get(); }

    public static AttributeSupplier.Builder createAttributes() {
        return AbstractSkeleton.createAttributes()
                //.add(Attributes.SPAWN_REINFORCEMENTS_CHANCE, 0.0)
                .add(Attributes.MAX_HEALTH, 24.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.24D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D)
                .add(Attributes.FOLLOW_RANGE, 32.0D);
    }

// Put these inside your HexanGuardEntity class (replace the old methods)

    private boolean isAllyForFriendlyFire(LivingEntity ent, LivingEntity target) {
        if (ent == null) return false;
        if (ent == this) return false;
        if (ent == target) return false;
        if (!ent.isAlive()) return false;

        // Same-class guards count as allies
        if (ent.getClass() == this.getClass()) return true;

        // Scoreboard/team / vanilla allied check
        if (ent.isAlliedTo(this)) return true;

        // Optionally treat villagers and golems as allies (uncomment if desired)
        // if (ent instanceof Villager || ent instanceof IronGolem) return true;

        return false;
    }

    /**
     * Quick corridor check: builds a thin AABB between shooter eyes and target eyes and
     * tests for allied LivingEntity inside. If any ally is found, returns false (not clear).
     *
     * use from custom bow ranged attack if one is ever made:
     * if (!guard.isClearShotTo(target, false)) {
     *     // skip firing this tick
     *     return;
     * }
     *
     *
     * @param target       target entity
     * @param ignoreBlocks whether to skip block line-of-sight check
     * @return true if the straight shot is "clear enough" (no allies in the corridor)
     */


    public boolean isClearShotTo(LivingEntity target, boolean ignoreBlocks) {
        if (target == null || !target.isAlive()) return false;
        if (this.level() == null) return false;

        Vec3 start = new Vec3(this.getX(), this.getEyeY(), this.getZ());
        Vec3 end = new Vec3(target.getX(), target.getEyeY(), target.getZ());

        // Optional but cheap block clip — if a block blocks and is not extremely close to the target, consider blocked.
        if (!ignoreBlocks) {
            ClipContext cc = new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this);
            HitResult hr = this.level().clip(cc);
            if (hr.getType() == HitResult.Type.BLOCK && hr instanceof BlockHitResult) {
                // if block hit is not practically on the target, treat as blocked
                double hitDist = start.distanceTo(((BlockHitResult) hr).getLocation());
                double totalDist = start.distanceTo(end);
                if (hitDist < (totalDist - 1.0D)) { // allow block if it's basically at target (tolerance 1.0)
                    return false;
                }
            }
        }

        // Thin corridor AABB along the shot line — small radius so we only catch entities directly in the path
        double corridorRadius = 0.6D;
        AABB corridor = new AABB(start, end).inflate(corridorRadius, corridorRadius, corridorRadius);

        List<LivingEntity> ents = this.level().getEntitiesOfClass(
                LivingEntity.class,
                corridor,
                (l) -> isAllyForFriendlyFire(l, target)
        );

        return ents.isEmpty();
    }

    /**
     * Sampling check for arced or potentially wider paths (useful for grenade throws).
     * Samples a few positions between shooter and target, building a small AABB at each sample
     * and checking for allied entities nearby.
     *
     * @param target        target entity
     * @param samples       number of sample points (e.g. 3)
     * @param sampleRadius  radius of the small AABB at each sample (e.g. 0.6)
     * @return true if no ally is found in any sample boxes
     */
    public boolean isClearShotBySampling(LivingEntity target, int samples, double sampleRadius) {
        if (target == null || !target.isAlive()) return false;
        if (this.level() == null) return false;
        if (samples <= 0) samples = 1;
        if (sampleRadius <= 0.0D) sampleRadius = 0.5D;

        Vec3 start = new Vec3(this.getX(), this.getEyeY(), this.getZ());
        Vec3 end = new Vec3(target.getX(), target.getEyeY(), target.getZ());
        Vec3 dir = end.subtract(start);

        // sample N points evenly spaced along the line (excluding exact endpoints)
        for (int i = 1; i <= samples; ++i) {
            double t = (double) i / (samples + 1);
            Vec3 samplePos = start.add(dir.scale(t));

            AABB box = new AABB(
                    samplePos.x() - sampleRadius, samplePos.y() - sampleRadius, samplePos.z() - sampleRadius,
                    samplePos.x() + sampleRadius, samplePos.y() + sampleRadius, samplePos.z() + sampleRadius
            );

            List<LivingEntity> found = this.level().getEntitiesOfClass(
                    LivingEntity.class,
                    box,
                    (l) -> isAllyForFriendlyFire(l, target)
            );

            if (!found.isEmpty()) {
                return false;
            }
        }

        return true;
    }


}
