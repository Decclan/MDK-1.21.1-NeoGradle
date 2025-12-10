package com.deimoshexxus.netherhexedkingdom;

import com.deimoshexxus.netherhexedkingdom.client.ModModelLayers;
import com.deimoshexxus.netherhexedkingdom.client.model.HexanGuardModel;
import com.deimoshexxus.netherhexedkingdom.content.ModBlocks;
import com.deimoshexxus.netherhexedkingdom.content.ModEntities;
import com.deimoshexxus.netherhexedkingdom.world.ModWorldFeatures;
import net.minecraft.client.renderer.RenderType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.neoforged.neoforge.common.world.BiomeModifiers;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = NetherHexedKingdom.MODID, dist = Dist.CLIENT)
// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid = NetherHexedKingdom.MODID, value = Dist.CLIENT)
public class NetherHexedKingdomClient {
    public NetherHexedKingdomClient(ModContainer container) {
        // Allows NeoForge to create a config screen for this mod's configs.
        // The config screen is accessed by going to the Mods screen > clicking on your mod > clicking on config.
        // Do not forget to add translations for your config options to the en_us.json file.
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        // Main model layer
        event.registerLayerDefinition(
                ModModelLayers.HEXAN_GUARD_BODY,
                HexanGuardModel::createBodyLayer
        );
        // Armor layers
        event.registerLayerDefinition(
                ModModelLayers.HEXAN_GUARD_ARMOR_INNER,
                () -> HexanGuardModel.createArmorLayer(0.5F)
        );

        event.registerLayerDefinition(
                ModModelLayers.HEXAN_GUARD_ARMOR_OUTER,
                () -> HexanGuardModel.createArmorLayer(1.0F)
        );

        event.registerLayerDefinition(
                ModModelLayers.GARGOYLE_POSSESSED,
                com.deimoshexxus.netherhexedkingdom.client.model.GargoylePossessedModel::createBodyLayer
        );
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            EntityRenderers.register(ModEntities.HEXED_ZOMBIE.get(),
                    com.deimoshexxus.netherhexedkingdom.client.renderer.HexedZombieRenderer::new);

            EntityRenderers.register(ModEntities.HEXED_ZOMBIE_HUSK.get(),
                    com.deimoshexxus.netherhexedkingdom.client.renderer.HexedZombieHuskRenderer::new);

            EntityRenderers.register(ModEntities.HEXAN_GUARD.get(),
                    com.deimoshexxus.netherhexedkingdom.client.renderer.HexanGuardRenderer::new);

            EntityRenderers.register(ModEntities.GARGOYLE_POSSESSED.get(),
                    com.deimoshexxus.netherhexedkingdom.client.renderer.GargoylePossessedRenderer::new);

            com.deimoshexxus.netherhexedkingdom.client.GasSourceAmbientHandler.register();

        });
    }
}
