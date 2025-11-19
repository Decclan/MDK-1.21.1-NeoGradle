package com.deimoshexxus.netherhexedkingdom.content.entities.ai;

import com.deimoshexxus.netherhexedkingdom.content.ModSounds;
import com.deimoshexxus.netherhexedkingdom.content.entities.GrenadeProjectileEntity;
import com.deimoshexxus.netherhexedkingdom.content.entities.HexanGuardEntity;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

/**
 * Goal that throws a grenade at the current target using a ballistic solver.
 */
public class ThrowGrenadeGoal extends Goal {
    private final HexanGuardEntity guard;
    private LivingEntity target;
    private int cooldownTicks = 0;

    private final int cooldownBase;      // base ticks between throws
    private final double maxRangeSq;     // squared max throw range
    private final double projectileSpeed; // initial speed magnitude

    public ThrowGrenadeGoal(HexanGuardEntity guard, int cooldownTicks, double maxRange, double projectileSpeed) {
        this.guard = guard;
        this.cooldownBase = cooldownTicks;
        this.maxRangeSq = maxRange * maxRange;
        this.projectileSpeed = projectileSpeed;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    public ThrowGrenadeGoal(HexanGuardEntity guard) {
        // defaults: 30 ticks cooldown (~2s), 24 block range, speed 1.0
        this(guard, 30, 24.0D, 1.0D);
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
        if (target == null || !target.isAlive()) return;

        // keep looking at the target
        guard.getLookControl().setLookAt(target, 30.0F, 30.0F);

        if (cooldownTicks > 0) {
            cooldownTicks--;
            return;
        }

        // source position (eye level)
        Vec3 source = new Vec3(guard.getX(), guard.getEyeY() - 0.2D, guard.getZ());

        // predict target movement: simple linear lead scaled by distance
        Vec3 targetPos = new Vec3(target.getX(), target.getEyeY(), target.getZ());
        Vec3 targetMotion = target.getDeltaMovement();
        double distance = source.distanceTo(targetPos);
        double leadFactor = Math.min(1.0D, distance / 20.0D);
        Vec3 predicted = targetPos.add(targetMotion.scale(5.0D * leadFactor));

        // create projectile with ballistic solver
        GrenadeProjectileEntity proj = GrenadeProjectileEntity.createThrownAtTarget(
                guard.level(),
                guard,
                predicted,
                this.projectileSpeed,
                0.12D
        );

        // arm swing animation
        guard.swing(InteractionHand.MAIN_HAND, true);

        // throw sound placeholder
//        guard.level().playSound(
//                null,
//                guard.getX(),
//                guard.getY(),
//                guard.getZ(),
//                ModSounds.GUARD_THROW_GRENADE.get(),
//                SoundSource.HOSTILE,
//                1.0F,
//                1.0F
//        );

        // position and spawn
        proj.setPos(source.x(), source.y(), source.z());
        guard.level().addFreshEntity(proj);

        // reset cooldown with small variance
        cooldownTicks = cooldownBase + guard.level().getRandom().nextInt(Math.max(1, cooldownBase / 2));
    }
}
