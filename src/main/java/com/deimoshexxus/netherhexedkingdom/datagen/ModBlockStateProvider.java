package com.deimoshexxus.netherhexedkingdom.datagen;

import com.deimoshexxus.netherhexedkingdom.NetherHexedKingdom;
import com.deimoshexxus.netherhexedkingdom.content.ModBlocks;
import com.deimoshexxus.netherhexedkingdom.content.custom.blocks.BracketFungusBlock;
import com.deimoshexxus.netherhexedkingdom.content.custom.blocks.RotatableBlock;
import com.deimoshexxus.netherhexedkingdom.content.custom.blocks.GasSourceBlock;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelBuilder;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.ModelBuilder;

// JSON
import com.google.gson.JsonObject;

// Utility: to save .mcmeta files
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;



public class ModBlockStateProvider extends BlockStateProvider {

    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, NetherHexedKingdom.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        System.out.println(">>> Nether Hexed Kingdom blockstate datagen running");

        // ----------------------
        // Simple cube blocks
        // ----------------------
        simpleBlockWithItem(ModBlocks.MILITUS_ALLOY_ORE.get(), cubeAll(ModBlocks.MILITUS_ALLOY_ORE.get()));
        simpleBlockWithItem(ModBlocks.MILITUS_ALLOY_BLOCK.get(), cubeAll(ModBlocks.MILITUS_ALLOY_BLOCK.get()));
        simpleBlockWithItem(ModBlocks.IRON_PLATE_BLOCK.get(), cubeAll(ModBlocks.IRON_PLATE_BLOCK.get()));
        simpleBlockWithItem(ModBlocks.GILDED_BRICKS_OF_LOST_TIME.get(), cubeAll(ModBlocks.GILDED_BRICKS_OF_LOST_TIME.get()));
        simpleBlockWithItem(ModBlocks.RED_GILDED_BRICKS_OF_LOST_TIME.get(), cubeAll(ModBlocks.RED_GILDED_BRICKS_OF_LOST_TIME.get()));
        simpleBlockWithItem(ModBlocks.HEXAN_CHISELED_NETHER_BRICKS.get(), cubeAll(ModBlocks.HEXAN_CHISELED_NETHER_BRICKS.get()));
        simpleBlockWithItem(ModBlocks.HEXAN_CHISELED_POLISHED_BLACKSTONE.get(), cubeAll(ModBlocks.HEXAN_CHISELED_POLISHED_BLACKSTONE.get()));
        generateGasBlock(ModBlocks.GAS_SOURCE.get(), "poison_gas_source");
        generateGasBlock(ModBlocks.GAS_CHILD.get(), "poison_gas");

        // ----------------------
        // Vegetation
        // ----------------------
        generateMushroom(ModBlocks.MASONIAE_MUSHROOM.get(), "masoniae_mushroom");
        generateBracketFungus(ModBlocks.LINGZHI_MUSHROOM.get(), "lingzhi_mushroom", 3);
        generateGlowingMushroom(ModBlocks.SOULGLOW_MUSHROOM.get(), "soul_glow_mushroom");

        // ----------------------
        // Pillar block
        // ----------------------
        axisBlock(ModBlocks.ETERNAL_LIGHT_BLOCK.get(),
                modLoc("block/eternal_light_block_side"),
                modLoc("block/eternal_light_block_end"));

        // generate item model for the block
        simpleBlockItem(ModBlocks.ETERNAL_LIGHT_BLOCK.get(),
                models().cubeBottomTop("eternal_light_block",
                        modLoc("block/eternal_light_block_side"),
                        modLoc("block/eternal_light_block_side"),
                        modLoc("block/eternal_light_block_end")));

        // ----------------------
        // Custom manual blocks
        // ----------------------
        // Use pre-existing JSON, do not generate
        simpleBlock(ModBlocks.BLACKSTONE_FIRESTAND_BLOCK.get(),
                models().getExistingFile(modLoc("block/blackstone_firestand_block")));

        // ----------------------
        // Gargoyles (Originally South Facing)
        // ----------------------

        // Horizontal rotatable gargoyle statues
        horizontalRotatableBlockInverted(ModBlocks.GARGOYLE_GOLD_BLOCK.get(), "gargoyle_gold_block");
        horizontalRotatableBlockInverted(ModBlocks.GARGOYLE_BLACKSTONE_BLOCK.get(), "gargoyle_blackstone_block");
        horizontalRotatableBlockInverted(ModBlocks.GARGOYLE_QUARTZ_BLOCK.get(), "gargoyle_quartz_block");
        horizontalRotatableBlockInverted(ModBlocks.GARGOYLE_BASALT_BLOCK.get(), "gargoyle_basalt_block");
        horizontalRotatableBlockInverted(ModBlocks.GARGOYLE_OBSIDIAN_BLOCK.get(), "gargoyle_obsidian_block");
        horizontalRotatableBlockInverted(ModBlocks.GARGOYLE_AMETHYST_BLOCK.get(), "gargoyle_amethyst_block");

        // Horizontal rotatable skeleton halves
        horizontalRotatableBlockInverted(ModBlocks.HUMAN_SKELETON_TOP_BLOCK.get(), "human_skeleton_top_block");
        horizontalRotatableBlockInverted(ModBlocks.HUMAN_SKELETON_BOTTOM_BLOCK.get(), "human_skeleton_bottom_block");



    }

    // ----------------------
    // Helpers
    // ----------------------

    private void horizontalRotatableBlockInverted(Block block, String modelName) {
        // For models that are facing SOUTH in Blockbench by default.
        getVariantBuilder(block).forAllStates(state -> {
            Direction dir = state.getValue(RotatableBlock.HORIZONTAL_FACING);
            int yRot;

            // Invert direction: north ↔ south, east ↔ west
            switch (dir) {
                case NORTH: yRot = 180; break; // normally 0
                case SOUTH: yRot = 0;   break; // normally 180
                case WEST:  yRot = 90; break; // normally 90
                case EAST:  yRot = 270;  break; // normally 270
                default:    yRot = 0;
            }

            return ConfiguredModel.builder()
                    .modelFile(models().getExistingFile(modLoc("block/" + modelName)))
                    .rotationY(yRot)
                    .build();
        });
        // Also generate the matching item model
        simpleBlockItem(block, models().getExistingFile(modLoc("block/" + modelName)));
    }

    private void generateGasBlock(Block block, String name) {
        getVariantBuilder(block).forAllStates(state -> {
            int distance = state.getValue(GasSourceBlock.DISTANCE);

            // Use cubeAll so the model has geometry (a cube) and is not invisible.
            return ConfiguredModel.builder()
                    .modelFile(models()
                            .cubeAll(name + "_" + distance, modLoc("block/poison_gas_" + distance))
                            .renderType("minecraft:translucent") // adds "render_type":"minecraft:translucent"
                    )
                    .build();
        });
    }

    private void generateMushroom(Block block, String name) {
        // Blockstate: simple plant-like “cross”
        simpleBlock(block,
                models().cross(name, modLoc("block/" + name)).renderType("minecraft:cutout_mipped"));

        // Item model: also uses cross
        simpleBlockItem(block,
                models().cross(name, modLoc("block/" + name))
                        .renderType("minecraft:cutout")); // optional; item models usually handle this
    }

    private void generateGlowingMushroom(Block block, String name) {
        // 1) Base model (try to reuse an existing model file if present)
        ModelFile baseModel;
        try {
            baseModel = models().getExistingFile(modLoc("block/" + name));
        } catch (Exception ignored) {
            // Fallback: create a simple cross model that points to the base texture
            baseModel = models().withExistingParent(name, mcLoc("block/cross"))
                    .texture("cross", modLoc("block/" + name));
        }

        // Register base blockstate + item model
        simpleBlock(block, baseModel);
        simpleBlockItem(block, baseModel);

        // 2) Attempt to create the glow model that references the emissive texture.
        //    ModelBuilder.texture(...) may throw IllegalArgumentException in datagen if it
        //    can't find the PNG in the datagen resource context. Catch that and fall back.
        ModelFile glowModel;
        String emissiveTextureName = "block/" + name + "_emissive"; // modid:block/<name>_emissive

        try {
            glowModel = models()
                    .withExistingParent(name + "_glow", mcLoc("block/cross"))
                    .texture("cross", modLoc(emissiveTextureName));
            // Generate an item model variant too if you'd like (optional)
            simpleBlockItem(block, glowModel);
            NetherHexedKingdom.LOGGER.info("[Datagen] Generated glow model referencing '{}'", emissiveTextureName);
        } catch (IllegalArgumentException ex) {
            // Datagen couldn't validate the emissive PNG. Fall back safely to base texture
            NetherHexedKingdom.LOGGER.warn("[Datagen] emissive texture '{}' not visible to datagen; generating fallback glow model that uses base texture.",
                    emissiveTextureName);

            // Fallback glow model uses the base texture (so datagen will succeed)
            glowModel = models()
                    .withExistingParent(name + "_glow", mcLoc("block/cross"))
                    .texture("cross", modLoc("block/" + name));

            // Create the fallback item model as well (optional)
            simpleBlockItem(block, glowModel);
        }

        // 3) IMPORTANT: do NOT overwrite the blockstate with the glow model here unless you want
        //    the block in-world to use the glow texture. We only generate the glow model JSON so
        //    your renderer can load it as a standalone model at runtime.
        //
        // If you *do* want the block to use the glow model in-world, you could call:
        // simpleBlock(block, glowModel);
        //
        // But typically we keep the baseModel as the blockstate and let the renderer render the glow
        // overlay using the standalone model (registered in ModelEvent.RegisterAdditional).
    }



//    private void generateGlowingMushroom(Block block, String name) {
//        // Use NeoForge model builder to generate the JSON automatically
//        ModelFile modelFile = models()
//                .withExistingParent(name, mcLoc("block/cross"))
//                .texture("cross", modLoc("block/" + name))
//                .renderType("minecraft:cutout_mipped");
//
//        // Generate blockstate JSON pointing to the model
//        simpleBlock(block, modelFile);
//
//        // Generate item model referencing the same model
//        simpleBlockItem(block, modelFile);
//    }

    private void generateBracketFungus(Block block, String name, int stages) {

        for (int i = 0; i < stages; i++) {

            models()
                    .withExistingParent(name + "_stage" + i, "minecraft:block/block")
                    .renderType("minecraft:cutout")

                    // Main texture
                    .texture("texture",  modLoc("block/" + name + "_stage" + i))
                    .texture("particle", "#texture")

                    .element()
                    .from(0, 4, 0)
                    .to(16, 4.01F, 16)

                    .face(Direction.UP)
                    .texture("#texture")
                    .uvs(0, 0, 16, 16)
                    .end()

                    .face(Direction.DOWN)
                    .texture("#texture")
                    .uvs(0, 0, 16, 16)
                    .rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN)
                    .end()
                    .end();
        }

        // Blockstate rotation based on FACING
        getVariantBuilder(block).forAllStates(state -> {
            int age = state.getValue(BracketFungusBlock.AGE);
            Direction dir = state.getValue(BracketFungusBlock.FACING);

            int yRot = switch (dir) {
                case SOUTH -> 0;
                case WEST  -> 90;
                case NORTH -> 180;
                case EAST  -> 270;
                default -> 0;
            };

            return ConfiguredModel.builder()
                    .modelFile(models().getExistingFile(modLoc("block/" + name + "_stage" + age)))
                    .rotationY(yRot)
                    .build();
        });
    }


}
