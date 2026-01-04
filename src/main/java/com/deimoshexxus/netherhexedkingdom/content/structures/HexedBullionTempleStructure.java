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

        NetherHexedKingdom.LOGGER.info("[HexedBullionTemple] findGenerationPoint called");

        ChunkPos chunkPos = context.chunkPos();
        int x = chunkPos.getMiddleBlockX();
        int z = chunkPos.getMiddleBlockZ();

        NetherHexedKingdom.LOGGER.info(
                "[HexedBullionTemple] Chunk {}, evaluating position x={}, z={}",
                chunkPos, x, z
        );

        // --- SAFE NETHER RANGE ---
        int minY = 64;
        int maxY = 96;

        int startY = context.random().nextInt(minY, maxY);
        NetherHexedKingdom.LOGGER.info(
                "[HexedBullionTemple] Starting vertical scan at Y={}",
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
                    "[HexedBullionTemple] No solid ground found in chunk {}, aborting",
                    chunkPos
            );
            return Optional.empty();
        }

        BlockPos basePos = new BlockPos(x, y + 1, z);
        Rotation rotation = Rotation.getRandom(context.random());

        NetherHexedKingdom.LOGGER.info(
                "[HexedBullionTemple] Ground found at Y={}, placing base at {} with rotation {}",
                y, basePos, rotation
        );

        return Optional.of(new GenerationStub(basePos, builder -> {

            StructureTemplateManager manager =
                    context.structureTemplateManager();

            // --- BASE ---
            builder.addPiece(new HexedBullionTemplePiece(
                    manager,
                    MAIN,
                    basePos,
                    rotation,
                    0
            ));


            NetherHexedKingdom.LOGGER.info(
                    "[HexedBullionTemple] Structure assembled successfully at {}",
                    basePos
            );
        }));
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
