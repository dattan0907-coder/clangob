package com.goblinclan;

import com.goblinclan.entity.GoblinChieftainEntity;
import com.goblinclan.entity.GoblinEntity;
import com.goblinclan.entity.GoblinScoutEntity;
import com.goblinclan.entity.GoblinShamanEntity;
import com.goblinclan.entity.ShrekEntity;
import com.goblinclan.registry.ModEntities;
import com.goblinclan.registry.ModItems;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

@Mod(GoblinClanMod.MODID)
public class GoblinClanMod {
    public static final String MODID = "goblin_clan";

    public GoblinClanMod(IEventBus modEventBus) {
        ModEntities.ENTITIES.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);

        modEventBus.addListener(this::registerAttributes);
        modEventBus.addListener(this::buildCreativeTabs);
    }

    private void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.GOBLIN.get(), GoblinEntity.createAttributes().build());
        event.put(ModEntities.GOBLIN_SCOUT.get(), GoblinScoutEntity.createAttributes().build());
        event.put(ModEntities.GOBLIN_SHAMAN.get(), GoblinShamanEntity.createAttributes().build());
        event.put(ModEntities.GOBLIN_CHIEFTAIN.get(), GoblinChieftainEntity.createAttributes().build());
        event.put(ModEntities.SHREK.get(), ShrekEntity.createAttributes().build());
    }

    private void buildCreativeTabs(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
            event.accept(ModItems.GOBLIN_SPAWN_EGG.get());
            event.accept(ModItems.GOBLIN_SCOUT_SPAWN_EGG.get());
            event.accept(ModItems.GOBLIN_SHAMAN_SPAWN_EGG.get());
            event.accept(ModItems.GOBLIN_CHIEFTAIN_SPAWN_EGG.get());
            event.accept(ModItems.SHREK_SPAWN_EGG.get());
        }
    }
}
