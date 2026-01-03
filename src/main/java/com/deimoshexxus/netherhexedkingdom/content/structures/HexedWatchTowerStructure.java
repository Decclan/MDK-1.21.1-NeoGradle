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
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

import java.util.Optional;

public class HexedWatchTowerStructure extends Structure {

    public static final MapCodec<HexedWatchTowerStructure> CODEC =
            simpleCodec(HexedWatchTowerStructure::new);

    private static final ResourceLocation FOUNDATION =
            rl("hexed_watch_tower_foundation");

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

        NetherHexedKingdom.LOGGER.info("[HexedWatchTower] findGenerationPoint called");

        ChunkPos chunkPos = context.chunkPos();
        int x = chunkPos.getMiddleBlockX();
        int z = chunkPos.getMiddleBlockZ();

        NetherHexedKingdom.LOGGER.info(
                "[HexedWatchTower] Chunk {}, evaluating position x={}, z={}",
                chunkPos, x, z
        );

        // --- SAFE NETHER RANGE ---
        int minY = 32;
        int maxY = 90;

        int startY = context.random().nextInt(minY, maxY);
        NetherHexedKingdom.LOGGER.info(
                "[HexedWatchTower] Starting vertical scan at Y={}",
                startY
        );

        var column = context.chunkGenerator().getBaseColumn(
                x,
                z,
                context.heightAccessor(),
                context.randomState()
        );

        int y = startY;
        boolean foundGround = false;

        // Walk downward to find solid ground
        for (int i = 0; i < 24 && y > minY; i++) {
            if (column.getBlock(y).isSolid()) {
                foundGround = true;
                break;
            }
            y--;
        }

        if (!foundGround) {
            NetherHexedKingdom.LOGGER.info(
                    "[HexedWatchTower] No solid ground found in chunk {}, aborting",
                    chunkPos
            );
            return Optional.empty();
        }

        BlockPos basePos = new BlockPos(x, y + 1, z);
        Rotation rotation = Rotation.getRandom(context.random());

        NetherHexedKingdom.LOGGER.info(
                "[HexedWatchTower] Ground found at Y={}, placing base at {} with rotation {}",
                y, basePos, rotation
        );

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


            // --- FOUNDATION ---

            int currentY = basePos.getY() - 5;

            builder.addPiece(new HexedWatchTowerPiece(
                    manager,
                    FOUNDATION,
                    new BlockPos(x, currentY, z),
                    rotation,
                    -1
            ));

            currentY += 13;

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

            NetherHexedKingdom.LOGGER.info(
                    "[HexedWatchTower] Structure assembled successfully at {}",
                    basePos
            );
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
