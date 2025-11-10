package com.deimoshexxus.netherhexedkingdom.datagen;

import com.deimoshexxus.netherhexedkingdom.NetherHexedKingdomMain;
import com.deimoshexxus.netherhexedkingdom.content.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider {

    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, NetherHexedKingdomMain.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {

        // ----------------------
        // Simple cube blocks
        // ----------------------
        simpleBlockWithItem(ModBlocks.MILITUS_ALLOY_ORE.get(), cubeAll(ModBlocks.MILITUS_ALLOY_ORE.get()));
        simpleBlockWithItem(ModBlocks.MILITUS_ALLOY_BLOCK.get(), cubeAll(ModBlocks.MILITUS_ALLOY_BLOCK.get()));
        simpleBlockWithItem(ModBlocks.IRON_PLATE_BLOCK.get(), cubeAll(ModBlocks.IRON_PLATE_BLOCK.get()));

        // ----------------------
        // Pillar block
        // ----------------------
        try {
            axisBlockWithRenderType(ModBlocks.ETERNAL_LIGHT_BLOCK.get(), "cutout");
        } catch (NoSuchMethodError e) {
            axisBlock((RotatedPillarBlock) ModBlocks.ETERNAL_LIGHT_BLOCK.get());
        }

        // ----------------------
        // Custom manual blocks
        // ----------------------
        // Use pre-existing JSON, do not generate
        simpleBlock(ModBlocks.BLACKSTONE_FIRESTAND_BLOCK.get(),
                models().getExistingFile(modLoc("block/blackstone_firestand_block")));

        // ----------------------
        // Symmetrical custom blocks
        // ----------------------
        simpleBlock(ModBlocks.GARGOYLE_GOLD_BLOCK.get(),
                models().getExistingFile(modLoc("block/gargoyle_gold_block")));
        simpleBlock(ModBlocks.GARGOYLE_BLACKSTONE_BLOCK.get(),
                models().getExistingFile(modLoc("block/gargoyle_blackstone_block")));
        simpleBlock(ModBlocks.GARGOYLE_QUARTZ_BLOCK.get(),
                models().getExistingFile(modLoc("block/gargoyle_quartz_block")));
        simpleBlock(ModBlocks.GARGOYLE_BASALT_BLOCK.get(),
                models().getExistingFile(modLoc("block/gargoyle_basalt_block")));
        simpleBlock(ModBlocks.GARGOYLE_OBSIDIAN_BLOCK.get(),
                models().getExistingFile(modLoc("block/gargoyle_obsidian_block")));

        // ----------------------
        // BlockBench-generated models
        // ----------------------
        String topName = "human_skeleton_top_block";
        String bottomName = "human_skeleton_bottom_block";

//        // Tell the generator to create models using existing parents (BlockBench JSON)
//        models().withExistingParent(topName, modLoc("block/" + topName)).renderType("cutout");
//        models().withExistingParent(bottomName, modLoc("block/" + bottomName)).renderType("cutout");

        // Bind generated models to block states
        simpleBlock(ModBlocks.HUMAN_SKELETON_TOP_BLOCK.get(),
                models().getExistingFile(modLoc("block/" + topName)));
        simpleBlock(ModBlocks.HUMAN_SKELETON_BOTTOM_BLOCK.get(),
                models().getExistingFile(modLoc("block/" + bottomName)));

        // ----------------------
        // Optional: Rotatable blocks
        // ----------------------
        // If any RotatableBlock requires rotation/facing state, use getVariantBuilder(...)
        // Example:
        // getVariantBuilder(ModBlocks.HUMAN_SKELETON_TOP_BLOCK.get())
        //     .forAllStates(state -> ConfiguredModel.builder()
        //         .modelFile(models().getExistingFile(modLoc("block/" + topName)))
        //         .rotationY(calcRotationFromState(state))
        //         .build());
    }
}
