package com.deimoshexxus.netherhexedkingdom.content.custom;

import com.deimoshexxus.netherhexedkingdom.content.ModBlocks;
import com.deimoshexxus.netherhexedkingdom.utils.GasUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.item.context.BlockPlaceContext;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

/**
 * Gas source block with two fixes:
 *  - won't remove gas children that are reachable from other nearby sources (avoids oscillation)
 *  - cannot be replaced by placed blocks (overrides canBeReplaced)
 */
public class GasSourceBlock extends Block {
    public static final IntegerProperty DISTANCE = GasUtil.DISTANCE;

    private static final int MAX_HEIGHT = 6;
    private static final int HORIZONTAL_RADIUS = 3;
    private static final int TICK_DELAY = 20;

    public GasSourceBlock(BlockBehaviour.Properties props) {
        super(props);
        this.registerDefaultState(this.stateDefinition.any().setValue(DISTANCE, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(DISTANCE);
    }

    // Prevent placement from replacing the source: return false so placement can't overwrite it
    @Override
    public boolean canBeReplaced(BlockState state, BlockPlaceContext useContext) {
        return false;
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!level.isClientSide() && level instanceof ServerLevel serverLevel) {
            createGasCloud(serverLevel, pos);
            level.scheduleTick(pos, this, TICK_DELAY);
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean moving) {
        if (!level.isClientSide()) {
            level.scheduleTick(pos, this, 2);
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
        createGasCloud(level, pos);
        level.scheduleTick(pos, this, TICK_DELAY);
    }

    private void createGasCloud(ServerLevel level, BlockPos sourcePos) {
        final int maxH = MAX_HEIGHT;
        final int radius = HORIZONTAL_RADIUS;

        // 1-block horizontal spread at source level (unchanged)
        BlockPos base = sourcePos;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                if (dx == 0 && dz == 0) continue;
                BlockPos spreadPos = base.offset(dx, 0, dz);
                BlockState current = level.getBlockState(spreadPos);
                if (current.is(ModBlocks.GAS_SOURCE.get())) continue;
                if (isPassableOrGas(level, spreadPos) || current.is(ModBlocks.GAS_CHILD.get())) {
                    BlockState desired = ModBlocks.GAS_CHILD.get().defaultBlockState().setValue(DISTANCE, 1);
                    if (!current.equals(desired)) level.setBlock(spreadPos, desired, 2);
                }
            }
        }

        // Tall rounded cloud above
        Set<BlockPos> reachable = computeReachablePositions(level, sourcePos, maxH, radius);

        for (BlockPos target : reachable) {
            if (target.equals(sourcePos)) continue;
            int dist = target.getY() - sourcePos.getY();
            if (dist < 1 || dist > maxH) continue;
            BlockState current = level.getBlockState(target);
            if (current.is(ModBlocks.GAS_SOURCE.get())) continue;
            if (canGasOccupy(current)) {
                BlockState desired = ModBlocks.GAS_CHILD.get().defaultBlockState().setValue(DISTANCE, dist);
                if (!current.equals(desired)) level.setBlock(target, desired, 2);
            }
        }

        // Cleanup unreachable children, BUT only remove them if NO nearby source can claim them.
        int cleanup = radius + 1;
        for (int dx = -cleanup; dx <= cleanup; dx++) {
            for (int dz = -cleanup; dz <= cleanup; dz++) {
                for (int dy = 1; dy <= maxH; dy++) {
                    BlockPos check = sourcePos.offset(dx, dy, dz);
                    if (!level.getBlockState(check).is(ModBlocks.GAS_CHILD.get())) continue;
                    if (reachable.contains(check)) continue; // this source still claims it
                    // If any other nearby source claims it, do not remove
                    if (isClaimedByAnySource(level, check)) continue;
                    level.removeBlock(check, false);
                }
            }
        }
    }

    private Set<BlockPos> computeReachablePositions(ServerLevel level, BlockPos sourcePos,
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

    private static boolean isPassableOrGas(ServerLevel level, BlockPos pos) {
        BlockState bs = level.getBlockState(pos);
        return bs.isAir() || bs.canBeReplaced() || bs.is(ModBlocks.GAS_CHILD.get());
    }

    private static boolean canGasOccupy(BlockState state) {
        if (state.is(ModBlocks.GAS_SOURCE.get()) || state.is(ModBlocks.GAS_CHILD.get())) return false;
        return state.isAir() || state.canBeReplaced();
    }

    private static boolean isWithinRoundedVolume(BlockPos origin, BlockPos pos,
                                                 int maxHeight, int horizontalRadius) {
        int dx = pos.getX() - origin.getX();
        int dz = pos.getZ() - origin.getZ();
        int dy = pos.getY() - origin.getY();

        double nx = (dx * dx) / (double) (horizontalRadius * horizontalRadius);
        double nz = (dz * dz) / (double) (horizontalRadius * horizontalRadius);
        double ny = (dy * dy) / (double) (maxHeight * maxHeight);

        return (nx + nz + ny) <= 1.0;
    }

    /**
     * Check whether any nearby source (within a small search box) can reach the position.
     * If yes, the child at 'pos' should not be removed by this source's cleanup.
     */
    private boolean isClaimedByAnySource(ServerLevel level, BlockPos pos) {
        int searchRadius = HORIZONTAL_RADIUS + 1;
        // we'll look for candidate sources within horizontal box and up to MAX_HEIGHT below the checked position
        for (int dx = -searchRadius; dx <= searchRadius; dx++) {
            for (int dz = -searchRadius; dz <= searchRadius; dz++) {
                for (int down = 0; down <= MAX_HEIGHT; down++) {
                    BlockPos candidate = pos.offset(dx, -down, dz);
                    if (candidate.getY() > pos.getY()) continue;
                    if (!level.isLoaded(candidate)) continue;
                    if (!level.getBlockState(candidate).is(ModBlocks.GAS_SOURCE.get())) continue;
                    // candidate is a source — check if it can reach pos
                    if (isReachableFromSource(level, candidate, pos)) return true;
                }
            }
        }
        return false;
    }

    /**
     * Bounded reachability check: can the source at sourcePos reach targetPos using the
     * same rules as computeReachablePositions? This is a small BFS limited by horizontal radius and height.
     */
    private boolean isReachableFromSource(ServerLevel level, BlockPos sourcePos, BlockPos targetPos) {
        final int maxH = MAX_HEIGHT;
        final int radius = HORIZONTAL_RADIUS;

        int dy = targetPos.getY() - sourcePos.getY();
        if (dy < 1 || dy > maxH) return false;

        java.util.Queue<BlockPos> queue = new java.util.ArrayDeque<>();
        java.util.Set<BlockPos> visited = new java.util.HashSet<>();

        BlockPos start = sourcePos.above(1);
        if (isPassableOrGas(level, start)) {
            queue.add(start);
            visited.add(start);
        } else {
            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    BlockPos seed = sourcePos.offset(dx, 1, dz);
                    if (isPassableOrGas(level, seed)) {
                        queue.add(seed);
                        visited.add(seed);
                    }
                }
            }
        }

        while (!queue.isEmpty()) {
            BlockPos cur = queue.remove();
            if (cur.equals(targetPos)) return true;

            int curDy = cur.getY() - sourcePos.getY();
            if (curDy < 1 || curDy > maxH) continue;
            if (!isWithinRoundedVolume(sourcePos, cur, maxH, radius)) continue;

            BlockPos[] neighbors = new BlockPos[]{
                    cur.north(), cur.south(), cur.east(), cur.west(), cur.above(), cur.below()
            };

            for (BlockPos nb : neighbors) {
                if (nb.getY() <= sourcePos.getY()) continue;
                if (!isWithinRoundedVolume(sourcePos, nb, maxH, radius)) continue;
                if (visited.contains(nb)) continue;
                if (isPassableOrGas(level, nb)) {
                    visited.add(nb);
                    queue.add(nb);
                }
            }
        }

        return false;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!level.isClientSide() && level instanceof ServerLevel serverLevel) {
            for (int y = 1; y <= MAX_HEIGHT; y++) {
                for (int dx = -HORIZONTAL_RADIUS; dx <= HORIZONTAL_RADIUS; dx++) {
                    for (int dz = -HORIZONTAL_RADIUS; dz <= HORIZONTAL_RADIUS; dz++) {
                        BlockPos target = pos.offset(dx, y, dz);
                        if (serverLevel.getBlockState(target).is(ModBlocks.GAS_CHILD.get())) {
                            // only remove if no other source claims it
                            if (!isClaimedByAnySource(serverLevel, target)) {
                                serverLevel.removeBlock(target, false);
                            }
                        }
                    }
                }
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }
}
