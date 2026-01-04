package com.deimoshexxus.netherhexedkingdom.content.structures;

import com.deimoshexxus.netherhexedkingdom.content.ModStructurePieces;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public class HexedBullionTemplePiece extends TemplateStructurePiece {

    // Runtime constructor (worldgen)
    public HexedBullionTemplePiece(
            StructureTemplateManager templateManager,
            ResourceLocation templateId,
            BlockPos pos,
            Rotation rotation,
            int genDepth
    ) {
        super(
                ModStructurePieces.HEXED_BULLION_TEMPLE_PIECE.get(),
                genDepth,
                templateManager,
                templateId,
                templateId.toString(),
                makeSettings(rotation),
                pos
        );
    }

    // NBT constructor (loading from disk)
    public HexedBullionTemplePiece(
            StructurePieceSerializationContext context,
            CompoundTag tag
    ) {
        super(
                ModStructurePieces.HEXED_BULLION_TEMPLE_PIECE.get(),
                tag,
                context.structureTemplateManager(),
                HexedBullionTemplePiece::makeSettingsFromTemplate
        );
    }

    // Used ONLY for NBT reload
    private static StructurePlaceSettings makeSettingsFromTemplate(ResourceLocation id) {
        return new StructurePlaceSettings()
                .setRotation(Rotation.NONE)
                .setMirror(Mirror.NONE)
                .setIgnoreEntities(false);
    }

    private static StructurePlaceSettings makeSettings(Rotation rotation) {
        return new StructurePlaceSettings()
                .setRotation(rotation)
                .setMirror(Mirror.NONE)
                .setIgnoreEntities(false);
    }

    @Override
    protected void handleDataMarker(
            String name,
            BlockPos pos,
            ServerLevelAccessor level,
            RandomSource random,
            BoundingBox box
    ) {
        // optional markers (chests, mobs, etc.)
    }
}
