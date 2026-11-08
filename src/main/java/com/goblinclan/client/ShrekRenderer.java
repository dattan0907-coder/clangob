package com.goblinclan.client;

import com.goblinclan.GoblinClanMod;
import com.goblinclan.entity.ShrekEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

/**
 * Loads assets/goblin_clan/geo/entity/shrek.geo.json,
 * animations/entity/shrek.animation.json and textures/entity/shrek.png
 */
public class ShrekRenderer extends GeoEntityRenderer<ShrekEntity> {
    public ShrekRenderer(EntityRendererProvider.Context context) {
        super(context, new DefaultedEntityGeoModel<>(
                ResourceLocation.fromNamespaceAndPath(GoblinClanMod.MODID, "shrek"), true));
    }
}
