package com.deimoshexxus.netherhexedkingdom.content.entities;

import com.deimoshexxus.netherhexedkingdom.content.ModEntities;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

/**
 * Grenade projectile that uses a small ballistic simulation (gravity per tick)
 * and explodes on hit (destroying blocks).
 */
public class GrenadeProjectileEntity extends Fireball {

    // tuning constants
    private static final double DEFAULT_SPEED = 1.0D;      // initial speed (blocks/tick)
    private static final double DEFAULT_INACCURACY = 0.12D; // gaussian spread
    private static final double GRAVITY = 0.06D;           // per-tick downward velocity added

    // Required constructor for EntityType loading / spawning
    public GrenadeProjectileEntity(EntityType<? extends GrenadeProjectileEntity> type, Level level) {
        super((EntityType<? extends Fireball>)(Object) type, level);
    }

    // Runtime spawn constructor (shooter + initial velocity vector)
    public GrenadeProjectileEntity(Level level, LivingEntity shooter, Vec3 velocity) {
        super((EntityType<? extends Fireball>)(Object) ModEntities.GRENADE.get(), shooter, velocity, level);
    }

    /** Convenience: create using shooter's look (biased upward) with defaults. */
    public static GrenadeProjectileEntity createThrown(Level level, LivingEntity shooter) {
        return createThrown(level, shooter, DEFAULT_SPEED, DEFAULT_INACCURACY);
    }

    /** Convenience: create using shooter's look (biased upward) with configurable speed/inaccuracy. */
    public static GrenadeProjectileEntity createThrown(Level level, LivingEntity shooter, double speed, double inaccuracy) {
        // base direction from look vector
        Vec3 dir = shooter.getLookAngle();
        // gentle upward bias so throws lob
        Vec3 biased = dir.add(0.0, 0.08, 0.0).normalize();
        RandomSource rnd = level.getRandom();

        Vec3 noisy = new Vec3(
                biased.x() + (rnd.nextDouble() - 0.5) * inaccuracy,
                biased.y() + (rnd.nextDouble() - 0.5) * inaccuracy,
                biased.z() + (rnd.nextDouble() - 0.5) * inaccuracy
        ).normalize().scale(speed);

        GrenadeProjectileEntity g = new GrenadeProjectileEntity(level, shooter, noisy);
        g.setPos(shooter.getX(), shooter.getEyeY() - 0.1, shooter.getZ());
        g.setDeltaMovement(noisy);
        return g;
    }

    /**
     * Create a grenade with a ballistic solution aimed at targetPos.
     * Uses the solver for a low-arc trajectory when possible; falls back to biased direct aim if no solution.
     *
     * @param level       world
     * @param shooter     shooter entity
     * @param targetPos   absolute target position (Vec3)
     * @param speed       magnitude of initial speed (blocks/tick)
     * @param inaccuracy  gaussian spread magnitude
     * @return ready-to-spawn GrenadeProjectileEntity
     */
    public static GrenadeProjectileEntity createThrownAtTarget(Level level, LivingEntity shooter, Vec3 targetPos, double speed, double inaccuracy) {
        Vec3 source = new Vec3(shooter.getX(), shooter.getEyeY() - 0.1D, shooter.getZ());

        double dx = targetPos.x() - source.x();
        double dz = targetPos.z() - source.z();
        double dy = targetPos.y() - source.y();

        double horizontalDist = Math.sqrt(dx * dx + dz * dz);
        double v = speed;
        double g = GRAVITY;

        Vec3 velocity;
        if (horizontalDist < 1e-6) {
            // target nearly vertical -> fallback vertical shot
            velocity = new Vec3(0.0, Math.signum(dy) * v, 0.0);
        } else {
            double v2 = v * v;
            double v4 = v2 * v2;
            double underSqrt = v4 - g * (g * horizontalDist * horizontalDist + 2.0 * dy * v2);

            if (underSqrt >= 0.0D) {
                double sqrtTerm = Math.sqrt(underSqrt);
                // choose low-angle solution for faster projectile
                double angle = Math.atan2(v2 - sqrtTerm, g * horizontalDist);
                double cos = Math.cos(angle);
                double sin = Math.sin(angle);

                double vx = (dx / horizontalDist) * v * cos;
                double vz = (dz / horizontalDist) * v * cos;
                double vy = v * sin;
                velocity = new Vec3(vx, vy, vz);
            } else {
                // no ballistic solution with this speed -> aim directly with upward bias
                Vec3 dir = targetPos.subtract(source).normalize();
                dir = dir.add(0.0, 0.12, 0.0).normalize();
                velocity = dir.scale(v);
            }
        }

        GrenadeProjectileEntity grenade = new GrenadeProjectileEntity(level, shooter, velocity);
        grenade.setPos(source.x(), source.y(), source.z());
        grenade.setDeltaMovement(velocity);

        return applyInaccuracy(grenade, level.getRandom(), inaccuracy);
    }

    // small helper to add perpendicular gaussian inaccuracy
    private static GrenadeProjectileEntity applyInaccuracy(GrenadeProjectileEntity proj, RandomSource rnd, double inaccuracy) {
        if (inaccuracy <= 0.0D) return proj;
        Vec3 v = proj.getDeltaMovement();
        double vx = v.x();
        double vy = v.y();
        double vz = v.z();
        double horiz = Math.sqrt(vx * vx + vz * vz);
        if (horiz > 1e-6) {
            double px = -vz / horiz;
            double pz = vx / horiz;
            double spread = rnd.nextGaussian() * inaccuracy;
            double upNoise = rnd.nextGaussian() * inaccuracy * 0.5;
            vx += px * spread;
            vz += pz * spread;
            vy += upNoise;
        } else {
            vx += rnd.nextGaussian() * inaccuracy;
            vz += rnd.nextGaussian() * inaccuracy;
        }
        Vec3 newVel = new Vec3(vx, vy, vz);
        proj.setDeltaMovement(newVel);
        return proj;
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
                    1.5F, // tuned a little lower than TNT, change if desired
                    Level.ExplosionInteraction.BLOCK
            );
            this.discard();
        }
    }

    /**
     * Apply gravity every tick so projectile arcs.
     * We apply gravity after super.tick() so movement from the current delta is applied,
     * then gravity reduces the Y velocity for the following ticks.
     */
    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide()) {
            Vec3 motion = this.getDeltaMovement();
            motion = motion.add(0.0D, -GRAVITY, 0.0D);
            this.setDeltaMovement(motion);
        }
    }
}
