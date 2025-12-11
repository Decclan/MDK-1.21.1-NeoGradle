package com.deimoshexxus.netherhexedkingdom.content.custom.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SoulGlowMushroomBlock extends Block {

    private static final VoxelShape SHAPE = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 5.0D, 12.0D);
    //private static final VoxelShape SHAPE = Block.box(3.0D, 0.0D, 3.0D, 13.0D, 13.0D, 13.0D);

    public SoulGlowMushroomBlock(BlockBehaviour.Properties props) {
        super(BlockBehaviour.Properties.of()
                .strength(0.2F)
                .noOcclusion()
                .instabreak()
                .lightLevel(state -> 5) // emits light
        );
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        // Return EMPTY so entities can pass through
        return Shapes.empty();
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (random.nextInt(5) == 0) { // ~20% chance
            double x = pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 0.3;
            double y = pos.getY() + 0.7;
            double z = pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 0.3;

            level.addParticle(ParticleTypes.GLOW, x, y, z, 0.0, 0.01, 0.0);
        }
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
                                            Player player, BlockHitResult hit) {
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
                        0.0, 0.04, 0.0
                );
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter getter, BlockPos pos) {
        return true;
    }
}

