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
import net.minecraft.world.level.chunk.BlockColumn;

import java.util.Optional;

public class HexedRedPyramidStructure extends Structure {

    public static final MapCodec<HexedRedPyramidStructure> CODEC =
            simpleCodec(HexedRedPyramidStructure::new);

    private static final ResourceLocation CORNER =
            rl("hexed_red_pyramid/hexed_red_pyramid_corner");

    private static final ResourceLocation[] FOUNDATIONS = {
            rl("hexed_red_pyramid/hexed_red_pyramid_foundation_1"),
            rl("hexed_red_pyramid/hexed_red_pyramid_foundation_2"),
            rl("hexed_red_pyramid/hexed_red_pyramid_foundation_3"),
            rl("hexed_red_pyramid/hexed_red_pyramid_foundation_4"),
            rl("hexed_red_pyramid/hexed_red_pyramid_foundation_5"),
            rl("hexed_red_pyramid/hexed_red_pyramid_foundation_6"),
            rl("hexed_red_pyramid/hexed_red_pyramid_foundation_7"),
            rl("hexed_red_pyramid/hexed_red_pyramid_foundation_8"),
            rl("hexed_red_pyramid/hexed_red_pyramid_foundation_9")
    };

    public HexedRedPyramidStructure(StructureSettings settings) {
        super(settings);
    }

    @Override
    protected Optional<GenerationStub> findGenerationPoint(GenerationContext context) {

        ChunkPos chunk = context.chunkPos();
        int x = chunk.getMiddleBlockX();
        int z = chunk.getMiddleBlockZ();

        int minY = 32;
        int maxY = 90;

        int startY = context.random().nextInt(minY, maxY);

        BlockColumn column = context.chunkGenerator().getBaseColumn(
                x, z,
                context.heightAccessor(),
                context.randomState()
        );

        int groundY = findGround(column, startY, minY);
        if (groundY == -1) return Optional.empty();

        StructureTemplateManager manager = context.structureTemplateManager();
        StructureTemplate cornerTemplate = manager.getOrCreate(CORNER);

        var sizeVec = cornerTemplate.getSize(Rotation.NONE);
        BlockPos size = new BlockPos(sizeVec);

        Rotation baseRotation = Rotation.getRandom(context.random());

        // Anchor = NW corner of the full 2x2 structure
        BlockPos startPos = new BlockPos(
                x - size.getX(),
                groundY + 1,
                z - size.getZ()
        );

        return Optional.of(new GenerationStub(startPos, builder -> {

            // Local 2x2 offsets (unrotated)
            BlockPos[] localOffsets = new BlockPos[] {
                    new BlockPos(0, 0, 0),
                    new BlockPos(size.getX(), 0, 0),
                    new BlockPos(0, 0, size.getZ()),
                    new BlockPos(size.getX(), 0, size.getZ())
            };

            // --- SELECT 4 UNIQUE FOUNDATIONS ---
            ResourceLocation[] selected = new ResourceLocation[4];
            boolean[] used = new boolean[FOUNDATIONS.length];

            for (int j = 0; j < 4; j++) {
                int idx;
                do {
                    idx = context.random().nextInt(FOUNDATIONS.length);
                } while (used[idx]);

                used[idx] = true;
                selected[j] = FOUNDATIONS[idx];
            }

            for (int i = 0; i < 4; i++) {

                Rotation localRotation = switch (i) {
                    case 0 -> Rotation.CLOCKWISE_180;
                    case 1 -> Rotation.CLOCKWISE_90;
                    case 2 -> Rotation.COUNTERCLOCKWISE_90;
                    case 3 -> Rotation.NONE;
                    default -> Rotation.NONE;
                };

                Rotation finalRotation = localRotation.getRotated(baseRotation);

                // ORIGINAL (WORKING) POSITION LOGIC — UNCHANGED
                BlockPos rotatedOffset = rotateOffset(localOffsets[i], baseRotation);
                BlockPos pivotFix = getPivotCorrection(size, finalRotation);

                BlockPos piecePos = startPos
                        .offset(rotatedOffset)
                        .offset(pivotFix);

                // --- FOUNDATION (ALL 4, UNIQUE, FIXED HEIGHT) ---
                ResourceLocation foundationId = selected[i];

                StructureTemplate foundationTemplate = manager.getOrCreate(foundationId);

// get correct height
                int foundationDepth = foundationTemplate.getSize(finalRotation).getY();

// place foundation so its TOP aligns exactly with corner base
                BlockPos foundationPos = piecePos.below(foundationDepth);

// slight safety offset (prevents bounding box merge/cull edge case)
                foundationPos = foundationPos.offset(0, -1, 0);

                builder.addPiece(piece(
                        manager,
                        foundationId,
                        foundationPos,
                        finalRotation,
                        -1
                ));

                // --- CORNER ---
                builder.addPiece(piece(
                        manager,
                        CORNER,
                        piecePos,
                        finalRotation,
                        0
                ));
            }

            NetherHexedKingdom.LOGGER.debug(
                    "[RedPyramid] Generated at {} (groundY={})",
                    startPos,
                    groundY
            );
        }));
    }

    // --- HELPERS ---

    private static int findGround(BlockColumn column, int startY, int minY) {
        for (int y = startY; y >= minY; y--) {
            if (column.getBlock(y).canOcclude()) {
                return y;
            }
        }
        return -1;
    }

    private static HexedRedPyramidPiece piece(
            StructureTemplateManager manager,
            ResourceLocation id,
            BlockPos pos,
            Rotation rotation,
            int depth
    ) {
        return new HexedRedPyramidPiece(manager, id, pos, rotation, depth);
    }

    private static BlockPos rotateOffset(BlockPos offset, Rotation rotation) {
        return switch (rotation) {
            case NONE -> offset;
            case CLOCKWISE_90 -> new BlockPos(-offset.getZ(), offset.getY(), offset.getX());
            case CLOCKWISE_180 -> new BlockPos(-offset.getX(), offset.getY(), -offset.getZ());
            case COUNTERCLOCKWISE_90 -> new BlockPos(offset.getZ(), offset.getY(), -offset.getX());
        };
    }

    private static BlockPos getPivotCorrection(BlockPos size, Rotation rotation) {
        return switch (rotation) {
            case NONE -> BlockPos.ZERO;
            case CLOCKWISE_90 -> new BlockPos(0, 0, -size.getX());
            case CLOCKWISE_180 -> new BlockPos(-size.getX(), 0, -size.getZ());
            case COUNTERCLOCKWISE_90 -> new BlockPos(-size.getZ(), 0, 0);
        };
    }

    @Override
    public StructureType<?> type() {
        return ModStructures.HEXED_RED_PYRAMID_STRUCTURE.get();
    }

    private static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(
                NetherHexedKingdom.MODID,
                path
        );
    }
}