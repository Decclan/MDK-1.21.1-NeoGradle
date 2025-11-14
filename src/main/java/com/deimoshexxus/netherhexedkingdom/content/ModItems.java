package com.deimoshexxus.netherhexedkingdom.content;

import com.deimoshexxus.netherhexedkingdom.NetherHexedKingdomMain;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.SpawnEggItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Registers all standalone items for Nether Hexed Kingdom.
 * Tools, armor, and other custom items will be added later.
 */
public class ModItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(NetherHexedKingdomMain.MODID);

    // --- Ores, Metals & Fragments ---

    public static final DeferredItem<Item> MILITUS_ALLOY_INGOT = registerItem("militus_alloy_ingot");
    public static final DeferredItem<Item> MILITUS_ALLOY_NUGGET = registerItem("militus_alloy_nugget");
    public static final DeferredItem<Item> IRON_CLUMP = registerItem("iron_clump");
    public static final DeferredItem<Item> NETHERITE_FRAGMENT_BUNDLE = registerItem("netherite_fragment_bundle");
    public static final DeferredItem<Item> NETHERITE_FRAGMENT = registerItem("netherite_fragment");
    public static final DeferredItem<Item> NETHERITE_OXIDE = registerItem("netherite_oxide");
    public static final DeferredItem<Item> IMPERIAL_COINS = registerItem("imperial_coins");

//    public static final DeferredItem<Item> ANCIENT_FRAGMENT = registerItem("ancient_fragment");
//    public static final DeferredItem<Item> ANCIENT_CORE = registerItem("ancient_core");
//
//    public static final DeferredItem<Item> SOUL_INFUSED_SHARD = registerItem("soul_infused_shard");
//    public static final DeferredItem<Item> HEXAN_CRYSTAL = registerItem("hexan_crystal");
//
//    // --- Magical & Alchemical Materials ---
//
//    public static final DeferredItem<Item> VOID_ESSENCE = registerItem("void_essence");
//    public static final DeferredItem<Item> NETHER_EMBER = registerItem("nether_ember");
//    public static final DeferredItem<Item> RUBRUM_CATALYST = registerItem("rubrum_catalyst");
//    public static final DeferredItem<Item> BLOOD_INFUSED_GEM = registerItem("blood_infused_gem");
//
//    // --- Relics & Artifacts ---
//
//    public static final DeferredItem<Item> RELIC_OF_THE_DEEP = registerItem("relic_of_the_deep");
//    public static final DeferredItem<Item> RELIC_OF_FIRE = registerItem("relic_of_fire");
//    public static final DeferredItem<Item> RELIC_OF_TIME = registerItem("relic_of_time");
//    public static final DeferredItem<Item> FORBIDDEN_BOOK_OF_SCORCHED_HEARTS = registerItem("forbidden_book_of_scorched_hearts");
//
//    // --- Basic Consumables (example) ---
//
//    public static final DeferredItem<Item> ADUSTUS_FRUIT = ITEMS.register("adustus_fruit",
//            () -> new Item(new Item.Properties()
//                    .food(new FoodProperties.Builder()
//                            .nutrition(4)
//                            .saturationModifier(0.4f)
//                            .alwaysEdible()
//                            .build())
//                    .stacksTo(64)));
//
//    // --- Placeholder for Future Equipment ---

    // public static final DeferredItem<Item> MILITUS_SWORD = ITEMS.register("militus_sword", () -> new SwordItem(...));
    // public static final DeferredItem<Item> HEXAN_ARMOR_HELMET = ITEMS.register("hexan_helmet", () -> new ArmorItem(...));
    // (Will be added after custom tool/armor tiers are defined)

    // redundant?
    public static final DeferredItem<Item> ETERNAL_LIGHT_BLOCK_ITEM =
            ITEMS.register("eternal_light_block",
                    () -> new BlockItem(ModBlocks.ETERNAL_LIGHT_BLOCK.get(), new Item.Properties()));

    // --- Entity Eggs ---

    public static final DeferredHolder<Item, SpawnEggItem> HEXED_ZOMBIE_SPAWN_EGG =
            ITEMS.register("hexed_zombie_spawn_egg", () ->
                    new SpawnEggItem(
                            ModEntities.HEXED_ZOMBIE.get(),
                            0x4B503D,
                            0x8A9C88,
                            new Item.Properties()
                    )
            );

    public static final DeferredHolder<Item, SpawnEggItem> HEXED_ZOMBIE_HUSK_SPAWN_EGG =
            ITEMS.register("hexed_zombie_husk_spawn_egg", () ->
                    new SpawnEggItem(
                            ModEntities.HEXED_ZOMBIE_HUSK.get(),
                            0x4B503D,
                            0x8A9C88,
                            new Item.Properties()
                    )
            );

    public static final DeferredHolder<Item, SpawnEggItem> HEXAN_GUARD_SPAWN_EGG =
            ITEMS.register("hexan_guard_spawn_egg", () ->
                    new SpawnEggItem(
                            ModEntities.HEXAN_GUARD.get(),
                            0x4B503D,
                            0x8A9C88,
                            new Item.Properties()
                    )
            );


    // --- Registry Helper ---

    private static DeferredItem<Item> registerItem(String name) {
        return ITEMS.register(name, () -> new Item(new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
