package com.deimoshexxus.netherhexedkingdom.datagen;

import com.deimoshexxus.netherhexedkingdom.NetherHexedKingdom;
import com.deimoshexxus.netherhexedkingdom.content.ModBlocks;
import com.deimoshexxus.netherhexedkingdom.content.custom.RotatableBlock;
import com.deimoshexxus.netherhexedkingdom.content.custom.GasSourceBlock;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

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
        // BlockBench-generated models
        // ----------------------
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

    private void horizontalRotatableBlock(Block block, String modelName) {
        // Use the Variant builder directly and rotate by Y depending on horizontal facing
        getVariantBuilder(block).forAllStates(state -> {
            Direction dir = state.getValue(RotatableBlock.HORIZONTAL_FACING);
            int yRot;
            switch (dir) {
                case SOUTH: yRot = 180; break;
                case WEST:  yRot = 90;  break;
                case EAST:  yRot = 270; break;
                default:    yRot = 0;   // NORTH
            }
            return ConfiguredModel.builder()
                    .modelFile(models().getExistingFile(modLoc("block/" + modelName)))
                    .rotationY(yRot)
                    .build();
        });

        // also generate a simple item model that references the block model
        // this creates models/item/<name>.json that points to models/block/<name>
        simpleBlockItem(block, models().getExistingFile(modLoc("block/" + modelName)));
    }

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

    private void generateGasBlock(Block block, String baseName) {
        getVariantBuilder(block).forAllStates(state -> {
            int distance = state.getValue(GasSourceBlock.DISTANCE);

            // Use cubeAll so the model has geometry (a cube) and is not invisible.
            return ConfiguredModel.builder()
                    .modelFile(models()
                            .cubeAll(baseName + "_" + distance, modLoc("block/poison_gas_" + distance))
                            .renderType("minecraft:translucent") // adds "render_type":"minecraft:translucent"
                    )
                    .build();
        });
    }


}
