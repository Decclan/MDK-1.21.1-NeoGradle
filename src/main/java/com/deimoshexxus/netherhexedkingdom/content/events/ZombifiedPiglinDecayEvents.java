package com.deimoshexxus.netherhexedkingdom.content.events;

import com.deimoshexxus.netherhexedkingdom.content.ModEntities;
import com.deimoshexxus.netherhexedkingdom.content.entities.DecayedZombifiedPiglinEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import com.deimoshexxus.netherhexedkingdom.NetherHexedKingdom;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;

public final class ZombifiedPiglinDecayEvents {

    public static final String DECAY_INFECTED =
            "DecayCarrier";

    private static final String DECAY_PROGRESS =
            "DecayProgress";

    private static final int SEARCH_RADIUS = 6;

    private static final int CHECK_INTERVAL = 200;

    private static final int CONVERT_THRESHOLD = 18;


    private ZombifiedPiglinDecayEvents() {}


    @SubscribeEvent
    public static void onTick(EntityTickEvent.Post event) {

        if (!(event.getEntity()
                instanceof ZombifiedPiglin piglin)) {
            return;
        }

        if (piglin instanceof
                DecayedZombifiedPiglinEntity) {
            return;
        }

        Level level = piglin.level();

        if (level.isClientSide()) {
            return;
        }

        CompoundTag data =
                piglin.getPersistentData();


        /*
         * Acquire infection
         */

        if (!data.getBoolean(DECAY_INFECTED)) {

            if (isNearDecayed(level, piglin)) {

                data.putBoolean(
                        DECAY_INFECTED,
                        true
                );

                NetherHexedKingdom.LOGGER.debug(
                        "Zombified Piglin {} decay infected",
                        piglin.getUUID()
                );

            } else {
                return;
            }
        }


        /*
         * Progress infection
         */

        if (piglin.tickCount % CHECK_INTERVAL != 0) {
            return;
        }

        int progress =
                data.getInt(DECAY_PROGRESS) + 1;

        data.putInt(
                DECAY_PROGRESS,
                progress
        );

        if (level instanceof ServerLevel serverLevel) {
            if (progress > 6) {
                serverLevel.sendParticles(
                        ParticleTypes.FALLING_SPORE_BLOSSOM,
                        piglin.getX(),
                        piglin.getY() + 1.0D,
                        piglin.getZ(),
                        12,
                        0.35D,
                        0.6D,
                        0.35D,
                        0.03D
                );
            }
        }

        if (progress >= 3 && piglin.tickCount % 20 == 0) {

            // convulsion animation
            piglin.hurt(piglin.damageSources().magic(), 0.0F);

            // small twitch motion (optional, subtle)
            piglin.setDeltaMovement(
                    (piglin.getRandom().nextDouble() - 0.5D) * 0.03D,
                    0.01D,
                    (piglin.getRandom().nextDouble() - 0.5D) * 0.03D
            );
        }


        NetherHexedKingdom.LOGGER.debug(
                "Decay progress {} / {}",
                progress,
                CONVERT_THRESHOLD
        );


        if (progress >= CONVERT_THRESHOLD) {
            convert(piglin);
        }
    }


    private static boolean isNearDecayed(
            Level level,
            ZombifiedPiglin piglin
    ) {

        return !level.getEntitiesOfClass(

                DecayedZombifiedPiglinEntity.class,

                piglin.getBoundingBox()
                        .inflate(SEARCH_RADIUS)

        ).isEmpty();
    }



    private static void convert(
            ZombifiedPiglin piglin
    ) {

        Level level = piglin.level();

        DecayedZombifiedPiglinEntity decayed =

                ModEntities
                        .DECAYED_ZOMBIFIED_PIGLIN
                        .get()
                        .create(level);

        if (decayed == null) {
            return;
        }

        CompoundTag tag =
                piglin.saveWithoutId(
                        new CompoundTag()
                );

        tag.remove(DECAY_INFECTED);
        tag.remove(DECAY_PROGRESS);

        tag.remove("UUID");

        decayed.load(tag);

        decayed.moveTo(
                piglin.position(),
                piglin.getYRot(),
                piglin.getXRot()
        );

        decayed.setDeltaMovement(
                piglin.getDeltaMovement()
        );

        level.addFreshEntity(decayed);

        piglin.discard();

        NetherHexedKingdom.LOGGER.debug(
                "Zombified Piglin {} decayed",
                piglin.getUUID()
        );
    }
}
