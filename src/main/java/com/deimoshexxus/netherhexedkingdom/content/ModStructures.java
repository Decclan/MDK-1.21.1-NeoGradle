package com.deimoshexxus.netherhexedkingdom.content;

import com.deimoshexxus.netherhexedkingdom.NetherHexedKingdom;
import com.deimoshexxus.netherhexedkingdom.content.structures.ExampleStructure;
import com.deimoshexxus.netherhexedkingdom.content.structures.HexedPrisonStructure;
import com.deimoshexxus.netherhexedkingdom.content.structures.HexedWatchTowerStructure;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModStructures {

    private ModStructures() {}

    public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPES = DeferredRegister.create(Registries.STRUCTURE_TYPE, NetherHexedKingdom.MODID);

    public static final DeferredHolder<StructureType<?>, StructureType<ExampleStructure>> EXAMPLE_STRUCTURE = STRUCTURE_TYPES.register("example_structure", () -> explicitStructureTypeTyping(ExampleStructure.CODEC));

    public static final DeferredHolder<StructureType<?>, StructureType<HexedWatchTowerStructure>> HEXED_WATCH_TOWER_STRUCTURE = STRUCTURE_TYPES.register("hexed_watch_tower_structure", () -> explicitStructureTypeTyping(HexedWatchTowerStructure.CODEC));

    public static final DeferredHolder<StructureType<?>, StructureType<HexedPrisonStructure>> HEXED_PRISON_STRUCTURE = STRUCTURE_TYPES.register("hexed_prison_structure", () -> explicitStructureTypeTyping(HexedPrisonStructure.CODEC));

    private static <T extends Structure> StructureType<T> explicitStructureTypeTyping(MapCodec<T> structureCodec) {
        return () -> structureCodec;
    }
}
