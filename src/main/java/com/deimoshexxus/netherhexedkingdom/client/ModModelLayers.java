package com.deimoshexxus.netherhexedkingdom.client;

import com.deimoshexxus.netherhexedkingdom.NetherHexedKingdom;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public class ModModelLayers {

    // Body model
    public static final ModelLayerLocation HEXAN_GUARD_BODY =
            new ModelLayerLocation(
                    ResourceLocation.fromNamespaceAndPath(NetherHexedKingdom.MODID, "hexan_guard_body"),
                    "main"
            );

    // INNER ARMOR – must have unique path
    public static final ModelLayerLocation HEXAN_GUARD_ARMOR_INNER =
            new ModelLayerLocation(
                    ResourceLocation.fromNamespaceAndPath(NetherHexedKingdom.MODID, "hexan_guard_armor_inner"),
                    "main"
            );

    // OUTER ARMOR – must have unique path
    public static final ModelLayerLocation HEXAN_GUARD_ARMOR_OUTER =
            new ModelLayerLocation(
                    ResourceLocation.fromNamespaceAndPath(NetherHexedKingdom.MODID, "hexan_guard_armor_outer"),
                    "main"
            );

    public static final ModelLayerLocation GARGOYLE_POSSESSED =
            new ModelLayerLocation(
                    ResourceLocation.fromNamespaceAndPath(NetherHexedKingdom.MODID, "gargoyle_possessed"),
                    "main"
            );

}
