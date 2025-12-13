package com.deimoshexxus.netherhexedkingdom.content.custom.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BracketFungusBlock extends HorizontalDirectionalBlock implements BonemealableBlock {
    private static final Block[] VALID_HOSTS = {
            Blocks.STRIPPED_CRIMSON_STEM,
            Blocks.STRIPPED_WARPED_STEM,
            Blocks.CRIMSON_STEM,    //.get(),
            Blocks.WARPED_STEM    //.get()
    };


    public static final IntegerProperty AGE = BlockStateProperties.AGE_2;

    // Model coordinates (px) match datagen element: from(4,10,12) to(12,12,16)
    // Normalised 0..1:
    private static final double X1 = 4.0D / 16.0D;
    private static final double X2 = 12.0D / 16.0D;
    private static final double Y1 = 3.0D / 16.0D; // was 4.0
    private static final double Y2 = 5.0D / 16.0D; // was 6.0
    private static final double Z1 = 12.0D / 16.0D; // (near south face)
    private static final double Z_OUT = 5.0D / 16.0D; // (outward thickness)

    // Shapes that correspond to the model when rotated:
    private static final VoxelShape SHAPE_SOUTH = Shapes.box(X1, Y1, Z1, X2, Y2, 1.0D);
    private static final VoxelShape SHAPE_NORTH = Shapes.box(X1, Y1, 0.0D, X2, Y2, Z_OUT);
    private static final VoxelShape SHAPE_EAST  = Shapes.box(1.0D - Z_OUT, Y1, X1, 1.0D, Y2, X2);
    private static final VoxelShape SHAPE_WEST  = Shapes.box(0.0D, Y1, X1, Z_OUT, Y2, X2);

    public BracketFungusBlock(BlockBehaviour.Properties props) {
        super(props);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.SOUTH) // default SOUTH to match authored model orientation
                .setValue(AGE, 0)
        );
    }

    private boolean isValidHost(BlockState state) {
        for (Block host : VALID_HOSTS) {
            if (state.is(host)) return true;
        }
        return false;
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return MapCodec.unit(this);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, AGE);
    }

    /**
     * Placement attaches to the clicked face. If the clicked face is not horizontal, placement fails.
     * Example: click the east face of a log => fungus is placed at pos.adjacent and FACING=EAST.
     */
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        Direction face = ctx.getClickedFace();
        if (face.getAxis().isHorizontal()) {
            return this.defaultBlockState().setValue(FACING, face.getOpposite());
        }
        return null;
    }

    /**
     * Survival: there must be a solid block on the opposite side of the facing (the support).
     * Example: FACING = EAST -> support is at pos.relative(WEST).
     */

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        Direction facing = state.getValue(FACING);
        BlockPos supportPos = pos.relative(facing);

        BlockState supportState = level.getBlockState(supportPos);

        return isValidHost(supportState);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return state.getValue(AGE) < 2;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        // 45% chance like cocoa
        return random.nextFloat() < 0.45f;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        int age = state.getValue(AGE);
        if (age < 2) {
            level.setBlock(pos, state.setValue(AGE, age + 1), 2);
        }
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int age = state.getValue(AGE);

        if (age < 2 && random.nextInt(5) == 0) { // 20% chance per tick
            if (canSurvive(state, level, pos)) {
                level.setBlock(pos, state.setValue(AGE, age + 1), 2);
            }
        }
    }

    /**
     * If support is removed, remove the fungus (return AIR). You may replace this with drop logic later.
     */
    @Override
    public BlockState updateShape(BlockState state, Direction side, BlockState neighbor,
                                  LevelAccessor level, BlockPos pos, BlockPos neighborPos) {

        if (!canSurvive(state, level, pos)) {
            return Blocks.AIR.defaultBlockState();
        }
        return state;
    }

    /**
     * Per-facing visual shape (matches the model element when the model is rotated by Y).
     */
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
        return switch (state.getValue(FACING)) {
            case SOUTH -> SHAPE_SOUTH;
            case NORTH -> SHAPE_NORTH;
            case EAST  -> SHAPE_EAST;
            case WEST  -> SHAPE_WEST;
            default   -> SHAPE_SOUTH;
        };
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
        return getShape(state, world, pos, ctx);
    }
}