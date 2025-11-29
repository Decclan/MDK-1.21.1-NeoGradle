package com.deimoshexxus.netherhexedkingdom.content.entities;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.*;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
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
    // constructor required by EntityType
    public GargoylePossessedEntity(EntityType<? extends GargoylePossessedEntity> type, Level level) {
        super(type, level);
        this.setPathfindingMalus(PathType.DANGER_FIRE, -1.0F);
        this.setPersistenceRequired(); // so tame pets don't despawn
    }

    // ---------- Attributes ---------- //
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 24.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.28D)
                .add(Attributes.ATTACK_DAMAGE, 5.0D)
                .add(Attributes.FOLLOW_RANGE, 24.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.1D);
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
        // Basic movement / look
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new SitWhenOrderedToGoal(this)); // sit when ordered
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.2D, true));
        this.goalSelector.addGoal(4, new FollowOwnerGoal(this, 1.1D, 2.0F, 10.0F));
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 0.9D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));

        // Targeting: defend owner, retaliate, attack owner's targets
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this));

        // Prioritise zombified entities (explicit)
        this.targetSelector.addGoal(4,
                new NearestAttackableTargetGoal<>(this, Zombie.class, true));

        // Fallback: attack living entities with predicate that excludes owner & friendly tamed entities
        Predicate<LivingEntity> generalTargetPredicate = (living) -> {
            if (living == null) return false;
            if (living == this) return false;
            if (!living.isAlive()) return false;
            // don't attack your owner
            if (this.isOwnedBy(living)) return false;
            // don't attack other same-species gargoyles
            if (living.getClass() == this.getClass()) return false;
            // don't attack animals owned by same owner
            if (living instanceof TamableAnimal) {
                TamableAnimal ta = (TamableAnimal) living;
                if (ta.isTame() && this.getOwner() != null) {
                    LivingEntity otherOwner = ta.getOwner();
                    if (otherOwner != null && otherOwner.getUUID().equals(this.getOwner().getUUID())) return false;
                }
            }
            // okay otherwise
            return true;
        };

        this.targetSelector.addGoal(5,
                new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, generalTargetPredicate::test));
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
