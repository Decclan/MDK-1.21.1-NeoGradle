package com.deimoshexxus.netherhexedkingdom.content.custom.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class SoulGlowMushroomBlock extends BushBlock implements EntityBlock {

    public static final MapCodec<SoulGlowMushroomBlock> CODEC =
            simpleCodec(SoulGlowMushroomBlock::new);

    private static final VoxelShape SHAPE =
            Block.box(4.0D, 0.0D, 4.0D, 12.0D, 5.0D, 12.0D);

    @Override
    public MapCodec<SoulGlowMushroomBlock> codec() {
        return CODEC;
    }

    public SoulGlowMushroomBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos below = pos.below();
        BlockState soil = level.getBlockState(below);

        return super.canSurvive(state, level, pos)
                || soil.is(Blocks.SOUL_SOIL)
                || soil.is(Blocks.MYCELIUM);
    }

    /**
     * Slower than vanilla mushrooms.
     * Vanilla: 1/25 chance (~4%)
     * Soul Glow: 1/200 chance (0.5%)
     */
    @Override
    protected void randomTick(
            BlockState state,
            ServerLevel level,
            BlockPos pos,
            RandomSource random
    ) {
        if (!level.isAreaLoaded(pos, 1)) {
            return;
        }

        if (random.nextInt(200) == 0) {
            spreadMushroom(state, level, pos, random);
        }
    }

    private void spreadMushroom(
            BlockState state,
            ServerLevel level,
            BlockPos pos,
            RandomSource random
    ) {
        BlockPos targetPos = pos.offset(
                random.nextInt(3) - 1,
                random.nextInt(2) - 1,
                random.nextInt(3) - 1
        );

        if (level.isEmptyBlock(targetPos)
                && state.canSurvive(level, targetPos)) {
            level.setBlock(targetPos, state, 2);
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SoulGlowMushroomBlockEntity(pos, state);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected VoxelShape getShape(
            BlockState state,
            BlockGetter level,
            BlockPos pos,
            CollisionContext context
    ) {
        return SHAPE;
    }

    @Override
    protected VoxelShape getCollisionShape(
            BlockState state,
            BlockGetter level,
            BlockPos pos,
            CollisionContext context
    ) {
        return Shapes.empty();
    }

    @Override
    public void animateTick(
            BlockState state,
            Level level,
            BlockPos pos,
            RandomSource random
    ) {
        if (random.nextInt(5) == 0) {
            double x = pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 0.3;
            double y = pos.getY() + 0.7;
            double z = pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 0.3;

            level.addParticle(
                    ParticleTypes.GLOW,
                    x,
                    y,
                    z,
                    0.0,
                    0.01,
                    0.0
            );
        }
    }

    @Override
    protected InteractionResult useWithoutItem(
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            BlockHitResult hit
    ) {
        if (level.isClientSide) {
            double x = pos.getX() + 0.5;
            double y = pos.getY() + 1.0;
            double z = pos.getZ() + 0.5;

            for (int i = 0; i < 4; i++) {
                level.addParticle(
                        ParticleTypes.GLOW,
                        x + (level.random.nextDouble() - 0.5) * 0.4,
                        y,
                        z + (level.random.nextDouble() - 0.5) * 0.4,
                        0.0,
                        0.04,
                        0.0
                );
            }
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    protected boolean propagatesSkylightDown(
            BlockState state,
            BlockGetter level,
            BlockPos pos
    ) {
        return true;
    }
}
