package com.deimoshexxus.netherhexedkingdom.content.custom;

import com.deimoshexxus.netherhexedkingdom.content.ModBlocks;
import com.deimoshexxus.netherhexedkingdom.utils.GasUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

/**
 * Passive gas child block. Does not move itself. Will remove itself if it cannot be reached
 * from a valid source within MAX_SOURCE_SEARCH blocks below.
 */
public class GasChildBlock extends Block {
    public static final IntegerProperty DISTANCE = GasUtil.DISTANCE;
    private static final int MAX_SOURCE_SEARCH = 6;

    public GasChildBlock(BlockBehaviour.Properties props) {
        super(props);
        this.registerDefaultState(this.stateDefinition.any().setValue(DISTANCE, MAX_SOURCE_SEARCH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(DISTANCE);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        // children are passive — source maintains them. No scheduled self-cleanup.
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, net.minecraft.world.level.block.Block block, BlockPos fromPos, boolean moving) {
        if (level.isClientSide()) return;

        boolean stillConnected = false;
        for (int down = 1; down <= MAX_SOURCE_SEARCH; down++) {
            BlockPos below = pos.below(down);
            if (!level.isLoaded(below)) break;
            if (level.getBlockState(below).is(ModBlocks.GAS_SOURCE.get())) {
                if (isReachableFromSource((ServerLevel) level, below, pos)) {
                    stillConnected = true;
                    break;
                }
            }
        }

        if (!stillConnected) {
            level.removeBlock(pos, false);
        }
    }

    private boolean isReachableFromSource(ServerLevel level, BlockPos sourcePos, BlockPos targetPos) {
        final int maxH = 6;
        final int radius = 3;

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
            if (!isWithinHorizontalRadius(sourcePos, cur, radius)) continue;

            BlockPos[] neighbors = new BlockPos[] {
                    cur.north(), cur.south(), cur.east(), cur.west(), cur.above(), cur.below()
            };

            for (BlockPos nb : neighbors) {
                if (nb.getY() <= sourcePos.getY()) continue;
                if (!isWithinHorizontalRadius(sourcePos, nb, radius)) continue;
                if (visited.contains(nb)) continue;
                if (isPassableOrGas(level, nb)) {
                    visited.add(nb);
                    queue.add(nb);
                }
            }
        }

        return false;
    }

    private static boolean isPassableOrGas(Level level, BlockPos pos) {
        BlockState bs = level.getBlockState(pos);
        return bs.isAir() || bs.canBeReplaced() || bs.is(ModBlocks.GAS_CHILD.get());
    }

    private static boolean isWithinHorizontalRadius(BlockPos origin, BlockPos pos, int radius) {
        int dx = Math.abs(pos.getX() - origin.getX());
        int dz = Math.abs(pos.getZ() - origin.getZ());
        return dx <= radius && dz <= radius;
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (level.isClientSide()) return;
        if (!(entity instanceof LivingEntity le)) return;
        if (le.isSpectator()) return;

        int duration = 60; // ticks (3 seconds)
        if (!le.hasEffect(MobEffects.POISON)) {
            le.addEffect(new MobEffectInstance(MobEffects.POISON, duration, 0, false, true, true));
        }
        if (!le.hasEffect(MobEffects.MOVEMENT_SLOWDOWN)) {
            le.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, duration, 0, false, true, true));
        }
    }

    @Override
    public boolean isAir(BlockState state) {
        return false;
    }
}
