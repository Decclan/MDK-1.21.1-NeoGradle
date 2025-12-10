package com.deimoshexxus.netherhexedkingdom.world;

import com.deimoshexxus.netherhexedkingdom.NetherHexedKingdom;
import com.deimoshexxus.netherhexedkingdom.content.ModBlocks;
import net.minecraft.core.Holder;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.function.Supplier;


public final class ModWorldFeatures {
    private ModWorldFeatures() {}
}

//public final class ModWorldFeatures {
//
//    public static final DeferredRegister<PlacedFeature> PLACED_FEATURES =
//            DeferredRegister.create(Registries.PLACED_FEATURE, NetherHexedKingdom.MODID);
//
//    public static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES =
//            DeferredRegister.create(Registries.CONFIGURED_FEATURE, NetherHexedKingdom.MODID);
//
//
//    /**
//     * Placed feature that places a single MASONIAE_MUSHROOM block only if the block BELOW
//     * the placement position is BONE_BLOCK.
//     *
//     * Implementation notes (1.21.1):
//     * - ConfiguredFeature is constructed directly (no .configured(...) helper).
//     * - SimpleBlockConfiguration expects a BlockStateProvider.
//     * - BlockPredicate.matchesBlocks(Vec3i offset, Block...) lets us match the block below.
//     */
//
//    public static final Supplier<ConfiguredFeature<?, ?>> MASONIAE_MUSHROOM =
//            CONFIGURED_FEATURES.register("masoniae_mushroom", () ->
//                    new ConfiguredFeature<>(
//                            Feature.SIMPLE_BLOCK,
//                            new SimpleBlockConfiguration(
//                                    BlockStateProvider.simple(ModBlocks.MASONIAE_MUSHROOM.get())
//                            )
//                    )
//            );
//
//
//
//    public static final Supplier<PlacedFeature> MASONIAE_MUSHROOM_PLACED =
//            PLACED_FEATURES.register("masoniae_mushroom_placed", () -> {
//                // configured feature: place a single block (the mushroom's default state)
//                ConfiguredFeature<SimpleBlockConfiguration, ?> configured =
//                        new ConfiguredFeature<>(
//                                Feature.SIMPLE_BLOCK,
//                                new SimpleBlockConfiguration(
//                                        BlockStateProvider.simple(ModBlocks.MASONIAE_MUSHROOM.get())
//                                )
//                        );
//
//                // BlockPredicate: offset by (0, -1, 0) and require Blocks.BONE_BLOCK
//                BlockPredicate belowIsBone = BlockPredicate.matchesBlocks(new Vec3i(0, -1, 0), Blocks.BONE_BLOCK);
//                //BlockPredicate belowIsGravel = BlockPredicate.matchesBlocks(new Vec3i(0, -1, 0), Blocks.GRAVEL);
//
//                // Build placed feature with predicate + vertical range placement
//                return new PlacedFeature(
//                        Holder.direct(configured),
//                        List.of(
//                                BlockPredicateFilter.forPredicate(belowIsBone),
//                                HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(128))
//                        )
//                );
//            });
//
//    private ModWorldFeatures() {}
//}
