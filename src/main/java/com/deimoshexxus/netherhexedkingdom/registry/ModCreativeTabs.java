package com.deimoshexxus.netherhexedkingdom.registry;

import com.deimoshexxus.netherhexedkingdom.NetherHexedKingdomMain;
import com.deimoshexxus.netherhexedkingdom.content.ModBlocks;
import com.deimoshexxus.netherhexedkingdom.content.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(net.minecraft.core.registries.Registries.CREATIVE_MODE_TAB, NetherHexedKingdomMain.MODID);

    // Your main mod creative tab
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> NETHER_HEXED_TAB =
            CREATIVE_TABS.register("nether_hexed_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup." + NetherHexedKingdomMain.MODID)) // e.g. "Nether Hexed Kingdom"
                    .withTabsBefore(CreativeModeTabs.COMBAT)
                    .icon(() -> ModItems.MILITUS_ALLOY_INGOT.get().getDefaultInstance()) // Placeholder item
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.MILITUS_ALLOY_INGOT.get());
                        output.accept(ModBlocks.MILITUS_ALLOY_BLOCK.get());
                        // add future blocks/items here
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_TABS.register(eventBus);
    }
}
