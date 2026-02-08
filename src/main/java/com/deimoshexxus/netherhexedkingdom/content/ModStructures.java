package com.deimoshexxus.netherhexedkingdom.content;

import com.deimoshexxus.netherhexedkingdom.NetherHexedKingdom;
import com.deimoshexxus.netherhexedkingdom.content.structures.*;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModStructures {

    private ModStructures() {}

    public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPES = DeferredRegister.create(Registries.STRUCTURE_TYPE, NetherHexedKingdom.MODID);

    public static final DeferredHolder<StructureType<?>, StructureType<HexedWatchTowerStructure>> HEXED_WATCH_TOWER_STRUCTURE = STRUCTURE_TYPES.register("hexed_watch_tower_structure", () -> explicitStructureTypeTyping(HexedWatchTowerStructure.CODEC));

    public static final DeferredHolder<StructureType<?>, StructureType<HexedPrisonStructure>> HEXED_PRISON_STRUCTURE = STRUCTURE_TYPES.register("hexed_prison_structure", () -> explicitStructureTypeTyping(HexedPrisonStructure.CODEC));

    public static final DeferredHolder<StructureType<?>, StructureType<HexedLookoutStructure>> HEXED_LOOKOUT_STRUCTURE = STRUCTURE_TYPES.register("hexed_lookout_structure", () -> explicitStructureTypeTyping(HexedLookoutStructure.CODEC));

    public static final DeferredHolder<StructureType<?>, StructureType<HexedBullionTempleStructure>> HEXED_BULLION_TEMPLE_STRUCTURE = STRUCTURE_TYPES.register("hexed_bullion_temple_structure", () -> explicitStructureTypeTyping(HexedBullionTempleStructure.CODEC));

    public static final DeferredHolder<StructureType<?>, StructureType<HexedOutpostStructure>> HEXED_OUTPOST_STRUCTURE = STRUCTURE_TYPES.register("hexed_outpost_structure", () -> explicitStructureTypeTyping(HexedOutpostStructure.CODEC));

    public static final DeferredHolder<StructureType<?>, StructureType<HexedNetherFossilStructure>> HEXED_NETHER_FOSSIL = STRUCTURE_TYPES.register("hexed_nether_fossil_structure", () -> explicitStructureTypeTyping(HexedNetherFossilStructure.CODEC));

    public static final DeferredHolder<StructureType<?>, StructureType<CrimsonMotherFungusStructure>> CRIMSON_MOTHER_FUNGUS = STRUCTURE_TYPES.register("crimson_mother_fungus_structure", () -> explicitStructureTypeTyping(CrimsonMotherFungusStructure.CODEC));

    public static final DeferredHolder<StructureType<?>, StructureType<WarpedMotherFungusStructure>> WARPED_MOTHER_FUNGUS = STRUCTURE_TYPES.register("warped_mother_fungus_structure", () -> explicitStructureTypeTyping(WarpedMotherFungusStructure.CODEC));

    public static final DeferredHolder<StructureType<?>, StructureType<HexedGreedMinesStructure>> HEXED_GREED_MINES_STRUCTURE = STRUCTURE_TYPES.register("hexed_greed_mines_structure", () -> explicitStructureTypeTyping(HexedGreedMinesStructure.CODEC));

    private static <T extends Structure> StructureType<T> explicitStructureTypeTyping(MapCodec<T> structureCodec) {
        return () -> structureCodec;
    }
}
