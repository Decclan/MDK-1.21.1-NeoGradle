package com.deimoshexxus.netherhexedkingdom.datagen;

import com.deimoshexxus.netherhexedkingdom.NetherHexedKingdomMain;
import com.deimoshexxus.netherhexedkingdom.content.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagsProvider extends BlockTagsProvider {

    public ModBlockTagsProvider(PackOutput output,
                                CompletableFuture<HolderLookup.Provider> lookupProvider,
                                ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, NetherHexedKingdomMain.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.MILITUS_ALLOY_BLOCK.get())
                .add(ModBlocks.MILITUS_ALLOY_ORE.get())
                .add(ModBlocks.HEXAN_CHISELED_NETHER_BRICKS.get())
                .add(ModBlocks.GILDED_BRICKS_OF_LOST_TIME.get())
                .add(ModBlocks.RED_GILDED_BRICKS_OF_LOST_TIME.get())
                .add(ModBlocks.IRON_PLATE_BLOCK.get())
                .add(ModBlocks.HEXAN_CHISELED_POLISHED_BLACKSTONE.get());

        tag(BlockTags.NEEDS_IRON_TOOL)
                .add(ModBlocks.MILITUS_ALLOY_BLOCK.get())
                .add(ModBlocks.MILITUS_ALLOY_ORE.get());
    }

    @Override
    public String getName() {
        return "Nether Hexed Kingdom Block Tags";
    }
}
