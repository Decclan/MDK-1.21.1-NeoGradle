package com.deimoshexxus.netherhexedkingdom.content.entities.ai;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

public class GargoyleRangedController {

    private int cooldownTicks;

    public GargoyleRangedController(int initialCooldown) {
        this.cooldownTicks = initialCooldown;
    }

    public void tick() {
        if (cooldownTicks > 0) {
            cooldownTicks--;
        }
    }

    public boolean canAttack(Mob mob, LivingEntity target, double maxDistanceSq) {
        if (cooldownTicks > 0) return false;
        if (!mob.hasLineOfSight(target)) return false;
        if (mob.distanceToSqr(target) > maxDistanceSq) return false;
        return true;
    }

    public void resetCooldown(int ticks) {
        this.cooldownTicks = ticks;
    }
}
