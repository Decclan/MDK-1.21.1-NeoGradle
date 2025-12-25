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

        int y = context.chunkGenerator()
                .getFirstOccupiedHeight(
                        x,
                        z,
                        Heightmap.Types.WORLD_SURFACE_WG,
                        context.heightAccessor(),
                        context.randomState()
                );

        BlockPos basePos = new BlockPos(x, y, z);
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

            // height of base NBT (example: 6)
            int currentY = y + 6;

            // --- COLUMN (random) ---
            ResourceLocation column =
                    COLUMNS[context.random().nextInt(COLUMNS.length)];

            builder.addPiece(new HexedWatchTowerPiece(
                    manager,
                    column,
                    new BlockPos(x, currentY, z),
                    rotation,
                    1
            ));

            // height of column NBT (example: 10)
            currentY += 10;

            // --- TOP (random) ---
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
