package com.deimoshexxus.netherhexedkingdom.content;


import com.deimoshexxus.netherhexedkingdom.NetherHexedKingdom;
import com.deimoshexxus.netherhexedkingdom.content.structures.HexedBullionTemplePiece;
import com.deimoshexxus.netherhexedkingdom.content.structures.HexedLookoutPiece;
import com.deimoshexxus.netherhexedkingdom.content.structures.HexedOutpostPiece;
import com.deimoshexxus.netherhexedkingdom.content.structures.HexedWatchTowerPiece;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModStructurePieces {

    private ModStructurePieces() {}

    public static final DeferredRegister<StructurePieceType> STRUCTURE_PIECES =
            DeferredRegister.create(Registries.STRUCTURE_PIECE, NetherHexedKingdom.MODID);

    public static final DeferredHolder<StructurePieceType, StructurePieceType>
            HEXED_WATCH_TOWER_PIECE = STRUCTURE_PIECES.register(
            "hexed_watch_tower_piece",
            () -> HexedWatchTowerPiece::new
    );

    public static final DeferredHolder<StructurePieceType, StructurePieceType>
            HEXED_LOOKOUT_PIECE = STRUCTURE_PIECES.register(
            "hexed_lookout_piece",
            () -> HexedLookoutPiece::new
    );

    public static final DeferredHolder<StructurePieceType, StructurePieceType>
            HEXED_BULLION_TEMPLE_PIECE = STRUCTURE_PIECES.register(
            "hexed_bullion_temple_piece",
            () -> HexedBullionTemplePiece::new
    );

    public static final DeferredHolder<StructurePieceType, StructurePieceType>
            HEXED_OUTPOST_PIECE = STRUCTURE_PIECES.register(
            "hexed_outpost_piece",
            () -> HexedOutpostPiece::new
    );


}

