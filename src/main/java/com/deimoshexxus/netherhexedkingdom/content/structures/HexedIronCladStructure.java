package com.deimoshexxus.netherhexedkingdom.content.structures;

import com.deimoshexxus.netherhexedkingdom.NetherHexedKingdom;
import com.deimoshexxus.netherhexedkingdom.content.ModStructures;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

import java.util.Optional;

public class HexedIronCladStructure extends Structure {

    public static final MapCodec<HexedIronCladStructure> CODEC =
            simpleCodec(HexedIronCladStructure::new);


    private static final ResourceLocation MAIN =
            rl("hexed_iron_clad");

    public HexedIronCladStructure(StructureSettings settings) {
        super(settings);
    }

    @Override
    protected Optional<GenerationStub> findGenerationPoint(GenerationContext context) {

        ChunkPos chunkPos = context.chunkPos();
        int x = chunkPos.getMiddleBlockX();
        int z = chunkPos.getMiddleBlockZ();

        int minY = 20;
        int maxY = 120;

        var column = context.chunkGenerator().getBaseColumn(
                x,
                z,
                context.heightAccessor(),
                context.randomState()
        );

        int lavaSurfaceY = -1;

        // Find lava surface - lava with non-lava above
        for (int y = maxY; y >= minY; y--) {
            if (column.getBlock(y).is(Blocks.LAVA)
                    && !column.getBlock(y + 1).is(Blocks.LAVA)) {

                lavaSurfaceY = y;
                break;
            }
        }

        if (lavaSurfaceY == -1) {
            return Optional.empty();
        }

        // Offset DOWN by 2
        BlockPos basePos = new BlockPos(x, lavaSurfaceY - 2, z);

        Rotation rotation = Rotation.getRandom(context.random());

        NetherHexedKingdom.LOGGER.info(
                "[HexedIronClad] Lava surface at Y={}, placing at {} (offset -2)",
                lavaSurfaceY,
                basePos
        );

        return Optional.of(new GenerationStub(basePos, builder -> {
            builder.addPiece(new HexedIronCladPiece(
                    context.structureTemplateManager(),
                    MAIN,
                    basePos,
                    rotation,
                    0
            ));
        }));
    }

    @Override
    public StructureType<?> type() {
        return ModStructures.HEXED_IRON_CLAD_STRUCTURE.get();
    }

    private static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(
                NetherHexedKingdom.MODID,
                path
        );
    }
}
