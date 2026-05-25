package com.example.rpgminecraft;

import net.fabricmc.api.ModInitializer;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RpgMinecraftMod implements ModInitializer {

    public static final String MOD_ID = "rpgminecraft";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final EntityType<RpgRocketEntity> RPG_ROCKET = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(MOD_ID, "rpg_rocket"),
            EntityType.Builder.<RpgRocketEntity>create(RpgRocketEntity::new, SpawnGroup.MISC)
                    .maxTrackingRange(4)
                    .trackingTickInterval(10)
                    .build()
    );

    public static final Item RPG_LAUNCHER = registerLauncher("rpg_launcher", RpgRocketEntity.RocketMode.STANDARD);
    public static final Item NUKE_LAUNCHER = registerLauncher("nuke_launcher", RpgRocketEntity.RocketMode.NUKE);
    public static final Item HUNTER_LAUNCHER = registerLauncher("hunter_launcher", RpgRocketEntity.RocketMode.HUNTER);

    private static Item registerLauncher(String id, RpgRocketEntity.RocketMode mode) {
        return Registry.register(Registries.ITEM, Identifier.of(MOD_ID, id), new RpgLauncherItem(new Item.Settings().maxCount(1), mode));
    }

    @Override
    public void onInitialize() {
        LOGGER.info("RPG Minecraft loaded!");
    }
}
