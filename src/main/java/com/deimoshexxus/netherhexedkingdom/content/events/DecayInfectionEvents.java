package com.deimoshexxus.netherhexedkingdom.content.events;

import com.deimoshexxus.netherhexedkingdom.content.entities.DecayedZombifiedPiglinEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

public final class DecayInfectionEvents {

    private static final float INFECTION_CHANCE = 0.25F;

    private DecayInfectionEvents() {}

    @SubscribeEvent
    public static void onPiglinDamaged(LivingDamageEvent.Post event) {
        if (!(event.getEntity() instanceof AbstractPiglin piglin)) return;

        Entity attacker = event.getSource().getEntity();
        if (!(attacker instanceof DecayedZombifiedPiglinEntity)) return;

        Level level = piglin.level();
        if (level.isClientSide()) return;

        if (piglin.getRandom().nextFloat() > INFECTION_CHANCE) return;

        convertToZombifiedPiglin(piglin);
    }

    private static void convertToZombifiedPiglin(AbstractPiglin piglin) {
        Level level = piglin.level();

        ZombifiedPiglin zombie =
                EntityType.ZOMBIFIED_PIGLIN.create(level);

        if (zombie == null) return;

        zombie.moveTo(
                piglin.getX(),
                piglin.getY(),
                piglin.getZ(),
                piglin.getYRot(),
                piglin.getXRot()
        );

        level.addFreshEntity(zombie);
        piglin.discard();
    }
}
