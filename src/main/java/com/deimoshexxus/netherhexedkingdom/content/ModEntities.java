package com.deimoshexxus.netherhexedkingdom.content;

import com.deimoshexxus.netherhexedkingdom.NetherHexedKingdom;
import com.deimoshexxus.netherhexedkingdom.content.entities.*;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.apache.commons.codec.binary.Hex;

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

    public static final DeferredHolder<EntityType<?>, EntityType<DecayedZombieHuskEntity>> DECAYED_MUMMY =
            ENTITY_TYPES.register("decayed_mummy", () ->
                    EntityType.Builder.of(DecayedZombieHuskEntity::new, MobCategory.MONSTER)
                            .sized(0.6F, 1.95F)
                            .build(NetherHexedKingdom.MODID + ":decayed_mummy")
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
                            .fireImmune()
                            .sized(0.6F, 1.9F)
                            .build(NetherHexedKingdom.MODID + ":hexan_guard")
            );

    public static final DeferredHolder<EntityType<?>, EntityType<GargoylePossessedEntity>> GARGOYLE_POSSESSED =
            ENTITY_TYPES.register("gargoyle_possessed", () ->
                    EntityType.Builder.of(GargoylePossessedEntity::new, MobCategory.MONSTER)
                            .fireImmune()
                            .sized(0.8F, 0.95F) 
                            .build(NetherHexedKingdom.MODID + ":gargoyle_possessed")
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


    public static final DeferredHolder<EntityType<?>, EntityType<NetherPortalOrbEntity>> NETHER_PORTAL_ORB =
            ENTITY_TYPES.register("nether_portal_orb",
                    () -> EntityType.Builder.<NetherPortalOrbEntity>of(
                                    NetherPortalOrbEntity::new,
                                    MobCategory.MISC)
                            .sized(0.25F, 0.25F)
                            .clientTrackingRange(4)
                            .updateInterval(10)
                            .build("nether_portal_orb"));

    public static final DeferredHolder<EntityType<?>, EntityType<GargoyleSpitEntity>> GARGOYLE_SPIT =
            ENTITY_TYPES.register("gargoyle_spit",
                    () -> EntityType.Builder.<GargoyleSpitEntity>of(GargoyleSpitEntity::new, MobCategory.MISC)
                            .sized(0.25F, 0.25F)
                            .clientTrackingRange(4)
                            .updateInterval(10)
                            .build("gargoyle_spit")
            );

    public static final DeferredHolder<EntityType<?>, EntityType<HexedZombieHorseEntity>> HEXED_ZOMBIE_HORSE =
            ENTITY_TYPES.register("hexed_zombie_horse", () ->
                    EntityType.Builder.of(HexedZombieHorseEntity::new, MobCategory.MONSTER)
                            .sized(1.3965F, 1.4F)
                            //.passengerAttachments(0.0F, 1.2F, 0.0F)
                            .vehicleAttachment(new Vec3(0.0D, 0.0D, 0.0D))
                            .build("hexed_zombie_horse")
            );

    public static final DeferredHolder<EntityType<?>, EntityType<WitherSkeletonHorseEntity>> WITHER_SKELETON_HORSE =
            ENTITY_TYPES.register("wither_skeleton_horse", () ->
                    EntityType.Builder.of(WitherSkeletonHorseEntity::new, MobCategory.MONSTER)
                            .sized(1.6758F, 1.6F)
                            .vehicleAttachment(new Vec3(0.0D, 0.0D, 0.0D))
                            //.passengerAttachments(0.0F, 0.0F, 0.0F)
                            .fireImmune()
                            .build("wither_skeleton_horse")
            );

    public static final DeferredHolder<EntityType<?>, EntityType<HexedZombieHorseJockeyEntity>> HEXED_ZOMBIE_HORSE_JOCKEY =
            ENTITY_TYPES.register("hexed_zombie_horse_jockey", () ->
                    EntityType.Builder.of(HexedZombieHorseJockeyEntity::new, MobCategory.MONSTER)
                            .sized(0.6F, 1.99F)
                            .vehicleAttachment(new Vec3(0.0D, 0.7D, 0.0D))
                            .build("hexed_zombie_horse_jockey")
            );

    public static final DeferredHolder<EntityType<?>, EntityType<WitherSkeletonHorseJockeyEntity>> WITHER_SKELETON_HORSE_JOCKEY =
            ENTITY_TYPES.register("wither_skeleton_horse_jockey", () ->
                    EntityType.Builder.of(WitherSkeletonHorseJockeyEntity::new, MobCategory.MONSTER)
                            .sized(0.6F, 1.99F)
                            .vehicleAttachment(new Vec3(0.0D, 1.0D, 0.0D))
                            .fireImmune()
                            .build("wither_skeleton_horse_jockey")
            );

    public static final DeferredHolder<EntityType<?>, EntityType<GuardZombieHorseJockeyEntity>> GUARD_ZOMBIE_HORSE_JOCKEY =
            ENTITY_TYPES.register("guard_zombie_horse_jockey", () ->
                    EntityType.Builder.of(GuardZombieHorseJockeyEntity::new, MobCategory.MONSTER)
                            .sized(0.6F, 1.99F)
                            .vehicleAttachment(new Vec3(0.0D, 0.6D, 0.0D))
                            .build("guard_zombie_horse_jockey")
            );

    public static void register(IEventBus bus) {
        ENTITY_TYPES.register(bus);
    }
}
