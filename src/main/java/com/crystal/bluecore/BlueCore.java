package com.crystal.bluecore;

import com.crystal.bluecore.block.entity.BasicFluidTankBlockEntity;
import com.crystal.bluecore.enchantment.ModEnchantmentEffects;
import com.crystal.bluecore.entity.MantisEntity;
import com.crystal.bluecore.event.AttackEntityEvent;
import com.crystal.bluecore.event.HammerUsageEvent;
import com.crystal.bluecore.registry.*;
import com.crystal.bluecore.world.biome.ModBiomeModifications;
import com.crystal.bluecore.world.biome.surface.ModMaterialRules;
import com.crystal.bluecore.world.region.ColdestForestRegion;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.minecraft.item.Items;
import net.minecraft.potion.Potions;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import terrablender.api.RegionType;
import terrablender.api.Regions;
import terrablender.api.SurfaceRuleManager;
import terrablender.api.TerraBlenderApi;

public class BlueCore implements ModInitializer, TerraBlenderApi {
	// 模组ID
	public static final String MOD_ID = "bluecore";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	/**
	 * @param id 命名空间
	 * @return 模组物品方块的ID
	 */
	public static Identifier of(String id) {
		return Identifier.of(MOD_ID, id);
	}

	@Override
	public void onTerraBlenderInitialized() {
		// 注册区域（生态群系）
		Regions.register(new ColdestForestRegion(Identifier.of(MOD_ID, "coldest_forest"), RegionType.OVERWORLD, 0));
		// 注册生成规则
		SurfaceRuleManager.addSurfaceRules(SurfaceRuleManager.RuleCategory.OVERWORLD, MOD_ID, ModMaterialRules.coldestForestSurface());
	}

	@Override
	public void onInitialize() {
		// 事件的初始化
		eventInitialize();
		// 注册自定义酿造台配方
		brewingRecipes();
		// 物品与方块的初始化
		ModItems.registerModItemsInfo();
		ModBlocks.registerBlockInfo();
		// 方块实体类型的初始化
		ModBlockEntityType.registerBlockEntityInfo();

		// 物品组或创造标签页的初始化
		ModItemGroups.registerItemGroupsInfo();
		// 音效和唱片初始化
		ModSounds.registerSoundsInfo();
		// 屏幕处理器初始化
		ModScreenHandlerTypes.registerModScreenHandlerTypesInfo();
		// 数据组件初始化
		ModDataComponentTypes.registerDataComponentTypesInfo();
		// 粒子初始化
		ModParticleTypes.registerParticleInfo();
		// 生态群系初始化
		ModBiomes.registerBiomeInfo();
		ModBiomeModifications.registerBiomeInfo();
		// 传送门登记处
		ModCustomPortal.registerPortal();
		// 效果初始化
		ModEffects.registerEffectsInfo();
		// 药水初始化
		ModPotions.registerPotionInfo();
		// 附魔初始化
		ModEnchantmentEffects.registerEntityEffectInfo();
		// 液体初始化
		ModFluids.registerFluidInfo();
		// 实体初始化
		ModEntity.registerModEntitiesInfo();

		// 注册物品燃料（物品，燃烧时间）
		FuelRegistry.INSTANCE.add(ModItems.STARLIGHT_ASHES, 600);
		// 注册方块实体的存储系统
		ItemStorage.SIDED.registerForBlockEntity(BasicFluidTankBlockEntity::getInventoryProvider, ModBlockEntityType.BASIC_FLUID_TANK_BLOCK_ENTITY);
		// 注册方块实体的流体系统
		FluidStorage.SIDED.registerForBlockEntity(BasicFluidTankBlockEntity::getFluidTankProvider, ModBlockEntityType.BASIC_FLUID_TANK_BLOCK_ENTITY);
		// Fabric 默认属性注册
		FabricDefaultAttributeRegistry.register(ModEntity.MANTIS, MantisEntity.createAttributes());
	}

	private void eventInitialize() {
		// 注册事件
		PlayerBlockBreakEvents.BEFORE.register(new HammerUsageEvent());
		AttackEntityCallback.EVENT.register(new AttackEntityEvent());

		/* 甜蜜浆果出现的概率 */
		CompostingChanceRegistry.INSTANCE.add(ModItems.HONEY_BERRIES, 0.15f);
	}

	private void brewingRecipes() {
		FabricBrewingRecipeRegistryBuilder.BUILD.register(builder -> {
			// 输入物品：粗制的药水和黏液球，输出物品：粘稠药水
			builder.registerPotionRecipe(Potions.AWKWARD, Items.SLIME_BALL, ModPotions.SLIME_POTION);
		});
	}
}