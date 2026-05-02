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
        var manager = context.structureTemplateManager();

        var templateOpt = manager.get(MAIN);
        if (templateOpt.isEmpty()) {
            NetherHexedKingdom.LOGGER.error("[HexedLookout] Missing template {}", MAIN);
            return Optional.empty();
        }

        var template = templateOpt.get();

        // --- Nether-safe surface reference ---
        int surfaceY = generator.getFirstOccupiedHeight(
                x,
                z,
                net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                context.heightAccessor(),
                context.randomState()
        );

        // Hard cap to ignore Nether ceiling
        surfaceY = Math.min(surfaceY, 110);

        for (int attempt = 0; attempt < 4; attempt++) {

            // Push DOWN into terrain (fixes ceiling spawning)
            int yOffset = context.random().nextInt(6, 24);
            int y = surfaceY - yOffset;

            int structureHeight = template.getSize().getY();

            // Clamp using structure height
            y = net.minecraft.util.Mth.clamp(y, minBuild + 8, maxBuild - structureHeight);

            Rotation rotation = Rotation.getRandom(context.random());

            var settings = new net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings()
                    .setRotation(rotation);

            // --- Rotation aware centering ---
            var size = template.getSize();

            int sizeX = (rotation == Rotation.CLOCKWISE_90 || rotation == Rotation.CLOCKWISE_180)
                    ? size.getZ()
                    : size.getX();

            int sizeZ = (rotation == Rotation.CLOCKWISE_90 || rotation == Rotation.COUNTERCLOCKWISE_90)
                    ? size.getX()
                    : size.getZ();

            BlockPos pos = new BlockPos(
                    x - sizeX / 2,
                    y,
                    z - sizeZ / 2
            );

            BoundingBox box = template.getBoundingBox(settings, pos);

            // STRICT bounds check (this is what actually prevents out-of-world placement)
            if (box.minY() < minBuild || box.maxY() > maxBuild) {
                continue;
            }

            NetherHexedKingdom.LOGGER.debug(
                    "[HexedLookout] SUCCESS at {} (surfaceY={}, offset={}, rot={}, attempt={})",
                    pos,
                    surfaceY,
                    yOffset,
                    rotation,
                    attempt
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
