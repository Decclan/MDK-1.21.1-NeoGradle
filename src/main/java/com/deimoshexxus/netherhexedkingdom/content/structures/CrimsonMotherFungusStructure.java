package com.deimoshexxus.netherhexedkingdom.content.structures;

import com.deimoshexxus.netherhexedkingdom.NetherHexedKingdom;
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
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

import java.util.Optional;

public class CrimsonMotherFungusStructure extends Structure {

    public static final MapCodec<CrimsonMotherFungusStructure> CODEC =
            simpleCodec(CrimsonMotherFungusStructure::new);

    private static final ResourceLocation MAIN =
            rl("crimson_mother_fungus");

    public CrimsonMotherFungusStructure(StructureSettings settings) {
        super(settings);
    }

    @Override
    protected Optional<GenerationStub> findGenerationPoint(GenerationContext context) {

        ChunkPos chunkPos = context.chunkPos();
        int x = chunkPos.getMiddleBlockX();
        int z = chunkPos.getMiddleBlockZ();

        // --- SAFE NETHER RANGE ---
        int minY = 64;
        int maxY = 96;

        int startY = context.random().nextIntBetweenInclusive(minY, maxY);

        var column = context.chunkGenerator().getBaseColumn(
                x,
                z,
                context.heightAccessor(),
                context.randomState()
        );

        int groundY = -1;

        // Full downward scan (no artificial limit)
        for (int y = startY; y >= minY; y--) {
            if (column.getBlock(y).canOcclude()) {
                groundY = y;
                break;
            }
        }

        if (groundY == -1) {
            return Optional.empty();
        }

        BlockPos basePos = new BlockPos(x, groundY + 1, z);
        Rotation rotation = Rotation.getRandom(context.random());

        StructureTemplateManager templateManager = context.structureTemplateManager();
        StructureTemplate template = templateManager.getOrCreate(MAIN);

        var size = template.getSize(rotation);

        BoundingBox box = BoundingBox.fromCorners(
                basePos,
                basePos.offset(size.getX(), size.getY(), size.getZ())
        );

        // --- SURFACE SAFETY CHECK ---
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

            builder.addPiece(new CrimsonMotherFungusPiece(
                    templateManager,
                    MAIN,
                    basePos,
                    rotation,
                    0
            ));
        }));
    }

    @Override
    public StructureType<?> type() {
        return ModStructures.CRIMSON_MOTHER_FUNGUS.get();
    }

    private static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(
                NetherHexedKingdom.MODID,
                path
        );
    }
}