package com.deimoshexxus.netherhexedkingdom.worldgen;

import com.deimoshexxus.netherhexedkingdom.NetherHexedKingdom;
import com.deimoshexxus.netherhexedkingdom.content.ModEntities;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.List;

public class ModBiomeModifiers {
    public static final ResourceKey<BiomeModifier> ADD_MILITUS_ALLOY_ORE = registerKey("add_militus_alloy_ore");

    public static final ResourceKey<BiomeModifier> ADD_WITHER_SKELETONS = registerKey("add_wither_skeletons");

    public static final ResourceKey<BiomeModifier> ADD_GARGOYLE_POSSESSED = registerKey("add_gargoyle_possessed");

    public static final ResourceKey<BiomeModifier> ADD_DECAYED_ZOMBIFIED_PIGLIN = registerKey("add_decayed_zombified_piglin");

    public static final ResourceKey<BiomeModifier> ADD_HEXED_ZOMBIE_HORSE = registerKey("add_hexed_zombie_horse");

    public static final ResourceKey<BiomeModifier> ADD_WITHER_SKELETON_HORSE = registerKey("add_wither_skeleton_horse");

    public static final ResourceKey<BiomeModifier> ADD_HEXED_ZOMBIE_HORSE_JOCKEY = registerKey("add_hexed_zombie_horse_jockey");

    public static final ResourceKey<BiomeModifier> ADD_WITHER_SKELETON_HORSE_JOCKEY = registerKey("add_wither_skeleton_horse_jockey");

    public static final ResourceKey<BiomeModifier> ADD_GUARD_ZOMBIE_HORSE_JOCKEY = registerKey("add_guard_zombie_horse_jockey");

    public static void bootstrap(BootstrapContext<BiomeModifier> context) {
        // CF -> PF -> BM
        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        var biomes = context.lookup(Registries.BIOME);

        // Ores
        context.register(ADD_MILITUS_ALLOY_ORE, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_NETHER),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.MILITUS_ALLOY_ORE_PLACED_KEY)),
                GenerationStep.Decoration.UNDERGROUND_ORES));


        context.register(ADD_WITHER_SKELETONS,
                new BiomeModifiers.AddSpawnsBiomeModifier(
                        HolderSet.direct(biomes.getOrThrow(Biomes.NETHER_WASTES)),
                        List.of(new MobSpawnSettings.SpawnerData(
                                EntityType.WITHER_SKELETON,
                                20, // weight
                                2,  // min
                                5   // max
                        ))
                )
        );

        context.register(ADD_GARGOYLE_POSSESSED,
                new BiomeModifiers.AddSpawnsBiomeModifier(
                        HolderSet.direct(biomes.getOrThrow(Biomes.BASALT_DELTAS)),
                        List.of(new MobSpawnSettings.SpawnerData(
                                ModEntities.GARGOYLE_POSSESSED.get(),
                                5,
                                1,
                                2
                        ))
                )
        );

        context.register(ADD_DECAYED_ZOMBIFIED_PIGLIN,
                new BiomeModifiers.AddSpawnsBiomeModifier(
                        HolderSet.direct(biomes.getOrThrow(Biomes.NETHER_WASTES)),
                        List.of(new MobSpawnSettings.SpawnerData(
                                ModEntities.DECAYED_ZOMBIFIED_PIGLIN.get(),
                                10,
                                1,
                                2
                        ))
                )
        );

        context.register(ADD_HEXED_ZOMBIE_HORSE,
                new BiomeModifiers.AddSpawnsBiomeModifier(
                        HolderSet.direct(biomes.getOrThrow(Biomes.CRIMSON_FOREST)),
                        List.of(new MobSpawnSettings.SpawnerData(
                                ModEntities.HEXED_ZOMBIE_HORSE.get(),
                                3,
                                1,
                                1
                        ))
                )
        );

        context.register(ADD_WITHER_SKELETON_HORSE,
                new BiomeModifiers.AddSpawnsBiomeModifier(
                        HolderSet.direct(biomes.getOrThrow(Biomes.NETHER_WASTES)),
                        List.of(new MobSpawnSettings.SpawnerData(
                                ModEntities.WITHER_SKELETON_HORSE.get(),
                                2,
                                1,
                                2
                        ))
                )
        );

        context.register(ADD_HEXED_ZOMBIE_HORSE_JOCKEY,
                new BiomeModifiers.AddSpawnsBiomeModifier(
                        HolderSet.direct(biomes.getOrThrow(Biomes.SOUL_SAND_VALLEY)),
                        List.of(new MobSpawnSettings.SpawnerData(
                                ModEntities.HEXED_ZOMBIE_HORSE_JOCKEY.get(),
                                5,
                                1,
                                3
                        ))
                )
        );

        context.register(ADD_WITHER_SKELETON_HORSE_JOCKEY,
                new BiomeModifiers.AddSpawnsBiomeModifier(
                        HolderSet.direct(biomes.getOrThrow(Biomes.NETHER_WASTES)),
                        List.of(new MobSpawnSettings.SpawnerData(
                                ModEntities.WITHER_SKELETON_HORSE_JOCKEY.get(),
                                10,
                                1,
                                3
                        ))
                )
        );

        context.register(ADD_GUARD_ZOMBIE_HORSE_JOCKEY,
                new BiomeModifiers.AddSpawnsBiomeModifier(
                        HolderSet.direct(biomes.getOrThrow(Biomes.NETHER_WASTES)),
                        List.of(new MobSpawnSettings.SpawnerData(
                                ModEntities.GUARD_ZOMBIE_HORSE_JOCKEY.get(),
                                15,
                                1,
                                3
                        ))
                )
        );

    }

    private static ResourceKey<BiomeModifier> registerKey(String name) {
        return ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ResourceLocation.fromNamespaceAndPath(NetherHexedKingdom.MODID, name));
    }
}