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
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
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

        if (level().isClientSide()) return;

        ServerLevel serverLevel = (ServerLevel) level();

        BlockPos centerPos = (hitResult instanceof BlockHitResult blockHit)
                ? blockHit.getBlockPos()
                : BlockPos.containing(hitResult.getLocation());

        boolean success = placePortalWithBase(serverLevel, centerPos);

        if (success) {
            // SUCCESS → consume orb naturally (no drop)
            this.discard();
            return;
        }

        // FAILURE → drop item refund
        dropAsItem(serverLevel);

        this.discard();
    }

    private void dropAsItem(ServerLevel level) {
        ItemStack stack = new ItemStack(getDefaultItem());

        // Preserve count if needed
        stack.setCount(1);

        ItemEntity itemEntity = new ItemEntity(
                level,
                getX(),
                getY(),
                getZ(),
                stack
        );

        itemEntity.setPickUpDelay(10); // small delay so it doesn’t instantly re-pickup
        level.addFreshEntity(itemEntity);

        LOGGER.info("Dropped Nether Portal Orb due to failed placement at {}", blockPosition());
    }

    private boolean placePortalWithBase(ServerLevel level, BlockPos centerPos) {

        Rotation rotation = getRotationFromOwner();

        // 1. Find a SAFE anchor first (this is the fix)
        BlockPos anchor = findSafeAnchor(level, centerPos);
        if (anchor == null) {
            LOGGER.warn("No valid anchor found at {}", centerPos);
            return false;
        }

        BlockPos portalOrigin = anchor.offset(rotateOffset(PORTAL_OFFSET, rotation));
        BlockPos baseOrigin   = portalOrigin.below(3); // 🔥 FIX: always directly under portal

        boolean portalPlaced = placeTemplate(level, PORTAL_ID, portalOrigin, rotation);

        if (!portalPlaced) {
            LOGGER.warn("Portal placement failed → aborting");
            return false;
        }

        // Base is now deterministic relative to portal, NOT centerPos
        boolean basePlaced = placeTemplate(level, BASE_ID, baseOrigin, rotation);

        if (!basePlaced) {
            LOGGER.warn("Base failed but portal succeeded → continuing");
        }

        level.playSound(
                null,
                portalOrigin,
                SoundEvents.PORTAL_TRIGGER,
                SoundSource.BLOCKS,
                1.5F,
                0.9F + level.random.nextFloat() * 0.2F
        );

        spawnPortalParticles(level, portalOrigin, rotation);

        return true;
    }

    private boolean placeTemplate(
            ServerLevel level,
            ResourceLocation id,
            BlockPos origin,
            Rotation rotation
    ) {
        StructureTemplate template = level.getStructureManager().getOrCreate(id);

        Vec3i size = template.getSize();
        if (size.equals(Vec3i.ZERO)) {
            LOGGER.error("Structure {} is EMPTY or missing", id);
            return false;
        }

        // --- ROTATION SIZE ---
        BlockPos rotatedSize = switch (rotation) {
            case CLOCKWISE_90, COUNTERCLOCKWISE_90 ->
                    new BlockPos(size.getZ(), size.getY(), size.getX());
            default ->
                    new BlockPos(size.getX(), size.getY(), size.getZ());
        };

        int structureHeight = rotatedSize.getY();

        // --- DIMENSION RULES ---
        boolean isNether = level.dimension() == Level.NETHER;

        int minY = level.getMinBuildHeight();
        int maxY = level.getMaxBuildHeight() - 1;

        // Nether-specific ceiling (below bedrock roof)
        int ceilingLimit = isNether ? 123 : maxY;

        // Clamp start position
        int startY = Math.min(origin.getY(), ceilingLimit);

        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos(
                origin.getX(), startY, origin.getZ()
        );

        // --- STEP 1: FIND GROUND ---
        boolean foundGround = false;

        for (int i = 0; i < 128; i++) {
            if (!level.getBlockState(cursor).canBeReplaced()) {
                foundGround = true;
                break;
            }
            cursor.move(0, -1, 0);

            if (cursor.getY() <= minY) break;
        }

        if (!foundGround) {
            LOGGER.warn("No ground found for {} near {}", id, origin);
            return false;
        }

        // place on top of ground
        cursor.move(0, 1, 0);

        // --- STEP 2: CHECK CLEARANCE ---
        for (int y = 0; y < structureHeight; y++) {
            BlockPos check = cursor.offset(0, y, 0);

            if (check.getY() > ceilingLimit) {
                LOGGER.warn("Ceiling violation for {} at {}", id, check);
                return false;
            }

            if (check.getY() < minY) {
                LOGGER.warn("Below world for {} at {}", id, check);
                return false;
            }

            if (!level.getBlockState(check).canBeReplaced()) {
                LOGGER.warn("Blocked space for {} at {}", id, check);
                return false;
            }
        }

        // --- STEP 3: WORLD BORDER ---
        BlockPos max = cursor.offset(
                rotatedSize.getX() - 1,
                rotatedSize.getY() - 1,
                rotatedSize.getZ() - 1
        );

        if (!level.getWorldBorder().isWithinBounds(cursor) ||
                !level.getWorldBorder().isWithinBounds(max)) {
            LOGGER.warn("Out of world border for {}: {} -> {}", id, cursor, max);
            return false;
        }

        // --- PLACE ---
        StructurePlaceSettings settings = new StructurePlaceSettings()
                .setIgnoreEntities(true)
                .setRotation(rotation);

        boolean placed = template.placeInWorld(
                level,
                cursor,
                cursor,
                settings,
                level.random,
                2
        );

        LOGGER.info(
                "Placed {} at {} (dim={}, height={}, success={})",
                id,
                cursor,
                level.dimension().location(),
                structureHeight,
                placed
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

    private BlockPos findSafeAnchor(ServerLevel level, BlockPos start) {

        int minY = level.getMinBuildHeight();
        int maxY = level.getMaxBuildHeight() - 5; // keep safety margin

        BlockPos.MutableBlockPos pos = start.mutable();

        // Clamp initial Y
        pos.setY(Math.min(pos.getY(), maxY));

        // 1. go down until we find solid ground
        while (pos.getY() > minY) {
            BlockPos below = pos.below();

            if (!level.getBlockState(below).canBeReplaced()) {
                return below.above(); // stand on top
            }

            pos.move(0, -1, 0);
        }

        return null;
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
