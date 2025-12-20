package com.deimoshexxus.netherhexedkingdom.content.events;

import com.deimoshexxus.netherhexedkingdom.NetherHexedKingdom;
import com.deimoshexxus.netherhexedkingdom.content.ModEntities;
import com.deimoshexxus.netherhexedkingdom.content.entities.DecayedZombifiedPiglinEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

public final class ZombifiedPiglinDecayEvents {

    private static final String DECAY_PROGRESS = "DecayProgress";

    private static final int CHECK_INTERVAL = 40; // every 2s
    private static final int CONVERT_THRESHOLD = 5; // FAST TEST
    private static final int SEARCH_RADIUS = 8;

    private ZombifiedPiglinDecayEvents() {}

    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.Post event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof ZombifiedPiglin piglin)) return;
        if (piglin instanceof DecayedZombifiedPiglinEntity) return;

        Level level = piglin.level();
        if (level.isClientSide()) return;

        if (piglin.tickCount % CHECK_INTERVAL != 0) return;

        CompoundTag data = piglin.getPersistentData();
        int progress = data.getInt(DECAY_PROGRESS);

        boolean nearDecayed = isNearDecayed(level, piglin);

        if (nearDecayed) {
            progress++;
        } else {
            progress = Math.max(0, progress - 1);
        }

        data.putInt(DECAY_PROGRESS, progress);

//        NetherHexedKingdom.LOGGER.info(
//                "Decay progress {} (nearDecayed={}) for {}",
//                progress,
//                nearDecayed,
//                piglin.getUUID()
//        );

        if (progress >= CONVERT_THRESHOLD) {
            convert(piglin);
        }
    }

    private static boolean isNearDecayed(Level level, ZombifiedPiglin piglin) {
        AABB area = piglin.getBoundingBox().inflate(SEARCH_RADIUS);

        return !level.getEntitiesOfClass(
                DecayedZombifiedPiglinEntity.class,
                area
        ).isEmpty();
    }

    private static void convert(ZombifiedPiglin piglin) {
        Level level = piglin.level();

        var decayed = ModEntities.DECAYED_ZOMBIFIED_PIGLIN.get().create(level);
        if (decayed == null) {
            //NetherHexedKingdom.LOGGER.error("Failed to create Decayed Zombified Piglin");
            return;
        }

        decayed.moveTo(piglin.position());
        level.addFreshEntity(decayed);
        piglin.discard();

        //NetherHexedKingdom.LOGGER.info("Zombified Piglin decayed!");
    }
}
