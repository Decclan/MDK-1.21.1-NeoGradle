package com.deimoshexxus.netherhexedkingdom.content.structures;

import com.deimoshexxus.netherhexedkingdom.NetherHexedKingdom;
import com.deimoshexxus.netherhexedkingdom.content.ModStructures;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.BlockColumn;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

import java.util.Optional;

public class HexedOutpostStructure extends Structure {

    public static final MapCodec<HexedOutpostStructure> CODEC =
            simpleCodec(HexedOutpostStructure::new);

    private static final ResourceLocation FOUNDATION = rl("hexed_outpost_foundation");
    private static final ResourceLocation BASE = rl("hexed_outpost");

    private static final ResourceLocation[] TOPS = {
            rl("hexed_outpost_top"),
            rl("hexed_outpost_top_2")
    };

    public HexedOutpostStructure(StructureSettings settings) {
        super(settings);
    }

    @Override
    protected Optional<GenerationStub> findGenerationPoint(GenerationContext context) {

        ChunkPos chunk = context.chunkPos();
        int x = chunk.getMiddleBlockX();
        int z = chunk.getMiddleBlockZ();

        int minY = 31;
        int maxY = 64;

        int startY = context.random().nextInt(minY, maxY);

        BlockColumn column = context.chunkGenerator().getBaseColumn(
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

        ResourceLocation topId = TOPS[context.random().nextInt(TOPS.length)];
        StructureTemplate top = manager.getOrCreate(topId);

        // --- ROTATION-AWARE SIZES ---
        var baseSize = base.getSize(rotation);
        var foundationSize = foundation.getSize(rotation);

        // --- CENTER BASE ---
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

            // TOP
            currentY += baseSize.getY();
            builder.addPiece(piece(manager, topId, basePos.atY(currentY), rotation, 1));

            NetherHexedKingdom.LOGGER.debug(
                    "[Outpost] Generated at {} (groundY={})",
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

    private static HexedOutpostPiece piece(
            StructureTemplateManager manager,
            ResourceLocation id,
            BlockPos pos,
            Rotation rotation,
            int depth
    ) {
        return new HexedOutpostPiece(manager, id, pos, rotation, depth);
    }

    @Override
    public StructureType<?> type() {
        return ModStructures.HEXED_OUTPOST_STRUCTURE.get();
    }

    private static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(
                NetherHexedKingdom.MODID,
                path
        );
    }
}