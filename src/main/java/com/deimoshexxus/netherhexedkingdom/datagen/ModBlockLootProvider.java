package com.deimoshexxus.netherhexedkingdom.datagen;

import com.deimoshexxus.netherhexedkingdom.content.ModBlocks;
import com.deimoshexxus.netherhexedkingdom.content.ModItems;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.data.loot.LootTableProvider.SubProviderEntry;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;
//import net.minecraft.data.loot.LootContextParamSets;

import java.util.ArrayList;
import java.util.Set;

/**
 * Block loot datagen provider for 1.21.1 / NeoForge 21.1.215.
 *
 * Note: this constructor signature matches the NeoForge / vanilla datagen changes:
 *  public BlockLootSubProvider(Set<Item> explosionResistant, FeatureFlagSet flags, HolderLookup.Provider lookupProvider)
 *
 * You'll add this via a SubProviderEntry when constructing your LootTableProvider in gatherData.
 */
public class ModBlockLootProvider extends BlockLootSubProvider {

    // constructor receives the HolderLookup.Provider when the datagen system constructs the subprovider
    public ModBlockLootProvider(HolderLookup.Provider lookupProvider) {
        // no explosion-resistant items (empty set), default feature flags, and pass lookup provider
        super(Set.of(), FeatureFlags.DEFAULT_FLAGS, lookupProvider);
    }

    @Override
    protected void generate() {
        for (Block block : ModBlocks.BLOCKS.getEntries().stream().map(DeferredHolder::get).toList()) {

            if (block == ModBlocks.LINGZHI_MUSHROOM.get()) {
                this.add(block, createSingleItemTable(ModItems.LINGZHI_MUSHROOM_ITEM));
                continue;
            }

            if (block == ModBlocks.ETERNAL_LIGHT_BLOCK.get()) {
                this.add(block, LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                .add(LootItem.lootTableItem(Items.GLOWSTONE_DUST)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 5)))
                                )
                        )
                );
                continue;
            }

            if (block == ModBlocks.MILITUS_ALLOY_ORE.get()) {
                // resolve Holder<Enchantment> for FORTUNE
                HolderLookup.RegistryLookup<Enchantment> enchLookup =
                        this.registries.lookupOrThrow(net.minecraft.core.registries.Registries.ENCHANTMENT);
                Holder<Enchantment> fortune = enchLookup.getOrThrow(Enchantments.FORTUNE);
                this.add(block,
                        LootTable.lootTable()
                                .withPool(LootPool.lootPool()
                                        .setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(ModItems.MILITUS_ALLOY_NUGGET.get())
                                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 7)))
                                                .apply(ApplyBonusCount.addOreBonusCount(fortune))
                                                .apply(ApplyExplosionDecay.explosionDecay())
                                        )
                                )
                );
                continue;
            }

            // DEFAULT BEHAVIOR:
            // specify none → drop itself
            this.dropSelf(block);
        }
    }


    // Provide an iterable of all blocks your provider should validate / know about.
    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return new ArrayList<>(ModBlocks.BLOCKS.getEntries().stream()
                .map(DeferredHolder::get)
                .toList());
    }

}
