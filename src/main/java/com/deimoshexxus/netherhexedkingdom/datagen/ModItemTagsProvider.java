package com.deimoshexxus.netherhexedkingdom.datagen;

import com.deimoshexxus.netherhexedkingdom.NetherHexedKingdom;
import com.deimoshexxus.netherhexedkingdom.content.ModBlocks;
import com.deimoshexxus.netherhexedkingdom.content.ModItems;
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
import net.minecraft.tags.ItemTags;

import java.util.concurrent.CompletableFuture;

public class ModItemTagsProvider extends ItemTagsProvider {
    // The "minecraft:mineable/pickaxe" item tag
    public static final TagKey<Item> MINEABLE_WITH_PICKAXE =
            TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("minecraft", "mineable/pickaxe"));

    public static final TagKey<Item> FUNGI_ITEMS =
            TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(NetherHexedKingdom.MODID, "fungi"));


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

        // Custom fungi grouping
        tag(FUNGI_ITEMS)
                .add(ModItems.LINGZHI_MUSHROOM_ITEM.get())
                .add(ModItems.MASONIAE_MUSHROOM_ITEM.get())
                .add(ModItems.SOULGLOW_MUSHROOM_ITEM.get());

        // Vanilla fungi tag
        tag(ItemTags.FOX_FOOD)
                .add(ModItems.LINGZHI_MUSHROOM_ITEM.get())
                .add(ModItems.MASONIAE_MUSHROOM_ITEM.get())
                .add(ModItems.SOULGLOW_MUSHROOM_ITEM.get());

        // Optional: if alloy can be used for armor trims
        // tag(ItemTags.TRIM_MATERIALS)
        //         .add(ModItems.MILITUS_ALLOY_INGOT.get());
    }

    @Override
    public String getName() {
        return "Nether Hexed Kingdom Item Tags";
    }
}
