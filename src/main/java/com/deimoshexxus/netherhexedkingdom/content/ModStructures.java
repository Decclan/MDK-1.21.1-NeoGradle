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

    public static final DeferredHolder<StructureType<?>, StructureType<MasoniaeFossilStructure>> FOSSIL_MASONIAE = STRUCTURE_TYPES.register("fossil_masoniae", () -> explicitStructureTypeTyping(MasoniaeFossilStructure.CODEC));

    private static <T extends Structure> StructureType<T> explicitStructureTypeTyping(MapCodec<T> structureCodec) {
        return () -> structureCodec;
    }
}
