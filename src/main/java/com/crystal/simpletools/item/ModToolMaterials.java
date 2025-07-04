package com.crystal.simpletools.item;

import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;

/**
* ClassName: ModToolTypes<br>
* Description: <br>
* Datetime: 2025/6/4 12:52<br>
* @author Crystal
* @version 1.0
* @since 1.0
*/
public interface ModToolMaterials {
    ToolMaterial FLUIX = new ToolMaterial(BlockTags.INCORRECT_FOR_IRON_TOOL, 750, 7.2F, 3.2F, 15, ItemTags.IRON_TOOL_MATERIALS);
}
