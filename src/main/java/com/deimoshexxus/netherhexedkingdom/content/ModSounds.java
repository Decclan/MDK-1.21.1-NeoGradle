package com.deimoshexxus.netherhexedkingdom.content;

import com.deimoshexxus.netherhexedkingdom.NetherHexedKingdomMain;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModSounds {

    public static final DeferredRegister<SoundEvent> SOUNDS =
            DeferredRegister.create(Registries.SOUND_EVENT, NetherHexedKingdomMain.MODID);

    public static final Supplier<SoundEvent> GUARD_AMBIENT =
            register("hexan_guard_ambient");

    public static final Supplier<SoundEvent> GUARD_HURT =
            register("hexan_guard_hurt");

    public static final Supplier<SoundEvent> GUARD_DEATH =
            register("hexan_guard_death");

//    public static final Supplier<SoundEvent> GUARD_THROW_GRENADE =
//            register("hexan_guard_throw_grenade");

    private static Supplier<SoundEvent> register(String name) {
        return SOUNDS.register(
                name,
                () -> SoundEvent.createVariableRangeEvent(
                        ResourceLocation.fromNamespaceAndPath(NetherHexedKingdomMain.MODID, name)
                )
        );
    }
}
