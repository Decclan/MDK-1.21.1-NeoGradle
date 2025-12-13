//package com.deimoshexxus.netherhexedkingdom.content.events;
//
//import com.deimoshexxus.netherhexedkingdom.NetherHexedKingdom;
//import com.deimoshexxus.netherhexedkingdom.registry.ModEntities;
//import net.minecraft.world.entity.monster.ZombifiedPiglin;
//import net.minecraft.world.level.Level;
//import net.minecraftforge.event.entity.living.LivingEvent;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import net.minecraftforge.fml.common.Mod;
//
//@Mod.EventBusSubscriber(modid = NetherHexedKingdom.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
//public final class DecayEvents {
//
//    private DecayEvents() {} // Prevent instantiation
//
//    private static final int DECAY_COOLDOWN_TICKS = 20 * 60 * 10; // 10 minutes
//    private static final int MIN_TICKS_ALIVE = 20 * 60 * 5;      // 5 minutes
//    private static final float DECAY_CHANCE = 0.0025F;
//
//    @SubscribeEvent
//    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
//        if (!(event.getEntity() instanceof ZombifiedPiglin piglin)) return;
//
//        Level level = piglin.level();
//        if (level.isClientSide()) return;
//
//        var data = piglin.getPersistentData();
//
//        // Handle cooldown to prevent immediate re-decay
//        if (data.getInt("DecayCooldown") > 0) {
//            data.putInt("DecayCooldown", data.getInt("DecayCooldown") - 1);
//            return;
//        }
//
//        // Only decay in the Nether after minimum age
//        if (!level.dimension().equals(Level.NETHER) || piglin.tickCount < MIN_TICKS_ALIVE) return;
//
//        // Random chance for decay
//        if (piglin.getRandom().nextFloat() > DECAY_CHANCE) return;
//
//        convertToDecayed(piglin);
//    }
//
//    private static void convertToDecayed(ZombifiedPiglin piglin) {
//        Level level = piglin.level();
//        var decayed = ModEntities.DECAYED_ZOMBIFIED_PIGLIN.get().create(level);
//        if (decayed == null) return;
//
//        // Copy position and rotation
//        decayed.moveTo(piglin.getX(), piglin.getY(), piglin.getZ(), piglin.getYRot(), piglin.getXRot());
//
//        // Preserve anger state
//        decayed.setPersistentAngerTarget(piglin.getPersistentAngerTarget());
//        decayed.setRemainingPersistentAngerTime(piglin.getRemainingPersistentAngerTime());
//
//        // Copy equipment
//        piglin.getAllSlots().forEach(slot -> decayed.setItemSlot(slot, piglin.getItemBySlot(slot).copy()));
//
//        // Set cooldown to prevent instant re-decay
//        decayed.getPersistentData().putInt("DecayCooldown", DECAY_COOLDOWN_TICKS);
//
//        // Spawn decayed entity and remove original
//        level.addFreshEntity(decayed);
//        piglin.discard();
//    }
//}
