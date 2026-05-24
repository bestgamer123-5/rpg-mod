package com.example.rpgminecraft;

import net.fabricmc.api.ModInitializer;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterials;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import net.minecraft.util.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RpgMinecraftMod implements ModInitializer {

    public static final String MOD_ID = "rpgminecraft";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final Item RPG_SWORD = Registry.register(
            Registries.ITEM,
            Identifier.of(MOD_ID, "rpg_sword"),
            new SwordItem(
                    ToolMaterials.DIAMOND,
                    6,
                    -2.2f,
                    new Item.Settings()
            )
    );

    @Override
    public void onInitialize() {

        LOGGER.info("RPG Minecraft loaded!");
    }

    public static void awardExperience(
            PlayerEntity player,
            LivingEntity defeatedEntity
    ) {

        ItemStack mainHand = player.getMainHandStack();

        if (!mainHand.isOf(RPG_SWORD)) {
            return;
        }

        int bonusXp = defeatedEntity.getMaxHealth() >= 20 ? 4 : 2;

        player.addExperience(bonusXp);

        LOGGER.info(
                "Granted {} bonus XP to {}",
                bonusXp,
                player.getName().getString()
        );
    }
}