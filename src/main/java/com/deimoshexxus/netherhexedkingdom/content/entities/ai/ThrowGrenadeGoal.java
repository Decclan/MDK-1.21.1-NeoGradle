package com.deimoshexxus.netherhexedkingdom.content.entities.ai;

import com.deimoshexxus.netherhexedkingdom.content.ModEntities;
import com.deimoshexxus.netherhexedkingdom.content.entities.GrenadeProjectileEntity;
import com.deimoshexxus.netherhexedkingdom.content.entities.HexanGuardEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

/**
 * Throws a grenade at the current target. Simple prediction + cooldown.
 * - Uses GrenadeProjectileEntity(Level, LivingEntity, Vec3)
 * - Adjust SPEED to tune arc / travel time
 */
public class ThrowGrenadeGoal extends Goal {
    private final HexanGuardEntity guard;
    private LivingEntity target;
    private int cooldownTicks = 0;

    // config
    private final int cooldownBase;     // ticks between throws
    private final double maxRangeSq;    // squared max throw range
    private final double projectileSpeed; // magnitude of initial velocity

    public ThrowGrenadeGoal(HexanGuardEntity guard, int cooldownTicks, double maxRange, double projectileSpeed) {
        this.guard = guard;
        this.cooldownBase = cooldownTicks;
        this.maxRangeSq = maxRange * maxRange;
        this.projectileSpeed = projectileSpeed;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    public ThrowGrenadeGoal(HexanGuardEntity guard) {
        // defaults: 60 ticks cooldown (3s), 20 block max, 0.6 speed
        this(guard, 60, 20.0D, 0.6D);
    }

    @Override
    public boolean canUse() {
        if (guard.getVariant() == null) return false;
        if (guard.getVariant() != HexanGuardEntity.Variant.GRENADIER) return false;

        target = guard.getTarget(); // uses mob target system
        if (target == null) return false;
        if (!target.isAlive()) return false;

        double distSq = guard.distanceToSqr(target);
        return distSq <= maxRangeSq;
    }

    @Override
    public boolean canContinueToUse() {
        if (target == null || !target.isAlive()) return false;
        if (guard.getVariant() != HexanGuardEntity.Variant.GRENADIER) return false;
        return guard.distanceToSqr(target) <= maxRangeSq;
    }

    @Override
    public void start() {
        // optionally reset cooldown so they don't immediately throw on spawn
        if (cooldownTicks <= 0) cooldownTicks = guard.getRandom().nextInt(cooldownBase);
    }

    @Override
    public void stop() {
        target = null;
    }

    @Override
    public void tick() {
        if (target == null || !target.isAlive()) return;

        // Look at target
        guard.getLookControl().setLookAt(target, 30.0F, 30.0F);

        // Cooldown handling
        if (cooldownTicks > 0) {
            cooldownTicks--;
            return;
        }

        // Source position (slightly from eye level)
        Vec3 source = new Vec3(guard.getX(), guard.getEyeY() - 0.2D, guard.getZ());

        // Target prediction: aim at target eye position plus a fraction of its motion
        Vec3 targetPos = new Vec3(target.getX(), target.getEyeY(), target.getZ());
        Vec3 targetMotion = target.getDeltaMovement();

        // Simple linear prediction factor: depends on distance; tune multiplier if needed
        double distance = source.distanceTo(targetPos);
        double predictionFactor = Math.min(1.0D, distance / 20.0D); // scale from 0..1
        Vec3 predicted = targetPos.add(targetMotion.scale(5.0D * predictionFactor)); // 5 ticks lead scaled by distance

        // Direction vector
        Vec3 dir = predicted.subtract(source);

        // Normalize + scale to projectileSpeed
        Vec3 velocity = dir.normalize().scale(projectileSpeed);

        // Create projectile using constructor that takes (Level, LivingEntity, Vec3)
        GrenadeProjectileEntity proj = new GrenadeProjectileEntity(guard.level(), guard, velocity);

        // Ensure spawn position is correct
        proj.setPos(source.x(), source.y(), source.z());

        // Optionally set item (fire charge look) or other properties on projectile
        // proj.setItem(new ItemStack(Items.FIRE_CHARGE)); // if desired

        guard.level().addFreshEntity(proj);

        // reset cooldown (you can add some variance)
        cooldownTicks = cooldownBase + guard.getRandom().nextInt(cooldownBase / 2);
    }
}
