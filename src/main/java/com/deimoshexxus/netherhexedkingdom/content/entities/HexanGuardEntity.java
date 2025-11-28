package com.deimoshexxus.netherhexedkingdom.content.entities;

import com.deimoshexxus.netherhexedkingdom.content.ModItems;
import com.deimoshexxus.netherhexedkingdom.content.ModSounds;
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

        // Assign variant randomly (70% melee / 30% ranged)
        //this.setVariant(this.random.nextFloat() < 0.7F ? Variant.MELEE : Variant.RANGED);

        float roll = random.nextFloat();

        if (roll < 0.1F)  //0.6
            setVariant(Variant.MELEE);
        else if (roll < 0.2F)  //0.85
            setVariant(Variant.RANGED);
        else
            setVariant(Variant.GRENADIER);  // 15% chance


        // Apply gear and goals AFTER vanilla spawn logic
        applyVariantEquipment();
        setupGoalsForVariant();

        // Try to find a leader nearby (16-block radius)
        List<HexanGuardEntity> nearby = level.getEntitiesOfClass(
                HexanGuardEntity.class,
                this.getBoundingBox().inflate(16),
                e -> e.isSquadLeader()
        );

        if (nearby.isEmpty()) {
            this.setAsSquadLeader();
        } else {
            HexanGuardEntity leader = nearby.get(0);
            this.setSquadLeader(leader); // pass the entity -> sets uuid+entityId
        }

        // Leader-only combat buffs
        if (this.isSquadLeader()) {
            // Gold helmet
            this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.GOLDEN_HELMET));
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.GOLDEN_AXE));

            // Buffs
            this.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 999999, 1)); // Strength II
            this.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 999999, 0)); // Resistance I
            this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 999999, 0)); // Speed I
        }
        //NetherHexedKingdom.LOGGER.debug("Finalized spawn for HexanGuard variant={} at {}", this.getVariant(), this.blockPosition());
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

                // show something unique in hand (optional)
                this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.FIRE_CHARGE));
            }

        }

        // Prevent natural drops
        for (EquipmentSlot slot : EquipmentSlot.values()) this.setDropChance(slot, 0.0F);
//        NetherHexedKingdom.LOGGER.debug("Applied equipment for HexanGuard variant={} main={} off={}",
//                getVariant(), this.getItemInHand(InteractionHand.MAIN_HAND), this.getItemInHand(InteractionHand.OFF_HAND));
    }

    // ---------------------------
    // Goals
    // ---------------------------
    private void setupGoalsForVariant() {
        if (this.level() == null || this.level().isClientSide()) {
            //NetherHexedKingdom.LOGGER.debug("setupGoalsForVariant on client - ignoring");
            return;
        }

        // safe clear
        clearGoalsSafe(this.goalSelector);
        clearGoalsSafe(this.targetSelector);

        //NetherHexedKingdom.LOGGER.debug("Setting up goals for HexanGuard variant={} at {}", this.getVariant(), this.blockPosition());

        // shared targeting
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Piglin.class, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, PiglinBrute.class, true));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, Villager.class, true));

        // variant attack
        if (getVariant() == Variant.MELEE) {
            this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2D, false));
            this.goalSelector.addGoal(2, new BreakDoorGoal(this, difficulty -> difficulty != Difficulty.PEACEFUL));
        }else if (getVariant() == Variant.GRENADIER) {
            this.goalSelector.addGoal(1, new ThrowGrenadeGoal(this));
        } else {
            this.goalSelector.addGoal(1, new RangedBowAttackGoal<>(this, 1.0D, 20, 16.0F));
        }

        // shared nav & avoids
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Cat.class, 8.0F, 1.0, 1.2));
        this.goalSelector.addGoal(4, new AvoidEntityGoal<>(this, ZombifiedPiglin.class, 6.0F, 1.0, 1.2));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        // only follow leader?
        //this.goalSelector.addGoal(8, new FollowMobGoal(this, 1.0D, 2.0F, 16.0F));
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
}
