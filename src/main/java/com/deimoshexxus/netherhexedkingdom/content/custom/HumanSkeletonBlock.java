package com.deimoshexxus.netherhexedkingdom.content.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class HumanSkeletonBlock extends RotatableBlock {
    public static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D);
    // 8 instead of 16 — half height. 6 more like slab

//    // If your skeleton has irregular geometry (e.g., legs sticking out), you can combine multiple boxes using
//    private static final VoxelShape SHAPE = Shapes.or(
//            Block.box(0, 0, 0, 16, 4, 16),   // base
//            Block.box(4, 4, 4, 12, 8, 12)    // upper bones
//    );

    public HumanSkeletonBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }
}

