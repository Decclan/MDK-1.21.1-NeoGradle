package com.deimoshexxus.netherhexedkingdom.content.structures;

import com.deimoshexxus.netherhexedkingdom.content.ModStructurePieces;
import com.deimoshexxus.netherhexedkingdom.content.ModStructures;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;

import java.util.Optional;

public class ExampleStructure extends Structure {

    public static final MapCodec<ExampleStructure> CODEC =
            simpleCodec(ExampleStructure::new);

    protected ExampleStructure(StructureSettings settings) {
        super(settings);
    }

    @Override
    protected Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
        return Structure.onTopOfChunkCenter(
                context,
                net.minecraft.world.level.levelgen.Heightmap.Types.WORLD_SURFACE_WG,
                builder -> {
                    ChunkPos chunkPos = context.chunkPos();

                    int x = chunkPos.getMiddleBlockX();
                    int z = chunkPos.getMiddleBlockZ();

                    int y = context.chunkGenerator().getFirstOccupiedHeight(
                            x,
                            z,
                            net.minecraft.world.level.levelgen.Heightmap.Types.WORLD_SURFACE_WG,
                            context.heightAccessor(),
                            context.randomState()
                    );

                    BlockPos pos = new BlockPos(x, y, z);
                    builder.addPiece(new ExampleStructurePiece(pos));
                }
        );
    }

    @Override
    public StructureType<?> type() {
        return ModStructures.EXAMPLE_STRUCTURE.get();
    }
}
