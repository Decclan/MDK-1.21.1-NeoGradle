package com.deimoshexxus.netherhexedkingdom.datagen;

import com.deimoshexxus.netherhexedkingdom.NetherHexedKingdom;
import com.deimoshexxus.netherhexedkingdom.content.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagsProvider extends BlockTagsProvider {

    public ModBlockTagsProvider(
            PackOutput output,
            CompletableFuture<HolderLookup.Provider> lookupProvider,
            ExistingFileHelper existingFileHelper) {

        super(output, lookupProvider, NetherHexedKingdom.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {

        // --------------------------------------------------------------------
        // Mineable
        // --------------------------------------------------------------------

        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.MILITUS_ALLOY_BLOCK.value())
                .add(ModBlocks.MILITUS_ALLOY_ORE.value())
                .add(ModBlocks.IRON_PLATE_BLOCK.value())

                .add(ModBlocks.GARGOYLE_GOLD_BLOCK.value())
                .add(ModBlocks.GARGOYLE_BLACKSTONE_BLOCK.value())
                .add(ModBlocks.GARGOYLE_QUARTZ_BLOCK.value())
                .add(ModBlocks.GARGOYLE_BASALT_BLOCK.value())
                .add(ModBlocks.GARGOYLE_OBSIDIAN_BLOCK.value())
                .add(ModBlocks.GARGOYLE_AMETHYST_BLOCK.value())

                .add(ModBlocks.BLACKSTONE_FIRESTAND_BLOCK.value())

                .add(ModBlocks.HEXAN_CHISELED_NETHER_BRICKS.value())
                .add(ModBlocks.GILDED_BRICKS_OF_LOST_TIME.value())
                .add(ModBlocks.RED_GILDED_BRICKS_OF_LOST_TIME.value())
                .add(ModBlocks.GILDED_BLACKSTONE_BRICKS_OF_LOST_TIME.value())
                .add(ModBlocks.HEXAN_CHISELED_POLISHED_BLACKSTONE.value());

        // --------------------------------------------------------------------
        // Stone Tier
        // --------------------------------------------------------------------

        tag(BlockTags.NEEDS_STONE_TOOL)
                .add(ModBlocks.GARGOYLE_BLACKSTONE_BLOCK.value())
                .add(ModBlocks.GARGOYLE_BASALT_BLOCK.value())
                .add(ModBlocks.BLACKSTONE_FIRESTAND_BLOCK.value());

        // --------------------------------------------------------------------
        // Iron Tier
        // --------------------------------------------------------------------

        tag(BlockTags.NEEDS_IRON_TOOL)
                .add(ModBlocks.MILITUS_ALLOY_BLOCK.value())
                .add(ModBlocks.MILITUS_ALLOY_ORE.value())
                .add(ModBlocks.GARGOYLE_GOLD_BLOCK.value());

        // --------------------------------------------------------------------
        // Diamond Tier
        // --------------------------------------------------------------------

        tag(BlockTags.NEEDS_DIAMOND_TOOL)
                .add(ModBlocks.GARGOYLE_OBSIDIAN_BLOCK.value());

        // --------------------------------------------------------------------
        // Misc Tags
        // --------------------------------------------------------------------

        tag(BlockTags.REPLACEABLE)
                .add(ModBlocks.LINGZHI_MUSHROOM.value());

        tag(BlockTags.FLOWERS)
                .add(ModBlocks.LINGZHI_MUSHROOM.value());
    }

    @Override
    public String getName() {
        return "Nether Hexed Kingdom Block Tags";
    }
}
