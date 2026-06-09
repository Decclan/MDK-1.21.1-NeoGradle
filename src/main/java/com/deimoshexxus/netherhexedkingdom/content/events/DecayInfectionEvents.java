package com.deimoshexxus.netherhexedkingdom.content.events;

import com.deimoshexxus.netherhexedkingdom.content.entities.DecayedZombifiedPiglinEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import com.deimoshexxus.netherhexedkingdom.NetherHexedKingdom;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;

public final class DecayInfectionEvents {

    public static final String INFECTED = "DecayInfected";
    public static final String EXPOSURE = "DecayExposure";

    private static final int REQUIRED_EXPOSURES = 3;

    private DecayInfectionEvents() {}

    @SubscribeEvent
    public static void onPiglinDamaged(LivingDamageEvent.Post event) {
        if (!(event.getEntity() instanceof AbstractPiglin piglin)) {
            return;
        }

        Entity attacker = event.getSource().getEntity();
        if (!(attacker instanceof DecayedZombifiedPiglinEntity)) {
            return;
        }

        Level level = piglin.level();
        if (level.isClientSide()) {
            return;
        }

        CompoundTag data = piglin.getPersistentData();

        if (data.getBoolean(INFECTED)) {
            return;
        }

        int exposure = data.getInt(EXPOSURE) + 1;
        data.putInt(EXPOSURE, exposure);

        NetherHexedKingdom.LOGGER.info(
                "Piglin {} exposed to decay ({}/{})",
                piglin.getUUID(),
                exposure,
                REQUIRED_EXPOSURES
        );

        if (exposure >= REQUIRED_EXPOSURES) {
            data.putBoolean(INFECTED, true);

            if (level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(
                        ParticleTypes.SMOKE,
                        piglin.getX(),
                        piglin.getY() + 1.0D,
                        piglin.getZ(),
                        15,
                        0.3D,
                        0.4D,
                        0.3D,
                        0.02D
                );
            }

            NetherHexedKingdom.LOGGER.info(
                    "Piglin {} became infected",
                    piglin.getUUID()
            );
        }
    }
}
