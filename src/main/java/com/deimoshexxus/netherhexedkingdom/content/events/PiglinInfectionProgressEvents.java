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

    private static final String INFECTION_PROGRESS =
            "InfectionProgress";

    private static final int CHECK_INTERVAL = 100;
    private static final int CONVERT_THRESHOLD = 24;

    private PiglinInfectionProgressEvents() {}

    @SubscribeEvent
    public static void onTick(EntityTickEvent.Post event) {

        if (!(event.getEntity() instanceof AbstractPiglin piglin)) {
            return;
        }

        if (piglin.level().isClientSide()) {
            return;
        }

        CompoundTag data = piglin.getPersistentData();

        if (!data.getBoolean(
                DecayInfectionEvents.INFECTED)) {
            return;
        }

        if (piglin.tickCount % CHECK_INTERVAL != 0) {
            return;
        }

        int progress =
                data.getInt(INFECTION_PROGRESS) + 1;

        data.putInt(INFECTION_PROGRESS, progress);

        NetherHexedKingdom.LOGGER.info(
                "Piglin {} infection {}/{}",
                piglin.getUUID(),
                progress,
                CONVERT_THRESHOLD
        );

        if (progress >= CONVERT_THRESHOLD) {
            convert(piglin);
        }
    }


    private static void convert(AbstractPiglin piglin) {

        Level level = piglin.level();

        ZombifiedPiglin zombified =
                EntityType.ZOMBIFIED_PIGLIN.create(level);

        if (zombified == null) {
            return;
        }

        CompoundTag tag =
                piglin.saveWithoutId(new CompoundTag());

        tag.remove(DecayInfectionEvents.INFECTED);
        tag.remove(DecayInfectionEvents.EXPOSURE);
        tag.remove(INFECTION_PROGRESS);

        tag.remove("UUID");

        zombified.load(tag);

        if (piglin.isBaby()) {
            zombified.setBaby(true);
        }

        zombified.moveTo(
                piglin.position(),
                piglin.getYRot(),
                piglin.getXRot()
        );

        zombified.setDeltaMovement(
                piglin.getDeltaMovement()
        );

        level.addFreshEntity(zombified);

        piglin.discard();

        NetherHexedKingdom.LOGGER.info(
                "Piglin {} zombified",
                piglin.getUUID()
        );
    }
}
