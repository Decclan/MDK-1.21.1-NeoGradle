package com.deimoshexxus.netherhexedkingdom.client;

import com.deimoshexxus.netherhexedkingdom.content.ModBlocks;
import com.deimoshexxus.netherhexedkingdom.content.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.AbstractSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.common.NeoForge;

import java.util.ArrayList;
import java.util.List;

public final class GasSourceAmbientHandler {

    // ==== TUNING PARAMETERS ===================================================
    private static final int TICKS_BETWEEN_SCANS = 6;   // ~0.3s
    private static final int SCAN_RADIUS = 10;          // horizontal search
    private static final int VERTICAL_RANGE = 10;       // vertical search
    private static final int PLAY_CHANCE = 3;           // 1/3 chance per block
    private static final int CLUSTER_RADIUS = 4;        // suppress neighbors

    private static int tickCounter = 0;

    private GasSourceAmbientHandler() {}

    // ==== PUBLIC API ==========================================================
    public static void register() {
        NeoForge.EVENT_BUS.addListener(
                EventPriority.NORMAL,
                false,
                ClientTickEvent.Post.class,
                GasSourceAmbientHandler::onClientTick
        );
    }

    // ==== EVENT HANDLER =======================================================
    private static void onClientTick(ClientTickEvent.Post evt) {
        Minecraft mc = Minecraft.getInstance();
        if (mc == null || mc.player == null || mc.level == null)
            return;

        // throttle
        tickCounter++;
        if (tickCounter < TICKS_BETWEEN_SCANS)
            return;
        tickCounter = 0;

        Level level = mc.level;
        BlockPos playerPos = mc.player.blockPosition();

        List<BlockPos> clusterSuppress = new ArrayList<>();
        int clusterSq = CLUSTER_RADIUS * CLUSTER_RADIUS;

        for (int dx = -SCAN_RADIUS; dx <= SCAN_RADIUS; dx++) {
            for (int dy = -VERTICAL_RANGE; dy <= VERTICAL_RANGE; dy++) {
                for (int dz = -SCAN_RADIUS; dz <= SCAN_RADIUS; dz++) {
                    BlockPos pos = playerPos.offset(dx, dy, dz);
                    BlockState state = level.getBlockState(pos);

//                    if (!(state.is(ModBlocks.GAS_SOURCE.get()) ||
//                            state.is(ModBlocks.GAS_CHILD.get())))
//                        continue;
                    if (!state.is(ModBlocks.GAS_SOURCE.get()))
                        continue;

                    // cluster suppression
                    boolean blocked = false;
                    for (BlockPos played : clusterSuppress) {
                        if (played.distSqr(pos) <= clusterSq) {
                            blocked = true;
                            break;
                        }
                    }
                    if (blocked)
                        continue;

                    // random trigger
                    if (level.random.nextInt(PLAY_CHANCE) != 0)
                        continue;

                    playGasSound(mc, pos);
                    clusterSuppress.add(pos.immutable());
                }
            }
        }
    }

    // ==== SOUND PLAYING (CORRECTED FOR 1.21.1) ================================
    private static void playGasSound(Minecraft mc, BlockPos pos) {
        float volume = 0.1f; // adjust as needed
        SimpleSoundInstance sound = new SimpleSoundInstance(
                ModSounds.GAS_AMBIENT.get(),
                SoundSource.BLOCKS,
                volume,
                0.5f,
                mc.level.random,
                pos
        );

        mc.getSoundManager().play(sound);

        //System.out.println("Sound event location: " + ModSounds.GAS_AMBIENT.get().getLocation());

        // Debug print
        // System.out.println("Played gas @ " + pos + " vol=" + volume);
    }
}
