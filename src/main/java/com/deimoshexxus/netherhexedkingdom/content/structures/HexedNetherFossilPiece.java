package com.deimoshexxus.netherhexedkingdom.content.structures;

import com.deimoshexxus.netherhexedkingdom.NetherHexedKingdom;
import com.deimoshexxus.netherhexedkingdom.content.ModStructurePieces;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

import java.util.Optional;

public class HexedNetherFossilPiece extends TemplateStructurePiece {

    // Runtime constructor (worldgen)
    public HexedNetherFossilPiece(
            StructureTemplateManager templateManager,
            ResourceLocation templateId,
            BlockPos pos,
            Rotation rotation,
            int genDepth
    ) {
        super(
                ModStructurePieces.HEXED_NETHER_FOSSIL_PIECE.get(),
                genDepth,
                templateManager,
                templateId,
                templateId.toString(),
                makeSettings(rotation),
                pos
        );
    }

    // NBT constructor (world reload)
    public HexedNetherFossilPiece(
            StructurePieceSerializationContext context,
            CompoundTag tag
    ) {
        super(
                ModStructurePieces.HEXED_NETHER_FOSSIL_PIECE.get(),
                tag,
                context.structureTemplateManager(),
                id -> makeSettings(Rotation.NONE)
        );
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
        // optional
    }

    @Override
    public void postProcess(
            net.minecraft.world.level.WorldGenLevel level,
            net.minecraft.world.level.StructureManager structureManager,
            net.minecraft.world.level.chunk.ChunkGenerator generator,
            RandomSource random,
            BoundingBox box,
            ChunkPos chunkPos,
            BlockPos pos
    ) {
        boolean placed = this.template.placeInWorld(
                level,
                this.templatePosition,
                pos,
                this.placeSettings,
                random,
                2
        );

        if (placed) {
            NetherHexedKingdom.LOGGER.info(
                    "[HexedNetherFossilPiece] Placed template {} at {} rot={} box={}",
                    this.templateName,
                    this.templatePosition,
                    this.placeSettings.getRotation(),
                    box
            );
        } else {
            NetherHexedKingdom.LOGGER.warn(
                    "[HexedNetherFossilPiece] FAILED to place template {} at {}",
                    this.templateName,
                    this.templatePosition
            );
        }

        // Handle data markers exactly like TemplateStructurePiece does
        if (placed) {
            for (StructureTemplate.StructureBlockInfo info :
                    this.template.filterBlocks(
                            this.templatePosition,
                            this.placeSettings,
                            net.minecraft.world.level.block.Blocks.STRUCTURE_BLOCK
                    )) {

                if (info.nbt() != null &&
                        net.minecraft.world.level.block.state.properties.StructureMode
                                .valueOf(info.nbt().getString("mode"))
                                == net.minecraft.world.level.block.state.properties.StructureMode.DATA) {

                    this.handleDataMarker(
                            info.nbt().getString("metadata"),
                            info.pos(),
                            level,
                            random,
                            box
                    );
                }
            }
        }
    }

}
