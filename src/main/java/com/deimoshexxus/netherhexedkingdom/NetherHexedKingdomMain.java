package com.deimoshexxus.netherhexedkingdom;

import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import com.deimoshexxus.netherhexedkingdom.content.ModBlocks;
import com.deimoshexxus.netherhexedkingdom.content.ModItems;
import com.deimoshexxus.netherhexedkingdom.registry.ModCreativeTabs;

/**
 * Main mod class for Nether Hexed Kingdom
 * Matches META-INF/neoforge.mods.toml modId value.
 */
@Mod(NetherHexedKingdomMain.MODID)
public class NetherHexedKingdomMain {
    public static final String MODID = "netherhexedkingdom";
    public static final Logger LOGGER = LogUtils.getLogger();

    /* Deferred registers (kept here so content classes can reference them) */
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public NetherHexedKingdomMain(IEventBus modEventBus, ModContainer modContainer) {
        // register lifecycle listeners
        modEventBus.addListener(this::commonSetup);

        // register our content's deferred registers to the mod event bus
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);

        // register content classes (these will call BLOCKS/ITEMS/CREATIVE_MODE_TABS behind the scenes)
        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        ModCreativeTabs.register(modEventBus);

        // register this class to NeoForge event bus for server / other events
        NeoForge.EVENT_BUS.register(this);

        // register config object (if present)
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        // optionally register additional listeners (client-only render setup is done in ModBlocks if needed)
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("Nether Hexed Kingdom common setup");

        if (Config.LOG_DIRT_BLOCK.getAsBoolean()) {
            LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));
        }

        LOGGER.info("{}{}", Config.MAGIC_NUMBER_INTRODUCTION.get(), Config.MAGIC_NUMBER.getAsInt());
        Config.ITEM_STRINGS.get().forEach((item) -> LOGGER.info("ITEM >> {}", item));
    }

    // Example: add items to an existing creative tab (if you need to)
    private void addToVanillaCreativeTabs(BuildCreativeModeTabContentsEvent event) {
        // sample: put a specific item into building blocks tab
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            // example: event.accept(ModItems.MILITUS_ALLOY_INGOT.get()); // if using RegistryObject style
        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("Server starting - Nether Hexed Kingdom");
    }
}
