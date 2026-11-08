package com.goblinclan.client;

import com.goblinclan.GoblinClanMod;
import com.goblinclan.entity.AbstractGoblinEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

/**
 * One renderer for all four goblins. For asset name "goblin_scout" it loads:
 *   assets/goblin_clan/geo/entity/goblin_scout.geo.json
 *   assets/goblin_clan/animations/entity/goblin_scout.animation.json
 *   assets/goblin_clan/textures/entity/goblin_scout.png
 * and, when glow = true, the emissive layer
 *   assets/goblin_clan/textures/entity/goblin_scout_glowmask.png
 */
public class GoblinRenderer<T extends AbstractGoblinEntity> extends GeoEntityRenderer<T> {
    public GoblinRenderer(EntityRendererProvider.Context context, String assetName, boolean glow) {
        super(context, new DefaultedEntityGeoModel<>(
                ResourceLocation.fromNamespaceAndPath(GoblinClanMod.MODID, assetName), true));
        if (glow) {
            addRenderLayer(new AutoGlowingGeoLayer<>(this));
        }
    }
}
