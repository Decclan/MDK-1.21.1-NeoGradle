package com.deimoshexxus.netherhexedkingdom.content.entities;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class GargoyleSpitEntity extends Projectile implements ItemSupplier {

    public GargoyleSpitEntity(EntityType<? extends GargoyleSpitEntity> type, Level level) {
        super(type, level);
    }

    public GargoyleSpitEntity(EntityType<? extends GargoyleSpitEntity> type, Level level, LivingEntity owner) {
        this(type, level);
        this.setOwner(owner);
        this.setPos(owner.getX(), owner.getEyeY() - 0.1F, owner.getZ());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }

    @Override
    public void tick() {
        super.tick();

        HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        if (hitresult.getType() != HitResult.Type.MISS) {
            this.onHit(hitresult);
        }

        this.setDeltaMovement(this.getDeltaMovement().scale(0.99));
        this.move(MoverType.SELF, this.getDeltaMovement());

        if (this.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(
                    ParticleTypes.SNEEZE,
                    this.getX(), this.getY(), this.getZ(),
                    1, 0, 0, 0, 0
            );
        }
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide) {
            this.discard();
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        if (!(result.getEntity() instanceof LivingEntity target)) return;

        Entity owner = this.getOwner();
        DamageSource source = this.damageSources().mobProjectile(this, owner instanceof LivingEntity le ? le : null);

        target.hurt(source, 3.0F);
        target.addEffect(new MobEffectInstance(MobEffects.POISON, 100, 0)); // 5 sec
        target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 0));

        this.discard();
    }

    @Override
    public ItemStack getItem() {
        // Use a real item (custom grenade item is ideal)
        return new ItemStack(Items.SLIME_BALL);
    }
}