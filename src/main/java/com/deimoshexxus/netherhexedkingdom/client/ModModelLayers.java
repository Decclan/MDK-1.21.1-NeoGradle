package com.deimoshexxus.netherhexedkingdom.client;

import com.deimoshexxus.netherhexedkingdom.NetherHexedKingdom;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public class ModModelLayers {

    public static final ModelLayerLocation HEXAN_GUARD_BODY =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(NetherHexedKingdom.MODID, "hexan_guard_body"), "main");

    public static final ModelLayerLocation HEXAN_GUARD_ARMOR_INNER =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(NetherHexedKingdom.MODID, "hexan_guard_armor"), "inner");

    public static final ModelLayerLocation HEXAN_GUARD_ARMOR_OUTER =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(NetherHexedKingdom.MODID, "hexan_guard_armor"), "outer");
}
