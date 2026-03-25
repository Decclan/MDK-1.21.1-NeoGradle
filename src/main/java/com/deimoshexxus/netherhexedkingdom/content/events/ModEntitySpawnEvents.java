package com.deimoshexxus.netherhexedkingdom.content.events;

import com.deimoshexxus.netherhexedkingdom.content.ModEntities;
import com.deimoshexxus.netherhexedkingdom.content.entities.GargoylePossessedEntity;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

public final class ModEntitySpawnEvents {

    private ModEntitySpawnEvents() {}

    public static void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {
        event.register(
                ModEntities.GARGOYLE_POSSESSED.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                GargoylePossessedEntity::canSpawn,
                RegisterSpawnPlacementsEvent.Operation.REPLACE
        );
    }
}