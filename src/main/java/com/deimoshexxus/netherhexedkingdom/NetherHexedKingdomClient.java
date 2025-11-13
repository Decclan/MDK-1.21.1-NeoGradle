package com.deimoshexxus.netherhexedkingdom;

import com.deimoshexxus.netherhexedkingdom.content.ModEntities;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.minecraft.client.renderer.entity.EntityRenderers;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = NetherHexedKingdomMain.MODID, dist = Dist.CLIENT)
// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid = NetherHexedKingdomMain.MODID, value = Dist.CLIENT)
public class NetherHexedKingdomClient {
    public NetherHexedKingdomClient(ModContainer container) {
        // Allows NeoForge to create a config screen for this mod's configs.
        // The config screen is accessed by going to the Mods screen > clicking on your mod > clicking on config.
        // Do not forget to add translations for your config options to the en_us.json file.
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        // Some client setup code
        NetherHexedKingdomMain.LOGGER.info("HELLO FROM CLIENT SETUP");
        NetherHexedKingdomMain.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        event.enqueueWork(() -> {
            EntityRenderers.register(ModEntities.HEXED_ZOMBIE.get(), com.deimoshexxus.netherhexedkingdom.client.renderer.HexedZombieRenderer::new);
        });
    }
}
