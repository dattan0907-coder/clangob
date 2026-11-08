package com.goblinclan.registry;

import com.goblinclan.GoblinClanMod;
import com.goblinclan.entity.GoblinChieftainEntity;
import com.goblinclan.entity.GoblinEntity;
import com.goblinclan.entity.GoblinScoutEntity;
import com.goblinclan.entity.GoblinShamanEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(Registries.ENTITY_TYPE, GoblinClanMod.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<GoblinEntity>> GOBLIN =
            ENTITIES.register("goblin", () ->
                    EntityType.Builder.of(GoblinEntity::new, MobCategory.MONSTER)
                            .sized(0.6F, 1.6F).clientTrackingRange(8).build("goblin"));

    public static final DeferredHolder<EntityType<?>, EntityType<GoblinScoutEntity>> GOBLIN_SCOUT =
            ENTITIES.register("goblin_scout", () ->
                    EntityType.Builder.of(GoblinScoutEntity::new, MobCategory.MONSTER)
                            .sized(0.5F, 1.1F).clientTrackingRange(8).build("goblin_scout"));

    public static final DeferredHolder<EntityType<?>, EntityType<GoblinShamanEntity>> GOBLIN_SHAMAN =
            ENTITIES.register("goblin_shaman", () ->
                    EntityType.Builder.of(GoblinShamanEntity::new, MobCategory.MONSTER)
                            .sized(0.6F, 1.9F).clientTrackingRange(8).build("goblin_shaman"));

    public static final DeferredHolder<EntityType<?>, EntityType<GoblinChieftainEntity>> GOBLIN_CHIEFTAIN =
            ENTITIES.register("goblin_chieftain", () ->
                    EntityType.Builder.of(GoblinChieftainEntity::new, MobCategory.MONSTER)
                            .sized(1.2F, 2.6F).clientTrackingRange(10).build("goblin_chieftain"));
}
