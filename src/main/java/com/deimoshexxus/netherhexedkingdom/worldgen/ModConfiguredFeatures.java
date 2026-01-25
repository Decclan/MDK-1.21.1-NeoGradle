package com.deimoshexxus.netherhexedkingdom.worldgen;

import com.deimoshexxus.netherhexedkingdom.NetherHexedKingdom;
import com.deimoshexxus.netherhexedkingdom.content.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.feature.configurations.NetherForestVegetationConfig;

import java.util.List;

public class ModConfiguredFeatures {

    public static final ResourceKey<ConfiguredFeature<?, ?>> MILITUS_ALLOY_ORE_KEY = registerKey("militus_alloy_ore");

//    public static final ResourceKey<ConfiguredFeature<?, ?>> MASONIAE_NETHER_FOSSIL =
//            ResourceKey.create(
//                    Registries.CONFIGURED_FEATURE,
//                    ResourceLocation.fromNamespaceAndPath(
//                            NetherHexedKingdom.MODID,
//                            "masoniae_nether_fossil"
//                    )
//            );

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        // Ores
        RuleTest netherrackReplacables = new BlockMatchTest(Blocks.NETHERRACK);

        List<OreConfiguration.TargetBlockState> netherMilitusAlloyOres = List.of(
                OreConfiguration.target(netherrackReplacables, ModBlocks.MILITUS_ALLOY_ORE.get().defaultBlockState()));

        register(context, MILITUS_ALLOY_ORE_KEY, Feature.ORE, new OreConfiguration(netherMilitusAlloyOres, 5));

        // Biome Features

//        var pools = context.lookup(Registries.TEMPLATE_POOL);
//
//        var masoniaePool = pools.getOrThrow(
//                ResourceKey.create(
//                        Registries.TEMPLATE_POOL,
//                        ResourceLocation.fromNamespaceAndPath(
//                                NetherHexedKingdom.MODID,
//                                "nether_fossils/masoniae"
//                        )
//                )
//        );

//        // Vanilla uses the same pool for bones + skulls
//        NetherFossilConfiguration config =
//                new NetherFossilConfiguration(
//                        masoniaePool,
//                        masoniaePool,
//                        0.1F // coal replacement chance
//                );
//
//        context.register(
//                MASONIAE_NETHER_FOSSIL,
//                new ConfiguredFeature<>(Feature.NETHER_FOSSIL, config)
//        );

    }

    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(NetherHexedKingdom.MODID, name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstrapContext<ConfiguredFeature<?, ?>> context,
                                                                                          ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}