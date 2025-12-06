package com.deimoshexxus.netherhexedkingdom.content;

import com.deimoshexxus.netherhexedkingdom.NetherHexedKingdom;
import com.deimoshexxus.netherhexedkingdom.content.custom.GasChildBlock;
import com.deimoshexxus.netherhexedkingdom.content.custom.HumanSkeletonBlock;
import com.deimoshexxus.netherhexedkingdom.content.custom.RotatableBlock;
import com.deimoshexxus.netherhexedkingdom.content.custom.GasSourceBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Central registry class for Nether Hexed Kingdom blocks and their block items.
 * Safe to register in the main mod constructor via ModBlocks.register(eventBus).
 */
public class ModBlocks {

    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(NetherHexedKingdom.MODID);
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(NetherHexedKingdom.MODID);

    // -------------------------
    // Metallic & Ore Blocks
    // -------------------------

    public static final DeferredBlock<Block> MILITUS_ALLOY_ORE = register("militus_alloy_ore",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .strength(16f, 13f)
                    .sound(SoundType.STONE)
                    .requiresCorrectToolForDrops()));

    public static final DeferredBlock<Block> MILITUS_ALLOY_BLOCK = register("militus_alloy_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(20f, 16f)
                    .sound(SoundType.METAL)
                    .requiresCorrectToolForDrops()));

    public static final DeferredBlock<Block> IRON_PLATE_BLOCK = register("iron_plate_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(10f, 8f)
                    .sound(SoundType.METAL)
                    .requiresCorrectToolForDrops()));

    // -------------------------
    // Decorative / Light Blocks
    // -------------------------

    public static final DeferredBlock<RotatedPillarBlock> ETERNAL_LIGHT_BLOCK = register("eternal_light_block",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_LIGHT_GRAY)
                    .strength(1f, 1f)
                    .sound(SoundType.GLASS)
                    .lightLevel(state -> 14)));

    // -------------------------
    // Skeleton Statues
    // -------------------------

    public static final DeferredBlock<HumanSkeletonBlock> HUMAN_SKELETON_TOP_BLOCK = register("human_skeleton_top_block",
            () -> new HumanSkeletonBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.CLAY)
                    .strength(1f, 1f)
                    .sound(SoundType.BONE_BLOCK)
                    .noOcclusion()));

    public static final DeferredBlock<HumanSkeletonBlock> HUMAN_SKELETON_BOTTOM_BLOCK = register("human_skeleton_bottom_block",
            () -> new HumanSkeletonBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.CLAY)
                    .strength(1f, 1f)
                    .sound(SoundType.BONE_BLOCK)
                    .noOcclusion()));

// -------------------------
// Gargoyle Statues
// -------------------------

    public static final DeferredBlock<RotatableBlock> GARGOYLE_GOLD_BLOCK = register("gargoyle_gold_block",
            () -> new RotatableBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.GOLD)
                    .strength(3f, 5f)
                    .lightLevel(state -> 3)
                    .sound(SoundType.METAL)
                    .noOcclusion()
                    .requiresCorrectToolForDrops()));


    public static final DeferredBlock<RotatableBlock> GARGOYLE_BLACKSTONE_BLOCK = register("gargoyle_blackstone_block",
            () -> new RotatableBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .strength(2f, 3f)
                    .sound(SoundType.STONE)
                    .noOcclusion()
                    .requiresCorrectToolForDrops()));

    public static final DeferredBlock<RotatableBlock> GARGOYLE_QUARTZ_BLOCK = register("gargoyle_quartz_block",
            () -> new RotatableBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.QUARTZ)
                    .strength(2f, 3f)
                    .sound(SoundType.STONE)
                    .noOcclusion()
                    .requiresCorrectToolForDrops()));

    public static final DeferredBlock<RotatableBlock> GARGOYLE_BASALT_BLOCK = register("gargoyle_basalt_block",
            () -> new RotatableBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GRAY)
                    .strength(2f, 3f)
                    .sound(SoundType.STONE)
                    .noOcclusion()
                    .requiresCorrectToolForDrops()));

    public static final DeferredBlock<RotatableBlock> GARGOYLE_OBSIDIAN_BLOCK = register("gargoyle_obsidian_block",
            () -> new RotatableBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLACK)
                    .strength(6f, 8f)
                    .sound(SoundType.STONE)
                    .noOcclusion()
                    .requiresCorrectToolForDrops()));

    public static final DeferredBlock<RotatableBlock> GARGOYLE_AMETHYST_BLOCK = register("gargoyle_amethyst_block",
            () -> new RotatableBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_MAGENTA)
                    .strength(2f, 3f)
                    .sound(SoundType.AMETHYST)
                    .noOcclusion()
                    .requiresCorrectToolForDrops()));


    // -------------------------
    // Specialty Blocks
    // -------------------------

    public static final DeferredBlock<Block> BLACKSTONE_FIRESTAND_BLOCK = register("blackstone_firestand_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .strength(2f, 6f)
                    .sound(SoundType.STONE)
                    .lightLevel(state -> 11)
                    .noOcclusion()
                    .requiresCorrectToolForDrops()));

    public static final DeferredBlock<Block> HEXAN_CHISELED_NETHER_BRICKS = register("hexan_chiseled_nether_bricks_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.NETHER)
                    .strength(5f, 10f)
                    .sound(SoundType.NETHER_BRICKS)
                    .requiresCorrectToolForDrops()));

    public static final DeferredBlock<Block> GILDED_BRICKS_OF_LOST_TIME = register("gilded_bricks_of_lost_time_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.GOLD)
                    .strength(32f, 15f)
                    .sound(SoundType.NETHER_BRICKS)
                    .requiresCorrectToolForDrops()));

    public static final DeferredBlock<Block> RED_GILDED_BRICKS_OF_LOST_TIME = register("red_gilded_bricks_of_lost_time_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_RED)
                    .strength(32f, 15f)
                    .sound(SoundType.NETHER_BRICKS)
                    .requiresCorrectToolForDrops()));

    public static final DeferredBlock<Block> HEXAN_CHISELED_POLISHED_BLACKSTONE = register("hexan_chiseled_polished_blackstone_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLACK)
                    .strength(4f, 8f)
                    .sound(SoundType.NETHER_BRICKS)
                    .requiresCorrectToolForDrops()));

    public static final DeferredBlock<Block> GAS_SOURCE = register("poison_gas_source",
            () -> new GasSourceBlock(BlockBehaviour.Properties.of()
                    .replaceable()
                    .noCollission()   // entities can move through
                    .noOcclusion()    // invisible for pathfinding, light
                    .strength(0.0F)
                    //.strength(-1.0F, 3600000.0F) // unbreakable by players
                    .randomTicks()    // schedule ticks if needed
                    .lightLevel(state -> 11)
            ));

    public static final DeferredBlock<Block> GAS_CHILD = register("poison_gas",
            () -> new GasChildBlock(BlockBehaviour.Properties.of()
                    .replaceable()
                    .noCollission()
                    .noOcclusion()
                    .strength(0.0F)
                    .randomTicks()
            ));

    // -------------------------
    // Registry Helpers
    // -------------------------

    private static <T extends Block> DeferredBlock<T> register(String name, java.util.function.Supplier<T> blockSupplier) {
        DeferredBlock<T> registeredBlock = BLOCKS.register(name, blockSupplier);
        ITEMS.register(name, () -> new BlockItem(registeredBlock.get(), new Item.Properties()));
        return registeredBlock;
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        ITEMS.register(eventBus);
    }
}
