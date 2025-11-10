package com.deimoshexxus.netherhexedkingdom.datagen;

import com.deimoshexxus.netherhexedkingdom.content.ModBlocks;
import com.deimoshexxus.netherhexedkingdom.content.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {

    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        // Example recipe — craft a Militus Alloy Block from Iron and Gold
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.MILITUS_ALLOY_BLOCK.get())
                .pattern("IGI")
                .pattern("GIG")
                .pattern("IGI")
                .define('I', Items.IRON_INGOT)
                .define('G', Items.GOLD_INGOT)
                .unlockedBy("has_iron", has(Items.IRON_INGOT))
                .save(output);

        // Example smelting recipe (if you have ores/items)
        // SimpleCookingRecipeBuilder.smelting(...)
    }
}
