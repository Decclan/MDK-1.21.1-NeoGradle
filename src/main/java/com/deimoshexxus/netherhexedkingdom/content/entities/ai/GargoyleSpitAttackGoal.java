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
    private final int attackInterval;
    private final double range;

    public GargoyleSpitAttackGoal(GargoylePossessedEntity gargoyle, int attackInterval, double range) {
        this.gargoyle = gargoyle;
        this.attackInterval = attackInterval;
        this.range = range;
        this.setFlags(EnumSet.of(Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (gargoyle.getSpitCooldown() > 0) return false;

        LivingEntity target = gargoyle.getTarget();
        return target != null
                && target.isAlive()
                && gargoyle.distanceToSqr(target) <= range * range;
    }

    @Override
    public void start() {
        gargoyle.resetSpitCooldown(attackInterval);
    }

    @Override
    public void tick() {
        LivingEntity target = gargoyle.getTarget();
        if (target == null || gargoyle.level().isClientSide) return;

        gargoyle.getLookControl().setLookAt(target, 30f, 30f);

        GargoyleSpitEntity spit = ModEntities.GARGOYLE_SPIT.get().create(gargoyle.level());
        if (spit != null) {
            spit.moveTo(gargoyle.getX(), gargoyle.getEyeY() - 0.1, gargoyle.getZ());
            spit.setDeltaMovement(gargoyle.getLookAngle().scale(1.2));
            spit.setOwner(gargoyle);

            gargoyle.level().addFreshEntity(spit);
            gargoyle.playSound(SoundEvents.SNOWBALL_THROW, 1.0F, 1.0F);
        }
    }
}
