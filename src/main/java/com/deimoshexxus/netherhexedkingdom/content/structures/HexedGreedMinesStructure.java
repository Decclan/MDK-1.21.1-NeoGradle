package com.deimoshexxus.netherhexedkingdom.content.structures;

import com.deimoshexxus.netherhexedkingdom.NetherHexedKingdom;
import com.deimoshexxus.netherhexedkingdom.content.ModStructures;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.pools.alias.PoolAliasLookup;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;


import java.util.Optional;

import static com.deimoshexxus.netherhexedkingdom.content.ModTemplatePools.HEXED_GREED_MINES_START;

public class HexedGreedMinesStructure extends Structure {

    public static final MapCodec<HexedGreedMinesStructure> CODEC =
            simpleCodec(HexedGreedMinesStructure::new);

    public HexedGreedMinesStructure(StructureSettings settings) {
        super(settings);
    }

    @Override
    protected Optional<GenerationStub> findGenerationPoint(GenerationContext context) {

        ChunkPos chunkPos = context.chunkPos();
        int x = chunkPos.getMiddleBlockX();
        int z = chunkPos.getMiddleBlockZ();

        // --- Hard vertical band ---
        final int MIN_Y = 31;
        final int MAX_Y = 42;

        // Get terrain reference (important for "burying")
        int surfaceY = context.chunkGenerator().getFirstOccupiedHeight(
                x,
                z,
                net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                context.heightAccessor(),
                context.randomState()
        );

        // Clamp surface into our allowed band (prevents roof nonsense)
        surfaceY = net.minecraft.util.Mth.clamp(surfaceY, MIN_Y, MAX_Y);

        var column = context.chunkGenerator().getBaseColumn(
                x,
                z,
                context.heightAccessor(),
                context.randomState()
        );

        // Try a few times to find a GOOD embedded position
        for (int attempt = 0; attempt < 5; attempt++) {

            // Bias downward so it's actually buried
            int yOffset = context.random().nextInt(-20, -4);
            int y = surfaceY + yOffset;

            // Clamp hard to required band
            y = net.minecraft.util.Mth.clamp(y, MIN_Y, MAX_Y);

            BlockPos startPos = new BlockPos(x, y, z);

            if (!isValidEmbedding(context, startPos)) {
                continue;
            }

            var poolRegistry = context.registryAccess()
                    .registryOrThrow(Registries.TEMPLATE_POOL);

            Optional<Holder.Reference<StructureTemplatePool>> poolOpt =
                    poolRegistry.getHolder(HEXED_GREED_MINES_START);

            if (poolOpt.isEmpty()) {
                NetherHexedKingdom.LOGGER.error(
                        "[HexedGreedMines] Missing start pool {}",
                        HEXED_GREED_MINES_START.location()
                );
                return Optional.empty();
            }

            Optional<GenerationStub> result = JigsawPlacement.addPieces(
                    context,
                    poolOpt.get(),
                    Optional.empty(),
                    7,
                    startPos,
                    false,
                    Optional.empty(),
                    128,
                    PoolAliasLookup.EMPTY,
                    JigsawStructure.DEFAULT_DIMENSION_PADDING,
                    JigsawStructure.DEFAULT_LIQUID_SETTINGS
            );

            if (result.isPresent()) {
                NetherHexedKingdom.LOGGER.debug(
                        "[HexedGreedMines] SUCCESS at {} (surfaceY={}, offset={}) attempt={}",
                        startPos,
                        surfaceY,
                        yOffset,
                        attempt
                );
                return result;
            }
        }

        NetherHexedKingdom.LOGGER.debug(
                "[HexedGreedMines] FAILED in chunk {}",
                chunkPos
        );

        return Optional.empty();
    }

    private boolean isValidEmbedding(
            GenerationContext context,
            BlockPos center
    ) {
        int solidChecks = 0;
        int totalChecks = 0;

        var generator = context.chunkGenerator();

        // Sample a 5x5 area around the structure center
        for (int dx = -2; dx <= 2; dx++) {
            for (int dz = -2; dz <= 2; dz++) {

                int sampleX = center.getX() + dx * 4;
                int sampleZ = center.getZ() + dz * 4;

                int surfaceY = generator.getBaseHeight(
                        sampleX,
                        sampleZ,
                        net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                        context.heightAccessor(),
                        context.randomState()
                );

                totalChecks++;

                // If terrain is ABOVE our structure → it's buried here
                if (surfaceY > center.getY() + 4) {
                    solidChecks++;
                }
            }
        }

        // Require most samples to be above → properly embedded
        return totalChecks > 0 && ((float) solidChecks / totalChecks) >= 0.7f;
    }

    @Override
    public StructureType<?> type() {
        return ModStructures.HEXED_GREED_MINES_STRUCTURE.get();
    }
}
