package com.example.rpgminecraft;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public class RpgRocketEntity extends ThrownItemEntity {
    private static final String MODE_NBT = "RocketMode";
    private RocketMode rocketMode = RocketMode.STANDARD;

    public enum RocketMode {
        STANDARD(2.5f, 1.7f),
        NUKE(8.0f, 1.2f),
        HUNTER(1.5f, 2.1f),
        MOB_CANNON(0.0f, 0.0f),
        ANYTHING_CANNON(0.0f, 0.0f);
        HUNTER(1.5f, 2.1f);

        public final float explosionPower;
        public final float velocity;

        RocketMode(float explosionPower, float velocity) {
            this.explosionPower = explosionPower;
            this.velocity = velocity;
        }
    }

    public RpgRocketEntity(EntityType<? extends RpgRocketEntity> entityType, World world) {
        super(entityType, world);
    }

    public RpgRocketEntity(World world, LivingEntity owner, RocketMode rocketMode) {
        super(RpgMinecraftMod.RPG_ROCKET, owner, world);
        this.rocketMode = rocketMode;
    }

    @Override
    protected Item getDefaultItem() {
        return RpgMinecraftMod.RPG_LAUNCHER;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.getWorld().isClient) {
            this.getWorld().addParticle(ParticleTypes.SMOKE, this.getX(), this.getY(), this.getZ(), 0, 0, 0);
        }
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);

        if (!this.getWorld().isClient) {
            if (rocketMode == RocketMode.HUNTER) {
                this.getWorld().getEntitiesByClass(
                        AnimalEntity.class,
                        this.getBoundingBox().expand(6.0),
                        animal -> true
                ).forEach(animal -> animal.damage(this.getDamageSources().explosion(this, this.getOwner()), 20.0f));
            }

            this.getWorld().createExplosion(
                    this,
                    this.getX(),
                    this.getY(),
                    this.getZ(),
                    rocketMode.explosionPower,
                    World.ExplosionSourceType.MOB
            );
            this.discard();
        }
    }

    @Override
    protected double getGravity() {
        return 0.0;
    protected float getGravity() {
        return 0.0f;
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putString(MODE_NBT, rocketMode.name());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains(MODE_NBT)) {
            this.rocketMode = RocketMode.valueOf(nbt.getString(MODE_NBT));
        }
    }
}
