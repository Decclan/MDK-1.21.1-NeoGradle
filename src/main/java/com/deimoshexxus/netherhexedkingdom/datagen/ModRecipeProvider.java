package com.deimoshexxus.netherhexedkingdom.datagen;

import com.deimoshexxus.netherhexedkingdom.content.ModBlocks;
import com.deimoshexxus.netherhexedkingdom.content.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {

    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        // Example recipe — craft a Militus Alloy Block from Iron and Gold
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.MILITUS_ALLOY_BLOCK.get())
                .pattern("III")
                .pattern("III")
                .pattern("III")
                .define('I', ModItems.MILITUS_ALLOY_INGOT)
                .unlockedBy("has_iron", has(Items.IRON_INGOT))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.IRON_PLATE_BLOCK.get())
                .pattern(" I ")
                .pattern("IWI")
                .pattern(" I ")
                .define('I', Items.IRON_NUGGET)
                .define('W', Blocks.CRIMSON_PLANKS)
                .unlockedBy("has_iron", has(Items.IRON_INGOT))
                .save(output);

//        SmithingTransformRecipeBuilder.smithing(template, base, addition, RecipeCategory.MISC, result)
//                .unlocks("criteria", criteria) // How the recipe is unlocked
//                .save(output, name);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.GILDED_BRICKS_OF_LOST_TIME.get())
                .pattern(" N ")
                .pattern("NBN")
                .pattern(" N ")
                .define('N', ModItems.MILITUS_ALLOY_NUGGET)
                .define('B', Blocks.NETHER_BRICKS)
                .unlockedBy("has_iron", has(Items.IRON_INGOT))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.RED_GILDED_BRICKS_OF_LOST_TIME.get())
                .pattern(" N ")
                .pattern("NBN")
                .pattern(" N ")
                .define('N', ModItems.MILITUS_ALLOY_NUGGET)
                .define('B', Blocks.RED_NETHER_BRICKS)
                .unlockedBy("has_iron", has(Items.IRON_INGOT))
                .save(output);

//        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.MILITUS_ALLOY_BLOCK.get())
//                .pattern("IGI")
//                .pattern("GIG")
//                .pattern("IGI")
//                .define('I', Items.IRON_INGOT)
//                .define('G', Items.GOLD_INGOT)
//                .unlockedBy("has_iron", has(Items.IRON_INGOT))
//                .save(output);

        // Example smelting recipe (if you have ores/items)
        // SimpleCookingRecipeBuilder.smelting(...)

    }
}
