package com.deimoshexxus.netherhexedkingdom.registry;

import com.deimoshexxus.netherhexedkingdom.NetherHexedKingdom;
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
            DeferredRegister.create(net.minecraft.core.registries.Registries.CREATIVE_MODE_TAB, NetherHexedKingdom.MODID);

    // Your main mod creative tab
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> NETHER_HEXED_TAB =
            CREATIVE_TABS.register("nether_hexed_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup." + NetherHexedKingdom.MODID)) // e.g. "Nether Hexed Kingdom"
                    .withTabsBefore(CreativeModeTabs.COMBAT)
                    .icon(() -> ModItems.MILITUS_ALLOY_HELMET.get().getDefaultInstance()) // Placeholder item
                    .displayItems((parameters, output) -> {
                        // Items
                        output.accept(ModItems.MILITUS_ALLOY_INGOT.get());
                        output.accept(ModItems.MILITUS_ALLOY_NUGGET.get());
                        output.accept(ModItems.IRON_CLUMP.get());
                        output.accept(ModItems.NETHERITE_FRAGMENT_BUNDLE.get());
                        output.accept(ModItems.NETHERITE_FRAGMENT.get());
                        output.accept(ModItems.NETHERITE_OXIDE.get());
                        output.accept(ModItems.IMPERIAL_COINS.get());
                        output.accept(ModItems.HEXED_ZOMBIE_SPAWN_EGG.get());
                        output.accept(ModItems.HEXED_ZOMBIE_HUSK_SPAWN_EGG.get());
                        output.accept(ModItems.HEXAN_GUARD_SPAWN_EGG.get());
                        output.accept(ModItems.GARGOYLE_POSSESSED_SPAWN_EGG.get());
                        output.accept(ModItems.MILITUS_ALLOY_HELMET.get());
                        output.accept(ModItems.MILITUS_ALLOY_CHESTPLATE.get());
                        output.accept(ModItems.MILITUS_ALLOY_LEGGINGS.get());
                        output.accept(ModItems.MILITUS_ALLOY_BOOTS.get());
                        output.accept(ModItems.MILITUS_ALLOY_HORSE_ARMOR.get());
                        output.accept(ModItems.POISON_GAS_ITEM.get());
                        output.accept(ModItems.LINGZHI_MUSHROOM_ITEM.get());
                        output.accept(ModItems.MASONIAE_MUSHROOM_ITEM.get());
                        //output.accept(ModItems.LINGZHI_MUSHROOM_FOOD.get());


                        // Blocks
                        output.accept(ModBlocks.MILITUS_ALLOY_BLOCK.get());
                        output.accept(ModBlocks.MILITUS_ALLOY_ORE.get());
                        output.accept(ModBlocks.GILDED_BRICKS_OF_LOST_TIME.get());
                        output.accept(ModBlocks.RED_GILDED_BRICKS_OF_LOST_TIME.get());
                        output.accept(ModBlocks.ETERNAL_LIGHT_BLOCK.get());
                        output.accept(ModBlocks.HUMAN_SKELETON_TOP_BLOCK.get());
                        output.accept(ModBlocks.HUMAN_SKELETON_BOTTOM_BLOCK.get());
                        output.accept(ModBlocks.BLACKSTONE_FIRESTAND_BLOCK.get());
                        output.accept(ModBlocks.HEXAN_CHISELED_NETHER_BRICKS.get());
                        output.accept(ModBlocks.HEXAN_CHISELED_POLISHED_BLACKSTONE.get());
                        output.accept(ModBlocks.IRON_PLATE_BLOCK.get());
                        output.accept(ModBlocks.GARGOYLE_BASALT_BLOCK.get());
                        output.accept(ModBlocks.GARGOYLE_BLACKSTONE_BLOCK.get());
                        output.accept(ModBlocks.GARGOYLE_QUARTZ_BLOCK.get());
                        output.accept(ModBlocks.GARGOYLE_GOLD_BLOCK.get());
                        output.accept(ModBlocks.GARGOYLE_OBSIDIAN_BLOCK.get());
                        output.accept(ModBlocks.GARGOYLE_AMETHYST_BLOCK.get());
                        //output.accept(ModBlocks.MASONIAE_MUSHROOM.get());
                        //output.accept(ModBlocks.LINGZHI_MUSHROOM.get());
                        //output.accept(ModBlocks.GAS_SOURCE.get());
                        // add future blocks/items here
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_TABS.register(eventBus);
    }
}
