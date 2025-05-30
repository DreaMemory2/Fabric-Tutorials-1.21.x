package com.crystal.simpletools.recipe;

import com.crystal.simpletools.SimpleToolsMod;
import com.crystal.simpletools.recipe.entropy.EntropyRecipe;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

/**
* ClassName: Mod Recipes<br>
* Description: <br>
* Datetime: 2025/5/28 12:52<br>
* @author Crystal
* @version 1.0
* @since 1.0
*/
public class ModRecipes {
    public static void init() {
        Registry.register(Registries.RECIPE_SERIALIZER, EntropyRecipe.Serializer.ID, EntropyRecipe.Serializer.INSTANCE);
        Registry.register(Registries.RECIPE_TYPE, SimpleToolsMod.of(EntropyRecipe.Type.ID), EntropyRecipe.Type.INSTANCE);
    }
}
