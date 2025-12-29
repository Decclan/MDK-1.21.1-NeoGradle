package com.deimoshexxus.netherhexedkingdom.content.structures;

import com.deimoshexxus.netherhexedkingdom.NetherHexedKingdom;
import com.deimoshexxus.netherhexedkingdom.content.ModStructurePieces;
import com.deimoshexxus.netherhexedkingdom.content.ModStructures;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

import java.util.Optional;

public class HexedWatchTowerStructure extends Structure {

    public static final MapCodec<HexedWatchTowerStructure> CODEC =
            simpleCodec(HexedWatchTowerStructure::new);

    private static final ResourceLocation BASE =
            rl("hexed_watch_tower_base");

    private static final ResourceLocation[] COLUMNS = {
            rl("hexed_watch_tower_column"),
            rl("hexed_watch_tower_column_2"),
            rl("hexed_watch_tower_column_3"),
            rl("hexed_watch_tower_column_4"),
            rl("hexed_watch_tower_column_5"),
            rl("hexed_watch_tower_column_6")
    };

    private static final ResourceLocation[] TOPS = {
            rl("hexed_watch_tower_top"),
            rl("hexed_watch_tower_top_2"),
            rl("hexed_watch_tower_top_3"),
            rl("hexed_watch_tower_top_4"),
            rl("hexed_watch_tower_top_5"),
            rl("hexed_watch_tower_top_6")
    };

    public HexedWatchTowerStructure(StructureSettings settings) {
        super(settings);
    }

    @Override
    protected Optional<GenerationStub> findGenerationPoint(GenerationContext context) {

        ChunkPos chunkPos = context.chunkPos();
        int x = chunkPos.getMiddleBlockX();
        int z = chunkPos.getMiddleBlockZ();

        // --- SAFE NETHER RANGE ---
        int minY = 32;
        int maxY = 90;

        int startY = context.random().nextInt(minY, maxY);

        var column = context.chunkGenerator().getBaseColumn(
                x,
                z,
                context.heightAccessor(),
                context.randomState()
        );

        int y = startY;

        // Walk downward to find solid ground (max 24 blocks)
        for (int i = 0; i < 24 && y > minY; i++) {
            if (column.getBlock(y).isSolid()) {
                break;
            }
            y--;
        }

        // Abort if no suitable ground found
        if (y <= minY) {
            return Optional.empty();
        }

        BlockPos basePos = new BlockPos(x, y + 1, z);
        Rotation rotation = Rotation.getRandom(context.random());

        return Optional.of(new GenerationStub(basePos, builder -> {

            StructureTemplateManager manager =
                    context.structureTemplateManager();

            // --- BASE ---
            builder.addPiece(new HexedWatchTowerPiece(
                    manager,
                    BASE,
                    basePos,
                    rotation,
                    0
            ));

            int currentY = basePos.getY() + 8;

            // --- COLUMN ---
            ResourceLocation columnPiece =
                    COLUMNS[context.random().nextInt(COLUMNS.length)];

            builder.addPiece(new HexedWatchTowerPiece(
                    manager,
                    columnPiece,
                    new BlockPos(x, currentY, z),
                    rotation,
                    1
            ));

            currentY += 20;

            // --- TOP ---
            ResourceLocation top =
                    TOPS[context.random().nextInt(TOPS.length)];

            builder.addPiece(new HexedWatchTowerPiece(
                    manager,
                    top,
                    new BlockPos(x, currentY, z),
                    rotation,
                    2
            ));
        }));
    }

    @Override
    public StructureType<?> type() {
        return ModStructures.HEXED_WATCH_TOWER_STRUCTURE.get();
    }

    private static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(
                NetherHexedKingdom.MODID,
                path
        );
    }
}
