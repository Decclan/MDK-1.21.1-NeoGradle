package com.deimoshexxus.netherhexedkingdom.datagen;

import com.deimoshexxus.netherhexedkingdom.NetherHexedKingdom;
import com.deimoshexxus.netherhexedkingdom.content.ModBlocks;
import com.deimoshexxus.netherhexedkingdom.content.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {

    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {

        // Armor #####################################################################################

        // Chainmail Helmet
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.MILITUS_ALLOY_HELMET.get())
                .pattern("MMM")
                .pattern("M M")
                .define('M', ModItems.MILITUS_ALLOY_INGOT.get())
                .unlockedBy("has_militus_ingot", has(ModItems.MILITUS_ALLOY_INGOT.get()))
                .save(output);

        // Chainmail Chestplate
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.MILITUS_ALLOY_CHESTPLATE.get())
                .pattern("M M")
                .pattern("MMM")
                .pattern("MMM")
                .define('M', ModItems.MILITUS_ALLOY_INGOT.get())
                .unlockedBy("has_militus_ingot", has(ModItems.MILITUS_ALLOY_INGOT.get()))
                .save(output);

        // Chainmail Leggings
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.MILITUS_ALLOY_LEGGINGS.get())
                .pattern("MMM")
                .pattern("M M")
                .pattern("M M")
                .define('M', ModItems.MILITUS_ALLOY_INGOT.get())
                .unlockedBy("has_militus_ingot", has(ModItems.MILITUS_ALLOY_INGOT.get()))
                .save(output);

        // Chainmail Boots
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.MILITUS_ALLOY_BOOTS.get())
                .pattern("M M")
                .pattern("M M")
                .define('M', ModItems.MILITUS_ALLOY_INGOT.get())
                .unlockedBy("has_militus_ingot", has(ModItems.MILITUS_ALLOY_INGOT.get()))
                .save(output);


        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.MILITUS_ALLOY_HORSE_ARMOR.get())
                .pattern("M M")
                .pattern("MMM")
                .pattern("M M")
                .define('M', ModItems.MILITUS_ALLOY_INGOT.get())
                .unlockedBy("has_militus_ingot", has(ModItems.MILITUS_ALLOY_INGOT.get()))
                .save(output);

        // Blocks #####################################################################################

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.MILITUS_ALLOY_BLOCK.get())
                .pattern("III")
                .pattern("III")
                .pattern("III")
                .define('I', ModItems.MILITUS_ALLOY_INGOT)
                .unlockedBy("has_militus_ingot", has(ModItems.MILITUS_ALLOY_INGOT))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.IRON_PLATE_BLOCK.get())
                .pattern(" I ")
                .pattern("IWI")
                .pattern(" I ")
                .define('I', Items.IRON_NUGGET)
                .define('W', Blocks.CRIMSON_PLANKS)
                .unlockedBy("has_iron", has(Items.IRON_NUGGET))
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
                .unlockedBy("has_militus_nugget", has(ModItems.MILITUS_ALLOY_NUGGET))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.RED_GILDED_BRICKS_OF_LOST_TIME.get())
                .pattern(" N ")
                .pattern("NBN")
                .pattern(" N ")
                .define('N', ModItems.MILITUS_ALLOY_NUGGET)
                .define('B', Blocks.RED_NETHER_BRICKS)
                .unlockedBy("has_militus_nugget", has(ModItems.MILITUS_ALLOY_NUGGET))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.GILDED_BLACKSTONE_BRICKS_OF_LOST_TIME.get())
                .pattern(" N ")
                .pattern("NBN")
                .pattern(" N ")
                .define('N', ModItems.MILITUS_ALLOY_NUGGET)
                .define('B', Blocks.POLISHED_BLACKSTONE_BRICKS)
                .unlockedBy("has_militus_nugget", has(ModItems.MILITUS_ALLOY_NUGGET))
                .save(output);

        // Misc #####################################################################################

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.MILITUS_ALLOY_INGOT.get())
                .pattern("NNN")
                .pattern("NNN")
                .pattern("NNN")
                .define('N', ModItems.MILITUS_ALLOY_NUGGET.get())
                .unlockedBy("has_militus_nugget", has(ModItems.MILITUS_ALLOY_NUGGET.get()))
                .save(output, NetherHexedKingdom.MODID + ":militus_alloy_ingot_from_nuggets");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.MILITUS_ALLOY_INGOT.get(), 9)
                .requires(ModBlocks.MILITUS_ALLOY_BLOCK.get())
                .unlockedBy("has_militus_alloy_block", has(ModBlocks.MILITUS_ALLOY_BLOCK.get()))
                .save(output, NetherHexedKingdom.MODID + ":militus_alloy_ingot_from_block");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.MILITUS_ALLOY_NUGGET.get(), 9)
                .requires(ModItems.MILITUS_ALLOY_INGOT.get())
                .unlockedBy("has_militus_ingot", has(ModItems.MILITUS_ALLOY_INGOT.get()))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.IRON_NUGGET, 4)
                .requires(ModItems.IRON_CLUMP.get())
                .unlockedBy("has_iron", has(ModItems.IRON_CLUMP.get()))
                .save(output);


        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.NETHERITE_FRAGMENT_BUNDLE.get())
                .pattern("FF")
                .pattern("FF")
                .define('F', ModItems.NETHERITE_FRAGMENT)
                .unlockedBy("has_netherite_fragment", has(ModItems.NETHERITE_FRAGMENT))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.NETHERITE_SCRAP)
                .pattern("BBB")
                .pattern("BBB")
                .define('B', ModItems.NETHERITE_FRAGMENT_BUNDLE)
                .unlockedBy("has_netherite_fragment_bundle", has(ModItems.NETHERITE_FRAGMENT_BUNDLE))
                .save(output);

        // Smelting #####################################################################################

        SimpleCookingRecipeBuilder.smelting(
                        Ingredient.of(ModItems.NETHERITE_OXIDE.get()),
                        RecipeCategory.MISC,
                        ModItems.NETHERITE_FRAGMENT.get(),
                        0.7f,
                        640        // cooking time (32 seconds)
                )
                .unlockedBy("has_netherite_oxide", has(ModItems.NETHERITE_OXIDE.get()))
                .save(output, NetherHexedKingdom.MODID + ":netherite_fragment_from_smelting");

        SimpleCookingRecipeBuilder.blasting(
                        Ingredient.of(ModItems.NETHERITE_OXIDE.get()),
                        RecipeCategory.MISC,
                        ModItems.NETHERITE_FRAGMENT.get(),
                        0.8f,
                        320        // (16 seconds)
                )
                .unlockedBy("has_netherite_oxide", has(ModItems.NETHERITE_OXIDE.get()))
                .save(output, NetherHexedKingdom.MODID + ":netherite_fragment_from_blasting");

        SimpleCookingRecipeBuilder.smelting(
                        Ingredient.of(ModBlocks.IRON_PLATE_BLOCK.get()),
                        RecipeCategory.MISC,
                        ModItems.IRON_CLUMP.get(),
                        0.2f,
                        240        // (12 seconds)
                )
                .unlockedBy("has_iron", has(ModBlocks.IRON_PLATE_BLOCK.get()))
                .save(output, NetherHexedKingdom.MODID + ":iron_clump_from_smelting");

        SimpleCookingRecipeBuilder.blasting(
                        Ingredient.of(ModBlocks.IRON_PLATE_BLOCK.get()),
                        RecipeCategory.MISC,
                        ModItems.IRON_CLUMP.get(),
                        0.3f,
                        120        // (6 seconds)
                )
                .unlockedBy("has_iron", has(ModBlocks.IRON_PLATE_BLOCK.get()))
                .save(output, NetherHexedKingdom.MODID + ":iron_clump_from_blasting");

        SimpleCookingRecipeBuilder.smelting(
                        Ingredient.of(ModItems.IMPERIAL_COINS.get()),
                        RecipeCategory.MISC,
                        Items.GOLD_NUGGET,
                        0.1f,
                        120        // (6 seconds)
                )
                .unlockedBy("has_coins", has(ModItems.IMPERIAL_COINS.get()))
                .save(output, NetherHexedKingdom.MODID + ":coin_nugget_from_smelting");

        SimpleCookingRecipeBuilder.blasting(
                        Ingredient.of(ModBlocks.IRON_PLATE_BLOCK.get()),
                        RecipeCategory.MISC,
                        Items.GOLD_NUGGET,
                        0.2f,
                        60        // (3 seconds)
                )
                .unlockedBy("has_coins", has(ModItems.IMPERIAL_COINS.get()))
                .save(output, NetherHexedKingdom.MODID + ":coin_nugget_from_blasting");

    }
}
