package com.deimoshexxus.netherhexedkingdom;

import com.deimoshexxus.netherhexedkingdom.content.*;
import com.deimoshexxus.netherhexedkingdom.content.events.DecayInfectionEvents;
import com.deimoshexxus.netherhexedkingdom.content.events.ModEntitySpawnEvents;
import com.deimoshexxus.netherhexedkingdom.content.events.PiglinInfectionProgressEvents;
import com.deimoshexxus.netherhexedkingdom.content.events.ZombifiedPiglinDecayEvents;
import com.deimoshexxus.netherhexedkingdom.content.material.ModArmorMaterials;
import com.deimoshexxus.netherhexedkingdom.registry.ModCreativeTabs;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;


/**
 * Main mod class for Nether Hexed Kingdom
 * Matches META-INF/neoforge.mods.toml modId value.
 */
@Mod(NetherHexedKingdom.MODID)
public class NetherHexedKingdom {
    public static final String MODID = "netherhexedkingdom";
    public static final Logger LOGGER = LogUtils.getLogger();

    public NetherHexedKingdom(IEventBus modEventBus, ModContainer modContainer) {
        // register lifecycle listeners
        modEventBus.addListener(this::commonSetup);
        // Register the data generator listener
        modEventBus.addListener(com.deimoshexxus.netherhexedkingdom.datagen.DataGenerators::gatherData);

        modEventBus.addListener(ModEntitySpawnEvents::registerSpawnPlacements);

        // register content classes (these will call BLOCKS/ITEMS/CREATIVE_MODE_TABS behind the scenes)
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModItems.register(modEventBus);

        ModSounds.SOUNDS.register(modEventBus);
        // register entities deferred register
        ModEntities.register(modEventBus);
        ModArmorMaterials.MATERIALS.register(modEventBus);

        ModStructures.STRUCTURE_TYPES.register(modEventBus);
        ModStructurePieces.STRUCTURE_PIECES.register(modEventBus);
        // ModTemplatePools doesn't need to be registered

        // Register Events
        NeoForge.EVENT_BUS.register(DecayInfectionEvents.class);
        NeoForge.EVENT_BUS.register(PiglinInfectionProgressEvents.class);
        NeoForge.EVENT_BUS.register(ZombifiedPiglinDecayEvents.class);

        // register this class to NeoForge event bus for server / other events
        NeoForge.EVENT_BUS.register(this);

        // register config object
        modContainer.registerConfig(ModConfig.Type.COMMON, CommonConfig.SPEC);

        ModCreativeTabs.register(modEventBus);
        // optionally register additional listeners (client-only render setup is done in ModBlocks if needed)

    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("Common setup");
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("Server starting - Nether Hexed Kingdom");
    }
}
