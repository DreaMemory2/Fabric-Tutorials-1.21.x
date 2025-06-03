package com.crystal.simpletools.datagen;

import com.crystal.simpletools.block.ModBlocks;
import com.crystal.simpletools.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

/**
* ClassName: ChineseLanguageProvider<br>
* Description: <br>
* Datetime: 2025/6/3 18:22<br>
* @author Crystal
* @version 1.0
* @since 1.0
*/
public class ChineseLanguageProvider extends FabricLanguageProvider {

    public ChineseLanguageProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, "zh_cn", registryLookup);
    }

    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup registryLookup, TranslationBuilder builder) {
        builder.add(ModBlocks.AZALEA_LEAVES, "杜鹃树叶");
        builder.add(ModBlocks.AZALEA_LOG, "杜鹃原木");
        builder.add(ModBlocks.BLAST_FURNACE, "高炉砖");

        builder.add(ModItems.ENTROPY_MANIPULATOR, "熵变机械臂");
        builder.add(ModItems.DIAMOND_APPLE_CORE, "钻石苹果核");

        /* 高级设置 */
        builder.add("itemgroup.simpletools.simple_groups", "简单工具");
    }
}
