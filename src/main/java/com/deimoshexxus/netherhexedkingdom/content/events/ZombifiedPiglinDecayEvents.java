package com.deimoshexxus.netherhexedkingdom.content.events;

import com.deimoshexxus.netherhexedkingdom.content.ModEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

public final class ZombifiedPiglinDecayEvents {

    private static final int MIN_TICKS_ALIVE = 20 * 60 * 5;
    private static final float DECAY_CHANCE = 0.5F; //0.0025F;

    private static final String DECAY_COOLDOWN = "DecayCooldown";

    private ZombifiedPiglinDecayEvents() {}

    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.Pre event) {
        if (!(event.getEntity() instanceof ZombifiedPiglin piglin)) return;

        if (piglin.getType() == ModEntities.DECAYED_ZOMBIFIED_PIGLIN.get())
            return;

        Level level = piglin.level();
        if (level.isClientSide()) return;

        CompoundTag data = piglin.getPersistentData();

        int cooldown = data.getInt(DECAY_COOLDOWN);
        if (cooldown > 0) {
            data.putInt(DECAY_COOLDOWN, cooldown - 1);
            return;
        }

        if (!level.dimension().equals(Level.NETHER)) return;
        if (piglin.tickCount < MIN_TICKS_ALIVE) return;
        if (piglin.getRandom().nextFloat() > DECAY_CHANCE) return;

        convertToDecayed(piglin);
    }


    private static void convertToDecayed(ZombifiedPiglin piglin) {
        Level level = piglin.level();

        var decayed = ModEntities.DECAYED_ZOMBIFIED_PIGLIN.get().create(level);
        if (decayed == null) return;

        decayed.moveTo(
                piglin.getX(),
                piglin.getY(),
                piglin.getZ(),
                piglin.getYRot(),
                piglin.getXRot()
        );

        // Prevent instant re-processing
        decayed.getPersistentData().putInt(DECAY_COOLDOWN, 20 * 60 * 10);

        level.addFreshEntity(decayed);
        piglin.discard();
    }
}
