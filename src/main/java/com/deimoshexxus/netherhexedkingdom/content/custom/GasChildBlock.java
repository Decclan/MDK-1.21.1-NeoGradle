package com.deimoshexxus.netherhexedkingdom.content.custom;

import com.deimoshexxus.netherhexedkingdom.content.ModBlocks;
import com.deimoshexxus.netherhexedkingdom.utils.GasUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;

public class GasChildBlock extends Block {
    public static final IntegerProperty DISTANCE = GasUtil.DISTANCE;
    private static final int MAX_SOURCE_SEARCH = GasUtil.DEFAULT_MAX_HEIGHT;
    private static final int RADIUS = GasUtil.DEFAULT_RADIUS;

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
        // children are passive
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, net.minecraft.world.level.block.Block block, BlockPos fromPos, boolean moving) {
        // intentionally empty: source manages cloud lifecycle to avoid flicker
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
    public boolean isAir(BlockState state) {
        return false;
    }
}
