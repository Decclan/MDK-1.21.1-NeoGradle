package com.deimoshexxus.netherhexedkingdom;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class CommonConfig {

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue HEXED_WATCH_TOWER =
            BUILDER.define("hexedWatchTower", true);

    public static final ModConfigSpec.BooleanValue HEXED_PRISON =
            BUILDER.define("hexedPrison", true);

    public static final ModConfigSpec.BooleanValue HEXED_LOOKOUT =
            BUILDER.define("hexedLookout", true);

    public static final ModConfigSpec.BooleanValue HEXED_BULLION_TEMPLE =
            BUILDER.define("hexedBullionTemple", true);

    public static final ModConfigSpec.BooleanValue HEXED_OUTPOST =
            BUILDER.define("hexedOutpost", true);

    public static final ModConfigSpec.BooleanValue HEXED_NETHER_FOSSIL =
            BUILDER.define("hexedNetherFossil", true);

    public static final ModConfigSpec.BooleanValue CRIMSON_MOTHER_FUNGUS =
            BUILDER.define("crimsonMotherFungus", true);

    public static final ModConfigSpec.BooleanValue WARPED_MOTHER_FUNGUS =
            BUILDER.define("warpedMotherFungus", true);

    public static final ModConfigSpec.BooleanValue HEXED_GREED_MINES =
            BUILDER.define("hexedGreedMines", true);

    public static final ModConfigSpec.BooleanValue HEXED_VOLCAN_PIT =
            BUILDER.define("hexedVolcanPit", true);

    public static final ModConfigSpec.BooleanValue HEXED_RED_SUN_TOWER =
            BUILDER.define("hexedRedSunTower", true);

    public static final ModConfigSpec.BooleanValue HEXED_IRON_CLAD =
            BUILDER.define("hexedIronClad", true);

    public static final ModConfigSpec.BooleanValue HEXED_RED_PYRAMID =
            BUILDER.define("hexedRedPyramid", true);


    public static final ModConfigSpec SPEC = BUILDER.build();
}