package com.deimoshexxus.netherhexedkingdom.content.entities;

import com.deimoshexxus.netherhexedkingdom.content.ModEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;

import java.util.List;

public class GrenadeProjectileEntity extends Projectile implements ItemSupplier {

    private static final double GRAVITY = 0.06D;
    private static final double DEFAULT_SPEED = 1.0D;
    private static final double DEFAULT_INACCURACY = 0.12D;
    private static final int MAX_LIFETIME = 60;

    public GrenadeProjectileEntity(EntityType<? extends GrenadeProjectileEntity> type, Level level) {
        super(type, level);
    }

    public GrenadeProjectileEntity(Level level, LivingEntity owner, Vec3 velocity) {
        this(ModEntities.GRENADE.get(), level);
        this.setOwner(owner);
        this.setPos(owner.getX(), owner.getEyeY() - 0.1D, owner.getZ());
        this.setDeltaMovement(velocity);
    }

    /* ------------------------------------------------------------ */
    /* Factory helpers                                              */
    /* ------------------------------------------------------------ */

    public static GrenadeProjectileEntity createThrown(Level level, LivingEntity shooter) {
        Vec3 dir = shooter.getLookAngle()
                .add(0.0, 0.08, 0.0)
                .normalize()
                .scale(DEFAULT_SPEED);

        return applyInaccuracy(
                new GrenadeProjectileEntity(level, shooter, dir),
                level.getRandom(),
                DEFAULT_INACCURACY
        );
    }

    public static GrenadeProjectileEntity createThrownAtTarget(
            Level level,
            LivingEntity shooter,
            Vec3 targetPos,
            double speed,
            double inaccuracy
    ) {
        Vec3 source = shooter.getEyePosition();
        Vec3 dir = targetPos.subtract(source).normalize();

        // Upward bias so it arcs
        dir = dir.add(0.0, 0.12, 0.0).normalize().scale(speed);

        GrenadeProjectileEntity grenade =
                new GrenadeProjectileEntity(level, shooter, dir);

        return applyInaccuracy(grenade, level.getRandom(), inaccuracy);
    }

    private static GrenadeProjectileEntity applyInaccuracy(
            GrenadeProjectileEntity proj,
            RandomSource rnd,
            double inaccuracy
    ) {
        if (inaccuracy <= 0) return proj;

        Vec3 v = proj.getDeltaMovement();
        v = v.add(
                rnd.nextGaussian() * inaccuracy,
                rnd.nextGaussian() * inaccuracy * 0.5,
                rnd.nextGaussian() * inaccuracy
        );

        proj.setDeltaMovement(v);
        return proj;
    }

    /* ------------------------------------------------------------ */
    /* Tick + Physics                                               */
    /* ------------------------------------------------------------ */

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide() && this.tickCount > MAX_LIFETIME) {
            explode();
            return;
        }

        Vec3 motion = this.getDeltaMovement();

        // --- HIT DETECTION ---
        HitResult hitResult = ProjectileUtil.getHitResultOnMoveVector(
                this,
                this::canHitEntity
        );

        if (hitResult.getType() != HitResult.Type.MISS) {
            this.onHit(hitResult);
        }

        // --- MOVE ---
        this.move(MoverType.SELF, motion);

        // --- GRAVITY ---
        this.setDeltaMovement(motion.add(0.0D, -GRAVITY, 0.0D));

        // --- ROTATION (optional but recommended) ---
        this.updateRotation();

        // --- FUSE SOUND ---
        if (!this.level().isClientSide() && this.tickCount % 4 == 0) {
            this.level().playSound(
                    null,
                    this.getX(), this.getY(), this.getZ(),
                    SoundEvents.CREEPER_PRIMED,
                    SoundSource.NEUTRAL,
                    0.8F,
                    1.0F
            );
        }
    }

    /* ------------------------------------------------------------ */
    /* Hit Handling                                                 */
    /* ------------------------------------------------------------ */

    @Override
    protected boolean canHitEntity(Entity entity) {
        return super.canHitEntity(entity)
                && entity != this.getOwner();
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);

        if (!this.level().isClientSide()) {
            explode();
        }
    }

    private void explode() {
        Level level = this.level();

        level.explode(
                this,
                this.getX(),
                this.getY(),
                this.getZ(),
                1.5F,
                Level.ExplosionInteraction.BLOCK
        );

        // AOE effects
        AABB area = this.getBoundingBox().inflate(3.0D);
        List<LivingEntity> entities =
                level.getEntitiesOfClass(LivingEntity.class, area, e -> e != this.getOwner());

        for (LivingEntity e : entities) {
            e.hurt(
                    this.level().damageSources().thrown(this, this.getOwner()),
                    8.0F
            );
            // do not use, causes suspicious holder creative tab crash bug
            //e.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 1)); // 3 seconds

            if (e instanceof Player player) {

                // 100 ticks = 5 seconds
                int shieldDisableTicks = 100;

                player.getCooldowns().addCooldown(Items.SHIELD, shieldDisableTicks);

                // Optional: force shield lowering immediately
                player.stopUsingItem();
            }
        }

        if (level instanceof ServerLevel server) {
            server.sendParticles(
                    ParticleTypes.EXPLOSION,
                    this.getX(), this.getY(), this.getZ(),
                    1,
                    0.3, 0.3, 0.3,
                    0.0
            );
        }
        this.discard();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
    }

    @Override
    public ItemStack getItem() {
        // Use a real item (custom grenade item is ideal)
        return new ItemStack(Items.FIRE_CHARGE);
    }



}
