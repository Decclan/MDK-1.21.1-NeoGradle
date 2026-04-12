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

public class HexedBloodPyramidStructure extends Structure {

    public static final MapCodec<HexedBloodPyramidStructure> CODEC =
            simpleCodec(HexedBloodPyramidStructure::new);

    private static final ResourceLocation CORNER =
            rl("hexed_blood_pyramid/hexed_blood_pyramid_corner");

    private static final ResourceLocation[] FOUNDATIONS = {
            rl("hexed_blood_pyramid/hexed_blood_temple_foundation_1"),
            rl("hexed_blood_pyramid/hexed_blood_temple_foundation_2"),
            rl("hexed_blood_pyramid/hexed_blood_temple_foundation_3"),
            rl("hexed_blood_pyramid/hexed_blood_temple_foundation_4"),
            rl("hexed_blood_pyramid/hexed_blood_temple_foundation_5"),
            rl("hexed_blood_pyramid/hexed_blood_temple_foundation_6"),
            rl("hexed_blood_pyramid/hexed_blood_temple_foundation_7"),
            rl("hexed_blood_pyramid/hexed_blood_temple_foundation_8"),
            rl("hexed_blood_pyramid/hexed_blood_temple_foundation_9")
    };

    public HexedBloodPyramidStructure(StructureSettings settings) {
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

        var size = cornerTemplate.getSize(Rotation.NONE);

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
                    new BlockPos(0, 0, 0),                         // NW (tile 0)
                    new BlockPos(size.getX(), 0, 0),               // NE (tile 1)
                    new BlockPos(0, 0, size.getZ()),               // SW (tile 2)
                    new BlockPos(size.getX(), 0, size.getZ())      // SE (tile 3)
            };

            for (int i = 0; i < 4; i++) {

                // Correct inward-facing rotations
                Rotation localRotation = switch (i) {
                    case 0 -> Rotation.NONE;
                    case 1 -> Rotation.CLOCKWISE_90;
                    case 2 -> Rotation.COUNTERCLOCKWISE_90;
                    case 3 -> Rotation.CLOCKWISE_180;
                    default -> Rotation.NONE;
                };

                Rotation finalRotation = localRotation.getRotated(baseRotation);

                // ✅ Rotate offset into world space
                BlockPos rotatedOffset = rotateOffset(localOffsets[i], baseRotation);
                BlockPos piecePos = startPos.offset(rotatedOffset);

                // ✅ FOUNDATION ONLY FOR FIRST TILE
                if (i == 0) {
                    ResourceLocation foundationId =
                            FOUNDATIONS[context.random().nextInt(FOUNDATIONS.length)];

                    StructureTemplate foundationTemplate =
                            manager.getOrCreate(foundationId);

                    var foundationSize = foundationTemplate.getSize(finalRotation);

                    BlockPos foundationPos = piecePos.below(foundationSize.getY());

                    builder.addPiece(piece(
                            manager,
                            foundationId,
                            foundationPos,
                            finalRotation,
                            -1
                    ));
                }

                // CORNER PIECE
                builder.addPiece(piece(
                        manager,
                        CORNER,
                        piecePos,
                        finalRotation,
                        0
                ));
            }

            NetherHexedKingdom.LOGGER.debug(
                    "[BloodPyramid] Generated at {} (groundY={})",
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

    private static HexedBloodPyramidPiece piece(
            StructureTemplateManager manager,
            ResourceLocation id,
            BlockPos pos,
            Rotation rotation,
            int depth
    ) {
        return new HexedBloodPyramidPiece(manager, id, pos, rotation, depth);
    }

    private static BlockPos rotateOffset(BlockPos offset, Rotation rotation) {
        return switch (rotation) {
            case NONE -> offset;
            case CLOCKWISE_90 -> new BlockPos(-offset.getZ(), offset.getY(), offset.getX());
            case CLOCKWISE_180 -> new BlockPos(-offset.getX(), offset.getY(), -offset.getZ());
            case COUNTERCLOCKWISE_90 -> new BlockPos(offset.getZ(), offset.getY(), -offset.getX());
        };
    }

    @Override
    public StructureType<?> type() {
        return ModStructures.HEXED_BLOOD_PYRAMID_STRUCTURE.get();
    }

    private static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(
                NetherHexedKingdom.MODID,
                path
        );
    }
}