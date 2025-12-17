package com.deimoshexxus.netherhexedkingdom.datagen;

import com.deimoshexxus.netherhexedkingdom.NetherHexedKingdom;
import com.deimoshexxus.netherhexedkingdom.worldgen.ModConfiguredFeatures;
import com.deimoshexxus.netherhexedkingdom.worldgen.ModPlacedFeatures;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;


//import com.deimoshexxus.netherhexedkingdom.NetherHexedKingdom;
//import net.minecraft.core.HolderLookup;
//import net.minecraft.core.RegistrySetBuilder;
//import net.minecraft.data.PackOutput;
//import net.minecraft.data.worldgen.BootstrapContext;
//import net.minecraft.world.level.levelgen.structure.Structure;
//import net.minecraft.world.level.levelgen.structure.StructureSet;
//import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModDatapackProvider extends DatapackBuiltinEntriesProvider {

    /**
     * Registry builder for all datapack-driven registries
     */
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            // Structures
            .add(net.minecraft.core.registries.Registries.STRUCTURE, ModStructures::bootstrap)
            .add(net.minecraft.core.registries.Registries.STRUCTURE_SET, ModStructureSets::bootstrap);

    // Add more later as needed:
    // .add(Registries.CONFIGURED_FEATURE, ...)
    // .add(Registries.PLACED_FEATURE, ...)
    // .add(Registries.BIOME, ...) (rarely needed)

    public ModDatapackProvider(
            PackOutput output,
            CompletableFuture<HolderLookup.Provider> registries,
            ExistingFileHelper existingFileHelper
    ) {
        super(
                output,
                registries,
                BUILDER,
                Set.of(NetherHexedKingdom.MODID)
        );
    }
}
