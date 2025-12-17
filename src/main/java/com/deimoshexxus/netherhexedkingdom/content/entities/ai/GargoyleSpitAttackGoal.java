package com.deimoshexxus.netherhexedkingdom.content.entities.ai;

import com.deimoshexxus.netherhexedkingdom.content.entities.GargoyleSpitEntity;
import com.deimoshexxus.netherhexedkingdom.content.ModEntities;
import com.deimoshexxus.netherhexedkingdom.content.entities.ai.GargoyleRangedController;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;
import java.util.EnumSet;

public class GargoyleSpitAttackGoal extends Goal {

    private final Mob mob;
    private final GargoyleRangedController controller;
    private final double maxDistanceSq;
    private final int cooldownTicks;

    public GargoyleSpitAttackGoal(Mob mob, int cooldownTicks, double maxDistance) {
        this.mob = mob;
        this.cooldownTicks = cooldownTicks;
        this.maxDistanceSq = maxDistance * maxDistance;
        this.controller = new GargoyleRangedController(0);
        this.setFlags(EnumSet.of(Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity target = mob.getTarget();
        return target != null && target.isAlive() && controller.canAttack(mob, target, maxDistanceSq);
    }

    @Override
    public void start() {
        LivingEntity target = mob.getTarget();
        if (target == null || mob.level().isClientSide) return;

        GargoyleSpitEntity spit = new GargoyleSpitEntity(
                ModEntities.GARGOYLE_SPIT.get(),
                mob.level(),
                mob
        );

        Vec3 direction = target.getEyePosition().subtract(mob.getEyePosition()).normalize();
        spit.setDeltaMovement(direction.scale(0.9));

        mob.level().addFreshEntity(spit);
        controller.resetCooldown(cooldownTicks);
    }

    @Override
    public void tick() {
        controller.tick();
    }
}
