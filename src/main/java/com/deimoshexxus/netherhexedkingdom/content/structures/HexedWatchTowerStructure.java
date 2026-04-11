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

public class HexedWatchTowerStructure extends Structure {

    public static final MapCodec<HexedWatchTowerStructure> CODEC =
            simpleCodec(HexedWatchTowerStructure::new);

    private static final ResourceLocation FOUNDATION = rl("hexed_watch_tower_foundation");
    private static final ResourceLocation BASE = rl("hexed_watch_tower_base");

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

        ChunkPos chunk = context.chunkPos();
        int x = chunk.getMiddleBlockX();
        int z = chunk.getMiddleBlockZ();

        int minY = 32;
        int maxY = 90;

        int startY = context.random().nextInt(minY, maxY);

        var column = context.chunkGenerator().getBaseColumn(
                x, z,
                context.heightAccessor(),
                context.randomState()
        );

        int groundY = findGround(column, startY, minY);
        if (groundY == -1) return Optional.empty();

        Rotation rotation = Rotation.getRandom(context.random());
        StructureTemplateManager manager = context.structureTemplateManager();

        // --- LOAD TEMPLATES ONCE ---
        StructureTemplate base = manager.getOrCreate(BASE);
        StructureTemplate foundation = manager.getOrCreate(FOUNDATION);

        ResourceLocation columnId = COLUMNS[context.random().nextInt(COLUMNS.length)];
        StructureTemplate columnTemplate = manager.getOrCreate(columnId);

        ResourceLocation topId = TOPS[context.random().nextInt(TOPS.length)];
        StructureTemplate topTemplate = manager.getOrCreate(topId);

        // --- ROTATION-AWARE SIZE ---
        var baseSize = base.getSize(rotation);
        var foundationSize = foundation.getSize(rotation);
        var columnSize = columnTemplate.getSize(rotation);

        // --- CENTER BASE CORRECTLY ---
        BlockPos basePos = new BlockPos(
                x - baseSize.getX() / 2,
                groundY + 1,
                z - baseSize.getZ() / 2
        );

        return Optional.of(new GenerationStub(basePos, builder -> {

            int currentY = basePos.getY();

            // FOUNDATION
            currentY -= foundationSize.getY();
            builder.addPiece(piece(manager, FOUNDATION, basePos.atY(currentY), rotation, -1));

            // BASE
            currentY += foundationSize.getY();
            builder.addPiece(piece(manager, BASE, basePos, rotation, 0));

            // COLUMN
            currentY += baseSize.getY();
            builder.addPiece(piece(manager, columnId, basePos.atY(currentY), rotation, 1));

            // TOP
            currentY += columnSize.getY();
            builder.addPiece(piece(manager, topId, basePos.atY(currentY), rotation, 2));

            NetherHexedKingdom.LOGGER.debug(
                    "[WatchTower] Generated at {} (groundY={})",
                    basePos, groundY
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

    private static HexedWatchTowerPiece piece(
            StructureTemplateManager manager,
            ResourceLocation id,
            BlockPos pos,
            Rotation rotation,
            int depth
    ) {
        return new HexedWatchTowerPiece(manager, id, pos, rotation, depth);
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