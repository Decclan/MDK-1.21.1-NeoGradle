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

public class HexedNetherFossilStructure extends Structure {

    public static final MapCodec<HexedNetherFossilStructure> CODEC =
            simpleCodec(HexedNetherFossilStructure::new);

    private static final ResourceLocation[] FOSSILS = {
            rl("nether_fossils/fossil_masoniae_1"),
            rl("nether_fossils/fossil_masoniae_2"),
            rl("nether_fossils/fossil_masoniae_3")
    };

    public HexedNetherFossilStructure(StructureSettings settings) {
        super(settings);
    }

    @Override
    protected Optional<GenerationStub> findGenerationPoint(GenerationContext context) {

        NetherHexedKingdom.LOGGER.info("[HexedNetherFossil] findGenerationPoint called");

        ChunkPos chunkPos = context.chunkPos();
        int x = chunkPos.getMiddleBlockX();
        int z = chunkPos.getMiddleBlockZ();

        NetherHexedKingdom.LOGGER.info(
                "[HexedNetherFossil] Evaluating chunk {} (x={}, z={})",
                chunkPos, x, z
        );

        // --- SAFE NETHER RANGE ---
        int minY = 32;
        int maxY = 80;

        int startY = context.random().nextInt(minY, maxY);

        var column = context.chunkGenerator().getBaseColumn(
                x,
                z,
                context.heightAccessor(),
                context.randomState()
        );

        int y = startY;
        boolean foundGround = false;

        for (int i = 0; i < 32 && y > minY; i++) {
            if (column.getBlock(y).isSolid()) {
                foundGround = true;
                break;
            }
            y--;
        }

        if (!foundGround) {
            NetherHexedKingdom.LOGGER.info(
                    "[HexedNetherFossil] No ground found in chunk {}, aborting",
                    chunkPos
            );
            return Optional.empty();
        }

        // --- SELECT RANDOM FOSSIL ---
        ResourceLocation fossilId =
                FOSSILS[context.random().nextInt(FOSSILS.length)];

        NetherHexedKingdom.LOGGER.info(
                "[HexedNetherFossil] Selected fossil template: {}",
                fossilId
        );

        StructureTemplateManager manager =
                context.structureTemplateManager();

        StructureTemplate template =
                manager.getOrCreate(fossilId);

        // --- CENTER THE FOSSIL ---
        BlockPos basePos = new BlockPos(
                x - template.getSize().getX() / 2,
                y + 1,
                z - template.getSize().getZ() / 2
        );

        Rotation rotation = Rotation.getRandom(context.random());

        NetherHexedKingdom.LOGGER.info(
                "[HexedNetherFossil] Ground found at Y={}, placing fossil at {} with rotation {}",
                y, basePos, rotation
        );

        return Optional.of(new GenerationStub(basePos, builder -> {

            builder.addPiece(new HexedNetherFossilPiece(
                    manager,
                    fossilId,
                    basePos,
                    rotation,
                    0
            ));

            NetherHexedKingdom.LOGGER.info(
                    "[HexedNetherFossil] Fossil placed successfully at {}",
                    basePos
            );
        }));
    }

    @Override
    public StructureType<?> type() {
        return ModStructures.HEXED_NETHER_FOSSIL.get();
    }

    private static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(
                NetherHexedKingdom.MODID,
                path
        );
    }
}
