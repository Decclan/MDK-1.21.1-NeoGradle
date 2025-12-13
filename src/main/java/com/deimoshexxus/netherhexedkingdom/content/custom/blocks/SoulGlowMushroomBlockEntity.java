package com.deimoshexxus.netherhexedkingdom.content.custom.blocks;

import com.deimoshexxus.netherhexedkingdom.content.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SoulGlowMushroomBlockEntity extends BlockEntity {

    public SoulGlowMushroomBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SOULGLOW_MUSHROOM.get(), pos, state);
    }
}
