package com.example.rpgminecraft;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
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

import java.util.List;

public class RpgLauncherItem extends Item {
    private static final String LOADED_ITEM_KEY = "LoadedItem";
public class RpgLauncherItem extends Item {
    private final RpgRocketEntity.RocketMode rocketMode;

    public RpgLauncherItem(Settings settings, RpgRocketEntity.RocketMode rocketMode) {
        super(settings);
        this.rocketMode = rocketMode;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack launcherStack = user.getStackInHand(hand);

        if (rocketMode == RpgRocketEntity.RocketMode.MOB_CANNON) {
            shootRandomNonHostile(world, user);
            return finishUse(world, user, hand, launcherStack, SoundEvents.ENTITY_FIREWORK_ROCKET_LAUNCH);
        }

        if (rocketMode == RpgRocketEntity.RocketMode.ANYTHING_CANNON) {
            if (user.isSneaking()) {
                boolean loaded = tryLoadFromOffhand(user, launcherStack);
                return loaded ? TypedActionResult.success(launcherStack) : TypedActionResult.fail(launcherStack);
            }

            if (shootLoadedItem(world, user, launcherStack)) {
                return finishUse(world, user, hand, launcherStack, SoundEvents.ENTITY_ARROW_SHOOT);
            }

            return TypedActionResult.fail(launcherStack);
        }
        ItemStack itemStack = user.getStackInHand(hand);

        if (!world.isClient) {
            RpgRocketEntity rocket = new RpgRocketEntity(world, user, rocketMode);
            rocket.setVelocity(user, user.getPitch(), user.getYaw(), 0.0f, rocketMode.velocity, 0.4f);
            world.spawnEntity(rocket);
        }

        return finishUse(world, user, hand, launcherStack, SoundEvents.ENTITY_FIREWORK_ROCKET_LAUNCH);
    }

    private TypedActionResult<ItemStack> finishUse(World world, PlayerEntity user, Hand hand, ItemStack stack, SoundEvent sound) {
        world.playSound(null, user.getX(), user.getY(), user.getZ(), sound, SoundCategory.PLAYERS, 0.8f, 0.8f + world.random.nextFloat() * 0.4f);
        user.incrementStat(Stats.USED.getOrCreateStat(this));
        EquipmentSlot slot = hand == Hand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
        stack.damage(1, user, slot);
        return TypedActionResult.success(stack, world.isClient());
    }

    private void shootRandomNonHostile(World world, PlayerEntity user) {
        if (world.isClient) return;
        List<net.minecraft.entity.EntityType<?>> passiveTypes = Registries.ENTITY_TYPE.stream()
                .filter(type -> type.getSpawnGroup() == SpawnGroup.CREATURE || type.getSpawnGroup() == SpawnGroup.AMBIENT || type.getSpawnGroup() == SpawnGroup.WATER_AMBIENT)
                .filter(net.minecraft.entity.EntityType::isSummonable)
                .toList();
        if (passiveTypes.isEmpty()) return;

        net.minecraft.entity.EntityType<?> selected = passiveTypes.get(world.random.nextInt(passiveTypes.size()));
        Entity entity = selected.create(world);
        if (entity == null) return;

        entity.setPosition(user.getX(), user.getEyeY() - 0.2, user.getZ());
        entity.setVelocity(user.getRotationVec(1.0f).multiply(1.6));
        world.spawnEntity(entity);
    }

    private boolean tryLoadFromOffhand(PlayerEntity user, ItemStack launcherStack) {
        ItemStack offhand = user.getOffHandStack();
        if (offhand.isEmpty() || offhand.getItem() instanceof RpgLauncherItem) {
            return false;
        }

        ItemStack single = offhand.copyWithCount(1);
        NbtCompound stackNbt = getOrCreateLauncherData(launcherStack);
        stackNbt.put(LOADED_ITEM_KEY, ItemStack.CODEC.encodeStart(NbtOps.INSTANCE, single).result().orElse(new NbtCompound()));
        launcherStack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(stackNbt));
        offhand.decrement(1);
        return true;
    }

    private boolean shootLoadedItem(World world, PlayerEntity user, ItemStack launcherStack) {
        NbtCompound stackNbt = getLauncherData(launcherStack);
        if (stackNbt == null || !stackNbt.contains(LOADED_ITEM_KEY)) return false;

        ItemStack loaded = ItemStack.CODEC.parse(NbtOps.INSTANCE, stackNbt.get(LOADED_ITEM_KEY)).result().orElse(ItemStack.EMPTY);
        if (loaded.isEmpty()) return false;

        if (!world.isClient) {
            ItemEntity projectile = new ItemEntity(world, user.getX(), user.getEyeY() - 0.2, user.getZ(), loaded.copy());
            projectile.setVelocity(user.getRotationVec(1.0f).multiply(1.8));
            projectile.setPickupDelay(40);
            world.spawnEntity(projectile);
        }

        stackNbt.remove(LOADED_ITEM_KEY);
        launcherStack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(stackNbt));
        return true;
    }

    private NbtCompound getOrCreateLauncherData(ItemStack stack) {
        NbtCompound existing = getLauncherData(stack);
        return existing == null ? new NbtCompound() : existing.copy();
    }

    private NbtCompound getLauncherData(ItemStack stack) {
        NbtComponent customData = stack.get(DataComponentTypes.CUSTOM_DATA);
        return customData == null ? null : customData.copyNbt();
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
