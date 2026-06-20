package com.deimoshexxus.netherhexedkingdom.content.structures;

import com.deimoshexxus.netherhexedkingdom.config.CommonConfig;
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

        if (!CommonConfig.HEXED_RED_SUN_TOWER.get()) {
            return Optional.empty();
        }

        ChunkPos chunkPos = context.chunkPos();
        int centerX = chunkPos.getMiddleBlockX();
        int centerZ = chunkPos.getMiddleBlockZ();

        Rotation rotation = Rotation.getRandom(context.random());
        StructureTemplateManager manager = context.structureTemplateManager();

        StructureTemplate foundationTemplate = manager.getOrCreate(FOUNDATION);
        StructureTemplate bottomTemplate = manager.getOrCreate(BOTTOM);

        ResourceLocation topId =
                TOP[context.random().nextInt(TOP.length)];

        StructureTemplate topTemplate = manager.getOrCreate(topId);

        // Rotation-safe sizes
        var foundationSize = foundationTemplate.getSize(rotation);
        var bottomSize = bottomTemplate.getSize(rotation);
        var topSize = topTemplate.getSize(rotation);

        int totalHeight =
                foundationSize.getY()
                        + bottomSize.getY()
                        + topSize.getY();

        // Scan only where the entire tower can fit
        int minGroundY = 32;
        int maxGroundY = 120 - totalHeight;

        var column = context.chunkGenerator().getBaseColumn(
                centerX,
                centerZ,
                context.heightAccessor(),
                context.randomState()
        );

        int groundY = -1;

        for (int y = maxGroundY; y >= minGroundY; --y) {
            var state = column.getBlock(y);

            if (!state.isAir() && state.blocksMotion()) {
                groundY = y;
                break;
            }
        }

        if (groundY < 0) {
            return Optional.empty();
        }

        BlockPos basePos = new BlockPos(
                centerX - bottomSize.getX() / 2,
                groundY + 1,
                centerZ - bottomSize.getZ() / 2
        );

        return Optional.of(new GenerationStub(basePos, builder -> {

            BlockPos foundationPos =
                    basePos.below(foundationSize.getY());

            builder.addPiece(new HexedRedSunTowerPiece(
                    manager,
                    FOUNDATION,
                    foundationPos,
                    rotation,
                    -1
            ));

            builder.addPiece(new HexedRedSunTowerPiece(
                    manager,
                    BOTTOM,
                    basePos,
                    rotation,
                    0
            ));

            BlockPos topPos =
                    basePos.above(bottomSize.getY());

            builder.addPiece(new HexedRedSunTowerPiece(
                    manager,
                    topId,
                    topPos,
                    rotation,
                    1
            ));
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
