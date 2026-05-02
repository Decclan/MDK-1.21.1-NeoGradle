package com.deimoshexxus.netherhexedkingdom.content.structures;

import com.deimoshexxus.netherhexedkingdom.NetherHexedKingdom;
import com.deimoshexxus.netherhexedkingdom.content.ModStructures;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.ChunkPos;
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

    private static final int MIN_Y = 32;
    private static final int MAX_Y = 64;
    private static final int MAX_DEPTH = 7;

    public HexedPrisonStructure(StructureSettings settings) {
        super(settings);
    }

    @Override
    protected Optional<GenerationStub> findGenerationPoint(GenerationContext context) {

        ChunkPos chunk = context.chunkPos();
        int x = chunk.getMiddleBlockX();
        int z = chunk.getMiddleBlockZ();

        Rotation rotation = Rotation.getRandom(context.random());

        // --- HEIGHTMAP-BASED Y (CRITICAL FIX) ---
        int surfaceY = context.chunkGenerator().getFirstFreeHeight(
                x,
                z,
                Heightmap.Types.WORLD_SURFACE_WG,
                context.heightAccessor(),
                context.randomState()
        );

        // Clamp into your intended nether band if needed
        int y = Math.max(MIN_Y, Math.min(surfaceY, MAX_Y));

        BlockPos startPos = new BlockPos(x, y, z);

        var poolRegistry = context.registryAccess()
                .registryOrThrow(Registries.TEMPLATE_POOL);

        Optional<Holder.Reference<StructureTemplatePool>> poolOpt =
                poolRegistry.getHolder(HEXED_PRISON_START);

        if (poolOpt.isEmpty()) {
            NetherHexedKingdom.LOGGER.error(
                    "[HexedPrison] Missing start pool: {}",
                    HEXED_PRISON_START.location()
            );
            return Optional.empty();
        }

        Optional<GenerationStub> result = JigsawPlacement.addPieces(
                context,
                poolOpt.get(),
                Optional.empty(),
                MAX_DEPTH,
                startPos,
                false,
                Optional.empty(),
                128,
                PoolAliasLookup.EMPTY,
                JigsawStructure.DEFAULT_DIMENSION_PADDING,
                JigsawStructure.DEFAULT_LIQUID_SETTINGS
        );

        if (result.isEmpty()) {
            NetherHexedKingdom.LOGGER.debug(
                    "[HexedPrison] Generation failed at {}",
                    startPos
            );
            return Optional.empty();
        }

        NetherHexedKingdom.LOGGER.debug(
                "[HexedPrison] Generated at {}",
                startPos
        );

        return result;
    }

    @Override
    public StructureType<?> type() {
        return ModStructures.HEXED_PRISON_STRUCTURE.get();
    }
}