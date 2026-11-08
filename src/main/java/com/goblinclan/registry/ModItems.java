package com.goblinclan.registry;

import com.goblinclan.GoblinClanMod;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(GoblinClanMod.MODID);

    public static final DeferredItem<DeferredSpawnEggItem> GOBLIN_SPAWN_EGG =
            ITEMS.register("goblin_spawn_egg", () ->
                    new DeferredSpawnEggItem(ModEntities.GOBLIN, 0x5E8C3A, 0x6B4A2B, new Item.Properties()));

    public static final DeferredItem<DeferredSpawnEggItem> GOBLIN_SCOUT_SPAWN_EGG =
            ITEMS.register("goblin_scout_spawn_egg", () ->
                    new DeferredSpawnEggItem(ModEntities.GOBLIN_SCOUT, 0x7BA83F, 0xC9B458, new Item.Properties()));

    public static final DeferredItem<DeferredSpawnEggItem> GOBLIN_SHAMAN_SPAWN_EGG =
            ITEMS.register("goblin_shaman_spawn_egg", () ->
                    new DeferredSpawnEggItem(ModEntities.GOBLIN_SHAMAN, 0x4B6E32, 0x7A3FA0, new Item.Properties()));

    public static final DeferredItem<DeferredSpawnEggItem> GOBLIN_CHIEFTAIN_SPAWN_EGG =
            ITEMS.register("goblin_chieftain_spawn_egg", () ->
                    new DeferredSpawnEggItem(ModEntities.GOBLIN_CHIEFTAIN, 0x3E5A2A, 0xA83232, new Item.Properties()));

    public static final DeferredItem<DeferredSpawnEggItem> SHREK_SPAWN_EGG =
            ITEMS.register("shrek_spawn_egg", () ->
                    new DeferredSpawnEggItem(ModEntities.SHREK, 0x7DA83A, 0x8A6A3A, new Item.Properties()));
}
