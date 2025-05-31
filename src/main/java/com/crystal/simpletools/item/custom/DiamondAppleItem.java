package com.crystal.simpletools.item.custom;

import com.crystal.simpletools.item.ModItems;
import com.crystal.simpletools.util.ColorFont;
import com.crystal.simpletools.util.ColorFont2;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ConsumableComponent;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.consume.ApplyEffectsConsumeEffect;
import net.minecraft.item.consume.UseAction;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Rarity;

import java.util.List;
import java.util.function.Consumer;

/**
* ClassName: DiamondAppleItem<br>
* Description: <br>
* Datetime: 2025/5/31 11:23<br>
* @author Crystal
* @version 1.0
* @since 1.0
*/
public class DiamondAppleItem extends Item {

    public DiamondAppleItem(Settings settings) {
        super(build(settings));
    }

    public static Settings build(Settings settings) {
        return settings
                .rarity(Rarity.RARE)
                .useCooldown(1)
                .maxCount(64)
                .fireproof()
                .food(fruit(), effect())
                // 使用后剩余物
                .useRemainder(ModItems.DIAMOND_APPLE_CORE)
                // 附魔效果
                .component(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true);
    }

    /**
     * nutrition 饥饿值（饱食度）<br>
     * saturation 饱和度<br>
     * canAlwaysEat 是否总是可以使用<br>
     * @return 返回食物组件
     */
    public static FoodComponent fruit() {
        return new FoodComponent(4, 2, true);
    }

    /**
     * @param stack 物品
     * @return 使用的行为：吃
     */
    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.EAT;
    }

    public static ConsumableComponent effect() {
        /* 抗性提升 + 伤害吸收 */
        StatusEffectInstance regeneration = new StatusEffectInstance(StatusEffects.REGENERATION, 100, 1);
        StatusEffectInstance absorption = new StatusEffectInstance(StatusEffects.ABSORPTION, 2400, 0);
        // 状态效果List集合
        List<StatusEffectInstance> list = List.of(regeneration, absorption);
        return ConsumableComponent.builder()
                .consumeSeconds(1.6F)
                .consumeEffect(new ApplyEffectsConsumeEffect(list))
                .useAction(UseAction.EAT)
                .sound(SoundEvents.ENTITY_GENERIC_EAT)
                .consumeParticles(true)
                .build();
    }

    /**
     *
     * @param stack 物品
     * @return 更改物品的名称
     */
    @Override
    public Text getName(ItemStack stack) {
        return Text.literal(ColorFont.makeColour("钻石苹果"));
    }

    /**
     *
     * @param stack 物品
     * @param context 文本提示
     * @param displayComponent 文本展示组件
     * @param textConsumer 文本
     * @param type 文本类型
     */
    @Deprecated
    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
        textConsumer.accept(Text.literal(ColorFont2.makeColour("这是一个钻石苹果")));
        textConsumer.accept(Text.literal(ColorFont2.makeColour("这是一个钻石苹果")));
    }
}
