//package com.deimoshexxus.netherhexedkingdom.client;
//
//import com.deimoshexxus.netherhexedkingdom.NetherHexedKingdom;
//import net.minecraft.client.Minecraft;
//import net.neoforged.api.distmarker.Dist;
//import net.neoforged.bus.api.SubscribeEvent;
//import net.neoforged.fml.common.EventBusSubscriber;
//import net.neoforged.neoforge.client.event.ModelEvent;
//
//@EventBusSubscriber(modid = NetherHexedKingdom.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
//public class ClientModelDebug {
//
//    @SubscribeEvent
//    public static void onModelBakeCompleted(ModelEvent.BakingCompleted event) {
//
//        Minecraft.getInstance().getModelManager().getModel().keySet().forEach(k -> {
//            if (k.toString().contains("glow")) {
//                NetherHexedKingdom.LOGGER.info("Loaded model key: {}", k);
//            }
//        });
//    }
//}
