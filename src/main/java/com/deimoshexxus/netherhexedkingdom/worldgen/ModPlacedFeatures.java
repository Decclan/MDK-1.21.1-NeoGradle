package com.deimoshexxus.netherhexedkingdom.worldgen;

/**
 * Placed features (registered to Registries.PLACED_FEATURE).
 */
public final class ModPlacedFeatures {

//    public static final DeferredRegister<PlacedFeature> PLACED =
//            DeferredRegister.create(Registries.PLACED_FEATURE, NetherHexedKingdom.MODID);
//
//    public static final DeferredHolder<PlacedFeature, PlacedFeature> MASONIAE_MUSHROOM_PLACED =
//            PLACED.register("masoniae_mushroom_placed", () -> {
//                // Get the Holder<ConfiguredFeature> from the configured-features DeferredHolder:
//                Holder<ConfiguredFeature<?, ?>> configuredHolder =
//                        ModConfiguredFeatures.MASONIAE_MUSHROOM.getHolder().orElseThrow(() ->
//                                new IllegalStateException("Configured feature not registered: masoniae_mushroom"));
//
//                // Build placed feature with proper placement modifiers.
//                return new PlacedFeature(
//                        configuredHolder,
//                        List.of(
//                                // 1) CountPlacement - how many attempts per placement
//                                CountPlacement.of(5),
//
//                                // 2) Spread inside the chunk
//                                InSquarePlacement.spread(),
//
//                                // 3) Height selection - place at surface (use onHeightmap so we get the surface)
//                                HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING),
//
//                                // 4) Block predicate: block below must be BONE_BLOCK (offset -1)
//                                BlockPredicateFilter.forPredicate(
//                                        BlockPredicate.matchesBlocks(new Vec3i(0, -1, 0), Blocks.BONE_BLOCK)
//                                ),
//
//                                // 5) Biome filter: feature only runs in matching biome context
//                                BiomeFilter.biome()
//                        )
//                );
//            });
//
//    private ModPlacedFeatures() {}
}
