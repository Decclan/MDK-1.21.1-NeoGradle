package com.deimoshexxus.netherhexedkingdom.utils;

import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.Direction;

public final class GasUtil {
    public static final IntegerProperty DISTANCE = IntegerProperty.create("distance", 0, 6);

    // Minimal "canPlaceGas" check: place in air or replaceable only.
    public static boolean canPlaceGas(Level level, BlockPos pos) {
        BlockState bs = level.getBlockState(pos);
        return bs.isAir() || bs.canBeReplaced();
    }

    public static Iterable<BlockPos> positionsAtDistance(BlockPos origin, int distance) {
        java.util.List<BlockPos> list = new java.util.ArrayList<>();
        int radius = Math.min(3, distance); // more spread with higher distance
        int yOffset = Math.max(0, distance/3); // small upward bias

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                // keep diamond/circle shape
                if (Math.abs(dx) + Math.abs(dz) <= radius) {
                    list.add(origin.offset(dx, yOffset + (distance / 6), dz)); // tweak bias as needed
                }
            }
        }
        // ensure center column (so near-source levels still include the direct column)
        list.add(origin.above(Math.max(1, distance / 1)));
        return list;
    }

}
