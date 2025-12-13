package com.deimoshexxus.netherhexedkingdom.content.entities;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

public class DecayedZombifiedPiglinEntity extends ZombifiedPiglin {

    public DecayedZombifiedPiglinEntity(EntityType<? extends ZombifiedPiglin> type, Level level) {
        super(type, level);
        this.setAggressive(true);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        // You can add additional goals here if needed
    }

    public static AttributeSupplier.Builder createAttributes() {
        return ZombifiedPiglin.createAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.35D)
                .add(Attributes.ATTACK_DAMAGE, 9.0D)
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.FOLLOW_RANGE, 40.0D);
    }
}
