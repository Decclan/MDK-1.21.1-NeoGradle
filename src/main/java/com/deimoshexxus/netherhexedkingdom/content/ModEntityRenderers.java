package com.deimoshexxus.netherhexedkingdom.content;

import com.deimoshexxus.netherhexedkingdom.NetherHexedKingdom;
import com.deimoshexxus.netherhexedkingdom.client.renderer.*;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = NetherHexedKingdom.MODID, value = Dist.CLIENT)
public class ModEntityRenderers {

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.DECAYED_ZOMBIE.get(), DecayedZombieRenderer::new);
        event.registerEntityRenderer(ModEntities.DECAYED_ZOMBIE_HUSK.get(), DecayedZombieHuskRenderer::new);
        event.registerEntityRenderer(ModEntities.DECAYED_ZOMBIFIED_PIGLIN.get(), DecayedZombifiedPiglinRenderer::new);
        event.registerEntityRenderer(ModEntities.HEXAN_GUARD.get(), HexanGuardRenderer::new);
        event.registerEntityRenderer(ModEntities.GARGOYLE_POSSESSED.get(), GargoylePossessedRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.SOULGLOW_MUSHROOM.get(), SoulGlowMushroomRenderer::new);
        event.registerEntityRenderer(ModEntities.GRENADE.get(), GrenadeRenderer::new);
        event.registerEntityRenderer(ModEntities.NETHER_PORTAL_ORB.get(), NetherPortalOrbRenderer::new);
        event.registerEntityRenderer(ModEntities.GARGOYLE_SPIT.get(), GargoyleSpitRenderer::new);
    }
}

