package com.deimoshexxus.netherhexedkingdom.content.structures;

import com.deimoshexxus.netherhexedkingdom.NetherHexedKingdom;
import com.deimoshexxus.netherhexedkingdom.content.ModStructures;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.Optional;

public class HexedNetherFossilStructure extends Structure {

    public static final MapCodec<HexedNetherFossilStructure> CODEC =
            simpleCodec(HexedNetherFossilStructure::new);


//    private static final ResourceLocation MAIN =
//            rl("hexed_nether_fossil");

    private static final ResourceLocation[] FOSSILS = {
            rl("nether_fossils/fossil_masoniae_1"),
            rl("nether_fossils/fossil_masoniae_2"),
            rl("nether_fossils/fossil_masoniae_3"),
            rl("nether_fossils/fossil_masoniae_4"),
            rl("nether_fossils/fossil_masoniae_5"),
            rl("nether_fossils/fossil_masoniae_6")
    };

    public HexedNetherFossilStructure(StructureSettings settings) {
        super(settings);
    }

    @Override
    protected Optional<GenerationStub> findGenerationPoint(GenerationContext context) {

        ChunkPos chunkPos = context.chunkPos();
        int x = chunkPos.getMiddleBlockX();
        int z = chunkPos.getMiddleBlockZ();

        int minY = 33;
        int maxY = 108;

        int startY = context.random().nextIntBetweenInclusive(minY, maxY);

        var column = context.chunkGenerator().getBaseColumn(
                x,
                z,
                context.heightAccessor(),
                context.randomState()
        );

        int groundY = -1;

        for (int y = startY; y >= minY; y--) {
            if (column.getBlock(y).canOcclude()) {
                groundY = y;
                break;
            }
        }

        if (groundY == -1) {
            return Optional.empty();
        }

//        BlockPos basePos = new BlockPos(x, groundY + 1, z);
//        Rotation rotation = Rotation.getRandom(context.random());
//
//        StructureTemplateManager templateManager = context.structureTemplateManager();
//        StructureTemplate template = templateManager.getOrCreate(MAIN);
//
//        if (template == null) {
//            return Optional.empty();
//        }
//
//        // Get rotated size
//        var size = template.getSize(rotation);
//
//        BoundingBox box = BoundingBox.fromCorners(
//                basePos,
//                basePos.offset(size.getX(), size.getY(), size.getZ())
//        );

        ResourceLocation chosen = FOSSILS[
                context.random().nextInt(FOSSILS.length)
                ];

        NetherHexedKingdom.LOGGER.info("[HexedNetherFossil] Selected fossil template: {}", chosen);
        BlockPos basePos = new BlockPos(x, groundY, z); // lowered by 1

        Rotation rotation = Rotation.getRandom(context.random());

        StructureTemplateManager templateManager = context.structureTemplateManager();
        StructureTemplate template = templateManager.getOrCreate(chosen);

        var size = template.getSize(rotation);

        BoundingBox box = BoundingBox.fromCorners(
                basePos,
                basePos.offset(size.getX(), size.getY(), size.getZ())
        );

        // Optional: reject if intersecting surface
        int surface = context.chunkGenerator().getFirstFreeHeight(
                x,
                z,
                Heightmap.Types.WORLD_SURFACE_WG,
                context.heightAccessor(),
                context.randomState()
        );

        if (box.maxY() > surface) {
            return Optional.empty();
        }

        return Optional.of(new GenerationStub(basePos, builder -> {
            builder.addPiece(new HexedNetherFossilPiece(
                    templateManager,
                    chosen,//MAIN,
                    basePos,
                    rotation,
                    0
            ));
        }));
    }

    @Override
    public StructureType<?> type() {
        return ModStructures.HEXED_NETHER_FOSSIL_STRUCTURE.get();
    }

    private static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(
                NetherHexedKingdom.MODID,
                path
        );
    }
}
