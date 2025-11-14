package com.deimoshexxus.netherhexedkingdom.content;

import com.deimoshexxus.netherhexedkingdom.NetherHexedKingdomMain;
import com.deimoshexxus.netherhexedkingdom.content.entities.HexanGuardEntity;
import com.deimoshexxus.netherhexedkingdom.content.entities.HexedZombieEntity;
import com.deimoshexxus.netherhexedkingdom.content.entities.HexedZombieHuskEntity;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = NetherHexedKingdomMain.MODID)
public class ModEntityAttributes {

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.HEXED_ZOMBIE.get(), HexedZombieEntity.createAttributes().build());
        event.put(ModEntities.HEXED_ZOMBIE_HUSK.get(), HexedZombieHuskEntity.createAttributes().build());
        event.put(ModEntities.HEXAN_GUARD.get(), HexanGuardEntity.createAttributes().build());
    }
}
