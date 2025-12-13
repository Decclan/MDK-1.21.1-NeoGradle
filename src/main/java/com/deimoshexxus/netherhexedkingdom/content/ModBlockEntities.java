package com.deimoshexxus.netherhexedkingdom.content;

import com.deimoshexxus.netherhexedkingdom.NetherHexedKingdom;
import com.deimoshexxus.netherhexedkingdom.content.custom.blocks.SoulGlowMushroomBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, NetherHexedKingdom.MODID);

    // --------------------------------
    // Example BlockEntity Registration
    // --------------------------------

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SoulGlowMushroomBlockEntity>>
            SOULGLOW_MUSHROOM = register(
            "soul_glow_mushroom",
            () -> BlockEntityType.Builder.of(
                    SoulGlowMushroomBlockEntity::new,
                    ModBlocks.SOULGLOW_MUSHROOM.get()
            ).build(null)
    );

    // -------------------------
    // Registry Helpers
    // -------------------------

    private static <T extends BlockEntityType<?>> DeferredHolder<BlockEntityType<?>, T> register(
            String name, Supplier<T> typeSupplier
    ) {
        return BLOCK_ENTITIES.register(name, typeSupplier);
    }

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
