package com.deimoshexxus.netherhexedkingdom.content.events;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import com.deimoshexxus.netherhexedkingdom.NetherHexedKingdom;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;

public final class PiglinInfectionProgressEvents {

    private static final String INFECTION_PROGRESS = "InfectionProgress";

    private static final int CHECK_INTERVAL = 100; // 5 seconds
    private static final int CONVERT_THRESHOLD = 24; // 2 minutes

    private PiglinInfectionProgressEvents() {}

    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.Post event) {
        if (!(event.getEntity() instanceof AbstractPiglin piglin)) {
            return;
        }

        Level level = piglin.level();
        if (level.isClientSide()) {
            return;
        }

        if (piglin.tickCount % CHECK_INTERVAL != 0) {
            return;
        }

        CompoundTag data = piglin.getPersistentData();

        if (!data.getBoolean(DecayInfectionEvents.INFECTED)) {
            return;
        }

        int progress = data.getInt(INFECTION_PROGRESS) + 1;
        data.putInt(INFECTION_PROGRESS, progress);

        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(
                    ParticleTypes.ASH,
                    piglin.getX(),
                    piglin.getY() + 1.0D,
                    piglin.getZ(),
                    2,
                    0.2D,
                    0.2D,
                    0.2D,
                    0.01D
            );
        }

        NetherHexedKingdom.LOGGER.info(
                "Piglin {} infection progress {}/{}",
                piglin.getUUID(),
                progress,
                CONVERT_THRESHOLD
        );

        if (progress >= CONVERT_THRESHOLD) {
            convert(piglin);
            NetherHexedKingdom.LOGGER.info(
                    "Piglin {} converting to Zombified Piglin",
                    piglin.getUUID()
            );
        }


    }

    private static void convert(AbstractPiglin piglin) {
        Level level = piglin.level();

        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(
                    ParticleTypes.POOF,
                    piglin.getX(),
                    piglin.getY() + 1.0D,
                    piglin.getZ(),
                    20,
                    0.4D,
                    0.5D,
                    0.4D,
                    0.05D
            );
        }

        ZombifiedPiglin zombified =
                EntityType.ZOMBIFIED_PIGLIN.create(level);

        if (zombified == null) {
            return;
        }

        CompoundTag tag = piglin.saveWithoutId(new CompoundTag());

        tag.remove(DecayInfectionEvents.INFECTED);
        tag.remove(DecayInfectionEvents.EXPOSURE);
        tag.remove(INFECTION_PROGRESS);

        // Extra safety
        tag.remove("UUID");

        zombified.load(tag);

        zombified.moveTo(
                piglin.getX(),
                piglin.getY(),
                piglin.getZ(),
                piglin.getYRot(),
                piglin.getXRot()
        );

        zombified.setDeltaMovement(
                piglin.getDeltaMovement()
        );

        level.addFreshEntity(zombified);

        piglin.discard();
    }
}
