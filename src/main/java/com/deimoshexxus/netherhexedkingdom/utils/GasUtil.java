package com.deimoshexxus.netherhexedkingdom.utils;

import com.deimoshexxus.netherhexedkingdom.content.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

public final class GasUtil {
    public static final int DEFAULT_MAX_HEIGHT = 6;
    public static final int DEFAULT_RADIUS = 3;

    public static final int MAX_GAS_HEIGHT = 6;

    // Blockstate property name kept for state use
    public static final net.minecraft.world.level.block.state.properties.IntegerProperty DISTANCE =
            net.minecraft.world.level.block.state.properties.IntegerProperty.create("distance", 0, DEFAULT_MAX_HEIGHT);


    /**
     * 5×5 gas pattern for each vertical layer.
     * Indexed by height (1..6). 0 = source layer (unused).
     *
     * A = empty (skip)
     * G = place gas
     * S = source (only in layer 0)
     */
    public static final String[][] GAS_CLOUD_PATTERN = new String[][]{
            // layer 0 (unused, source handles itself)
            {
                    "AAAAA",
                    "AAGAA",
                    "AGSGA",
                    "AAGAA",
                    "AAAAA"
            },
            // layer 1
            {
                    "AAAAA",
                    "AGGGA",
                    "AGGGA",
                    "AGGGA",
                    "AAAAA"
            },
            // layer 2
            {
                    "AAGAA",
                    "AGGGA",
                    "GGGGG",
                    "AGGGA",
                    "AAGAA"
            },
            // layer 3
            {
                    "AGGGA",
                    "GGGGG",
                    "GGGGG",
                    "GGGGG",
                    "AGGGA"
            },
            // layer 4
            {
                    "GGGGG",
                    "GGGGG",
                    "GGGGG",
                    "GGGGG",
                    "GGGGG"
            },
            // layer 5
            {
                    "AGGGA",
                    "GGGGG",
                    "GGGGG",
                    "GGGGG",
                    "AGGGA"
            },
            // layer 6
            {
                    "AAAAA",
                    "AGGGA",
                    "AGGGA",
                    "AGGGA",
                    "AAAAA"
            }
    };

    private GasUtil() {}

    /** Minimal "canPlaceGas" check: place in air or replaceable only. */
    public static boolean canPlaceGas(Level level, BlockPos pos) {
        BlockState bs = level.getBlockState(pos);
        return bs.isAir() || bs.canBeReplaced();
    }

    /** Allow pass-through if air, replaceable or already gas child. */
    public static boolean isPassableOrGas(Level level, BlockPos pos) {
        BlockState bs = level.getBlockState(pos);
        return bs.isAir() || bs.canBeReplaced() || bs.is(ModBlocks.GAS_CHILD.get());
    }

    /** Allow occupancy for placing a gas child (never overwrite source/child). */
    public static boolean canGasOccupy(BlockState state) {
        if (state.is(ModBlocks.GAS_SOURCE.get()) || state.is(ModBlocks.GAS_CHILD.get())) return false;
        return state.isAir() || state.canBeReplaced();
    }

    /**
     * Rounded ellipsoid test (normalized squared distance).
     * Returns true if pos is inside ellipsoid centered at origin with horizontalRadius and maxHeight.
     */
    public static boolean isWithinRoundedVolume(BlockPos origin, BlockPos pos, int maxHeight, int horizontalRadius) {
        int dx = pos.getX() - origin.getX();
        int dz = pos.getZ() - origin.getZ();
        int dy = pos.getY() - origin.getY();

        double nx = (dx * dx) / (double) (horizontalRadius * horizontalRadius);
        double nz = (dz * dz) / (double) (horizontalRadius * horizontalRadius);
        double ny = (dy * dy) / (double) (maxHeight * maxHeight);

        return (nx + nz + ny) <= 1.0;
    }

    /**
     * Bounded BFS that returns reachable positions for the gas cloud above sourcePos.
     * Pass-through positions must be passable or already gas. Uses same rules as before.
     */
    public static Set<BlockPos> computeReachablePositions(ServerLevel level, BlockPos sourcePos,
                                                          int maxHeight, int horizontalRadius) {
        Set<BlockPos> visited = new HashSet<>();
        Queue<BlockPos> queue = new ArrayDeque<>();

        int baseY = sourcePos.getY();

        // seeds around source at Y+1
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                BlockPos seed = sourcePos.offset(dx, 1, dz);
                if (isWithinRoundedVolume(sourcePos, seed, maxHeight, horizontalRadius)
                        && isPassableOrGas(level, seed)) {
                    visited.add(seed);
                    queue.add(seed);
                }
            }
        }

        BlockPos directAbove = sourcePos.above(1);
        if (!visited.contains(directAbove) && isPassableOrGas(level, directAbove)) {
            visited.add(directAbove);
            queue.add(directAbove);
        }

        while (!queue.isEmpty()) {
            BlockPos cur = queue.remove();
            int dy = cur.getY() - baseY;

            if (dy < 1 || dy > maxHeight) continue;
            if (!isWithinRoundedVolume(sourcePos, cur, maxHeight, horizontalRadius)) continue;

            BlockPos[] neighbors = new BlockPos[]{
                    cur.north(), cur.south(), cur.east(), cur.west(), cur.above(), cur.below()
            };

            for (BlockPos nb : neighbors) {
                if (nb.getY() <= baseY) continue;
                if (!isWithinRoundedVolume(sourcePos, nb, maxHeight, horizontalRadius)) continue;
                if (visited.contains(nb)) continue;
                if (isPassableOrGas(level, nb)) {
                    visited.add(nb);
                    queue.add(nb);
                }
            }
        }

        return visited;
    }

    /**
     * Small bounded BFS reachability test: can a source at sourcePos reach targetPos?
     * This mirrors computeReachablePositions but returns boolean and is cheaper for single-target checks.
     */
    public static boolean isReachableFromSource(ServerLevel level, BlockPos sourcePos, BlockPos targetPos,
                                                int maxHeight, int horizontalRadius) {
        int dy = targetPos.getY() - sourcePos.getY();
        if (dy < 1 || dy > maxHeight) return false;

        Queue<BlockPos> queue = new ArrayDeque<>();
        Set<BlockPos> visited = new HashSet<>();

        BlockPos start = sourcePos.above(1);
        if (isPassableOrGas(level, start)) {
            queue.add(start);
            visited.add(start);
        } else {
            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    BlockPos seed = sourcePos.offset(dx, 1, dz);
                    if (isPassableOrGas(level, seed)) {
                        visited.add(seed);
                        queue.add(seed);
                    }
                }
            }
        }

        while (!queue.isEmpty()) {
            BlockPos cur = queue.remove();
            if (cur.equals(targetPos)) return true;

            int curDy = cur.getY() - sourcePos.getY();
            if (curDy < 1 || curDy > maxHeight) continue;
            if (!isWithinRoundedVolume(sourcePos, cur, maxHeight, horizontalRadius)) continue;

            BlockPos[] neighbors = new BlockPos[]{
                    cur.north(), cur.south(), cur.east(), cur.west(), cur.above(), cur.below()
            };

            for (BlockPos nb : neighbors) {
                if (nb.getY() <= sourcePos.getY()) continue;
                if (!isWithinRoundedVolume(sourcePos, nb, maxHeight, horizontalRadius)) continue;
                if (visited.contains(nb)) continue;
                if (isPassableOrGas(level, nb)) {
                    visited.add(nb);
                    queue.add(nb);
                }
            }
        }

        return false;
    }

    /**
     * Returns true if ANY gas source within a small search box can claim the provided child position.
     * This prevents sources from deleting children that belong to other sources.
     */
    public static boolean isClaimedByAnySource(ServerLevel level, BlockPos pos, int maxHeight, int horizontalRadius) {
        return isClaimedByAnySource(level, pos, maxHeight, horizontalRadius, null);
    }

    /**
     * Overload: ignore a specific source position (excludeSource). Useful when that source is being removed.
     * If excludeSource is null, behaves like the original.
     */
    public static boolean isClaimedByAnySource(ServerLevel level, BlockPos pos, int maxHeight, int horizontalRadius, BlockPos excludeSource) {
        int searchRadius = horizontalRadius + 1;

        for (int dx = -searchRadius; dx <= searchRadius; dx++) {
            for (int dz = -searchRadius; dz <= searchRadius; dz++) {
                for (int down = 0; down <= maxHeight; down++) {
                    BlockPos candidate = pos.offset(dx, -down, dz);

                    if (excludeSource != null && candidate.equals(excludeSource)) continue;
                    if (candidate.getY() > pos.getY()) continue; // sanity
                    if (!level.isLoaded(candidate)) continue;
                    if (!level.getBlockState(candidate).is(ModBlocks.GAS_SOURCE.get())) continue;
                    if (isReachableFromSource(level, candidate, pos, maxHeight, horizontalRadius)) return true;
                }
            }
        }

        return false;
    }
}
