package com.crystal.bluecore.entity;

import com.crystal.bluecore.registry.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * @author Crystal
 * @version 1.0
 * @since 1.0
 */
public class NetheriteSpearEntity extends SpearEntity {

    public NetheriteSpearEntity(EntityType<? extends SpearEntity> entityType, World world) {
        super(entityType, world);
    }

    public NetheriteSpearEntity(EntityType<? extends SpearEntity> entityType, World world, LivingEntity owner, ItemStack stack) {
        super(entityType, world, owner, stack);
    }


    @Override
    protected ItemStack getDefaultItemStack() {
        return new ItemStack(ModItems.NETHERITE_SPEAR);
    }
}
