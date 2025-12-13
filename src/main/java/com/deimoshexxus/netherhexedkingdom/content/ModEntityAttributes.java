package com.deimoshexxus.netherhexedkingdom.content;

import com.deimoshexxus.netherhexedkingdom.NetherHexedKingdom;
import com.deimoshexxus.netherhexedkingdom.content.entities.GargoylePossessedEntity;
import com.deimoshexxus.netherhexedkingdom.content.entities.HexanGuardEntity;
import com.deimoshexxus.netherhexedkingdom.content.entities.DecayedZombieEntity;
import com.deimoshexxus.netherhexedkingdom.content.entities.DecayedZombieHuskEntity;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = NetherHexedKingdom.MODID)
public class ModEntityAttributes {

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.DECAYED_ZOMBIE.get(), DecayedZombieEntity.createAttributes().build());
        event.put(ModEntities.DECAYED_ZOMBIE_HUSK.get(), DecayedZombieHuskEntity.createAttributes().build());
        event.put(ModEntities.DECAYED_ZOMBIFIED_PIGLIN.get(), DecayedZombieHuskEntity.createAttributes().build());
        event.put(ModEntities.HEXAN_GUARD.get(), HexanGuardEntity.createAttributes().build());
        event.put(ModEntities.GARGOYLE_POSSESSED.get(), GargoylePossessedEntity.createAttributes().build());
    }
}
