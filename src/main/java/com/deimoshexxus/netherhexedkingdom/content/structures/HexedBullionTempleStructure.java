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

        // --- Get terrain reference (but DO NOT trust Nether ceiling) ---
        int surfaceY = context.chunkGenerator().getFirstOccupiedHeight(
                x,
                z,
                net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                context.heightAccessor(),
                context.randomState()
        );

        // Hard cap to avoid bedrock ceiling influence
        surfaceY = Math.min(surfaceY, 110);

        var manager = context.structureTemplateManager();
        var templateOpt = manager.get(MAIN);

        if (templateOpt.isEmpty()) {
            NetherHexedKingdom.LOGGER.error(
                    "[HexedBullionTemple] Missing template {}",
                    MAIN
            );
            return Optional.empty();
        }

        var template = templateOpt.get();
        int structureHeight = template.getSize().getY();

        // Try multiple attempts
        for (int attempt = 0; attempt < 4; attempt++) {

            // --- ALWAYS push downward into terrain ---
            int yOffset = context.random().nextInt(12, 32);
            int y = surfaceY - yOffset;

            // Ensure full structure fits under max height
            y = Math.min(y, 120 - structureHeight);

            // Final clamp (absolute safety)
            y = net.minecraft.util.Mth.clamp(y, 32, 100);

            BlockPos center = new BlockPos(x, y, z);

            if (!isValidEmbedding(context, center)) {
                continue;
            }

            Rotation rotation = Rotation.getRandom(context.random());

            var settings = new net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings()
                    .setRotation(rotation);

            BoundingBox box = template.getBoundingBox(settings, center);

            // --- STRICT containment check ---
            if (box.minY() < 32 || box.maxY() > 120) {
                continue;
            }

            NetherHexedKingdom.LOGGER.debug(
                    "[HexedBullionTemple] SUCCESS at {} (surfaceY={}, offset={}) rot={} attempt={}",
                    center,
                    surfaceY,
                    yOffset,
                    rotation,
                    attempt
            );

            return Optional.of(new GenerationStub(center, builder -> {
                builder.addPiece(new HexedBullionTemplePiece(
                        manager,
                        MAIN,
                        center,
                        rotation,
                        0
                ));
            }));
        }

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

        // Wider vertical sampling (better for larger structures)
        for (int dy = -10; dy <= 10; dy++) {
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

        // Slightly stricter = avoids exposed placements
        return total > 0 && ((float) solid / total) >= 0.75f;
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