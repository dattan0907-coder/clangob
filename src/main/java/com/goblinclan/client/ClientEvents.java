package com.goblinclan.client;

import com.goblinclan.GoblinClanMod;
import com.goblinclan.entity.GoblinChieftainEntity;
import com.goblinclan.entity.GoblinEntity;
import com.goblinclan.entity.GoblinScoutEntity;
import com.goblinclan.entity.GoblinShamanEntity;
import com.goblinclan.registry.ModEntities;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = GoblinClanMod.MODID, value = Dist.CLIENT)
public class ClientEvents {
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.GOBLIN.get(),
                ctx -> new GoblinRenderer<GoblinEntity>(ctx, "goblin", false));
        event.registerEntityRenderer(ModEntities.GOBLIN_SCOUT.get(),
                ctx -> new GoblinRenderer<GoblinScoutEntity>(ctx, "goblin_scout", true));
        event.registerEntityRenderer(ModEntities.GOBLIN_SHAMAN.get(),
                ctx -> new GoblinRenderer<GoblinShamanEntity>(ctx, "goblin_shaman", false));
        event.registerEntityRenderer(ModEntities.GOBLIN_CHIEFTAIN.get(),
                ctx -> new GoblinRenderer<GoblinChieftainEntity>(ctx, "goblin_chieftain", true));
        event.registerEntityRenderer(ModEntities.SHREK.get(), ShrekRenderer::new);
    }
}
