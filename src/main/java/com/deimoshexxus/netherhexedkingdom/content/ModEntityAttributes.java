package com.deimoshexxus.netherhexedkingdom.content;

import com.deimoshexxus.netherhexedkingdom.NetherHexedKingdom;
import com.deimoshexxus.netherhexedkingdom.content.entities.*;
import net.minecraft.world.entity.monster.Skeleton;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = NetherHexedKingdom.MODID)
public class ModEntityAttributes {

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.DECAYED_ZOMBIE.get(), DecayedZombieEntity.createAttributes().build());
        event.put(ModEntities.DECAYED_ZOMBIE_HUSK.get(), DecayedZombieHuskEntity.createAttributes().build());
        event.put(ModEntities.DECAYED_MUMMY.get(), DecayedZombieHuskEntity.createAttributes().build());
        event.put(ModEntities.DECAYED_ZOMBIFIED_PIGLIN.get(), DecayedZombieHuskEntity.createAttributes().build());
        event.put(ModEntities.HEXAN_GUARD.get(), HexanGuardEntity.createAttributes().build());
        event.put(ModEntities.GARGOYLE_POSSESSED.get(), GargoylePossessedEntity.createAttributes().build());
        event.put(ModEntities.HEXED_ZOMBIE_HORSE.get(), HexedZombieHorseEntity.createAttributes().build());
        event.put(ModEntities.WITHER_SKELETON_HORSE.get(), WitherSkeletonHorseEntity.createAttributes().build());
        event.put(ModEntities.WITHER_SKELETON_HORSE_JOCKEY.get(), Skeleton.createAttributes().build());
        event.put(ModEntities.HEXED_ZOMBIE_HORSE_JOCKEY.get(), Skeleton.createAttributes().build());
    }
}
