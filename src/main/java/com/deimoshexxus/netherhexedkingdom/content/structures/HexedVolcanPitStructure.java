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

        NetherHexedKingdom.LOGGER.info("[HexedVolcanPit] findGenerationPoint called");

        ChunkPos chunkPos = context.chunkPos();
        int centerX = chunkPos.getMiddleBlockX();
        int centerZ = chunkPos.getMiddleBlockZ();

        // --- SAFE NETHER RANGE ---
        int minY = 32;
        int maxY = 90;

        int startY = context.random().nextInt(minY, maxY);

        var column = context.chunkGenerator().getBaseColumn(
                centerX,
                centerZ,
                context.heightAccessor(),
                context.randomState()
        );

        int y = startY;
        boolean foundGround = false;

        for (int i = 0; i < 24 && y > minY; i++) {
            if (column.getBlock(y).isSolid()) {
                foundGround = true;
                break;
            }
            y--;
        }

        if (!foundGround) {
            return Optional.empty();
        }

        Rotation rotation = Rotation.getRandom(context.random());

        StructureTemplateManager manager = context.structureTemplateManager();
        StructureTemplate baseTemplate = manager.getOrCreate(MAIN);

        // --- CENTER THE MAIN TEMPLATE ---
        BlockPos basePos = new BlockPos(
                centerX - baseTemplate.getSize().getX() / 2,
                y + 1,
                centerZ - baseTemplate.getSize().getZ() / 2
        );

        NetherHexedKingdom.LOGGER.info(
                "[HexedVolcanPit] Ground found at Y={}, placing base at {} with rotation {}",
                y, basePos, rotation
        );

        return Optional.of(new GenerationStub(basePos, builder -> {

            // --- MAIN ---
            builder.addPiece(new HexedVolcanPitPiece(
                    manager,
                    MAIN,
                    basePos,
                    rotation,
                    0
            ));

            // --- FOUNDATION ---
            StructureTemplate foundationTemplate =
                    manager.getOrCreate(FOUNDATION);

            int foundationY =
                    basePos.getY() - foundationTemplate.getSize().getY();

            builder.addPiece(new HexedVolcanPitPiece(
                    manager,
                    FOUNDATION,
                    new BlockPos(basePos.getX(), foundationY, basePos.getZ()),
                    rotation,
                    -1
            ));

            NetherHexedKingdom.LOGGER.info(
                    "[HexedVolcanPit] Structure assembled successfully at {}",
                    basePos
            );
        }));
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
