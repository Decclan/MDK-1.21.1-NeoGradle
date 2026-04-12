package com.deimoshexxus.netherhexedkingdom.content.entities;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class DecayedZombieEntity extends Zombie {
    public DecayedZombieEntity(EntityType<? extends Zombie> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals(); // keep vanilla behavior
        this.goalSelector.addGoal(1, new RandomStrollGoal(this, 1.2D)); // modify/add
    }

    /** Be sure to register this attribute supplier on the EntityAttributeCreationEvent. */
    public static AttributeSupplier.Builder createAttributes() {
        return Zombie.createAttributes() // reuse zombie defaults
                .add(Attributes.FOLLOW_RANGE, 40.0)
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.24D)
                .add(Attributes.ATTACK_DAMAGE, 3.5D);
    }

    // vanilla:

//    public static AttributeSupplier.Builder createAttributes() {
//        return Monster.createMonsterAttributes()
//                .add(Attributes.FOLLOW_RANGE, 35.0)
//                .add(Attributes.MOVEMENT_SPEED, 0.23F)
//                .add(Attributes.ATTACK_DAMAGE, 3.0)
//                .add(Attributes.ARMOR, 2.0)
//                .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE);
//    }
}
