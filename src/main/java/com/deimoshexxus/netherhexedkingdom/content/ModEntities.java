package com.deimoshexxus.netherhexedkingdom.content;

import com.deimoshexxus.netherhexedkingdom.NetherHexedKingdomMain;
import com.deimoshexxus.netherhexedkingdom.content.entities.HexedZombieEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(Registries.ENTITY_TYPE, NetherHexedKingdomMain.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<HexedZombieEntity>> HEXED_ZOMBIE =
            ENTITY_TYPES.register("hexed_zombie", () ->
                    EntityType.Builder.of(HexedZombieEntity::new, MobCategory.MONSTER)
                            .sized(0.6F, 1.95F)
                            .build("hexed_zombie")
            );

    public static void register(IEventBus bus) {
        ENTITY_TYPES.register(bus);
    }
}
