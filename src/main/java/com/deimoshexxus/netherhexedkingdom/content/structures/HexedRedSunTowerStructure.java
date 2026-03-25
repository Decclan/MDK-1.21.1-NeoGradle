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

public class HexedRedSunTowerStructure extends Structure {

    public static final MapCodec<HexedRedSunTowerStructure> CODEC =
            simpleCodec(HexedRedSunTowerStructure::new);

    private static final ResourceLocation FOUNDATION =
            rl("hexed_red_sun_tower_foundation");

    private static final ResourceLocation BOTTOM =
            rl("hexed_red_sun_tower_bottom");

    private static final ResourceLocation[] TOP = {
            rl("hexed_red_sun_tower_top")
    };

    public HexedRedSunTowerStructure(StructureSettings settings) {
        super(settings);
    }

    @Override
    protected Optional<GenerationStub> findGenerationPoint(GenerationContext context) {

        ChunkPos chunkPos = context.chunkPos();
        int centerX = chunkPos.getMiddleBlockX();
        int centerZ = chunkPos.getMiddleBlockZ();

        int minY = 32;
        int maxY = 90; // allow full tower height safely

        var column = context.chunkGenerator().getBaseColumn(
                centerX,
                centerZ,
                context.heightAccessor(),
                context.randomState()
        );

        // --- FIND GROUND ---
        int groundY = -1;
        for (int y = maxY; y >= minY; y--) {
            if (column.getBlock(y).canOcclude()) {
                groundY = y;
                break;
            }
        }

        if (groundY == -1) {
            return Optional.empty();
        }

        Rotation rotation = Rotation.getRandom(context.random());
        StructureTemplateManager manager = context.structureTemplateManager();

        StructureTemplate bottomTemplate = manager.getOrCreate(BOTTOM);
        StructureTemplate foundationTemplate = manager.getOrCreate(FOUNDATION);
        ResourceLocation topId =
                TOP[context.random().nextInt(TOP.length)];

        StructureTemplate topTemplate =
                manager.getOrCreate(topId);

        // --- ROTATION SAFE SIZES ---
        var bottomSize = bottomTemplate.getSize(rotation);
        var foundationSize = foundationTemplate.getSize(rotation);
        var topSize = topTemplate.getSize(rotation);

        int totalHeight =
                foundationSize.getY()
                        + bottomSize.getY()
                        + topSize.getY();

        // --- HEIGHT SAFETY CHECK ---
        if (groundY + totalHeight > 120) {
            return Optional.empty();
        }

        // --- CENTERED BASE POSITION ---
        BlockPos basePos = new BlockPos(
                centerX - bottomSize.getX() / 2,
                groundY + 1,
                centerZ - bottomSize.getZ() / 2
        );

        NetherHexedKingdom.LOGGER.info(
                "[HexedRedSunTower] Ground={} TotalHeight={} Base={}",
                groundY, totalHeight, basePos
        );

        return Optional.of(new GenerationStub(basePos, builder -> {

            // --- FOUNDATION ---
            BlockPos foundationPos = basePos.below(foundationSize.getY());

            builder.addPiece(new HexedRedSunTowerPiece(
                    manager,
                    FOUNDATION,
                    foundationPos,
                    rotation,
                    -1
            ));

            // --- BOTTOM ---
            builder.addPiece(new HexedRedSunTowerPiece(
                    manager,
                    BOTTOM,
                    basePos,
                    rotation,
                    0
            ));

            // --- TOP ---
            BlockPos topPos = basePos.above(bottomSize.getY());

            builder.addPiece(new HexedRedSunTowerPiece(
                    manager,
                    topId,
                    topPos,
                    rotation,
                    1
            ));

            NetherHexedKingdom.LOGGER.info(
                    "[HexedRedSunTower] Assembled at {} (topY={})",
                    basePos,
                    topPos.getY()
            );
        }));
    }

    @Override
    public StructureType<?> type() {
        return ModStructures.HEXED_RED_SUN_TOWER_STRUCTURE.get();
    }

    private static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(
                NetherHexedKingdom.MODID,
                path
        );
    }
}
