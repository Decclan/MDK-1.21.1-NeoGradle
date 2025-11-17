package com.deimoshexxus.netherhexedkingdom.content.entities;

import com.deimoshexxus.netherhexedkingdom.content.ModEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class GrenadeProjectileEntity extends Fireball {

    // Required constructor for EntityType loading / spawning
    public GrenadeProjectileEntity(EntityType<? extends GrenadeProjectileEntity> type, Level level) {
        super((EntityType<? extends Fireball>)(Object) type, level);
    }

    // Runtime spawn constructor (shooter + velocity)
    public GrenadeProjectileEntity(Level level, LivingEntity shooter, Vec3 velocity) {
        super(
                (EntityType<? extends Fireball>)(Object) ModEntities.GRENADE.get(),
                shooter,
                velocity,
                level
        );
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);

        if (!this.level().isClientSide()) {

            // Explosion that destroys blocks
            this.level().explode(
                    this,
                    this.getX(),
                    this.getY(),
                    this.getZ(),
                    2.5F,
                    Level.ExplosionInteraction.BLOCK
            );

            this.discard();
        }
    }
}
