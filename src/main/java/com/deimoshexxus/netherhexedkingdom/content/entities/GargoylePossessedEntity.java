package com.deimoshexxus.netherhexedkingdom.content.entities;

import com.deimoshexxus.netherhexedkingdom.content.entities.ai.GargoylePlayTamedGoal;
import com.deimoshexxus.netherhexedkingdom.content.entities.ai.GargoyleSpitAttackGoal;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.*;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.network.protocol.Packet;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.pathfinder.PathType;

import javax.annotation.Nullable;
import java.util.function.Predicate;

/**
 * GargoylePossessedEntity
 * - Tamable (rotten flesh / phantom membrane / fermented spider eye)
 * - Sit / follow owner / defend owner / attack owner's targets
 * - Aggressive to living entities, extra-targeting for zombies
 *
 * NOTE: register attributes with your EntityAttributeCreation event:
 *      event.put(ModEntities.GARGOYLE_POSSESSED.get(), GargoylePossessedEntity.createAttributes().build());
 */
public class GargoylePossessedEntity extends TamableAnimal {

    private static final EntityDataAccessor<Boolean> DATA_SITTING =
            SynchedEntityData.defineId(GargoylePossessedEntity.class, EntityDataSerializers.BOOLEAN);

    // constructor required by EntityType
    public GargoylePossessedEntity(EntityType<? extends GargoylePossessedEntity> type, Level level) {
        super(type, level);
        this.setPathfindingMalus(PathType.DANGER_FIRE, -1.0F);
        this.setPersistenceRequired(); // so tame pets don't despawn
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_SITTING, false);
    }

    // ---------- Attributes ---------- //
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 26.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.28D)
                .add(Attributes.ATTACK_DAMAGE, 6.0D)
                .add(Attributes.FOLLOW_RANGE, 24.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.1D);
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        if (!(target instanceof LivingEntity livingTarget)) return false;

        // base damage from attribute
        float baseDamage = (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);

        // custom bonus against zombie types
        if (livingTarget instanceof Zombie || livingTarget instanceof ZombifiedPiglin || livingTarget instanceof Creeper) {
            baseDamage += 4.0F; // your bonus
        }

        // prepare damage source
        DamageSource ds = this.damageSources().mobAttack(this);

        // consider enchantments on the attacker's weapon (main hand)
        ItemStack weapon = this.getMainHandItem();

        float finalDamage = baseDamage;
        if (this.level() instanceof ServerLevel serverLevel) { // <-- use getter
            // let enchantments modify the damage value (adds Smite/Sharpness etc)
            finalDamage = EnchantmentHelper.modifyDamage(serverLevel, weapon, livingTarget, ds, baseDamage);
        }

        boolean hit = livingTarget.hurt(ds, finalDamage);

        if (hit && this.level() instanceof ServerLevel serverLevel) { // <-- use getter
            // trigger post-attack enchantment effects (thorns, channeling-like effects)
            EnchantmentHelper.doPostAttackEffects(serverLevel, livingTarget, ds);
        }

        return hit;
    }

    // ---------- Navigation (optional tuning) ---------- //
    @Override
    protected PathNavigation createNavigation(Level level) {
        // ground creature
        return new GroundPathNavigation(this, level);
    }

    // ---------- Goals ---------- //
    @Override
    protected void registerGoals() {
        // ------ NORMAL GOALS ------
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.2D, true));
        this.goalSelector.addGoal(3, new GargoyleSpitAttackGoal(this, 60, 16.0));
        this.goalSelector.addGoal(4, new FollowOwnerGoal(this, 1.1D, 10.0F, 2.0F));
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 0.9D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(8, new GargoylePlayTamedGoal(this, 0.7D, 10.0D));

        // ------ TARGETING GOALS ------
        // defend owner + owner's pets
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));

        // revenge on mobs that hurt it
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this));

        // base hostile-mob targeting (ONLY when tamed)
        this.targetSelector.addGoal(4,
                new NearestAttackableTargetGoal<>(this, Monster.class, true));

        // fallback clean predicate (no more attacking animals or friendly targets)
        Predicate<LivingEntity> tamedTargetPredicate = (target) -> {
            if (target == null || !target.isAlive()) return false;

            // If not tamed, behave normally and attack anything hostile
            if (!this.isTame()) return target instanceof Monster;

            // When tamed:
            // never attack owner
            if (this.isOwnedBy(target)) return false;

            // never attack owner's pets
            if (target instanceof TamableAnimal ta && ta.isTame()) {
                LivingEntity otherOwner = ta.getOwner();
                if (otherOwner != null && this.getOwner() != null &&
                        otherOwner.getUUID().equals(this.getOwner().getUUID())) {
                    return false;
                }
            }
            // never attack the same species
            if (target.getClass() == this.getClass()) return false;
            // never attack passive animals
            if (target instanceof Animal) return false;
            // never attack villagers
            if (target instanceof Villager) return false;
            // DO attack monsters
            if (target instanceof Monster) return true;
            // DO attack anything actively attacking owner or owner's pets
            if (target.getLastHurtByMob() != null) {
                LivingEntity last = target.getLastHurtByMob();
                if (last == this.getOwner()) return true;

                if (last instanceof TamableAnimal ta && ta.isTame() &&
                        this.getOwner() != null &&
                        ta.getOwner() != null &&
                        ta.getOwner().getUUID().equals(this.getOwner().getUUID())) {
                    return true;
                }
            }
            return false;
        };

        // fallback target selector using fixed predicate
        this.targetSelector.addGoal(5,
                new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, tamedTargetPredicate));
    }

    // ---------- Taming / Interaction ---------- //
    @Override
    public boolean isFood(ItemStack stack) {
        // Accept the three taming items
        return stack != null && (stack.is(Items.ROTTEN_FLESH)
                || stack.is(Items.PHANTOM_MEMBRANE)
                || stack.is(Items.FERMENTED_SPIDER_EYE));
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack held = player.getItemInHand(hand);

        // If player uses a taming item
        if (this.isFood(held) && !this.isTame()) {
            if (!this.level().isClientSide()) {
                // consume one item
                if (!player.getAbilities().instabuild) {
                    held.shrink(1);
                }

                // try to tame (chance)
                if (this.random.nextInt(3) == 0) {
                    this.tame(player);
                    this.navigation.stop();
                    this.setOrderedToSit(true);
                    this.level().broadcastEntityEvent(this, (byte)7); // hearts
                } else {
                    this.level().broadcastEntityEvent(this, (byte)6); // smoke
                }
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide());
        }

        // If already tamed and owner interacts, toggle sit like a wolf
        if (this.isTame() && this.isOwnedBy(player)) {
            if (!this.level().isClientSide()) {
                this.setOrderedToSit(!this.isOrderedToSit());
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide());
        }

        return super.mobInteract(player, hand);
    }

    @Override
    public boolean isInSittingPose() {
        return this.entityData.get(DATA_SITTING);
    }

    @Override
    public void setOrderedToSit(boolean sitting) {
        super.setOrderedToSit(sitting);
        this.entityData.set(DATA_SITTING, sitting);
    }

    // ---------- WantsToAttack guard: don't attack owner or owner's tamed pets ---------- //
    @Override
    public boolean wantsToAttack(LivingEntity potentialTarget, LivingEntity owner) {
        // This is consulted by some tamable helper goals; keep consistent
        if (potentialTarget == owner) return false;
        if (potentialTarget.getClass() == this.getClass()) return false;
        return true;
    }

    // ---------- Save/Load owner info handled by TamableAnimal; override if you add fields ---------- //
    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        // add any extra data here if needed
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        // read extras if added
    }

    // ---------- Optional: custom sounds (fill in your sound registry) ---------- //
    @Nullable
    protected SoundEvent getAmbientSound() { return null; }
    @Nullable
    protected SoundEvent getHurtSound(DamageSource p_21338_) { return null; }
    @Nullable
    protected SoundEvent getDeathSound() { return null; }

    // ---------- Breeding (disabled) ---------- //
    @Override
    @Nullable
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob mate) {
        // This entity is not breedable — return null
        return null;
    }

    // Removed Forge-specific spawn packet override: networking is handled by the platform/loader.
}
