package com.deimoshexxus.netherhexedkingdom.content.entities.ai;

import com.deimoshexxus.netherhexedkingdom.content.entities.GargoylePossessedEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;
import java.util.List;

public class GargoylePlayTamedGoal extends Goal {

    private final GargoylePossessedEntity gargoyle;
    private LivingEntity target;
    private final double speed;
    private final double radius;

    public GargoylePlayTamedGoal(GargoylePossessedEntity gargoyle, double speed, double radius) {
        this.gargoyle = gargoyle;
        this.speed = speed;
        this.radius = radius;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (!gargoyle.isTame() || gargoyle.isInSittingPose()) return false;

        List<LivingEntity> list = gargoyle.level().getEntitiesOfClass(
                LivingEntity.class,
                gargoyle.getBoundingBox().inflate(radius),
                e -> e instanceof TamableAnimal tamableAnimal
                && tamableAnimal.isTame()
                && tamableAnimal != gargoyle
        );
        if (!list.isEmpty()) {
            target = list.get(gargoyle.getRandom().nextInt(list.size()));
            return true;
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        return target != null
                && target.isAlive()
                && gargoyle.distanceTo(target) < radius
                && gargoyle.isTame();
    }

    @Override
    public void stop() {
        target = null;
        gargoyle.getNavigation().stop();
    }

    @Override
    public void tick() {
        if (target == null) return;

        gargoyle.getLookControl().setLookAt(target, 30f, 30f);

        if (gargoyle.distanceTo(target) > 2.0) {
            gargoyle.getNavigation().moveTo(target, speed);
        } else {
            gargoyle.getNavigation().stop();
        }
    }
}














