package com.deimoshexxus.netherhexedkingdom.content.structures;

import com.deimoshexxus.netherhexedkingdom.content.ModStructurePieces;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;

public class ExampleStructurePiece extends StructurePiece {

    public ExampleStructurePiece(BlockPos origin) {
        super(
                ModStructurePieces.EXAMPLE_PIECE.get(),
                0,
                new BoundingBox(
                        origin.getX(),
                        origin.getY(),
                        origin.getZ(),
                        origin.getX() + 6,
                        origin.getY() + 4,
                        origin.getZ() + 6
                )
        );
    }

    public ExampleStructurePiece(
            StructurePieceSerializationContext context,
            CompoundTag tag
    ) {
        super(ModStructurePieces.EXAMPLE_PIECE.get(), tag);
    }

    @Override
    protected void addAdditionalSaveData(
            StructurePieceSerializationContext context,
            CompoundTag tag
    ) {
        // nothing to save
    }

    @Override
    public void postProcess(
            WorldGenLevel level,
            StructureManager structureManager,
            ChunkGenerator generator,
            RandomSource random,
            BoundingBox box,
            ChunkPos chunkPos,
            BlockPos pos
    ) {
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();

        for (int x = box.minX(); x <= box.maxX(); x++) {
            for (int y = box.minY(); y <= box.maxY(); y++) {
                for (int z = box.minZ(); z <= box.maxZ(); z++) {
                    cursor.set(x, y, z);
                    level.setBlock(cursor, Blocks.GOLD_BLOCK.defaultBlockState(), 3);
                }
            }
        }
    }
}
