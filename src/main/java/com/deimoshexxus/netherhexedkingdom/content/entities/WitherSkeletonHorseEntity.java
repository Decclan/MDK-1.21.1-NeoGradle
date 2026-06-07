package com.deimoshexxus.netherhexedkingdom.content.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.Vec3;

public class WitherSkeletonHorseEntity extends Horse {

    public WitherSkeletonHorseEntity(EntityType<? extends Horse> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Horse.createBaseHorseAttributes()
                .add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 36.0D)
                .add(net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED, 0.3D)
                .add(net.minecraft.world.entity.ai.attributes.Attributes.JUMP_STRENGTH, 0.9D);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        boolean validFood = stack.is(Items.COAL) || stack.is(Items.CHARCOAL) || stack.is(Items.BONE_MEAL);

        if (!this.isTamed() && validFood) {
            this.heal(2.0F);
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            if (!this.level().isClientSide) {
                if (this.random.nextInt(5) == 0) { // 20% chance
                    this.setTamed(true);
                    this.setOwnerUUID(player.getUUID());
                    this.level().broadcastEntityEvent(this, (byte)7);
                } else {
                    this.level().broadcastEntityEvent(this, (byte)6);
                }
            }

            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        return super.mobInteract(player, hand);
    }

    public boolean isUndead() {
        return true;
    }

//    @Override defined on the EntityType.Builder
//    public boolean fireImmune() {
//        return true;
//    }

    @Override
    public boolean causeFallDamage(float fallDistance, float damageMultiplier, DamageSource source) {
        return super.causeFallDamage(fallDistance, damageMultiplier * 0.7F, source); // reduce fall damage by 30%
    }

    @Override
    public boolean canBeAffected(MobEffectInstance effectInstance) {
        var effect = effectInstance.getEffect();

        if (effect.is(MobEffects.WITHER)) {
            return false;
        }

        if (effect.is(MobEffects.POISON)) {
            return false;
        }

        return super.canBeAffected(effectInstance);
    }

    @Override
    public void baseTick() {
        super.baseTick();

        this.setAirSupply(this.getMaxAirSupply());
    }


    @Override
    public Horse getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        return null;
    }

    @Override
    public boolean canFallInLove() {
        return false;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SKELETON_HORSE_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SKELETON_HORSE_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {return SoundEvents.SKELETON_HORSE_HURT;}

    @Override
    protected Vec3 getPassengerAttachmentPoint(Entity passenger,
                                               EntityDimensions dimensions,
                                               float partialTick) {
        return new Vec3(0.0D, 0.8D, 0.0D);
    }

    public static boolean canSpawn(EntityType<WitherSkeletonHorseEntity> type, LevelAccessor level,
                                   MobSpawnType reason, BlockPos pos, RandomSource random) {
        return level.getBlockState(pos.below()).isSolid();
    }
}
