package com.deimoshexxus.netherhexedkingdom.content;

import com.deimoshexxus.netherhexedkingdom.NetherHexedKingdomMain;
import net.minecraft.world.item.Item;
import net.minecraft.world.food.FoodProperties;
import net.neoforged.bus.api.IEventBus;
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

    public static final DeferredItem<Item> ANCIENT_FRAGMENT = registerItem("ancient_fragment");
    public static final DeferredItem<Item> ANCIENT_CORE = registerItem("ancient_core");

    public static final DeferredItem<Item> SOUL_INFUSED_SHARD = registerItem("soul_infused_shard");
    public static final DeferredItem<Item> HEXAN_CRYSTAL = registerItem("hexan_crystal");

    // --- Magical & Alchemical Materials ---

    public static final DeferredItem<Item> VOID_ESSENCE = registerItem("void_essence");
    public static final DeferredItem<Item> NETHER_EMBER = registerItem("nether_ember");
    public static final DeferredItem<Item> HELLSTEEL_CATALYST = registerItem("hellsteel_catalyst");
    public static final DeferredItem<Item> BLOOD_INFUSED_GEM = registerItem("blood_infused_gem");

    // --- Relics & Artifacts ---

    public static final DeferredItem<Item> RELIC_OF_THE_DEEP = registerItem("relic_of_the_deep");
    public static final DeferredItem<Item> RELIC_OF_FIRE = registerItem("relic_of_fire");
    public static final DeferredItem<Item> RELIC_OF_TIME = registerItem("relic_of_time");

    // --- Basic Consumables (example) ---

    public static final DeferredItem<Item> HEXAN_FRUIT = ITEMS.register("hexan_fruit",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(4)
                            .saturationModifier(0.4f)
                            .alwaysEdible()
                            .build())
                    .stacksTo(64)));

    // --- Placeholder for Future Equipment ---

    // public static final DeferredItem<Item> MILITUS_SWORD = ITEMS.register("militus_sword", () -> new SwordItem(...));
    // public static final DeferredItem<Item> HEXAN_ARMOR_HELMET = ITEMS.register("hexan_helmet", () -> new ArmorItem(...));
    // (Will be added after custom tool/armor tiers are defined)

    // --- Registry Helper ---

    private static DeferredItem<Item> registerItem(String name) {
        return ITEMS.register(name, () -> new Item(new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
