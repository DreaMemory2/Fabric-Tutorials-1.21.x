package com.crystal.bluecore.event;

import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class AttackEntityEvent implements AttackEntityCallback {
    @Override
    public ActionResult interact(PlayerEntity player, World world, Hand hand, Entity entity, @Nullable EntityHitResult hitResult) {
        // 对绵羊攻击时事件
        if (entity instanceof SheepEntity sheep && !world.isClient()) {
            // 绵羊被攻击，且玩家主手有剪刀
            if (player.getMainHandStack().getItem() == Items.SHEARS) {
                player.sendMessage(Text.literal("The Player just hit a sheep with an END ROD! YOU SICK FRICK!"));
                player.sendMessage(Text.literal("请你右键使用剪刀！"));
                // 剪刀耐久减一
                player.getMainHandStack().decrement(1);
                // 并且绵羊有“中毒”效果
                sheep.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 600, 6));
            }
            // PASS会执行进一步处理，而SUCCESS会取消进一步处理。
            return ActionResult.PASS;
        }

        return ActionResult.PASS;
    }
}
