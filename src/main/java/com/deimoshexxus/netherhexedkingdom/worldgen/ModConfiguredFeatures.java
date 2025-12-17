package com.deimoshexxus.netherhexedkingdom.worldgen;

import com.deimoshexxus.netherhexedkingdom.NetherHexedKingdom;
import com.deimoshexxus.netherhexedkingdom.content.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Configured features (registered to Registries.CONFIGURED_FEATURE).
 */
public final class ModConfiguredFeatures {

    public static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED =
            DeferredRegister.create(Registries.CONFIGURED_FEATURE, NetherHexedKingdom.MODID);

    // DeferredHolder for the configured feature (we will reference its holder when making the placed feature)
    public static final DeferredHolder<ConfiguredFeature<?, ?>, ConfiguredFeature<?, ?>> MASONIAE_MUSHROOM =
            CONFIGURED.register("masoniae_mushroom", () ->
                    new ConfiguredFeature<>(
                            Feature.SIMPLE_BLOCK,
                            new SimpleBlockConfiguration(
                                    BlockStateProvider.simple(ModBlocks.MASONIAE_MUSHROOM.get())
                            )
                    )
            );

    private ModConfiguredFeatures() {}
}
