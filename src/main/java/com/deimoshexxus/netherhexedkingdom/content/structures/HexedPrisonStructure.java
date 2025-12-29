package com.deimoshexxus.netherhexedkingdom.content.structures;

import com.deimoshexxus.netherhexedkingdom.NetherHexedKingdom;
import com.deimoshexxus.netherhexedkingdom.content.ModStructures;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.pools.alias.PoolAliasLookup;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;

import java.util.Optional;

import static com.deimoshexxus.netherhexedkingdom.content.ModTemplatePools.HEXED_PRISON_START;

public class HexedPrisonStructure extends Structure {

    public static final MapCodec<HexedPrisonStructure> CODEC =
            simpleCodec(HexedPrisonStructure::new);

    public HexedPrisonStructure(StructureSettings settings) {
        super(settings);
    }

    @Override
    protected Optional<GenerationStub> findGenerationPoint(GenerationContext context) {

        NetherHexedKingdom.LOGGER.info("[HexedPrison] findGenerationPoint called");

        int x = context.chunkPos().getMiddleBlockX();
        int z = context.chunkPos().getMiddleBlockZ();

        int y = context.random().nextInt(40, 90);

        BlockPos startPos = new BlockPos(x, y, z);
        Rotation rotation = Rotation.getRandom(context.random());
        NetherHexedKingdom.LOGGER.info(
                "[HexedPrison] Chunk {}, start position {}",
                context.chunkPos(),
                startPos
        );

        var poolRegistry = context.registryAccess()
                .registryOrThrow(Registries.TEMPLATE_POOL);

        Optional<Holder.Reference<StructureTemplatePool>> poolOpt =
                poolRegistry.getHolder(HEXED_PRISON_START);

        if (poolOpt.isEmpty()) {
            NetherHexedKingdom.LOGGER.error(
                    "[HexedPrison] Start pool NOT found: {}",
                    HEXED_PRISON_START.location()
            );
            return Optional.empty();
        }

//        NetherHexedKingdom.LOGGER.info(
//                "[HexedPrison] Start pool found: {}",
//                HEXED_PRISON_START.location()
//        );

        Optional<GenerationStub> result = JigsawPlacement.addPieces(
                context,
                poolOpt.get(),
                Optional.empty(),
                7,                // max depth
                startPos,
                false,
                Optional.empty(),
                128,
                PoolAliasLookup.EMPTY,
                JigsawStructure.DEFAULT_DIMENSION_PADDING,
                JigsawStructure.DEFAULT_LIQUID_SETTINGS
        );

        if (result.isEmpty()) {
            NetherHexedKingdom.LOGGER.error(
                    "[HexedPrison] JigsawPlacement returned EMPTY"
            );
            return Optional.empty();
        }

//        NetherHexedKingdom.LOGGER.info(
//                "[HexedPrison] JigsawPlacement succeeded"
//        );

        return result;
    }

    @Override
    public StructureType<?> type() {
        return ModStructures.HEXED_PRISON_STRUCTURE.get();
    }
}
