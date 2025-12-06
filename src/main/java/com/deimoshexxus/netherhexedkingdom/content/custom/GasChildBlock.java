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
