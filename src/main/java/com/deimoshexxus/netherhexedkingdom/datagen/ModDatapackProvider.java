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

// src/main/java/.../ModDatapackProvider.java
public class ModDatapackProvider extends DatapackBuiltinEntriesProvider {

    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(net.minecraft.core.registries.Registries.STRUCTURE, ModStructures::bootstrap)
            .add(net.minecraft.core.registries.Registries.STRUCTURE_SET, ModStructureSets::bootstrap);

    // NOTE: using a separate namespace so generated pack does NOT shadow mod resources
    private static final Set<String> GENERATED_NAMESPACES = Set.of(NetherHexedKingdom.MODID + "_datagen");

    public ModDatapackProvider(
            PackOutput output,
            CompletableFuture<HolderLookup.Provider> registries,
            ExistingFileHelper existingFileHelper
    ) {
        super(
                output,
                registries,
                BUILDER,
                GENERATED_NAMESPACES
        );
    }
}
