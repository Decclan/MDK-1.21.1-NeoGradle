package com.deimoshexxus.netherhexedkingdom.content.entities.ai;

import com.deimoshexxus.netherhexedkingdom.content.entities.HexanGuardEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class FollowSquadLeaderGoal extends Goal {
    private final HexanGuardEntity guard;
    private HexanGuardEntity leader;
    private final double speed;
    private final float stopDistance;

    public FollowSquadLeaderGoal(HexanGuardEntity guard, double speed, float stopDistance) {
        this.guard = guard;
        this.speed = speed;
        this.stopDistance = stopDistance;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (guard.isSquadLeader()) return false;

        int leaderId = guard.getSquadLeaderEntityId();
        if (leaderId <= 0) return false; // -1 or 0 -> no leader known

        Entity e = guard.level().getEntity(leaderId);
        if (e instanceof HexanGuardEntity hex && hex.isAlive()) {
            this.leader = hex;
            return guard.distanceTo(leader) > stopDistance;
        } else {
            // Leader entity id not valid — clear local leader id so we don't repeatedly try
            guard.setSquadLeader((HexanGuardEntity)null);
            this.leader = null;
            return false;
        }
    }

    @Override
    public boolean canContinueToUse() {
        return this.leader != null && this.leader.isAlive() && !this.guard.isSquadLeader() && this.guard.distanceTo(this.leader) > stopDistance;
    }

    @Override
    public void stop() {
        this.leader = null;
        this.guard.getNavigation().stop();
    }

    @Override
    public void tick() {
        if (leader == null) return;
        if (guard.distanceTo(leader) > stopDistance) {
            guard.getNavigation().moveTo(leader, speed);
        } else {
            guard.getNavigation().stop();
        }
    }
}
