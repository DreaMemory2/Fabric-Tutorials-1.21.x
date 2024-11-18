package com.crystal.bluecore.item;

import com.crystal.bluecore.util.ModTags;
import net.minecraft.item.ToolMaterial;

/**
 * <p>1.21.3：自定义工具</p>
 * @author <a href="https://www.youtube.com/@ModdingByKaupenjoe">Modding By Kaupenjoe</a>
 * @see <a href="https://youtu.be/W_8RNYYv5IE?si=THLVmcJLVLZxNRiv">自定义工具</a><br>
 * <a href="https://github.com/Tutorials-By-Kaupenjoe/Fabric-Tutorial-1.21.X/blob/29-update-1-21-3/src/main/java/net/kaupenjoe/tutorialmod/item/ModToolMaterials.java">ModToolMaterials</a>
 */
public class ModToolMaterials {
    /**
     * 游戏版本1.21.3：自定义工具
     * @since 1.21.3-1.0.2
     */
    public static final ToolMaterial PINK_GEMSTONE = new ToolMaterial(
            ModTags.Blocks.NEEDS_PINK_GEMSTONE, // 需要使用粉红色工具挖掘方块标签
            1500, // durability：工具使用耐久
            7.0f, // speed：工具使用速度
            2.0f, // attackDamageBonus：工具伤害加成
            22, //enchantmentValue：附魔能力值
            ModTags.Items.PINK_GEMSTONE_REPAIR // 需要修理粉红色工具的物品标签
    );
}
