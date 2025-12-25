package com.deimoshexxus.netherhexedkingdom.content.structures;

import com.deimoshexxus.netherhexedkingdom.NetherHexedKingdom;
import com.deimoshexxus.netherhexedkingdom.content.ModStructurePieces;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;


public class ExampleStructurePiece extends StructurePiece {

    private static final ResourceLocation TEMPLATE_ID =
            ResourceLocation.fromNamespaceAndPath(
                    NetherHexedKingdom.MODID,
                    "example_structure"
            );

    private final BlockPos origin;

    public ExampleStructurePiece(BlockPos origin) {
        super(
                ModStructurePieces.EXAMPLE_PIECE.get(),
                0,
                BoundingBox.fromCorners(
                        origin,
                        origin.offset(6, 4, 6)
                )
        );
        this.origin = origin;
    }

    public ExampleStructurePiece(
            StructurePieceSerializationContext context,
            CompoundTag tag
    ) {
        super(ModStructurePieces.EXAMPLE_PIECE.get(), tag);
        this.origin = new BlockPos(
                tag.getInt("Ox"),
                tag.getInt("Oy"),
                tag.getInt("Oz")
        );
    }

    @Override
    protected void addAdditionalSaveData(
            StructurePieceSerializationContext context,
            CompoundTag tag
    ) {
        tag.putInt("Ox", this.origin.getX());
        tag.putInt("Oy", this.origin.getY());
        tag.putInt("Oz", this.origin.getZ());
    }

    @Override
    public void postProcess(
            WorldGenLevel level,
            StructureManager structureManager,
            ChunkGenerator generator,
            RandomSource random,
            BoundingBox box,
            ChunkPos chunkPos,
            BlockPos pos
    ) {
        StructureTemplateManager templateManager =
                level.getLevel().getServer().getStructureManager();

        StructureTemplate template =
                templateManager.getOrCreate(TEMPLATE_ID);

        StructurePlaceSettings settings = new StructurePlaceSettings()
                .setBoundingBox(box)
                .addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);

        template.placeInWorld(
                level,
                this.origin,
                this.origin,
                settings,
                random,
                3
        );
    }
}

