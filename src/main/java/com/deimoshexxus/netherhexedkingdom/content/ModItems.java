package com.deimoshexxus.netherhexedkingdom.content;

import com.deimoshexxus.netherhexedkingdom.NetherHexedKingdom;
import com.deimoshexxus.netherhexedkingdom.content.custom.items.SoulGlowMushroomItem;
import com.deimoshexxus.netherhexedkingdom.content.material.ModArmorMaterials;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Registers all standalone items for Nether Hexed Kingdom.
 * Tools, armor, and other custom items will be added later.
 */
public class ModItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(NetherHexedKingdom.MODID);

    // --- Ores, Metals & Fragments ---

    public static final DeferredItem<Item> MILITUS_ALLOY_INGOT = registerItem("militus_alloy_ingot");
    public static final DeferredItem<Item> MILITUS_ALLOY_NUGGET = registerItem("militus_alloy_nugget");
    public static final DeferredItem<Item> IRON_CLUMP = registerItem("iron_clump");
    public static final DeferredItem<Item> NETHERITE_FRAGMENT_BUNDLE = registerItem("netherite_fragment_bundle");
    public static final DeferredItem<Item> NETHERITE_FRAGMENT = registerItem("netherite_fragment");
    public static final DeferredItem<Item> NETHERITE_OXIDE = registerItem("netherite_oxide");
    public static final DeferredItem<Item> IMPERIAL_COINS = registerItem("imperial_coins");
    public static final DeferredItem<BlockItem> POISON_GAS_ITEM = ITEMS.register("poison_gas_source",
                    () -> new BlockItem(ModBlocks.GAS_SOURCE.get(), new Item.Properties()));


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


    // --- Consumables ---

    public static final DeferredItem<BlockItem> MASONIAE_MUSHROOM_ITEM = ITEMS.register("masoniae_mushroom",
            () -> new BlockItem(ModBlocks.MASONIAE_MUSHROOM.get(),
                    new Item.Properties()
                            .food(new FoodProperties.Builder()
                                    .nutrition(4)
                                    .saturationModifier(0.6f)
                                    .alwaysEdible()
                                    //do not use here, results in suspicious holder bug
                                    //.effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 200, 1), 1.0f) // 10s regen II
                                    .build()
                            )
            )
    );

    public static final DeferredItem<BlockItem> SOULGLOW_MUSHROOM_ITEM = ITEMS.register("soul_glow_mushroom",
            () -> new SoulGlowMushroomItem(ModBlocks.SOULGLOW_MUSHROOM.get(),
                    new Item.Properties()
                            .food(new FoodProperties.Builder()
                                    .nutrition(1)
                                    .saturationModifier(2.6f)
                                    .alwaysEdible()
                                    .build()
                            )
            )
    );

    public static final DeferredItem<BlockItem> LINGZHI_MUSHROOM_ITEM = ITEMS.register("lingzhi_mushroom",
            () -> new BlockItem(ModBlocks.LINGZHI_MUSHROOM.get(),
                    new Item.Properties()
                            .food(new FoodProperties.Builder()
                                    .nutrition(8)
                                    .saturationModifier(0.8f)
                                    .alwaysEdible()
                                    .build()
                            )
            )
    );

//    public static final DeferredItem<BlockItem> LINGZHI_MUSHROOM_ITEM =
//            ITEMS.register("lingzhi_mushroom",
//                    () -> new BlockItem(ModBlocks.LINGZHI_MUSHROOM.get(),
//                            new Item.Properties()
//                    ));


//    public static final DeferredItem<Item> MASONIAE_MUSHROOM_ITEM = ITEMS.register("masoniae_mushroom",
//            () -> new Item(new Item.Properties()
//                    .food(new FoodProperties.Builder()
//                            .nutrition(4)
//                            .saturationModifier(0.6f)
//                            .alwaysEdible()
//                            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 200, 1), 1.0f) // 10s regen II
//                            .build())
//                    .stacksTo(64)));
//
//    public static final DeferredItem<BlockItem> MASONIAE_MUSHROOM_BLOCK_ITEM = NetherHexedKingdom.ITEMS.register("masoniae_mushroom",
//            () -> new BlockItem(ModBlocks.MASONIAE_MUSHROOM.get(), new Item.Properties()
//                    .food(new FoodProperties.Builder()
//                            .nutrition(4)
//                            .saturationModifier(0.6f)
//                            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 200, 1), 1.0f)
//                            .build()
//                    )
//            )
//    );


//    public static final DeferredItem<BlockItem> MASONIAE_MUSHROOM_ITEM =
//            ITEMS.register("masoniae_mushroom",
//                    () -> new BlockItem(ModBlocks.MASONIAE_MUSHROOM.get(), new Item.Properties()));

    // redundant?
    public static final DeferredItem<Item> ETERNAL_LIGHT_BLOCK_ITEM =
            ITEMS.register("eternal_light_block",
                    () -> new BlockItem(ModBlocks.ETERNAL_LIGHT_BLOCK.get(), new Item.Properties()));

    // --- Entity Eggs ---

    public static final DeferredHolder<Item, DeferredSpawnEggItem> DECAYED_ZOMBIE_SPAWN_EGG =
            ITEMS.register("decayed_zombie_spawn_egg", () ->
                    new DeferredSpawnEggItem(
                            ModEntities.DECAYED_ZOMBIE,
                            0x6b0626,
                            0x8a8270,
                            new Item.Properties()
                    )
            );

    public static final DeferredHolder<Item, DeferredSpawnEggItem> DECAYED_ZOMBIE_HUSK_SPAWN_EGG =
            ITEMS.register("decayed_zombie_husk_spawn_egg", () ->
                    new DeferredSpawnEggItem(
                            ModEntities.DECAYED_ZOMBIE_HUSK,
                            0x4f0634,
                            0xaba28c,
                            new Item.Properties()
                    )
            );

    public static final DeferredHolder<Item, DeferredSpawnEggItem> DECAYED_ZOMBIFIED_PIGLIN_SPAWN_EGG =
            ITEMS.register("decayed_zombified_piglin_spawn_egg", () ->
                    new DeferredSpawnEggItem(
                            ModEntities.DECAYED_ZOMBIFIED_PIGLIN,
                            0x4f0634,
                            0x2e4a42,
                            new Item.Properties()
                    )
            );

    public static final DeferredHolder<Item, DeferredSpawnEggItem> HEXAN_GUARD_SPAWN_EGG =
            ITEMS.register("hexan_guard_spawn_egg", () ->
                    new DeferredSpawnEggItem(
                            ModEntities.HEXAN_GUARD,
                            0x7b8a67,
                            0x6b1706,
                            new Item.Properties()
                    )
            );

    public static final DeferredHolder<Item, DeferredSpawnEggItem> GARGOYLE_POSSESSED_SPAWN_EGG =
            ITEMS.register("gargoyle_possessed_spawn_egg", () ->
                    new DeferredSpawnEggItem(
                            ModEntities.GARGOYLE_POSSESSED,
                            0xd69d00,
                            0x992109,
                            new Item.Properties()
                    )
            );



    // --- Armor ---

    public static final DeferredHolder<Item, ArmorItem> MILITUS_ALLOY_HELMET =
            ITEMS.register("militus_alloy_helmet", () ->
                    new ArmorItem(ModArmorMaterials.MILITUS_ALLOY_MATERIAL,
                            ArmorItem.Type.HELMET,
                            new Item.Properties()));

    public static final DeferredHolder<Item, ArmorItem> MILITUS_ALLOY_CHESTPLATE =
            ITEMS.register("militus_alloy_chestplate", () ->
                    new ArmorItem(ModArmorMaterials.MILITUS_ALLOY_MATERIAL,
                            ArmorItem.Type.CHESTPLATE,
                            new Item.Properties()));

    public static final DeferredHolder<Item, ArmorItem> MILITUS_ALLOY_LEGGINGS =
            ITEMS.register("militus_alloy_leggings", () ->
                    new ArmorItem(ModArmorMaterials.MILITUS_ALLOY_MATERIAL,
                            ArmorItem.Type.LEGGINGS,
                            new Item.Properties()));

    public static final DeferredHolder<Item, ArmorItem> MILITUS_ALLOY_BOOTS =
            ITEMS.register("militus_alloy_boots", () ->
                    new ArmorItem(ModArmorMaterials.MILITUS_ALLOY_MATERIAL,
                            ArmorItem.Type.BOOTS,
                            new Item.Properties()));
    public static final DeferredHolder<Item, AnimalArmorItem> MILITUS_ALLOY_HORSE_ARMOR =
            ITEMS.register("militus_alloy_horse_armor", () ->
                    new AnimalArmorItem(ModArmorMaterials.MILITUS_ALLOY_MATERIAL,
                            AnimalArmorItem.BodyType.EQUESTRIAN,
                            false,
                            new Item.Properties().stacksTo(1)));

    // --- Registry Helper ---

    private static DeferredItem<Item> registerItem(String name) {
        return ITEMS.register(name, () -> new Item(new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
