package com.deimoshexxus.netherhexedkingdom.content.material;

import com.deimoshexxus.netherhexedkingdom.NetherHexedKingdomMain;
import com.deimoshexxus.netherhexedkingdom.content.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.List;
import java.util.Map;

public class ModArmorMaterials {

    public static final DeferredRegister<ArmorMaterial> MATERIALS =
            DeferredRegister.create(Registries.ARMOR_MATERIAL, NetherHexedKingdomMain.MODID);

    // Between Chainmail and Iron
    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> MILITUS_ALLOY_MATERIAL =
            MATERIALS.register("militus_alloy", () -> new ArmorMaterial(
                    Map.of(
                            ArmorItem.Type.HELMET, 3,
                            ArmorItem.Type.CHESTPLATE, 5,
                            ArmorItem.Type.LEGGINGS, 4,
                            ArmorItem.Type.BOOTS, 2
                    ),
                    15,                                                         // enchantability
                    net.minecraft.sounds.SoundEvents.ARMOR_EQUIP_IRON,          // equip sound
                    () -> Ingredient.of(ModItems.MILITUS_ALLOY_INGOT),   // repair ingredient
                    List.of(new ArmorMaterial.Layer(
                            ResourceLocation.fromNamespaceAndPath(
                                    NetherHexedKingdomMain.MODID,
                                    "militus_alloy"
                            )
                    )),
                    1.5f,         // toughness
                    0.1f          // knockback resistance
            ));
}
