package com.deimoshexxus.netherhexedkingdom.content;

import com.deimoshexxus.netherhexedkingdom.NetherHexedKingdomMain;
import com.deimoshexxus.netherhexedkingdom.content.entities.HexanGuardEntity;
import com.deimoshexxus.netherhexedkingdom.content.entities.HexedZombieEntity;
import com.deimoshexxus.netherhexedkingdom.content.entities.HexedZombieHuskEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
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
                            .build(NetherHexedKingdomMain.MODID + ":hexed_zombie")
            );

    public static final DeferredHolder<EntityType<?>, EntityType<HexedZombieHuskEntity>> HEXED_ZOMBIE_HUSK =
            ENTITY_TYPES.register("hexed_zombie_husk", () ->
                    EntityType.Builder.of(HexedZombieHuskEntity::new, MobCategory.MONSTER)
                            .sized(0.6F, 1.95F)
                            .build(NetherHexedKingdomMain.MODID + ":hexed_zombie_husk")
            );

    public static final DeferredHolder<EntityType<?>, EntityType<HexanGuardEntity>> HEXAN_GUARD =
            ENTITY_TYPES.register("hexan_guard", () ->
                    EntityType.Builder.of(HexanGuardEntity::new, MobCategory.MONSTER)
                            .sized(0.6F, 1.9F)
                            .build(NetherHexedKingdomMain.MODID + ":hexan_guard")
            );

    public static void register(IEventBus bus) {
        ENTITY_TYPES.register(bus);
    }
}
