package com.deimoshexxus.netherhexedkingdom.content.entities;

import com.deimoshexxus.netherhexedkingdom.content.ModEntities;
import com.deimoshexxus.netherhexedkingdom.content.ModItems;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.core.Vec3i;
import org.slf4j.Logger;

public class NetherPortalOrbEntity extends ThrowableItemProjectile {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final ResourceLocation PORTAL_ID =
            ResourceLocation.fromNamespaceAndPath(
                    "netherhexedkingdom",
                    "nether_portal_complete"
            );

    private static final ResourceLocation BASE_ID =
            ResourceLocation.fromNamespaceAndPath(
                    "netherhexedkingdom",
                    "nether_portal_complete_base"
            );

    private static final BlockPos PORTAL_OFFSET = new BlockPos(-2, 0, -3);
    private static final BlockPos BASE_OFFSET   = new BlockPos(-2, -3, -3);

    public NetherPortalOrbEntity(EntityType<? extends ThrowableItemProjectile> type, Level level) {
        super(type, level);
    }

    public NetherPortalOrbEntity(Level level, LivingEntity owner) {
        super(ModEntities.NETHER_PORTAL_ORB.get(), owner, level);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.NETHER_PORTAL_ORB.get();
    }

    private Rotation getRotationFromOwner() {
        if (!(getOwner() instanceof LivingEntity living)) {
            return Rotation.NONE;
        }

        return switch (living.getDirection()) {
            case NORTH, SOUTH -> Rotation.CLOCKWISE_90;
            default -> Rotation.NONE;
        };
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);

        if (!(level() instanceof ServerLevel serverLevel)) {
            return;
        }

        BlockPos centerPos = (hitResult instanceof BlockHitResult blockHit)
                ? blockHit.getBlockPos()
                : BlockPos.containing(hitResult.getLocation());

//        LOGGER.info(
//                "NetherPortalOrb hit at {} in dimension {}",
//                centerPos,
//                serverLevel.dimension().location()
//        );

        placePortalWithBase(serverLevel, centerPos);
        discard();
    }

    private void placePortalWithBase(ServerLevel level, BlockPos centerPos) {

        Rotation rotation = getRotationFromOwner();

        BlockPos portalOrigin =
                centerPos.offset(rotateOffset(PORTAL_OFFSET, rotation));

        BlockPos baseOrigin =
                centerPos.offset(rotateOffset(BASE_OFFSET, rotation));

        LOGGER.info(
                "Placing portal structure: rotation={}, portalOrigin={}, baseOrigin={}",
                rotation, portalOrigin, baseOrigin
        );

        if (!placeTemplate(level, PORTAL_ID, portalOrigin, rotation)) {
            LOGGER.error("Failed to place portal structure {}", PORTAL_ID);
            return;
        }

        if (!placeTemplate(level, BASE_ID, baseOrigin, rotation)) {
            LOGGER.error("Failed to place base structure {}", BASE_ID);
        }

        level.playSound(
                null,
                centerPos,
                SoundEvents.PORTAL_TRIGGER,
                SoundSource.BLOCKS,
                1.5F,
                0.9F + level.random.nextFloat() * 0.2F
        );

        spawnPortalParticles(level, centerPos, rotation);
    }

    // for debug
    private boolean placeTemplate(
            ServerLevel level,
            ResourceLocation id,
            BlockPos origin,
            Rotation rotation
    ) {
        StructureTemplate template = level.getStructureManager().getOrCreate(id);

        Vec3i size = template.getSize();

        if (size.equals(Vec3i.ZERO)) {
            LOGGER.error(
                    "Structure {} is EMPTY — resource not found or shadowed",
                    id
            );
            return false;
        }

        LOGGER.info(
                "Resolved structure {} with size {}",
                id, size
        );

        // --- ROTATION-AWARE SIZE ---
        BlockPos rotatedSize = switch (rotation) {
            case CLOCKWISE_90, COUNTERCLOCKWISE_90 ->
                    new BlockPos(size.getZ(), size.getY(), size.getX());
            default ->
                    new BlockPos(size.getX(), size.getY(), size.getZ());
        };

        // --- COMPUTE BOUNDS ---
        BlockPos min = origin;
        BlockPos max = origin.offset(
                rotatedSize.getX() - 1,
                rotatedSize.getY() - 1,
                rotatedSize.getZ() - 1
        );

        // --- HEIGHT LIMITS ---
        int minY = level.getMinBuildHeight();
        int maxY = level.getMaxBuildHeight() - 1;

        // Compute how much we need to move
        int shiftUp = 0;
        int shiftDown = 0;

        if (min.getY() < minY) {
            shiftUp = minY - min.getY();
        }

        if (max.getY() > maxY) {
            shiftDown = max.getY() - maxY;
        }

        int structureHeight = rotatedSize.getY();
        int worldHeight = maxY - minY + 1;

        if (structureHeight > worldHeight) {
            LOGGER.warn(
                    "Structure {} too tall for dimension: height={} allowed={}",
                    id, structureHeight, worldHeight
            );
            return false;
        }

        // Apply vertical adjustment
        int yOffset = shiftUp - shiftDown;

        if (yOffset != 0) {
            origin = origin.offset(0, yOffset, 0);

            // Recompute bounds after shift
            min = origin;
            max = origin.offset(
                    rotatedSize.getX() - 1,
                    rotatedSize.getY() - 1,
                    rotatedSize.getZ() - 1
            );

            LOGGER.info(
                    "Adjusted structure {} vertically by {} to fit bounds. New: {} -> {}",
                    id, yOffset, min, max
            );
        }

        // --- WORLD BORDER CHECK ---
        BlockPos corner1 = min;
        BlockPos corner2 = new BlockPos(max.getX(), min.getY(), min.getZ());
        BlockPos corner3 = new BlockPos(min.getX(), min.getY(), max.getZ());
        BlockPos corner4 = max;

        if (!level.getWorldBorder().isWithinBounds(corner1) ||
                !level.getWorldBorder().isWithinBounds(corner2) ||
                !level.getWorldBorder().isWithinBounds(corner3) ||
                !level.getWorldBorder().isWithinBounds(corner4)) {

            LOGGER.warn(
                    "Structure {} exceeds world border: {} -> {}",
                    id, min, max
            );
            return false;
        }

        if (min.getY() < minY || max.getY() > maxY) {
            LOGGER.error(
                    "Structure {} still out of bounds after adjustment: {} -> {}",
                    id, min, max
            );
            return false;
        }

        StructurePlaceSettings settings = new StructurePlaceSettings()
                .setIgnoreEntities(true)
                .setRotation(rotation);

        boolean placed = template.placeInWorld(
                level,
                origin,
                origin,
                settings,
                level.random,
                2
        );

        LOGGER.info(
                "Placement result for {} at {}: {}",
                id, origin, placed
        );

        return placed;
    }

    private void spawnPortalParticles(
            ServerLevel level,
            BlockPos center,
            Rotation rotation
    ) {
        for (int i = 0; i < 160; i++) {
            double localX = level.random.nextGaussian() * 3.0;
            double localZ = level.random.nextGaussian() * 3.0;
            double[] rotated = rotateOffset(localX, localZ, rotation);

            level.sendParticles(
                    ParticleTypes.PORTAL,
                    center.getX() + 0.5 + rotated[0],
                    center.getY() + level.random.nextDouble() * 4.0,
                    center.getZ() + 0.5 + rotated[1],
                    1,
                    0.0, 0.05, 0.0,
                    0.04
            );
        }

        for (int i = 0; i < 48; i++) {
            double localX = level.random.nextGaussian() * 1.6;
            double localZ = level.random.nextGaussian() * 1.6;
            double[] rotated = rotateOffset(localX, localZ, rotation);

            level.sendParticles(
                    ParticleTypes.REVERSE_PORTAL,
                    center.getX() + 0.5 + rotated[0],
                    center.getY() + 0.8 + level.random.nextDouble() * 2.2,
                    center.getZ() + 0.5 + rotated[1],
                    1,
                    0.0, 0.02, 0.0,
                    0.01
            );
        }
    }

    private static BlockPos rotateOffset(BlockPos offset, Rotation rotation) {
        return switch (rotation) {
            case CLOCKWISE_90 -> new BlockPos(-offset.getZ(), offset.getY(), offset.getX());
            case CLOCKWISE_180 -> new BlockPos(-offset.getX(), offset.getY(), -offset.getZ());
            case COUNTERCLOCKWISE_90 -> new BlockPos(offset.getZ(), offset.getY(), -offset.getX());
            default -> offset;
        };
    }

    private static double[] rotateOffset(double x, double z, Rotation rotation) {
        return switch (rotation) {
            case CLOCKWISE_90 -> new double[]{-z, x};
            case CLOCKWISE_180 -> new double[]{-x, -z};
            case COUNTERCLOCKWISE_90 -> new double[]{z, -x};
            default -> new double[]{x, z};
        };
    }
}
