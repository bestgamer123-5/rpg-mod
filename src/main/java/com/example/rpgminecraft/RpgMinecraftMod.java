package com.example.rpgminecraft;

import net.fabricmc.api.ModInitializer;

import com.carlib.api.CarIntegrationProvider;
import com.carlib.api.CarLibApi;

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
    public static final Item MOB_LAUNCHER = registerLauncher("mob_launcher", RpgRocketEntity.RocketMode.MOB_CANNON);
    public static final Item ANYTHING_LAUNCHER = registerLauncher("anything_launcher", RpgRocketEntity.RocketMode.ANYTHING_CANNON);

    private static Item registerLauncher(String id, RpgRocketEntity.RocketMode mode) {
        return Registry.register(Registries.ITEM, Identifier.of(MOD_ID, id), new RpgLauncherItem(new Item.Settings().maxCount(1), mode));
    }

    @Override
    public void onInitialize() {
        CarLibApi.registerProvider(new RpgCarLibProvider());
        LOGGER.info("RPG Minecraft loaded with CarLib integration!");
    }

    private static final class RpgCarLibProvider implements CarIntegrationProvider {
        @Override
        public String providerId() {
            return MOD_ID;
        }

        @Override
        public java.util.Set<String> supportedFeatures() {
            return java.util.Set.of("rpg_launchers", "rocket_modes");
        }

        @Override
        public Object invoke(String featureKey, Object input) {
            return switch (featureKey) {
                case "rpg_launchers" -> java.util.List.of("rpg_launcher", "nuke_launcher", "hunter_launcher", "mob_launcher", "anything_launcher");
                case "rocket_modes" -> java.util.List.of("STANDARD", "NUKE", "HUNTER", "MOB_CANNON", "ANYTHING_CANNON");
                default -> null;
            };
        }
        LOGGER.info("RPG Minecraft loaded!");
    }
}
