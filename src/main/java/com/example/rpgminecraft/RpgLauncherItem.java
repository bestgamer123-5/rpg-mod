package com.example.rpgminecraft;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class RpgLauncherItem extends Item {
    public RpgLauncherItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        if (!world.isClient) {
            RpgRocketEntity rocket = new RpgRocketEntity(world, user);
            rocket.setVelocity(user, user.getPitch(), user.getYaw(), 0.0f, 1.7f, 0.4f);
            world.spawnEntity(rocket);
        }

        world.playSound(
                null,
                user.getX(),
                user.getY(),
                user.getZ(),
                SoundEvents.ENTITY_FIREWORK_ROCKET_LAUNCH,
                SoundCategory.PLAYERS,
                0.8f,
                0.8f + world.random.nextFloat() * 0.4f
        );

        user.incrementStat(Stats.USED.getOrCreateStat(this));
        EquipmentSlot slot = hand == Hand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
        itemStack.damage(1, user, entity -> entity.sendEquipmentBreakStatus(slot));

        return TypedActionResult.success(itemStack, world.isClient());
    }
}
