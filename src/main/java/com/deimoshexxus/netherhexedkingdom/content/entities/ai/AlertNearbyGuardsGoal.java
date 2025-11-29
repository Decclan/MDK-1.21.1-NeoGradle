package com.deimoshexxus.netherhexedkingdom.content.entities.ai;

import com.deimoshexxus.netherhexedkingdom.content.entities.HexanGuardEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.List;

public class AlertNearbyGuardsGoal<T extends Mob> extends Goal {
    private final HexanGuardEntity guard;
    private final Class<T> allyClass;
    private final double radius;

    public AlertNearbyGuardsGoal(HexanGuardEntity guard, Class<T> allyClass, double radius) {
        this.guard = guard;
        this.allyClass = allyClass;
        this.radius = radius;
    }

    @Override
    public boolean canUse() {
        // Only trigger if the guard has a current attacker
        return guard.getTarget() != null;
    }

    @Override
    public void start() {
        LivingEntity attacker = guard.getTarget();
        if (attacker == null) return;

        // Use the getter instead of direct field access
        List<T> allies = guard.level().getEntitiesOfClass(
                allyClass,
                guard.getBoundingBox().inflate(radius),
                ally -> ally.getTarget() == null
        );

        for (T ally : allies) {
            ally.setTarget(attacker);
        }
    }
}
