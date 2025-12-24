package com.deimoshexxus.netherhexedkingdom.content;


import com.deimoshexxus.netherhexedkingdom.NetherHexedKingdom;
import com.deimoshexxus.netherhexedkingdom.content.structures.ExampleStructurePiece;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModStructurePieces {

    private ModStructurePieces() {}

    public static final DeferredRegister<StructurePieceType> STRUCTURE_PIECES =
            DeferredRegister.create(Registries.STRUCTURE_PIECE, NetherHexedKingdom.MODID);

    public static final DeferredHolder<StructurePieceType, StructurePieceType>
            EXAMPLE_PIECE = STRUCTURE_PIECES.register(
            "example_piece",
            () -> ExampleStructurePiece::new
    );
}

