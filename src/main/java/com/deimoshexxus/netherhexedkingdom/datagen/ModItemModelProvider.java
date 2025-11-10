package com.deimoshexxus.netherhexedkingdom.datagen;

import com.deimoshexxus.netherhexedkingdom.NetherHexedKingdomMain;
import com.deimoshexxus.netherhexedkingdom.content.ModBlocks;
import com.deimoshexxus.netherhexedkingdom.content.ModItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, NetherHexedKingdomMain.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(ModItems.MILITUS_ALLOY_INGOT.get());
        basicItem(ModItems.MILITUS_ALLOY_NUGGET.get());
        basicItem(ModItems.IRON_CLUMP.get());
        basicItem(ModItems.NETHERITE_FRAGMENT_BUNDLE.get());
        basicItem(ModItems.NETHERITE_FRAGMENT.get());
        basicItem(ModItems.NETHERITE_OXIDE.get());
        basicItem(ModItems.IMPERIAL_COINS.get());

        // Custom model block items
        withExistingParent(ModBlocks.BLACKSTONE_FIRESTAND_BLOCK.getId().getPath(),
                modLoc("block/blackstone_firestand_block"));
        withExistingParent(ModBlocks.HUMAN_SKELETON_TOP_BLOCK.getId().getPath(),
                modLoc("block/human_skeleton_top_block"));
        withExistingParent(ModBlocks.HUMAN_SKELETON_BOTTOM_BLOCK.getId().getPath(),
                modLoc("block/human_skeleton_bottom_block"));
    }
}
