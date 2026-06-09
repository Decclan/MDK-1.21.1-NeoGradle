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

    private static final String DECAY_PROGRESS = "DecayProgress";

    private static final int CHECK_INTERVAL = 200; // 10 seconds
    private static final int CONVERT_THRESHOLD = 18;
    private static final int SEARCH_RADIUS = 6;

    private ZombifiedPiglinDecayEvents() {}

    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.Post event) {
        Entity entity = event.getEntity();

        if (!(entity instanceof ZombifiedPiglin piglin)) {
            return;
        }

        if (piglin instanceof DecayedZombifiedPiglinEntity) {
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

        int progress = data.getInt(DECAY_PROGRESS);

        int nearbyDecayed = getNearbyDecayedCount(level, piglin);

        if (nearbyDecayed > 0) {
            progress += Math.min(nearbyDecayed, 3);
            if (level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(
                        ParticleTypes.ASH,
                        piglin.getX(),
                        piglin.getY() + 1.0D,
                        piglin.getZ(),
                        3,
                        0.25D,
                        0.25D,
                        0.25D,
                        0.01D
                );
            }
        } else {
            progress = Math.max(0, progress - 1);
        }

        NetherHexedKingdom.LOGGER.info(
                "Zombified Piglin {} sees {} decayed piglins",
                piglin.getUUID(),
                nearbyDecayed
        );

        data.putInt(DECAY_PROGRESS, progress);

        NetherHexedKingdom.LOGGER.info(
                "Zombified Piglin {} decay progress {}/{}",
                piglin.getUUID(),
                progress,
                CONVERT_THRESHOLD
        );

        if (progress >= CONVERT_THRESHOLD - 3) {
            if (level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(
                        ParticleTypes.SOUL,
                        piglin.getX(),
                        piglin.getY() + 1.0D,
                        piglin.getZ(),
                        5,
                        0.3D,
                        0.3D,
                        0.3D,
                        0.01D
                );
            }
        }

        if (progress >= CONVERT_THRESHOLD) {
            convert(piglin);
            NetherHexedKingdom.LOGGER.info(
                    "Zombified Piglin {} converting to Decayed",
                    piglin.getUUID()
            );
        }
    }

    private static int getNearbyDecayedCount(Level level, ZombifiedPiglin piglin) {
        AABB area = piglin.getBoundingBox().inflate(SEARCH_RADIUS);

        return level.getEntitiesOfClass(
                DecayedZombifiedPiglinEntity.class,
                area
        ).size();
    }

    private static void convert(ZombifiedPiglin piglin) {
        Level level = piglin.level();

        DecayedZombifiedPiglinEntity decayed =
                ModEntities.DECAYED_ZOMBIFIED_PIGLIN.get().create(level);

        if (decayed == null) {
            return;
        }

        CompoundTag tag = piglin.saveWithoutId(new CompoundTag());

        tag.remove(DECAY_PROGRESS);

        // Extra safety
        tag.remove("UUID");

        decayed.load(tag);

        decayed.moveTo(
                piglin.getX(),
                piglin.getY(),
                piglin.getZ(),
                piglin.getYRot(),
                piglin.getXRot()
        );

        decayed.setDeltaMovement(
                piglin.getDeltaMovement()
        );

        level.addFreshEntity(decayed);

        piglin.discard();
    }
}
