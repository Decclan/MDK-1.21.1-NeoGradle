package com.deimoshexxus.netherhexedkingdom.content.structures;

import com.deimoshexxus.netherhexedkingdom.NetherHexedKingdom;
import com.deimoshexxus.netherhexedkingdom.content.ModStructures;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

import java.util.Optional;

public class HexedLookoutStructure extends Structure {

    public static final MapCodec<HexedLookoutStructure> CODEC =
            simpleCodec(HexedLookoutStructure::new);


    private static final ResourceLocation MAIN =
            rl("hexed_lookout");

    public HexedLookoutStructure(StructureSettings settings) {
        super(settings);
    }

    @Override
    protected Optional<GenerationStub> findGenerationPoint(GenerationContext context) {

        ChunkPos chunkPos = context.chunkPos();
        int x = chunkPos.getMiddleBlockX();
        int z = chunkPos.getMiddleBlockZ();

        int minBuild = context.heightAccessor().getMinBuildHeight();
        int maxBuild = context.heightAccessor().getMaxBuildHeight();

        var generator = context.chunkGenerator();

        // Surface reference (Nether "ceiling-aware")
        int surfaceY = generator.getFirstOccupiedHeight(
                x,
                z,
                net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                context.heightAccessor(),
                context.randomState()
        );

        var manager = context.structureTemplateManager();
        var templateOpt = manager.get(MAIN);

        if (templateOpt.isEmpty()) {
            NetherHexedKingdom.LOGGER.error("[HexedLookout] Missing template {}", MAIN);
            return Optional.empty();
        }

        var template = templateOpt.get();

        // --- Try multiple placements ---
        for (int attempt = 0; attempt < 4; attempt++) {

            // Strong downward bias (keeps it embedded, avoids roof)
            int yOffset = context.random().nextInt(-24, -6);
            int y = surfaceY + yOffset;

            // Clamp to actual dimension limits (not hardcoded Nether guesses)
            y = net.minecraft.util.Mth.clamp(y, minBuild + 8, maxBuild - 8);

            // Extra protection: avoid Nether roof zone specifically
            if (y > maxBuild - 20) {
                continue;
            }

            BlockPos pos = new BlockPos(x, y, z);
            Rotation rotation = Rotation.getRandom(context.random());

            var settings = new net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings()
                    .setRotation(rotation);

            BoundingBox box = template.getBoundingBox(settings, pos);

            // Only reject if completely outside playable range
            if (box.maxY() < minBuild || box.minY() > maxBuild) {
                continue;
            }

            NetherHexedKingdom.LOGGER.info(
                    "[HexedLookout] SUCCESS at {} (surfaceY={}, offset={}, attempt={}) rot={}",
                    pos,
                    surfaceY,
                    yOffset,
                    attempt,
                    rotation
            );

            return Optional.of(new GenerationStub(pos, builder -> {
                builder.addPiece(new HexedLookoutPiece(
                        manager,
                        MAIN,
                        pos,
                        rotation,
                        0
                ));
            }));
        }

        NetherHexedKingdom.LOGGER.debug(
                "[HexedLookout] Failed to find valid position in chunk {}",
                chunkPos
        );

        return Optional.empty();
    }

    @Override
    public StructureType<?> type() {
        return ModStructures.HEXED_LOOKOUT_STRUCTURE.get();
    }

    private static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(
                NetherHexedKingdom.MODID,
                path
        );
    }
}
