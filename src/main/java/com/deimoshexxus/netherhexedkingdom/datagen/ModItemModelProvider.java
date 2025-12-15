package com.deimoshexxus.netherhexedkingdom.datagen;

import com.deimoshexxus.netherhexedkingdom.NetherHexedKingdom;
import com.deimoshexxus.netherhexedkingdom.content.ModBlocks;
import com.deimoshexxus.netherhexedkingdom.content.ModItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, NetherHexedKingdom.MODID, existingFileHelper);
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
        spawnEggItem(ModItems.DECAYED_ZOMBIE_SPAWN_EGG.get());
        spawnEggItem(ModItems.DECAYED_ZOMBIE_HUSK_SPAWN_EGG.get());
        spawnEggItem(ModItems.DECAYED_ZOMBIFIED_PIGLIN_SPAWN_EGG.get());
        spawnEggItem(ModItems.HEXAN_GUARD_SPAWN_EGG.get());
        spawnEggItem(ModItems.GARGOYLE_POSSESSED_SPAWN_EGG.get());

        basicItem(ModItems.MILITUS_ALLOY_HELMET.get());
        basicItem(ModItems.MILITUS_ALLOY_CHESTPLATE.get());
        basicItem(ModItems.MILITUS_ALLOY_LEGGINGS.get());
        basicItem(ModItems.MILITUS_ALLOY_BOOTS.get());
        basicItem(ModItems.MILITUS_ALLOY_HORSE_ARMOR.get());

        //basicItem(ModItems.LINGZHI_MUSHROOM_FOOD.get());


        withExistingParent(ModBlocks.GARGOYLE_GOLD_BLOCK.getId().getPath(),
                modLoc("block/gargoyle_gold_block"));
        withExistingParent(ModBlocks.GARGOYLE_BLACKSTONE_BLOCK.getId().getPath(),
                modLoc("block/gargoyle_blackstone_block"));
        withExistingParent(ModBlocks.GARGOYLE_QUARTZ_BLOCK.getId().getPath(),
                modLoc("block/gargoyle_quartz_block"));
        withExistingParent(ModBlocks.GARGOYLE_BASALT_BLOCK.getId().getPath(),
                modLoc("block/gargoyle_basalt_block"));
        withExistingParent(ModBlocks.GARGOYLE_OBSIDIAN_BLOCK.getId().getPath(),
                modLoc("block/gargoyle_obsidian_block"));
        withExistingParent(ModBlocks.GARGOYLE_AMETHYST_BLOCK.getId().getPath(),
                modLoc("block/gargoyle_amethyst_block"));


        withExistingParent(ModBlocks.BLACKSTONE_FIRESTAND_BLOCK.getId().getPath(),
                modLoc("block/blackstone_firestand_block"));
        withExistingParent(ModBlocks.HUMAN_SKELETON_TOP_BLOCK.getId().getPath(),
                modLoc("block/human_skeleton_top_block"));
        withExistingParent(ModBlocks.HUMAN_SKELETON_BOTTOM_BLOCK.getId().getPath(),
                modLoc("block/human_skeleton_bottom_block"));

        // Simple "generated" item model that points to a texture
        singleTexture(ModItems.POISON_GAS_ITEM.getId().getPath(),
                mcLoc("item/generated"),
                "layer0", modLoc("item/poison_gas_source"));

        singleTexture(ModItems.NETHER_PORTAL_ORB.getId().getPath(),
                mcLoc("item/generated"),
                "layer0", modLoc("item/nether_portal_orb"));

        singleTexture(ModItems.LINGZHI_MUSHROOM_ITEM.getId().getPath(),
                mcLoc("item/generated"),
                "layer0", modLoc("item/lingzhi_mushroom"));


        singleTexture(ModItems.SOULGLOW_MUSHROOM_ITEM.getId().getPath(),
                mcLoc("item/generated"),
                "layer0", modLoc("item/soul_glow_mushroom"));

//        withExistingParent(ModItems.LINGZHI_MUSHROOM_ITEM.getId().getPath(),
//                modLoc("block/lingzhi_mushroom_stage2"));



    }
}
