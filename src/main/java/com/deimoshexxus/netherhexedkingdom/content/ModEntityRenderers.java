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
        event.registerEntityRenderer(ModEntities.HEXED_ZOMBIE.get(), HexedZombieRenderer::new);
        event.registerEntityRenderer(ModEntities.HEXED_ZOMBIE_HUSK.get(), HexedZombieHuskRenderer::new);
        event.registerEntityRenderer(ModEntities.HEXAN_GUARD.get(), HexanGuardRenderer::new);
        event.registerEntityRenderer(ModEntities.GRENADE.get(), GrenadeRenderer::new);
        event.registerEntityRenderer(ModEntities.GARGOYLE_POSSESSED.get(), GargoylePossessedRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.SOULGLOW_MUSHROOM.get(), SoulGlowMushroomRenderer::new);
    }
}

