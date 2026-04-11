package com.deimoshexxus.netherhexedkingdom.content.structures;

import com.deimoshexxus.netherhexedkingdom.NetherHexedKingdom;
import com.deimoshexxus.netherhexedkingdom.content.ModStructures;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;

import java.util.Optional;

public class HexedBullionTempleStructure extends Structure {

    public static final MapCodec<HexedBullionTempleStructure> CODEC =
            simpleCodec(HexedBullionTempleStructure::new);

    private static final ResourceLocation MAIN =
            rl("hexed_bullion_temple");

    public HexedBullionTempleStructure(StructureSettings settings) {
        super(settings);
    }

    @Override
    protected Optional<GenerationStub> findGenerationPoint(GenerationContext context) {

        ChunkPos chunkPos = context.chunkPos();
        int x = chunkPos.getMiddleBlockX();
        int z = chunkPos.getMiddleBlockZ();

        // --- EMBEDDED RANGE (Nether mid-body, avoids lava oceans & roof) ---
        int minY = 48;
        int maxY = 96;

        // Try multiple attempts to find a good embedded location
        for (int attempt = 0; attempt < 5; attempt++) {

            int y = context.random().nextIntBetweenInclusive(minY, maxY);
            BlockPos center = new BlockPos(x, y, z);

            // Validate terrain density (we want mostly solid)
            if (!isValidEmbedding(context, center)) {
                continue;
            }

            Rotation rotation = Rotation.getRandom(context.random());

            NetherHexedKingdom.LOGGER.debug(
                    "[HexedBullionTemple] SUCCESS at {} rot={} attempt={}",
                    center,
                    rotation,
                    attempt
            );

            return Optional.of(new GenerationStub(center, builder -> {
                builder.addPiece(new HexedBullionTemplePiece(
                        context.structureTemplateManager(),
                        MAIN,
                        center,
                        rotation,
                        0
                ));
            }));
        }

        // Failed all attempts
        NetherHexedKingdom.LOGGER.debug(
                "[HexedBullionTemple] FAILED in chunk {}",
                chunkPos
        );

        return Optional.empty();
    }

    /**
     * Ensures the structure is embedded in terrain (not floating or exposed).
     */
    private boolean isValidEmbedding(GenerationContext context, BlockPos center) {

        var column = context.chunkGenerator().getBaseColumn(
                center.getX(),
                center.getZ(),
                context.heightAccessor(),
                context.randomState()
        );

        int solid = 0;
        int total = 0;

        // Sample a vertical slice around the center
        for (int dy = -6; dy <= 6; dy++) {
            int y = center.getY() + dy;

            if (y < context.heightAccessor().getMinBuildHeight() ||
                    y > context.heightAccessor().getMaxBuildHeight()) {
                continue;
            }

            total++;

            if (column.getBlock(y).canOcclude()) {
                solid++;
            }
        }

        // Require at least 70% solid blocks → embedded in terrain
        return total > 0 && ((float) solid / total) >= 0.7f;
    }

    @Override
    public StructureType<?> type() {
        return ModStructures.HEXED_BULLION_TEMPLE_STRUCTURE.get();
    }

    private static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(
                NetherHexedKingdom.MODID,
                path
        );
    }
}