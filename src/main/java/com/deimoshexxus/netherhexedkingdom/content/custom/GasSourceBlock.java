package com.deimoshexxus.netherhexedkingdom.content.custom;

import com.deimoshexxus.netherhexedkingdom.content.ModBlocks;
import com.deimoshexxus.netherhexedkingdom.content.ModSounds;
import com.deimoshexxus.netherhexedkingdom.utils.GasUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.Set;

public class GasSourceBlock extends Block {
    public static final IntegerProperty DISTANCE = GasUtil.DISTANCE;

    private static final int MAX_HEIGHT = GasUtil.DEFAULT_MAX_HEIGHT;
    private static final int HORIZONTAL_RADIUS = GasUtil.DEFAULT_RADIUS;
    private static final int TICK_DELAY = 20;

    public GasSourceBlock(BlockBehaviour.Properties props) {
        super(props);
        this.registerDefaultState(this.stateDefinition.any().setValue(DISTANCE, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(DISTANCE);
    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockPlaceContext ctx) {
        // If the player is placing anything except the gas source → allow replacement
        return ctx.getItemInHand().getItem() != this.asItem();
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
        if (!level.isClientSide()) level.scheduleTick(pos, this, 2);
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (level.isClientSide()) return;
        if (!(entity instanceof LivingEntity living)) return;
        if (living.isSpectator()) return;

        // Apply potion effects (only if not already present to avoid spam)
        int potionDuration = 160; // ticks (8 seconds)
        if (!living.hasEffect(MobEffects.POISON)) {
            living.addEffect(new MobEffectInstance(MobEffects.POISON, potionDuration, 1, false, true, true));
        }
        if (!living.hasEffect(MobEffects.MOVEMENT_SLOWDOWN)) {
            living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, potionDuration, 1, false, true, true));
        }
        if (!living.hasEffect(MobEffects.CONFUSION)) {
            living.addEffect(new MobEffectInstance(MobEffects.CONFUSION, potionDuration, 1, false, true, true));
        }
        if (!living.hasEffect(MobEffects.HUNGER)) {
            living.addEffect(new MobEffectInstance(MobEffects.HUNGER, potionDuration, 1, false, true, true));
        }
        if (!living.hasEffect(MobEffects.BLINDNESS)) {
            living.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, potionDuration, 0, false, true, true));
        }

        // Suffocation-like damage once per second
        long worldTime = level.getGameTime(); // <-- fixed: use level.getGameTime()
        if (worldTime % 20L == 0L) { // every 20 ticks (approx 1 second)
            living.hurt(level.damageSources().inWall(), 6.0F); // tune damage as needed
        }
    }

    /* Remove @Override if it causes compile errors in your mappings.
       Keep the method if your mappings support it; otherwise it's safe to delete. */
    @Override
    public boolean isPathfindable(BlockState state, PathComputationType type) {
        // Stop mobs from pathing through gas as if it was empty
        if (type == PathComputationType.LAND) {
            return false;
        }
        return super.isPathfindable(state, type);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
        createGasCloud(level, pos);
        level.scheduleTick(pos, this, TICK_DELAY);
    }

    private void createGasCloud(ServerLevel level, BlockPos sourcePos) {
        // --- (1) Place plus-shape (cardinal) children at y = source ---

        int[][] PATTERN_OFFSETS = {
                {0, -1}, // north
                {0,  1}, // south
                {-1, 0}, // west
                {1,  0}  // east
        };

        for (int[] off : PATTERN_OFFSETS) {
            int dx = off[0];
            int dz = off[1];
            BlockPos p = sourcePos.offset(dx, 0, dz);

            BlockState current = level.getBlockState(p);
            if (current.is(ModBlocks.GAS_SOURCE.get())) continue;

            if (GasUtil.isPassableOrGas(level, p) || current.is(ModBlocks.GAS_CHILD.get())) {
                BlockState desired = ModBlocks.GAS_CHILD.get().defaultBlockState().setValue(DISTANCE, 1);
                if (!current.equals(desired)) level.setBlock(p, desired, 2);
            }
        }

        // --- (2) BFS reachability determines what positions the gas may occupy ---
        Set<BlockPos> reachable =
                GasUtil.computeReachablePositions(level, sourcePos, MAX_HEIGHT, HORIZONTAL_RADIUS);

        // --- (3) Use the mushroom-cloud pattern placement ---
        placePatternCloud(level, sourcePos, reachable);

        // --- (4) Cleanup: remove gas children not reachable AND not claimed by any source ---
        int cleanup = HORIZONTAL_RADIUS + 1;

        for (int dx = -cleanup; dx <= cleanup; dx++) {
            for (int dz = -cleanup; dz <= cleanup; dz++) {
                for (int dy = 1; dy <= MAX_HEIGHT; dy++) {

                    BlockPos check = sourcePos.offset(dx, dy, dz);
                    BlockState current = level.getBlockState(check);

                    if (!current.is(ModBlocks.GAS_CHILD.get())) continue;

                    // keep if reachable
                    if (reachable.contains(check)) continue;

                    // keep if another source claims it
                    if (GasUtil.isClaimedByAnySource(level, check, MAX_HEIGHT, HORIZONTAL_RADIUS))
                        continue;

                    // otherwise remove
                    level.removeBlock(check, false);
                }
            }
        }
    }


    private void placePatternCloud(ServerLevel level, BlockPos sourcePos, Set<BlockPos> reachable) {

        for (int h = 1; h <= GasUtil.MAX_GAS_HEIGHT; h++) {

            String[] layer = GasUtil.GAS_CLOUD_PATTERN[h];
            int half = 2; // center index for 5x5 grid

            for (int row = 0; row < 5; row++) {
                for (int col = 0; col < 5; col++) {

                    char c = layer[row].charAt(col);
                    if (c != 'G') continue;

                    int dx = col - half;
                    int dz = row - half;
                    BlockPos target = sourcePos.offset(dx, h, dz);

                    // Respect BFS connectivity
                    if (!reachable.contains(target)) continue;

                    BlockState current = level.getBlockState(target);

                    // Do not replace solids or sources
                    if (!GasUtil.canGasOccupy(current)) continue;

                    int dist = h;
                    BlockState desired = ModBlocks.GAS_CHILD.get()
                            .defaultBlockState()
                            .setValue(DISTANCE, dist);

                    if (!current.equals(desired)) {
                        level.setBlock(target, desired, 2);
                    }
                }
            }
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource rand) {
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        // On explicit removal of the source (player placed block), remove children that belonged only to this source.
        if (!level.isClientSide() && level instanceof ServerLevel serverLevel) {
            for (int y = 1; y <= MAX_HEIGHT; y++) {
                for (int dx = -HORIZONTAL_RADIUS; dx <= HORIZONTAL_RADIUS; dx++) {
                    for (int dz = -HORIZONTAL_RADIUS; dz <= HORIZONTAL_RADIUS; dz++) {
                        BlockPos target = pos.offset(dx, y, dz);
                        if (!serverLevel.getBlockState(target).is(ModBlocks.GAS_CHILD.get())) continue;

                        // if any other source (excluding this one) claims it, keep it
                        if (GasUtil.isClaimedByAnySource(serverLevel, target, MAX_HEIGHT, HORIZONTAL_RADIUS, pos)) continue;

                        // otherwise remove it — this source was its owner
                        serverLevel.removeBlock(target, false);
                    }
                }
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }
}
