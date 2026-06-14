package com.deimoshexxus.netherhexedkingdom.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class MobSpawnConfig {

    public final ModConfigSpec.BooleanValue enabled;

    public MobSpawnConfig(
            ModConfigSpec.Builder builder,
            String name,
            boolean enabledDefault
    ) {
        builder.push(name);

        enabled = builder
                .comment("Enable spawning")
                .define("enabled", enabledDefault);

        builder.pop();
    }
}