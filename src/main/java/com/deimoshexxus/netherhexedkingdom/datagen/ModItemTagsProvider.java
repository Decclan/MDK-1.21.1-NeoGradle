package com.deimoshexxus.netherhexedkingdom.datagen;

import com.deimoshexxus.netherhexedkingdom.content.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class ModItemTagsProvider extends ItemTagsProvider {
    // The "minecraft:mineable/pickaxe" item tag
    public static final TagKey<Item> MINEABLE_WITH_PICKAXE =
            TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("minecraft", "mineable/pickaxe"));

    /**
     * Correct constructor signature for NeoForge 1.21.x:
     * @param output PackOutput
     * @param lookupProvider CompletableFuture<HolderLookup.Provider> from GatherDataEvent
     * @param modId your mod id
     * @param blockTagLookup CompletableFuture<TagLookup<Block>> from GatherDataEvent
     * @param existingFileHelper ExistingFileHelper
     */
    public ModItemTagsProvider(PackOutput output,
                               CompletableFuture<HolderLookup.Provider> lookupProvider,
                               CompletableFuture<TagsProvider.TagLookup<Block>> blockTagLookup,
                               String modId,
                               ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTagLookup, modId, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // Mirror block tags to item tags where appropriate
        tag(MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.MILITUS_ALLOY_BLOCK.get().asItem())
                .add(ModBlocks.MILITUS_ALLOY_ORE.get().asItem())
                .add(ModBlocks.HEXAN_CHISELED_NETHER_BRICKS.get().asItem());

        // Add standalone items if necessary
        // tag(MINEABLE_WITH_PICKAXE).add(ModItems.MILITUS_ALLOY_INGOT.get());
    }

    @Override
    public String getName() {
        return "Nether Hexed Kingdom Item Tags";
    }
}
