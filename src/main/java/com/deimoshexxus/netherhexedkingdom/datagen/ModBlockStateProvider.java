package com.deimoshexxus.netherhexedkingdom.datagen;

import com.deimoshexxus.netherhexedkingdom.NetherHexedKingdomMain;
import com.deimoshexxus.netherhexedkingdom.content.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider {

    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, NetherHexedKingdomMain.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        // Simple cube blocks
        simpleBlockWithItem(ModBlocks.MILITUS_ALLOY_ORE.get(), cubeAll(ModBlocks.MILITUS_ALLOY_ORE.get()));
        simpleBlockWithItem(ModBlocks.MILITUS_ALLOY_BLOCK.get(), cubeAll(ModBlocks.MILITUS_ALLOY_BLOCK.get()));
        simpleBlockWithItem(ModBlocks.IRON_PLATE_BLOCK.get(), cubeAll(ModBlocks.IRON_PLATE_BLOCK.get()));

        // Eternal light (pillar) block
        // Prefer axisBlockWithRenderType if present. If not, use axisBlock (pillar) helper.
        try {
            // many MDKs expose axisBlockWithRenderType(block, "cutout")
            axisBlockWithRenderType(ModBlocks.ETERNAL_LIGHT_BLOCK.get(), "cutout");
        } catch (NoSuchMethodError ex) {
            // fallback to the axisBlock helper (no explicit render type)
            axisBlock((RotatedPillarBlock) ModBlocks.ETERNAL_LIGHT_BLOCK.get());
        }

        /*
         * Skeleton blocks: these are custom blockbench models (non-cube).
         * Approach:
         *  - create a generated model which uses your existing Blockbench file as the parent
         *    and set renderType("cutout") on the generated model builder
         *  - point the blockstate to the generated model
         *
         * This avoids calling ResourceLocation constructors directly (we use modLoc helper).
         */

        // name strings (model filenames)
        String topName = "human_skeleton_top_block";
        String bottomName = "human_skeleton_bottom_block";

        // create a generated model that uses the Blockbench model as its parent, and set cutout
        // withExistingParent(String modelName, ResourceLocation parent)
        // note: modLoc("block/<name>") returns a ResourceLocation for "modid:block/<name>"
        models().withExistingParent(topName, modLoc("block/" + topName)).renderType("cutout");
        models().withExistingParent(bottomName, modLoc("block/" + bottomName)).renderType("cutout");

        // Use the generated model in the blockstate for each block:
        // simpleBlock(...) will write a blockstate pointing at the model name we created above.
        simpleBlock(ModBlocks.HUMAN_SKELETON_TOP_BLOCK.get(), models().getExistingFile(modLoc("block/" + topName)));
        simpleBlock(ModBlocks.HUMAN_SKELETON_BOTTOM_BLOCK.get(), models().getExistingFile(modLoc("block/" + bottomName)));

        // If your RotatableBlock uses rotation/facing state (not just a single model),
        // you can instead use getVariantBuilder(...) to supply rotated model variants per state.
        // Example skeleton rotation (if your block exposes a FACING or ROTATION property):
        // getVariantBuilder(ModBlocks.HUMAN_SKELETON_TOP_BLOCK.get())
        //     .forAllStates(state -> ConfiguredModel.builder()
        //         .modelFile(models().getExistingFile(modLoc("block/" + topName)))
        //         .rotationY(calcRotationFromState(state))
        //         .build());
    }
}
