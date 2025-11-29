package com.deimoshexxus.netherhexedkingdom.content.entities.ai;

import com.deimoshexxus.netherhexedkingdom.content.ModSounds;
import com.deimoshexxus.netherhexedkingdom.content.entities.GrenadeProjectileEntity;
import com.deimoshexxus.netherhexedkingdom.content.entities.HexanGuardEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

/**
 * Goal that throws a grenade at the current target using a ballistic solver.
 * Adds a cheap friendly-fire avoidance check via HexanGuardEntity.isClearShotBySampling(...)
 */
public class ThrowGrenadeGoal extends Goal {
    private final HexanGuardEntity guard;
    private LivingEntity target;
    private int cooldownTicks = 0;

    private final int cooldownBase;      // base ticks between throws
    private final double maxRangeSq;     // squared max throw range
    private final double projectileSpeed; // initial speed magnitude

    // tuning: how many samples along path to check for allies, and radius of each sample box
    private final int samplingCount;
    private final double samplingRadius;

    public ThrowGrenadeGoal(HexanGuardEntity guard, int cooldownTicks, double maxRange, double projectileSpeed, int samplingCount, double samplingRadius) {
        this.guard = guard;
        this.cooldownBase = cooldownTicks;
        this.maxRangeSq = maxRange * maxRange;
        this.projectileSpeed = projectileSpeed;
        this.samplingCount = samplingCount;
        this.samplingRadius = samplingRadius;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    public ThrowGrenadeGoal(HexanGuardEntity guard) {
        // defaults: 30 ticks cooldown (~2s), 24 block range, speed 1.0
        // sampling: 3 points, radius 0.6 (tweak to taste)
        this(guard, 30, 24.0D, 1.0D, 3, 0.6D);
    }

    @Override
    public boolean canUse() {
        if (guard.getVariant() == null) return false;
        if (guard.getVariant() != HexanGuardEntity.Variant.GRENADIER) return false;

        target = guard.getTarget();
        if (target == null || !target.isAlive()) return false;

        return guard.distanceToSqr(target) <= maxRangeSq;
    }

    @Override
    public boolean canContinueToUse() {
        if (target == null || !target.isAlive()) return false;
        if (guard.getVariant() != HexanGuardEntity.Variant.GRENADIER) return false;
        return guard.distanceToSqr(target) <= maxRangeSq;
    }

    @Override
    public void start() {
        // small random initial delay so multiple grenadiers don't sync perfectly
        if (cooldownTicks <= 0) cooldownTicks = guard.level().getRandom().nextInt(cooldownBase);
    }

    @Override
    public void stop() {
        target = null;
    }

    @Override
    public void tick() {
        // 1. Target check
        if (target == null || !target.isAlive())
            return;

        // 2. Face the target
        guard.getLookControl().setLookAt(target, 30.0F, 30.0F);

        // 3. Cooldown handling
        if (cooldownTicks > 0) {
            cooldownTicks--;
            return;
        }

        // 4. Optional: Max effective throwing range
        final double maxRange = 20.0D;  // adjust as needed
        if (guard.distanceToSqr(target) > (maxRange * maxRange))
            return;

        // 5. Clear-shot check
        if (!guard.isClearShotBySampling(target, 3, 0.6D)) {
            cooldownTicks = 5;  // retry after a short delay
            return;
        }

        // 6. Source position at guard's eyes
        Vec3 source = new Vec3(
                guard.getX(),
                guard.getEyeY() - 0.2D,
                guard.getZ()
        );

        // 7. Predict target movement
        Vec3 targetPos = new Vec3(target.getX(), target.getEyeY(), target.getZ());
        Vec3 motion = target.getDeltaMovement();
        double distance = source.distanceTo(targetPos);

        // factor scales with distance, clamped to [0,1]
        double leadFactor = Math.min(1.0D, distance / 20.0D);

        // clamp lead to prevent overshoot at close range
        double leadScale = Math.min(5.0D * leadFactor, distance);
        Vec3 predicted = targetPos.add(motion.scale(leadScale));

        // 8. Create projectile with ballistic solver
        GrenadeProjectileEntity proj = GrenadeProjectileEntity.createThrownAtTarget(
                guard.level(),
                guard,
                predicted,
                this.projectileSpeed,
                0.12D
        );

        // 9. Arm swing animation
        guard.swing(InteractionHand.MAIN_HAND, true);

        // 10. Position projectile and spawn
        proj.setPos(source.x(), source.y(), source.z());
        guard.level().addFreshEntity(proj);

        // throw sound
         guard.level().playSound(null, guard, SoundEvents.SNOWBALL_THROW, SoundSource.HOSTILE, 1.0F, 1.0F);

        // 11. Reset cooldown with a bit of random variation
        cooldownTicks = cooldownBase
                + guard.level().getRandom().nextInt(Math.max(1, cooldownBase / 2));
    }

}
