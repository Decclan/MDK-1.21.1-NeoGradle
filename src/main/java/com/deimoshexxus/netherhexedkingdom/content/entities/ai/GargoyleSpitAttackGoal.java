package com.deimoshexxus.netherhexedkingdom.content.entities.ai;

import com.deimoshexxus.netherhexedkingdom.content.ModEntities;
import com.deimoshexxus.netherhexedkingdom.content.entities.GargoylePossessedEntity;
import com.deimoshexxus.netherhexedkingdom.content.entities.GargoyleSpitEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class GargoyleSpitAttackGoal extends Goal {
    private final GargoylePossessedEntity gargoyle;
    private int cooldown;
    private final int attackInterval;
    private final double range;

    public GargoyleSpitAttackGoal(GargoylePossessedEntity gargoyle, int attackInterval, double range) {
        this.gargoyle = gargoyle;
        this.attackInterval = attackInterval;
        this.range = range;
        this.cooldown = 0;
        this.setFlags(EnumSet.of(Flag.LOOK, Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        LivingEntity target = gargoyle.getTarget();
        return target != null && target.isAlive() && gargoyle.distanceToSqr(target) <= range * range;
    }

    @Override
    public void tick() {
        LivingEntity target = gargoyle.getTarget();
        if (target == null) return;

        // Face target
        Vec3 toTarget = target.position().subtract(gargoyle.position());
        double dx = toTarget.x;
        double dz = toTarget.z;
        float yaw = (float) (Math.atan2(dz, dx) * (180 / Math.PI)) - 90f;

        gargoyle.setYRot(yaw);
        gargoyle.setYHeadRot(yaw);
        gargoyle.yBodyRot = yaw;
        gargoyle.yRotO = yaw;

        // Fire projectile
        if (cooldown <= 0) {
            GargoyleSpitEntity spit = ModEntities.GARGOYLE_SPIT.get().create(gargoyle.level());
            if (spit != null) {
                spit.moveTo(gargoyle.getX(), gargoyle.getEyeY() - 0.1, gargoyle.getZ());

                Vec3 direction = target.position()
                        .subtract(gargoyle.position())
                        .normalize()
                        .scale(1.2);
                spit.setDeltaMovement(direction);

                spit.setOwner(gargoyle); // optional, for damage attribution

                gargoyle.level().addFreshEntity(spit);
                gargoyle.playSound(SoundEvents.SNOWBALL_THROW, 1.0F, 1.0F);
            }

            cooldown = attackInterval;
        } else {
            cooldown--;
        }
    }

    @Override
    public boolean canContinueToUse() {
        return canUse();
    }
}
