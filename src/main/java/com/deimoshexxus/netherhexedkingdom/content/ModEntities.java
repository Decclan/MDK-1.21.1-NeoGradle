package com.deimoshexxus.netherhexedkingdom.content;

import com.deimoshexxus.netherhexedkingdom.NetherHexedKingdom;
import com.deimoshexxus.netherhexedkingdom.content.entities.*;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(Registries.ENTITY_TYPE, NetherHexedKingdom.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<DecayedZombieEntity>> DECAYED_ZOMBIE =
            ENTITY_TYPES.register("decayed_zombie", () ->
                    EntityType.Builder.of(DecayedZombieEntity::new, MobCategory.MONSTER)
                            .sized(0.6F, 1.95F)
                            .build(NetherHexedKingdom.MODID + ":decayed_zombie")
            );

    public static final DeferredHolder<EntityType<?>, EntityType<DecayedZombieHuskEntity>> DECAYED_ZOMBIE_HUSK =
            ENTITY_TYPES.register("decayed_zombie_husk", () ->
                    EntityType.Builder.of(DecayedZombieHuskEntity::new, MobCategory.MONSTER)
                            .sized(0.6F, 1.95F)
                            .build(NetherHexedKingdom.MODID + ":decayed_zombie_husk")
            );

    public static final DeferredHolder<EntityType<?>, EntityType<DecayedZombifiedPiglinEntity>> DECAYED_ZOMBIFIED_PIGLIN =
            ENTITY_TYPES.register("decayed_zombified_piglin",
                    () -> EntityType.Builder.of(
                                    DecayedZombifiedPiglinEntity::new,
                                    MobCategory.MONSTER)
                            .sized(0.6F, 1.95F)
                            .build("decayed_zombified_piglin"));


    public static final DeferredHolder<EntityType<?>, EntityType<HexanGuardEntity>> HEXAN_GUARD =
            ENTITY_TYPES.register("hexan_guard", () ->
                    EntityType.Builder.of(HexanGuardEntity::new, MobCategory.MONSTER)
                            .sized(0.6F, 1.9F)
                            .build(NetherHexedKingdom.MODID + ":hexan_guard")
            );

    public static final DeferredHolder<EntityType<?>, EntityType<GrenadeProjectileEntity>> GRENADE =
            ENTITY_TYPES.register("grenade", () ->
                    EntityType.Builder.<GrenadeProjectileEntity>of(GrenadeProjectileEntity::new, MobCategory.MISC)
                            .sized(0.25F, 0.25F)
                            .fireImmune()
                            .clientTrackingRange(8)
                            .updateInterval(10)
                            .build(NetherHexedKingdom.MODID + ":grenade")
            );

    public static final DeferredHolder<EntityType<?>, EntityType<GargoylePossessedEntity>> GARGOYLE_POSSESSED =
            ENTITY_TYPES.register("gargoyle_possessed", () ->
                    EntityType.Builder.of(GargoylePossessedEntity::new, MobCategory.CREATURE)
                            .sized(0.8F, 0.95F)   // set size you want
                            .build(NetherHexedKingdom.MODID + ":gargoyle_possessed")
            );

    public static void register(IEventBus bus) {
        ENTITY_TYPES.register(bus);
    }
}
