//package com.deimoshexxus.netherhexedkingdom.content.structures;
//
//import com.deimoshexxus.netherhexedkingdom.content.ModStructures;
//import com.mojang.serialization.MapCodec;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.level.block.Mirror;
//import net.minecraft.world.level.block.Rotation;
//import net.minecraft.world.level.levelgen.structure.Structure;
//import net.minecraft.world.level.levelgen.structure.StructureType;
//import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
//
//import java.util.List;
//import java.util.Optional;
//
//public class MasoniaeFossilStructure extends Structure {
//
//    public static final MapCodec<MasoniaeFossilStructure> CODEC =
//            simpleCodec(MasoniaeFossilStructure::new);
//
//    public static final List<ResourceLocation> FOSSIL_TEMPLATES = List.of(
//            ResourceLocation.fromNamespaceAndPath(
//                    "netherhexedkingdom", "nether_fossils/fossil_masoniae_1"),
//            ResourceLocation.fromNamespaceAndPath(
//                    "netherhexedkingdom", "nether_fossils/fossil_masoniae_2"),
//            ResourceLocation.fromNamespaceAndPath(
//                    "netherhexedkingdom", "nether_fossils/fossil_masoniae_3"));
//
//    public MasoniaeFossilStructure(StructureSettings settings) {
//        super(settings);
//    }
//
//    @Override
//    protected Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
//        var random = context.random();
//
//        ResourceLocation templateId =
//                FOSSIL_TEMPLATES.get(random.nextInt(FOSSIL_TEMPLATES.size()));
//
//        return Optional.of(
//                new GenerationStub(
//                        context.chunkPos().getMiddleBlockPosition(0),
//                        builder -> {
//
//
//                        }
//                )
//        );
//    }
//
//    @Override
//    public StructureType<?> type() {
//        return ModStructures.FOSSIL_MASONIAE.get();
//    }
//}
