package com.crystal.bluecore.item.custom;

import com.crystal.bluecore.api.EnchantmentHandler;
import com.crystal.bluecore.entity.SpearProjectileEntity;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.enchantment.effect.EnchantmentEffectTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.RegistryKey;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.List;

/**
 * @see <a href="https://github.com/Draylar/gate-of-babylon/blob/dc6007da7cca924ef635167d34deb8344e8bbc7c/src/main/java/draylar/gateofbabylon/item/SpearItem.java#L4">SpearItem</a>
 */
public class SpearItem extends ToolItem implements EnchantmentHandler {
    private final float effectiveDamage;
    private final float effectiveSpeed;

    public SpearItem(ToolMaterial material, float effectiveDamage, float effectiveSpeed, Settings settings) {
        super(material, settings);

        this.effectiveDamage = effectiveDamage - 1;
        this.effectiveSpeed = -4 + effectiveSpeed;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.SPEAR;
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 72000;
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.damage(1, attacker, EquipmentSlot.MAINHAND);
        return true;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        if (itemStack.getDamage() >= itemStack.getMaxDamage() - 1) {
            return TypedActionResult.fail(itemStack);
        } else {
            user.setCurrentHand(hand);
            return TypedActionResult.consume(itemStack);
        }
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity player) {
            int currentUseTime = this.getMaxUseTime(stack, user) - remainingUseTicks;

            if (currentUseTime >= 10) {
                if (!world.isClient) {
                    stack.damage(1, player, EquipmentSlot.MAINHAND);

                    // Create initial Spear entity
                    SpearProjectileEntity spearEntity = new SpearProjectileEntity(world, player, stack);
                    spearEntity.setVelocity(player, player.getPitch(), player.getYaw(), 0.0F, 2.5F, 1.0F);
                    if (player.getAbilities().creativeMode) {
                        spearEntity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
                    }
                    world.spawnEntity(spearEntity);

                    // Play SFX
                    world.playSoundFromEntity(null, spearEntity, SoundEvents.ITEM_TRIDENT_THROW.value(), SoundCategory.PLAYERS, 1.0F, 1.0F);

                    // Remove Spear from inventory after throw
                    if (!player.getAbilities().creativeMode) {
                        player.getInventory().removeOne(stack);
                    }
                }

                player.incrementStat(Stats.USED.getOrCreateStat(this));
            }
        }
    }

    @Override
    public List<EnchantmentEffectTarget> getEnchantmentTypes() {
        return Arrays.asList(EnchantmentEffectTarget.ATTACKER, EnchantmentEffectTarget.VICTIM);
    }

    @Override
    public boolean isInvalid(RegistryKey<Enchantment> enchantment) {
        return Enchantments.SWEEPING_EDGE == enchantment;
    }

    @Deprecated
    @Override
    public AttributeModifiersComponent getAttributeModifiers() {
        AttributeModifiersComponent.Builder builder = AttributeModifiersComponent.builder();

        builder.add(EntityAttributes.GENERIC_ATTACK_DAMAGE,
                new EntityAttributeModifier(BASE_ATTACK_DAMAGE_MODIFIER_ID, 8.0, EntityAttributeModifier.Operation.ADD_VALUE),
                        AttributeModifierSlot.MAINHAND)
                .add(EntityAttributes.GENERIC_ATTACK_SPEED,
                        new EntityAttributeModifier(BASE_ATTACK_SPEED_MODIFIER_ID, -2.9F, EntityAttributeModifier.Operation.ADD_VALUE),
                        AttributeModifierSlot.MAINHAND)
                .build();

        return builder.build();
    }
}
