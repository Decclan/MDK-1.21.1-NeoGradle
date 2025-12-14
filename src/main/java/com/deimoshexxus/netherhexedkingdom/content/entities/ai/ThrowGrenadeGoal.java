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
    private static final double MIN_GRENADE_RANGE_SQ = 9.0D; // 3 blocks

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
        this(guard, 30, 24.0D, 1.2D, 3, 0.6D);
    }

    @Override
    public boolean canUse() {
        if (guard.getVariant() != HexanGuardEntity.Variant.GRENADIER)
            return false;

        target = guard.getTarget();
        if (target == null || !target.isAlive())
            return false;

        double distSq = guard.distanceToSqr(target);

        if (distSq < MIN_GRENADE_RANGE_SQ)
            return false;

        return distSq <= maxRangeSq;
    }

    @Override
    public boolean canContinueToUse() {
        if (guard.getVariant() != HexanGuardEntity.Variant.GRENADIER)
            return false;

        if (target == null || !target.isAlive())
            return false;

        double distSq = guard.distanceToSqr(target);

        // Stop throwing if target gets close
        return distSq >= MIN_GRENADE_RANGE_SQ && distSq <= maxRangeSq;
    }

    private boolean hasAllyNearTarget(LivingEntity target, double radius) {
        return !guard.level().getEntitiesOfClass(
                LivingEntity.class,
                target.getBoundingBox().inflate(radius),
                e -> guard.isFriendlyFireRisk(e, target)
        ).isEmpty();
    }

    @Override
    public boolean isInterruptable() {
        return true;
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
        if (target == null || !target.isAlive())
            return;

        double distSq = guard.distanceToSqr(target);

        // Too close → let melee handle it
        if (distSq < MIN_GRENADE_RANGE_SQ)
            return;

        guard.getLookControl().setLookAt(target, 30.0F, 30.0F);

        if (cooldownTicks > 0) {
            cooldownTicks--;
            return;
        }

        if (!guard.isClearShotBySampling(target, samplingCount, samplingRadius)) {
            cooldownTicks = 5;
            return;
        }

        // Cheap blast-zone friendly fire check (2 blocks)
        if (hasAllyNearTarget(target, 2.0D)) {
            cooldownTicks = 8; // slightly longer hesitation
            return;
        }

        Vec3 source = new Vec3(
                guard.getX(),
                guard.getEyeY() - 0.2D,
                guard.getZ()
        );

        Vec3 targetPos = target.getEyePosition();
        Vec3 predicted = targetPos.add(target.getDeltaMovement().scale(0.5));

        GrenadeProjectileEntity proj =
                GrenadeProjectileEntity.createThrownAtTarget(
                        guard.level(),
                        guard,
                        predicted,
                        projectileSpeed,
                        0.035D
                ); // 0.04 = // balanced arc, ~20–24 blocks, snowball 0.03

        guard.swing(InteractionHand.OFF_HAND, true);
        proj.setPos(source);
        guard.level().addFreshEntity(proj);

        guard.level().playSound(
                null,
                guard,
                SoundEvents.SNOWBALL_THROW,
                SoundSource.HOSTILE,
                1.0F,
                1.0F
        );

        cooldownTicks = cooldownBase
                + guard.level().getRandom().nextInt(cooldownBase / 2 + 1);
    }

}
