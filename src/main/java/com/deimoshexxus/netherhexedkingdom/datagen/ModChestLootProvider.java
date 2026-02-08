package com.deimoshexxus.netherhexedkingdom.datagen;

import com.deimoshexxus.netherhexedkingdom.NetherHexedKingdom;
import com.deimoshexxus.netherhexedkingdom.content.ModBlocks;
import com.deimoshexxus.netherhexedkingdom.content.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SetPotionFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class ModChestLootProvider implements LootTableSubProvider {

    protected final HolderLookup.Provider registries;

    public ModChestLootProvider(HolderLookup.Provider registries) {
        this.registries = registries;
    }

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {

        ResourceLocation kingdom_loot_id = ResourceLocation.fromNamespaceAndPath(
                NetherHexedKingdom.MODID,
                "chests/hexed_kingdom_loot"
        );

        output.accept(
                ResourceKey.create(Registries.LOOT_TABLE, kingdom_loot_id),
                LootTable.lootTable()
                        .withPool(
                                LootPool.lootPool()
                                        // 2–5 total rolls
                                        //.setRolls(UniformGenerator.between(2, 5))
                                        .setRolls(UniformGenerator.between(6, 12))

                                        // common
                                        .add(LootItem.lootTableItem(Items.GOLD_NUGGET).setWeight(10))
                                        .add(LootItem.lootTableItem(Items.IRON_NUGGET).setWeight(12))
                                        .add(LootItem.lootTableItem(Items.ROTTEN_FLESH).setWeight(12))
                                        .add(LootItem.lootTableItem(Items.RED_DYE).setWeight(10))
                                        .add(LootItem.lootTableItem(Items.BLACK_CANDLE).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 3))))
                                        .add(LootItem.lootTableItem(Items.GLOWSTONE_DUST).setWeight(10))
                                        .add(LootItem.lootTableItem(Items.NETHER_BRICK).setWeight(24))
                                        .add(LootItem.lootTableItem(Items.NETHER_BRICK).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 24))))
                                        .add(LootItem.lootTableItem(Items.NETHER_BRICKS).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 12))))
                                        .add(LootItem.lootTableItem(Items.NETHER_BRICK_STAIRS).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 24))))
                                        .add(LootItem.lootTableItem(Items.NETHER_BRICK_FENCE).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 8))))
                                        .add(LootItem.lootTableItem(ModItems.MILITUS_ALLOY_NUGGET).setWeight(10))
                                        .add(LootItem.lootTableItem(ModItems.MILITUS_ALLOY_INGOT).setWeight(10))
                                        .add(LootItem.lootTableItem(Items.ARROW).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 12))))

                                        // uncommon
                                        .add(LootItem.lootTableItem(Items.GOLD_INGOT).setWeight(6))
                                        .add(LootItem.lootTableItem(Items.IRON_INGOT).setWeight(4))
                                        .add(LootItem.lootTableItem(Items.GLASS_BOTTLE).setWeight(6))
                                        .add(LootItem.lootTableItem(Items.BLAZE_ROD).setWeight(6))
                                        .add(LootItem.lootTableItem(Items.SUSPICIOUS_STEW).setWeight(4))
                                        .add(LootItem.lootTableItem(Items.COOKIE).setWeight(8))
                                        .add(LootItem.lootTableItem(Items.GOLDEN_APPLE).setWeight(4))
                                        .add(LootItem.lootTableItem(Items.GOLDEN_CARROT).setWeight(4))
                                        .add(LootItem.lootTableItem(Items.QUARTZ).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 6))))
                                        .add(LootItem.lootTableItem(Items.STONE_PICKAXE).setWeight(6))
                                        .add(LootItem.lootTableItem(Items.CHAINMAIL_CHESTPLATE).setWeight(3))
                                        .add(LootItem.lootTableItem(Items.GOLDEN_SWORD).setWeight(3))
                                        .add(LootItem.lootTableItem(ModItems.IMPERIAL_COINS).setWeight(8))
                                        .add(LootItem.lootTableItem(ModItems.ETERNAL_LIGHT_BLOCK_ITEM).setWeight(3))
                                        .add(LootItem.lootTableItem(Items.FEATHER).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 6))))


                                        // rare
                                        .add(LootItem.lootTableItem(ModItems.POISON_GAS_ITEM).setWeight(1))
                                        .add(LootItem.lootTableItem(ModItems.NETHERITE_OXIDE).setWeight(1))
                                        .add(LootItem.lootTableItem(Items.POTION).setWeight(1).apply(SetPotionFunction.setPotion(Potions.NIGHT_VISION)))
                                        .add(LootItem.lootTableItem(Items.POTION).setWeight(1).apply(SetPotionFunction.setPotion(Potions.FIRE_RESISTANCE)))

                        )
                        // bonus pool (50% chance)
                        .withPool(
                                LootPool.lootPool()
                                        .setRolls(ConstantValue.exactly(1))
                                        .when(LootItemRandomChanceCondition.randomChance(0.5f))
                                        .add(LootItem.lootTableItem(ModItems.MILITUS_ALLOY_INGOT).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5))))
                                        .add(LootItem.lootTableItem(ModItems.MILITUS_ALLOY_NUGGET).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 9))))
                                        .add(LootItem.lootTableItem(ModBlocks.GILDED_BRICKS_OF_LOST_TIME).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 6))))
                        ));

        ResourceLocation kingdom_loot_rare_id = ResourceLocation.fromNamespaceAndPath(
                NetherHexedKingdom.MODID,
                "chests/hexed_kingdom_rare_loot"
        );

        output.accept(
                ResourceKey.create(Registries.LOOT_TABLE, kingdom_loot_rare_id),
                LootTable.lootTable()
                        .withPool(
                                LootPool.lootPool()
                                        // 2–5 total rolls
                                        //.setRolls(UniformGenerator.between(2, 5))
                                        .setRolls(UniformGenerator.between(12, 16))

                                        // common
                                        .add(LootItem.lootTableItem(Items.GOLD_NUGGET).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 12))))
                                        .add(LootItem.lootTableItem(Items.IRON_NUGGET).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 16))))
                                        .add(LootItem.lootTableItem(Items.RED_DYE).setWeight(10))
                                        .add(LootItem.lootTableItem(Items.GLOWSTONE_DUST).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 24))))
                                        .add(LootItem.lootTableItem(Items.NETHER_BRICK).setWeight(24))
                                        .add(LootItem.lootTableItem(Items.NETHER_BRICK).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 24))))
                                        .add(LootItem.lootTableItem(Items.NETHER_BRICKS).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(12, 24))))
                                        .add(LootItem.lootTableItem(Items.HONEY_BOTTLE).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 3))))
                                        .add(LootItem.lootTableItem(ModItems.MILITUS_ALLOY_NUGGET).setWeight(3))
                                        .add(LootItem.lootTableItem(ModItems.MILITUS_ALLOY_INGOT).setWeight(6))
                                        .add(LootItem.lootTableItem(Items.ARROW).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(12, 32))))

                                        // uncommon
                                        .add(LootItem.lootTableItem(Items.GOLD_INGOT).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 6))))
                                        .add(LootItem.lootTableItem(Items.IRON_INGOT).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 3))))
                                        .add(LootItem.lootTableItem(Items.GLASS_BOTTLE).setWeight(6))
                                        .add(LootItem.lootTableItem(Items.BOOK).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
                                        .add(LootItem.lootTableItem(Items.BLAZE_ROD).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))
                                        .add(LootItem.lootTableItem(Items.SUSPICIOUS_STEW).setWeight(4))
                                        .add(LootItem.lootTableItem(Items.CAKE).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))
                                        .add(LootItem.lootTableItem(Items.GOLDEN_APPLE).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))
                                        .add(LootItem.lootTableItem(Items.GOLDEN_CARROT).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))
                                        .add(LootItem.lootTableItem(Items.QUARTZ).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 6))))
                                        .add(LootItem.lootTableItem(Items.IRON_PICKAXE).setWeight(3).apply(EnchantRandomlyFunction.randomEnchantment()))
                                        .add(LootItem.lootTableItem(Items.GOLDEN_CHESTPLATE).setWeight(3).apply(EnchantRandomlyFunction.randomEnchantment()))
                                        .add(LootItem.lootTableItem(Items.GOLDEN_SWORD).setWeight(3).apply(EnchantRandomlyFunction.randomEnchantment()))
                                        .add(LootItem.lootTableItem(Items.GOLDEN_AXE).setWeight(3).apply(EnchantRandomlyFunction.randomEnchantment()))
                                        .add(LootItem.lootTableItem(Items.GOLDEN_PICKAXE).setWeight(6))
                                        .add(LootItem.lootTableItem(Items.GOLDEN_PICKAXE).setWeight(4).apply(EnchantRandomlyFunction.randomEnchantment()))
                                        .add(LootItem.lootTableItem(Items.POTION).apply(SetPotionFunction.setPotion(Potions.THICK)).setWeight(3))
                                        .add(LootItem.lootTableItem(Items.POTION).apply(SetPotionFunction.setPotion(Potions.MUNDANE)).setWeight(3))
                                        .add(LootItem.lootTableItem(Items.POTION).apply(SetPotionFunction.setPotion(Potions.AWKWARD)).setWeight(3))
                                        .add(LootItem.lootTableItem(ModItems.IMPERIAL_COINS).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(9, 24))))
                                        .add(LootItem.lootTableItem(ModItems.ETERNAL_LIGHT_BLOCK_ITEM).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 3))))
                                        .add(LootItem.lootTableItem(ModItems.NETHERITE_OXIDE).setWeight(3))
                                        .add(LootItem.lootTableItem(Items.FEATHER).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 6))))

                                        // rare
                                        .add(LootItem.lootTableItem(ModItems.POISON_GAS_ITEM).setWeight(1))
                                        .add(LootItem.lootTableItem(ModItems.SOULGLOW_MUSHROOM_ITEM).setWeight(2))
                                        .add(LootItem.lootTableItem(ModItems.NETHERITE_OXIDE).setWeight(2))
                                        .add(LootItem.lootTableItem(ModItems.NETHERITE_FRAGMENT).setWeight(2))
                                        .add(LootItem.lootTableItem(ModItems.NETHERITE_FRAGMENT_BUNDLE).setWeight(1))
                                        .add(LootItem.lootTableItem(ModItems.NETHER_PORTAL_ORB).setWeight(3))
                                        .add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).setWeight(3).apply(EnchantRandomlyFunction.randomEnchantment()))
                                        .add(LootItem.lootTableItem(Items.DIAMOND).setWeight(1))
                                        .add(LootItem.lootTableItem(Items.POTION).setWeight(1).apply(SetPotionFunction.setPotion(Potions.NIGHT_VISION)))
                                        .add(LootItem.lootTableItem(Items.POTION).setWeight(1).apply(SetPotionFunction.setPotion(Potions.FIRE_RESISTANCE)))
                                        .add(LootItem.lootTableItem(Items.POTION).setWeight(1).apply(SetPotionFunction.setPotion(Potions.STRONG_HEALING)))

                        )
                        // bonus pool (50% chance)
                        .withPool(
                                LootPool.lootPool()
                                        .setRolls(ConstantValue.exactly(1))
                                        .when(LootItemRandomChanceCondition.randomChance(0.5f))
                                        .add(LootItem.lootTableItem(ModItems.MILITUS_ALLOY_INGOT).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(6, 9))))
                                        .add(LootItem.lootTableItem(ModItems.MILITUS_ALLOY_NUGGET).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 12))))
                                        .add(LootItem.lootTableItem(ModBlocks.GILDED_BRICKS_OF_LOST_TIME).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 9))))
                        ));

        ResourceLocation kingdom_loot_red_id = ResourceLocation.fromNamespaceAndPath(
                NetherHexedKingdom.MODID,
                "chests/hexed_kingdom_redsuntower_loot"
        );

        output.accept(
                ResourceKey.create(Registries.LOOT_TABLE, kingdom_loot_red_id),
                LootTable.lootTable()
                        .withPool(
                                LootPool.lootPool()
                                        // 2–5 total rolls
                                        //.setRolls(UniformGenerator.between(2, 5))
                                        .setRolls(UniformGenerator.between(16, 22))

                                        // common
                                        .add(LootItem.lootTableItem(Items.GOLD_NUGGET).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(9, 16))))
                                        .add(LootItem.lootTableItem(Items.IRON_NUGGET).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(9, 16))))
                                        .add(LootItem.lootTableItem(Items.RED_DYE).setWeight(10))
                                        .add(LootItem.lootTableItem(Items.BLACK_BANNER).setWeight(10))
                                        .add(LootItem.lootTableItem(Items.ORANGE_DYE).setWeight(10))
                                        .add(LootItem.lootTableItem(Items.YELLOW_DYE).setWeight(10))
                                        .add(LootItem.lootTableItem(Items.GLOWSTONE_DUST).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 24))))
                                        .add(LootItem.lootTableItem(Items.NETHER_BRICK).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(12, 24))))
                                        .add(LootItem.lootTableItem(Items.OBSIDIAN).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 12))))
                                        .add(LootItem.lootTableItem(Items.REDSTONE).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 9))))
                                        .add(LootItem.lootTableItem(Items.HONEY_BOTTLE).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 3))))
                                        .add(LootItem.lootTableItem(ModItems.MILITUS_ALLOY_NUGGET).setWeight(10))
                                        .add(LootItem.lootTableItem(ModItems.MILITUS_ALLOY_INGOT).setWeight(10))
                                        .add(LootItem.lootTableItem(Items.TIPPED_ARROW).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(4, 16)))
                                                .apply(SetPotionFunction.setPotion(Potions.HEALING)))


                                        // uncommon
                                        .add(LootItem.lootTableItem(Items.GOLD_INGOT).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(6, 9))))
                                        .add(LootItem.lootTableItem(Items.IRON_INGOT).setWeight(4).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 3))))
                                        .add(LootItem.lootTableItem(Items.GLASS_BOTTLE).setWeight(6))
                                        .add(LootItem.lootTableItem(Items.BLAZE_ROD).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 3))))
                                        .add(LootItem.lootTableItem(Items.FEATHER).setWeight(6))
                                        .add(LootItem.lootTableItem(Items.GOLDEN_APPLE).setWeight(4).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 3))))
                                        .add(LootItem.lootTableItem(Items.GOLDEN_CARROT).setWeight(4).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 3))))
                                        .add(LootItem.lootTableItem(Items.QUARTZ).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 6))))
                                        .add(LootItem.lootTableItem(Items.IRON_PICKAXE).setWeight(6).apply(EnchantRandomlyFunction.randomEnchantment()))
                                        .add(LootItem.lootTableItem(Items.GOLDEN_CHESTPLATE).setWeight(3).apply(EnchantRandomlyFunction.randomEnchantment()))
                                        .add(LootItem.lootTableItem(Items.GOLDEN_SWORD).setWeight(3).apply(EnchantRandomlyFunction.randomEnchantment()))
                                        .add(LootItem.lootTableItem(Items.GOLDEN_AXE).setWeight(3).apply(EnchantRandomlyFunction.randomEnchantment()))
                                        .add(LootItem.lootTableItem(Items.GOLDEN_PICKAXE).setWeight(3))
                                        .add(LootItem.lootTableItem(Items.GOLDEN_PICKAXE).setWeight(3).apply(EnchantRandomlyFunction.randomEnchantment()))
                                        .add(LootItem.lootTableItem(Items.POTION).apply(SetPotionFunction.setPotion(Potions.THICK)).setWeight(3))
                                        .add(LootItem.lootTableItem(Items.POTION).apply(SetPotionFunction.setPotion(Potions.MUNDANE)).setWeight(3))
                                        .add(LootItem.lootTableItem(Items.POTION).apply(SetPotionFunction.setPotion(Potions.AWKWARD)).setWeight(3))
                                        .add(LootItem.lootTableItem(ModItems.IMPERIAL_COINS).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(12, 32))))
                                        .add(LootItem.lootTableItem(ModItems.ETERNAL_LIGHT_BLOCK_ITEM).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 3))))
                                        .add(LootItem.lootTableItem(ModItems.NETHERITE_OXIDE).setWeight(3))
                                        .add(LootItem.lootTableItem(Items.TIPPED_ARROW).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 16)))
                                                .apply(SetPotionFunction.setPotion(Potions.HARMING)))
                                        .add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).setWeight(8).apply(EnchantRandomlyFunction.randomEnchantment()))

                                        // rare
                                        .add(LootItem.lootTableItem(ModItems.POISON_GAS_ITEM).setWeight(1))
                                        .add(LootItem.lootTableItem(ModItems.SOULGLOW_MUSHROOM_ITEM).setWeight(1))
                                        .add(LootItem.lootTableItem(ModItems.NETHERITE_OXIDE).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))
                                        .add(LootItem.lootTableItem(ModItems.NETHERITE_FRAGMENT).setWeight(2))
                                        .add(LootItem.lootTableItem(ModItems.NETHERITE_FRAGMENT_BUNDLE).setWeight(1))
                                        .add(LootItem.lootTableItem(ModItems.NETHER_PORTAL_ORB).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))
                                        .add(LootItem.lootTableItem(Items.DIAMOND).setWeight(1))
                                        .add(LootItem.lootTableItem(Items.POTION).setWeight(1).apply(SetPotionFunction.setPotion(Potions.NIGHT_VISION)))
                                        .add(LootItem.lootTableItem(Items.POTION).setWeight(1).apply(SetPotionFunction.setPotion(Potions.FIRE_RESISTANCE)))
                                        .add(LootItem.lootTableItem(Items.POTION).setWeight(1).apply(SetPotionFunction.setPotion(Potions.STRONG_HEALING)))

                        )
                        // bonus pool (50% chance)
                        .withPool(
                                LootPool.lootPool()
                                        .setRolls(ConstantValue.exactly(1))
                                        .when(LootItemRandomChanceCondition.randomChance(0.5f))
                                        .add(LootItem.lootTableItem(ModItems.MILITUS_ALLOY_INGOT).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(6, 9))))
                                        .add(LootItem.lootTableItem(ModItems.MILITUS_ALLOY_NUGGET).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 12))))
                        ));

    }
}


