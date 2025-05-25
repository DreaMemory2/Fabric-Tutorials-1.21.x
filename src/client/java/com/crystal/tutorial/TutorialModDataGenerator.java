package com.crystal.tutorial;

import com.crystal.tutorial.datagen.TutorialModelGenerator;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class TutorialModDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator generator) {
		// 获取数据构造器的打包器，进行类名封装处理
		FabricDataGenerator.Pack pack = generator.createPack();
		// 添加名单
		pack.addProvider(TutorialModelGenerator::new);
	}
}
