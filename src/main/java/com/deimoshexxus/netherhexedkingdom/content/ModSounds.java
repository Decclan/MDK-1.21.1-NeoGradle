package com.deimoshexxus.netherhexedkingdom.content;

import com.deimoshexxus.netherhexedkingdom.NetherHexedKingdom;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModSounds {

    public static final DeferredRegister<SoundEvent> SOUNDS =
            DeferredRegister.create(Registries.SOUND_EVENT, NetherHexedKingdom.MODID);

    public static final Supplier<SoundEvent> GUARD_AMBIENT =
            register("entity.hexan_guard.hexan_guard_ambient");

    public static final Supplier<SoundEvent> GUARD_STEP =
            register("entity.hexan_guard.hexan_guard_step");

    public static final Supplier<SoundEvent> GUARD_HURT =
            register("entity.hexan_guard.hexan_guard_hurt");

    public static final Supplier<SoundEvent> GUARD_DEATH =
            register("entity.hexan_guard.hexan_guard_death");

    public static final Supplier<SoundEvent> GUARD_THROW_GRENADE =
            register("entity.hexan_guard.throw_grenade");

    public static final Supplier<SoundEvent> GAS_AMBIENT =
            register("blocks.gas_ambient");

    private static Supplier<SoundEvent> register(String path) {
        return SOUNDS.register(
                path, // registry name
                () -> SoundEvent.createVariableRangeEvent(
                        ResourceLocation.fromNamespaceAndPath(NetherHexedKingdom.MODID, path)
                )
        );
    }
}
