package com.deimoshexxus.netherhexedkingdom.worldgen;


import com.deimoshexxus.netherhexedkingdom.NetherHexedKingdom;
import com.deimoshexxus.netherhexedkingdom.worldgen.ModOrePlacement;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class ModPlacedFeatures {

    public static final ResourceKey<PlacedFeature> MILITUS_ALLOY_ORE_PLACED_KEY = registerKey("militus_alloy_ore_placed");

//    public static final ResourceKey<PlacedFeature> MASONIAE_NETHER_FOSSIL_PLACED =
//            ResourceKey.create(
//                    Registries.PLACED_FEATURE,
//                    ResourceLocation.fromNamespaceAndPath(
//                            NetherHexedKingdom.MODID,
//                            "masoniae_nether_fossil_placed"
//                    )
//            );

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        var configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        // Ores
        register(context, MILITUS_ALLOY_ORE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.MILITUS_ALLOY_ORE_KEY),
                ModOrePlacement.commonOrePlacement(12, HeightRangePlacement.triangle(VerticalAnchor.absolute(24), VerticalAnchor.absolute(64))));

//        PlacementUtils.register(
//                context,
//                ORE_ANCIENT_DEBRIS_LARGE,
//                holder27,
//                InSquarePlacement.spread(),
//                HeightRangePlacement.triangle(VerticalAnchor.absolute(8), VerticalAnchor.absolute(24)),
//                BiomeFilter.biome()
//        );
//        PlacementUtils.register(context, ORE_ANCIENT_DEBRIS_SMALL, holder28, InSquarePlacement.spread(), PlacementUtils.RANGE_8_8, BiomeFilter.biome());


        // Biome Features
//        var configured = context.lookup(Registries.CONFIGURED_FEATURE);
//
//        context.register(
//                MASONIAE_NETHER_FOSSIL_PLACED,
//                new PlacedFeature(
//                        configured.getOrThrow(ModConfiguredFeatures.MASONIAE_NETHER_FOSSIL),
//                        List.of(
//                                RarityFilter.onAverageOnceEvery(64),
//                                InSquarePlacement.spread(),
//                                HeightRangePlacement.uniform(
//                                        VerticalAnchor.absolute(30),
//                                        VerticalAnchor.absolute(70)
//                                ),
//                                BiomeFilter.biome()
//                        )
//                )
//        );
//

    }


    private static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(NetherHexedKingdom.MODID, name));
    }

    private static void register(BootstrapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> configuration,
                                 List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }
}