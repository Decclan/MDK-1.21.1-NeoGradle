package com.deimoshexxus.netherhexedkingdom.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class CommonConfig {

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    // Structures
    public static final ModConfigSpec.BooleanValue HEXED_WATCH_TOWER =
            BUILDER.define("Generate Hexed Watch Tower?", true);

    public static final ModConfigSpec.BooleanValue HEXED_PRISON =
            BUILDER.define("Generate Hexed Prison?", true);

    public static final ModConfigSpec.BooleanValue HEXED_LOOKOUT =
            BUILDER.define("Generate Hexed Lookout?", true);

    public static final ModConfigSpec.BooleanValue HEXED_BULLION_TEMPLE =
            BUILDER.define("Generate Hexed Bullion Temple?", true);

    public static final ModConfigSpec.BooleanValue HEXED_OUTPOST =
            BUILDER.define("Generate Hexed Outpost?", true);

    public static final ModConfigSpec.BooleanValue HEXED_NETHER_FOSSIL =
            BUILDER.define("Generate Hexed Nether Fossil?", true);

    public static final ModConfigSpec.BooleanValue CRIMSON_MOTHER_FUNGUS =
            BUILDER.define("Generate Crimson Mother Fungus?", true);

    public static final ModConfigSpec.BooleanValue WARPED_MOTHER_FUNGUS =
            BUILDER.define("Generate Warped Mother Fungus?", true);

    public static final ModConfigSpec.BooleanValue HEXED_GREED_MINES =
            BUILDER.define("Generate Hexed Greed Mines?", true);

    public static final ModConfigSpec.BooleanValue HEXED_VOLCAN_PIT =
            BUILDER.define("Generate Hexed Volcan Pit?", true);

    public static final ModConfigSpec.BooleanValue HEXED_RED_SUN_TOWER =
            BUILDER.define("Generate Hexed Red Sun Tower?", true);

    public static final ModConfigSpec.BooleanValue HEXED_IRON_CLAD =
            BUILDER.define("Generate Hexed Iron Clad?", true);

    public static final ModConfigSpec.BooleanValue HEXED_RED_PYRAMID =
            BUILDER.define("Generate Hexed Red Pyramid?", true);

    // Mob configs
    public static final MobSpawnConfig GARGOYLE_POSSESSED;
    public static final MobSpawnConfig DECAYED_ZOMBIFIED_PIGLIN;
    public static final MobSpawnConfig HEXED_ZOMBIE_HORSE;
    public static final MobSpawnConfig WITHER_SKELETON_HORSE;
    public static final MobSpawnConfig HEXED_ZOMBIE_HORSE_JOCKEY;
    public static final MobSpawnConfig WITHER_SKELETON_HORSE_JOCKEY;
    public static final MobSpawnConfig GUARD_ZOMBIE_HORSE_JOCKEY;

    public static final ModConfigSpec SPEC;

    static {

        BUILDER.push("mobs");

        GARGOYLE_POSSESSED = new MobSpawnConfig(
                BUILDER,
                "Spawn Gargoyle Possessed in Basalt Delta?",
                true
        );

        DECAYED_ZOMBIFIED_PIGLIN = new MobSpawnConfig(
                BUILDER,
                "Spawn Zombified Piglin in Nether Wastes? (Decayed Infection Event!)",
                true
        );

        HEXED_ZOMBIE_HORSE = new MobSpawnConfig(
                BUILDER,
                "Spawn Hexed Zombie Horse in Crimson Forest?",
                true
        );

        WITHER_SKELETON_HORSE = new MobSpawnConfig(
                BUILDER,
                "Spawn Skeleton Horse in Nether Wastes?",
                true
        );

        HEXED_ZOMBIE_HORSE_JOCKEY = new MobSpawnConfig(
                BUILDER,
                "Hexed Zombie Horse Jockey in Soul Sand Valley?",
                true
        );

        WITHER_SKELETON_HORSE_JOCKEY = new MobSpawnConfig(
                BUILDER,
                "Spawn Wither Skeleton Horse Jockey in Nether Wastes",
                true
        );

        GUARD_ZOMBIE_HORSE_JOCKEY = new MobSpawnConfig(
                BUILDER,
                "Spawn Hexan Guard Zombie Horse Jockey in Nether Wastes?",
                true
        );

        BUILDER.pop();

        SPEC = BUILDER.build();
    }

    private CommonConfig() {}
}