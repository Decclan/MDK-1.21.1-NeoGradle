package com.deimoshexxus.netherhexedkingdom.content.custom.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.item.context.BlockPlaceContext;
import javax.annotation.Nullable;

import java.util.Optional;

public class MasoniaeMushroomBlock extends BushBlock implements BonemealableBlock {

    public static final MapCodec<MasoniaeMushroomBlock> CODEC =
            simpleCodec(MasoniaeMushroomBlock::new);

    protected static final VoxelShape SHAPE =
            Block.box(5.0D, 0.0D, 5.0D, 11.0D, 6.0D, 11.0D);

    private final ResourceKey<ConfiguredFeature<?, ?>> feature;

    @Override
    public MapCodec<MasoniaeMushroomBlock> codec() {
        return CODEC;
    }

    public MasoniaeMushroomBlock(
            ResourceKey<ConfiguredFeature<?, ?>> feature,
            BlockBehaviour.Properties properties
    ) {
        super(properties);
        this.feature = feature;
    }

    /**
     * Required because simpleCodec only supports Properties constructors.
     */
    public MasoniaeMushroomBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.feature = null;
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
    protected void randomTick(
            BlockState state,
            ServerLevel level,
            BlockPos pos,
            RandomSource random
    ) {
        // Slightly slower than vanilla (1/40 vs 1/25)
        if (random.nextInt(40) != 0) {
            return;
        }

        int remaining = 5;

        for (BlockPos checkPos : BlockPos.betweenClosed(
                pos.offset(-4, -1, -4),
                pos.offset(4, 1, 4)
        )) {
            if (level.getBlockState(checkPos).is(this)) {
                if (--remaining <= 0) {
                    return;
                }
            }
        }

        BlockPos spreadPos = pos.offset(
                random.nextInt(3) - 1,
                random.nextInt(2) - random.nextInt(2),
                random.nextInt(3) - 1
        );

        for (int i = 0; i < 4; ++i) {
            if (level.isEmptyBlock(spreadPos)
                    && state.canSurvive(level, spreadPos)) {
                pos = spreadPos;
            }

            spreadPos = pos.offset(
                    random.nextInt(3) - 1,
                    random.nextInt(2) - random.nextInt(2),
                    random.nextInt(3) - 1
            );
        }

        if (level.isEmptyBlock(spreadPos)
                && state.canSurvive(level, spreadPos)) {
            level.setBlock(spreadPos, state, 2);
        }
    }

    @Override
    protected boolean mayPlaceOn(
            BlockState state,
            BlockGetter level,
            BlockPos pos
    ) {
        return state.isSolidRender(level, pos);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = this.defaultBlockState();

        return state.canSurvive(
                context.getLevel(),
                context.getClickedPos()
        ) ? state : null;
    }

    @Override
    protected boolean canSurvive(
            BlockState state,
            LevelReader level,
            BlockPos pos
    ) {
        BlockPos belowPos = pos.below();
        BlockState belowState = level.getBlockState(belowPos);

        // Custom substrates
        if (belowState.is(Blocks.BONE_BLOCK) || belowState.is(Blocks.MYCELIUM)) {
            return true;
        }

        var soilDecision =
                belowState.canSustainPlant(level, belowPos, Direction.UP, state);

        return belowState.is(BlockTags.MUSHROOM_GROW_BLOCK)
                ? true
                : soilDecision.isDefault()
                ? level.getRawBrightness(pos, 0) < 13
                && this.mayPlaceOn(belowState, level, belowPos)
                : soilDecision.isTrue();
    }

    public boolean growMushroom(
            ServerLevel level,
            BlockPos pos,
            BlockState state,
            RandomSource random
    ) {
        if (feature == null) {
            return false;
        }

        Optional<? extends Holder<ConfiguredFeature<?, ?>>> optional =
                level.registryAccess()
                        .registryOrThrow(Registries.CONFIGURED_FEATURE)
                        .getHolder(feature);

        var event = net.neoforged.neoforge.event.EventHooks.fireBlockGrowFeature(
                level,
                random,
                pos,
                optional.orElse(null)
        );

        if (event.isCanceled()) {
            return false;
        }

        optional = Optional.ofNullable(event.getFeature());

        if (optional.isEmpty()) {
            return false;
        }

        level.removeBlock(pos, false);

        if (optional.get()
                .value()
                .place(
                        level,
                        level.getChunkSource().getGenerator(),
                        random,
                        pos
                )) {
            return true;
        }

        level.setBlock(pos, state, 3);
        return false;
    }

    @Override
    public boolean isValidBonemealTarget(
            LevelReader level,
            BlockPos pos,
            BlockState state
    ) {
        return feature != null;
    }

    @Override
    public boolean isBonemealSuccess(
            Level level,
            RandomSource random,
            BlockPos pos,
            BlockState state
    ) {
        return random.nextFloat() < 0.4F;
    }

    @Override
    public void performBonemeal(
            ServerLevel level,
            RandomSource random,
            BlockPos pos,
            BlockState state
    ) {
        growMushroom(level, pos, state, random);
    }
}
