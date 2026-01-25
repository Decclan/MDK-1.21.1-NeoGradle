package com.deimoshexxus.netherhexedkingdom.worldgen;

import com.deimoshexxus.netherhexedkingdom.NetherHexedKingdom;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ModBiomeModifiers {
    public static final ResourceKey<BiomeModifier> ADD_MILITUS_ALLOY_ORE = registerKey("add_militus_alloy_ore");

//    public static final ResourceKey<BiomeModifier> ADD_MASONIAE_NETHER_FOSSILS = registerKey("add_masoniae_nether_fossils");

    public static void bootstrap(BootstrapContext<BiomeModifier> context) {
        // CF -> PF -> BM
        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        var biomes = context.lookup(Registries.BIOME);

        // Ores
        context.register(ADD_MILITUS_ALLOY_ORE, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_NETHER),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.MILITUS_ALLOY_ORE_PLACED_KEY)),
                GenerationStep.Decoration.UNDERGROUND_ORES));

        // Biome Features
//        context.register(
//                ADD_MASONIAE_NETHER_FOSSILS,
//                new BiomeModifiers.AddFeaturesBiomeModifier(
//                        biomes.getOrThrow(BiomeTags.IS_NETHER),
//                        HolderSet.direct(
//                                placedFeatures.getOrThrow(ModPlacedFeatures.MASONIAE_NETHER_FOSSIL_PLACED)
//                        ),
//                        GenerationStep.Decoration.UNDERGROUND_DECORATION
//                )
//        );

    }

    private static ResourceKey<BiomeModifier> registerKey(String name) {
        return ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ResourceLocation.fromNamespaceAndPath(NetherHexedKingdom.MODID, name));
    }
}