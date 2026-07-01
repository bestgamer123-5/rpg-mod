package com.example.rpgminecraft;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;

public class RpgMinecraftClientMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(RpgMinecraftMod.RPG_ROCKET, FlyingItemEntityRenderer::new);
    }
}
