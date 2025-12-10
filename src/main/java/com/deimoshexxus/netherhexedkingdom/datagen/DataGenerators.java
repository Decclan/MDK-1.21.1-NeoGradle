package com.deimoshexxus.netherhexedkingdom.datagen;

import com.deimoshexxus.netherhexedkingdom.NetherHexedKingdom;
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
        CompletableFuture<HolderLookup.Provider> lookupFuture = event.getLookupProvider();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        ModBlockTagsProvider blockTagsProvider = new ModBlockTagsProvider(output, lookupFuture, existingFileHelper);
        generator.addProvider(true, blockTagsProvider);

        if (event.includeClient()) {
            generator.addProvider(true, new ModBlockStateProvider(output, existingFileHelper));
            generator.addProvider(true, new ModItemModelProvider(output, existingFileHelper));
        }

        if (event.includeServer()) {
            List<LootTableProvider.SubProviderEntry> subProviders = List.of(
                    new LootTableProvider.SubProviderEntry(ModBlockLootProvider::new, LootContextParamSets.BLOCK)
            );

            Set<ResourceKey<LootTable>> requiredTables = Set.of(); // optional validation

            CompletableFuture<HolderLookup.Provider> registries = event.getLookupProvider();

            generator.addProvider(true, new LootTableProvider(
                    output,
                    requiredTables,
                    subProviders,
                    registries
            ));
        }
    }

//    public static void gatherData(GatherDataEvent event) {
//        DataGenerator generator = event.getGenerator();
//        PackOutput output = generator.getPackOutput();
//        CompletableFuture<HolderLookup.Provider> lookupFuture = event.getLookupProvider();
//
//        // HolderLookup.Provider future used by many providers (recipes, tags providers use this)
//        var lookupProvider = event.getLookupProvider(); // CompletableFuture<HolderLookup.Provider>
//        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
//
//        lookupFuture.thenAccept(provider -> {
//            generator.addProvider(true, new ModBlockLootProvider(provider));
//        });
//
//        /*
//         * Create & register the BlockTags provider first, so we can get its tag lookup future
//         * to pass into the ItemTags provider (ModItemTagsProvider requires the block-tag future).
//         */
//        ModBlockTagsProvider blockTagsProvider = new ModBlockTagsProvider(output, lookupProvider, existingFileHelper);
//        generator.addProvider(true, blockTagsProvider); // register the single instance
//
//
//        // Client-side data
//        if (event.includeClient()) {
//            generator.addProvider(true, new ModBlockStateProvider(output, existingFileHelper));
//            generator.addProvider(true, new ModItemModelProvider(output, existingFileHelper));
//        }
//
//        // Server-side data
//        if (event.includeServer()) {
//            // Recipe provider uses the HolderLookup Provider future
//            generator.addProvider(true, new ModRecipeProvider(output, lookupProvider));
//
//            generator.addProvider(true, new ModBlockLootProvider(lookupProvider));
//
//
//            // Block tags are already registered above (blockTagsProvider).
//            // Now register the item tags provider and pass the block tags future from blockTagsProvider:
//            generator.addProvider(true, new ModItemTagsProvider(
//                    output,
//                    lookupProvider,                      // CompletableFuture<HolderLookup.Provider>
//                    blockTagsProvider.contentsGetter(), // CompletableFuture<TagsProvider.TagLookup<Block>>
//                    NetherHexedKingdom.MODID,
//                    existingFileHelper
//            ));
//        }
//    }
}
