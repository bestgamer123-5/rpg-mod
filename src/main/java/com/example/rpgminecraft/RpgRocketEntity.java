package com.example.rpgminecraft;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public class RpgRocketEntity extends ThrownItemEntity {
    public RpgRocketEntity(EntityType<? extends RpgRocketEntity> entityType, World world) {
        super(entityType, world);
    }

    public RpgRocketEntity(World world, LivingEntity owner) {
        super(RpgMinecraftMod.RPG_ROCKET, owner, world);
    }

    @Override
    protected Item getDefaultItem() {
        return RpgMinecraftMod.RPG_LAUNCHER;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.getWorld().isClient) {
            this.getWorld().addParticle(
                    ParticleTypes.SMOKE,
                    this.getX(),
                    this.getY(),
                    this.getZ(),
                    0,
                    0,
                    0
            );
        }
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);

        if (!this.getWorld().isClient) {
            this.getWorld().createExplosion(
                    this,
                    this.getX(),
                    this.getY(),
                    this.getZ(),
                    2.5f,
                    World.ExplosionSourceType.MOB
            );
            this.discard();
        }
    }

    @Override
    protected float getGravity() {
        return 0.0f;
    }
}
