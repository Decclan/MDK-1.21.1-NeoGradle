//package com.deimoshexxus.netherhexedkingdom.content.events;
//
//import com.deimoshexxus.netherhexedkingdom.NetherHexedKingdom;
//import com.deimoshexxus.netherhexedkingdom.registry.ModEntities;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.world.entity.EquipmentSlot;
//import net.minecraft.world.entity.monster.ZombifiedPiglin;
//import net.minecraft.world.level.Level;
//import net.neoforged.bus.api.SubscribeEvent;
//import net.neoforged.neoforge.event.entity.living.LivingTickEvent;
//import net.neoforged.fml.common.Mod;
//
//@Mod.EventBusSubscriber(
//        modid = NetherHexedKingdom.MODID,
//        bus = Mod.EventBusSubscriber.Bus.GAME
//)
//public final class DecayEvents {
//
//    private DecayEvents() {}
//
//    private static final String DECAY_COOLDOWN_TAG = "DecayCooldown";
//
//    private static final int MIN_TICKS_ALIVE = 20 * 60 * 5;      // 5 minutes
//    private static final int DECAY_COOLDOWN_TICKS = 20 * 60 * 10; // 10 minutes
//    private static final float DECAY_CHANCE = 0.0025F;
//
//    @SubscribeEvent
//    public static void onLivingTick(LivingTickEvent event) {
//        if (!(event.getEntity() instanceof ZombifiedPiglin piglin)) return;
//
//        Level level = piglin.level();
//        if (level.isClientSide()) return;
//
//        CompoundTag data = piglin.getPersistentData();
//
//        // Cooldown
//        int cooldown = data.getInt(DECAY_COOLDOWN_TAG);
//        if (cooldown > 0) {
//            data.putInt(DECAY_COOLDOWN_TAG, cooldown - 1);
//            return;
//        }
//
//        // Nether only
//        if (!level.dimension().equals(Level.NETHER)) return;
//
//        // Age requirement
//        if (piglin.tickCount < MIN_TICKS_ALIVE) return;
//
//        // Random chance
//        if (piglin.getRandom().nextFloat() > DECAY_CHANCE) return;
//
//        convertToDecayed(piglin);
//    }
//
//    private static void convertToDecayed(ZombifiedPiglin piglin) {
//        Level level = piglin.level();
//
//        var decayed = ModEntities.DECAYED_ZOMBIFIED_PIGLIN.get().create(level);
//        if (decayed == null) return;
//
//        decayed.moveTo(
//                piglin.getX(),
//                piglin.getY(),
//                piglin.getZ(),
//                piglin.getYRot(),
//                piglin.getXRot()
//        );
//
//        // Preserve anger
//        decayed.setPersistentAngerTarget(piglin.getPersistentAngerTarget());
//        decayed.setRemainingPersistentAngerTime(
//                piglin.getRemainingPersistentAngerTime()
//        );
//
//        // Copy equipment
//        for (EquipmentSlot slot : EquipmentSlot.values()) {
//            decayed.setItemSlot(slot, piglin.getItemBySlot(slot).copy());
//        }
//
//        // Loop prevention
//        decayed.getPersistentData().putInt(
//                DECAY_COOLDOWN_TAG,
//                DECAY_COOLDOWN_TICKS
//        );
//
//        level.addFreshEntity(decayed);
//        piglin.discard();
//    }
//}
