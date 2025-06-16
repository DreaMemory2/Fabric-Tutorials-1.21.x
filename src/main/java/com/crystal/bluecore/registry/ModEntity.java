package com.crystal.bluecore.registry;

import com.crystal.bluecore.BlueCore;
import com.crystal.bluecore.entity.MantisEntity;
import com.crystal.bluecore.entity.NetheriteSpearEntity;
import com.crystal.bluecore.entity.SpearEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

/**
 * @author Crystal
 * @version 1.0
 * @since 1.0
 */
public class ModEntity {
    public static final EntityType<MantisEntity> MANTIS = Registry.register(Registries.ENTITY_TYPE, BlueCore.of("mantis"),
            EntityType.Builder.create(MantisEntity::new, SpawnGroup.CREATURE)
                    // 设置碰撞箱，碰撞体积
                    .dimensions(1f, 2.5f).build());

    public static final EntityType<SpearEntity> NETHERITE_SPEAR = Registry.register(Registries.ENTITY_TYPE, BlueCore.of("spear"),
            EntityType.Builder.<SpearEntity>create(NetheriteSpearEntity::new, SpawnGroup.MISC)
                    .dimensions(0.5f, 0.5f).build());

    public static void registerModEntitiesInfo() {
        BlueCore.LOGGER.info("Registering Mod Entities for " + BlueCore.MOD_ID);
    }
}
