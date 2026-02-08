package com.deimoshexxus.netherhexedkingdom.content;

import com.deimoshexxus.netherhexedkingdom.NetherHexedKingdom;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

public final class ModTemplatePools {

    private ModTemplatePools() {}

    public static final ResourceKey<StructureTemplatePool> HEXED_PRISON_START =
            ResourceKey.create(
                    Registries.TEMPLATE_POOL,
                    ResourceLocation.fromNamespaceAndPath(
                            NetherHexedKingdom.MODID,
                            "hexed_prison/start"
                    )
            );

    public static final ResourceKey<StructureTemplatePool> HEXED_GREED_MINES_START =
            ResourceKey.create(
                    Registries.TEMPLATE_POOL,
                    ResourceLocation.fromNamespaceAndPath(
                            NetherHexedKingdom.MODID,
                            "hexed_greed_mines/start"
                    )
            );
}
