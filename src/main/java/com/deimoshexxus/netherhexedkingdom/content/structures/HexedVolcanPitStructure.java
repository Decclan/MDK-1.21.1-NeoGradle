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
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

import java.util.Optional;

public class HexedVolcanPitStructure extends Structure {

    public static final MapCodec<HexedVolcanPitStructure> CODEC =
            simpleCodec(HexedVolcanPitStructure::new);

    private static final ResourceLocation FOUNDATION =
            rl("hexed_volcan_pit_bottom");

    private static final ResourceLocation MAIN =
            rl("hexed_volcan_pit");

    public HexedVolcanPitStructure(StructureSettings settings) {
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
        var manager = context.structureTemplateManager();

        StructureTemplate mainTemplate = manager.getOrCreate(MAIN);
        StructureTemplate foundationTemplate = manager.getOrCreate(FOUNDATION);

        // --- Terrain reference (Basalt Deltas is uneven, so we anchor carefully) ---
        int surfaceY = generator.getFirstOccupiedHeight(
                x,
                z,
                net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                context.heightAccessor(),
                context.randomState()
        );

        for (int attempt = 0; attempt < 4; attempt++) {

            // Strong downward bias into basalt delta terrain
            int yOffset = context.random().nextInt(-18, -4);
            int y = surfaceY + yOffset;

            // Clamp into valid Nether build space
            y = net.minecraft.util.Mth.clamp(y, minBuild + 8, maxBuild - 40);

            BlockPos basePos = new BlockPos(
                    x - mainTemplate.getSize().getX() / 2,
                    y,
                    z - mainTemplate.getSize().getZ() / 2
            );

            Rotation rotation = Rotation.getRandom(context.random());

            var settings = new net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings()
                    .setRotation(rotation);

            var box = mainTemplate.getBoundingBox(settings, basePos);

            // Reject only completely invalid placements
            if (box.maxY() < minBuild || box.minY() > maxBuild) {
                continue;
            }

            if (!isSolidEnough(context, basePos)) {
                continue;
            }

            // --- Foundation placement (correctly anchored under rotated main) ---
            int foundationY = basePos.getY() - foundationTemplate.getSize().getY();

            BlockPos foundationPos = new BlockPos(
                    basePos.getX(),
                    foundationY,
                    basePos.getZ()
            );

            NetherHexedKingdom.LOGGER.info(
                    "[HexedVolcanPit] SUCCESS at {} surfaceY={} offset={} rot={} attempt={}",
                    basePos,
                    surfaceY,
                    yOffset,
                    rotation,
                    attempt
            );

            return Optional.of(new GenerationStub(basePos, builder -> {

                builder.addPiece(new HexedVolcanPitPiece(
                        manager,
                        MAIN,
                        basePos,
                        rotation,
                        0
                ));

                builder.addPiece(new HexedVolcanPitPiece(
                        manager,
                        FOUNDATION,
                        foundationPos,
                        rotation,
                        -1
                ));

                NetherHexedKingdom.LOGGER.info(
                        "[HexedVolcanPit] Assembled successfully at {}",
                        basePos
                );
            }));
        }

        NetherHexedKingdom.LOGGER.debug(
                "[HexedVolcanPit] Failed to place in chunk {}",
                chunkPos
        );

        return Optional.empty();
    }

    /**
     * Ensures structure is embedded into basalt terrain instead of floating in air pockets.
     */
    private boolean isSolidEnough(GenerationContext context, BlockPos pos) {

        var column = context.chunkGenerator().getBaseColumn(
                pos.getX(),
                pos.getZ(),
                context.heightAccessor(),
                context.randomState()
        );

        int solid = 0;
        int total = 0;

        for (int dy = -6; dy <= 6; dy++) {

            int y = pos.getY() + dy;

            if (y < context.heightAccessor().getMinBuildHeight()
                    || y > context.heightAccessor().getMaxBuildHeight()) {
                continue;
            }

            total++;

            if (column.getBlock(y).canOcclude()) {
                solid++;
            }
        }

        return total > 0 && ((float) solid / total) >= 0.65f;
    }

    @Override
    public StructureType<?> type() {
        return ModStructures.HEXED_VOLCAN_PIT_STRUCTURE.get();
    }

    private static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(
                NetherHexedKingdom.MODID,
                path
        );
    }
}