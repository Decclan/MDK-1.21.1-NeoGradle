package com.deimoshexxus.netherhexedkingdom.datagen;

import com.deimoshexxus.netherhexedkingdom.NetherHexedKingdom;
import com.deimoshexxus.netherhexedkingdom.datagen.ModChestLootProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.core.HolderLookup;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public final class DataGenerators {

    private DataGenerators() {}

    /**
     * NOTE: do NOT rely on @EventBusSubscriber here. Instead call:
     *   modEventBus.addListener(DataGenerators::gatherData);
     * from your mod constructor (NetherHexedKingdom).
     */

    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        ModBlockTagsProvider blockTagsProvider = new ModBlockTagsProvider(output, lookupProvider, existingFileHelper);
        generator.addProvider(true, blockTagsProvider);

        if (event.includeClient()) {
            generator.addProvider(true, new ModBlockStateProvider(output, existingFileHelper));
            generator.addProvider(true, new ModItemModelProvider(output, existingFileHelper));
        }


        generator.addProvider(event.includeServer(), new ModDatapackProvider(output, lookupProvider));

        if (event.includeServer()) {

            List<LootTableProvider.SubProviderEntry> subProviders = List.of(
                    new LootTableProvider.SubProviderEntry(
                            ModBlockLootProvider::new,
                            LootContextParamSets.BLOCK
                    ),
                    new LootTableProvider.SubProviderEntry(
                            ModChestLootProvider::new,
                            LootContextParamSets.CHEST
                    )
            );

            generator.addProvider(
                    true,
                    new LootTableProvider(
                            output,
                            Set.of(), // required tables (almost always empty)
                            subProviders,
                            lookupProvider
                    )
            );
        }


    }


}
