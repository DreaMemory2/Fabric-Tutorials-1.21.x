package com.crystal.bluecore.world.gen;

import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler.NoiseParameters;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.densityfunction.DensityFunctionTypes;
import net.minecraft.world.gen.densityfunction.DensityFunctions;
import net.minecraft.world.gen.noise.NoiseParametersKeys;
import net.minecraft.world.gen.noise.NoiseRouter;

public class VanillaDensityFunctions {
    /**
     * createSurfaceNoiseRouter(RegistryEntryLookup, RegistryEntryLookup, boolean, boolean)中的方法
     * @see net.minecraft.world.gen.densityfunction.DensityFunctions DensityFunctions
     */
    public static NoiseRouter createSurfaceNoiseRouter(RegistryEntryLookup<DensityFunction> density, RegistryEntryLookup<NoiseParameters> noise) {
        // 隔断：影响是否使用方块分隔含水层和洞穴其他区域。函数值越大越有可能分隔。
        DensityFunction barrierNoise = DensityFunctionTypes.noise(noise.getOrThrow(NoiseParametersKeys.AQUIFER_BARRIER), 0.5);
        // 流体高度泛滥程度：影响含水层生成液体的概率。函数值越大越有可能生成。该噪声值大于1.0的被视为1.0，小于-1.0的被视为-1.0。
        DensityFunction fluidLevelFloodednessNoise = DensityFunctionTypes.noise(noise.getOrThrow(NoiseParametersKeys.AQUIFER_FLUID_LEVEL_FLOODEDNESS), 0.67);
        // 流体高度扩散：影响某处含水层液体表面的高度。函数值越小液体表面越可能较低。
        DensityFunction fluidLevelSpreadNoise = DensityFunctionTypes.noise(noise.getOrThrow(NoiseParametersKeys.AQUIFER_FLUID_LEVEL_SPREAD), 0.7142857142857143);
        // 岩浆：影响某处含水层是否使用熔岩代替水。阈值为0.3。
        DensityFunction lavaNoise = DensityFunctionTypes.noise(noise.getOrThrow(NoiseParametersKeys.AQUIFER_LAVA), 1.0, 1.0);
        DensityFunction shiftX = entryHolder(density, of("shift_x"));
        DensityFunction shiftZ = entryHolder(density, of("shift_z"));
        // 温度：生物群系的温度噪声。这里和下方的六个值是用于生物群系放置的生物群系参数。某点处生物群系的生物群系参数向量与该点处的对应参数向量的2-范数距离是最小的。
        DensityFunction temperature = DensityFunctionTypes.shiftedNoise(shiftX, shiftZ, 0.25, noise.getOrThrow(NoiseParametersKeys.TEMPERATURE));
        // 植被：即humidity，生物群系的湿度噪声
        DensityFunction vegetation = DensityFunctionTypes.shiftedNoise(shiftX, shiftZ, 0.25, noise.getOrThrow(NoiseParametersKeys.VEGETATION));
        // 大陆：生物群系的大陆性噪声
        DensityFunction continents = entryHolder(density, of("overworld/continents"));
        // 侵蚀：生物群系的侵蚀噪声
        DensityFunction erosion = entryHolder(density, of("overworld/erosion"));
        // 深度：生物群系的深度噪声
        DensityFunction depth = entryHolder(density, of("overworld/depth"));
        // 奇异：即weirdness，生物群系的奇异噪声
        DensityFunction ridges = entryHolder(density, of("overworld/ridges"));
        // 主世界最终密度函数
        DensityFunction finalDensity = finalDensity(density, noise);
        // 不计锯齿度的初始深度：与含水层和地表规则的生成有关。在一XZ坐标下，从世界顶部开始以size_vertical*4个方块的精度从上到下查找，初次遇到大于25/64的值的高度作为世界生成的初始地表高度。该高度通常应该低于实际的地表高度（由final_density决定）
        DensityFunction initialDensityWithoutJaggedness = initialDensityWithoutJaggedness(density);
        // 矿脉开关：影响矿脉类型和垂直范围。这值大于0.0将是铜矿脉，小于等于0.0将是铁矿脉。
        DensityFunction veinToggle = veinToggle(density, noise);
        // 矿脉奇异：控制哪些方块会参与组成矿脉。如果小于0.0，则方块是矿脉的一部分（是否为矿石块由下方值决定）。
        DensityFunction veinRidged = veinRidged(density, noise);
        // 矿脉间隙：影响矿脉中的哪些方块将是矿石块。如果大于-0.3，并且随机数小于从0.4到0.6映射到0.1到0.3的vein_toggle的绝对值，则会放置矿石块，且有2%的概率变为粗金属块。否则，将放置矿脉类型对应的石块。
        DensityFunction veinGap = DensityFunctionTypes.noise(noise.getOrThrow(NoiseParametersKeys.ORE_GAP), 1.0, 1.0);
        return new NoiseRouter(
                barrierNoise, fluidLevelFloodednessNoise, fluidLevelSpreadNoise, lavaNoise,
                temperature, vegetation, continents, erosion, depth, ridges,
                initialDensityWithoutJaggedness,
                finalDensity,
                veinToggle, veinRidged, veinGap);
    }

    private static DensityFunction veinToggle(RegistryEntryLookup<DensityFunction> density, RegistryEntryLookup<NoiseParameters> noise) {
        DensityFunction whenInRange = DensityFunctionTypes.noise(noise.getOrThrow(NoiseParametersKeys.ORE_VEININESS), 1.5, 1.5);
        return DensityFunctionTypes.interpolated(
                DensityFunctionTypes.rangeChoice(entryHolder(density, of("y")), -60.0, 51.0, whenInRange,
                        DensityFunctionTypes.constant(0.0)));
    }

    private static DensityFunction veinRidged(RegistryEntryLookup<DensityFunction> density, RegistryEntryLookup<NoiseParameters> noise) {
        DensityFunction oreVeinA = DensityFunctionTypes.noise(noise.getOrThrow(NoiseParametersKeys.ORE_VEIN_A), 4.0, 4.0);
        DensityFunction argument1 = DensityFunctionTypes.interpolated(DensityFunctionTypes.rangeChoice(entryHolder(density, of("y")),
                -60.0, 51.0, oreVeinA,
                DensityFunctionTypes.constant(0.0))).abs();
        DensityFunction oreVeinB = DensityFunctionTypes.noise(noise.getOrThrow(NoiseParametersKeys.ORE_VEIN_B), 4.0, 4.0);
        DensityFunction argument2 = DensityFunctionTypes.interpolated(DensityFunctionTypes.rangeChoice(entryHolder(density, of("y")),
                -60.0, 51.0, oreVeinB,
                DensityFunctionTypes.constant(0.0))).abs();
        return DensityFunctionTypes.add(DensityFunctionTypes.constant(-0.07999999821186066), DensityFunctionTypes.max(argument1, argument2));
    }

    public static DensityFunction finalDensity(RegistryEntryLookup<DensityFunction> density, RegistryEntryLookup<NoiseParameters> noise) {
        /* 地表部分 */
        DensityFunction surface = DensityFunctionTypes.min(
                entryHolder(density, of("overworld/sloped_cheese")),
                DensityFunctionTypes.mul(DensityFunctionTypes.constant(5.0),
                        // 噪声洞穴入口，即连通地表与地下洞穴的一些洞穴
                        entryHolder(density, of("overworld/caves/entrances")))
        );
        /* 地下部分 */
        DensityFunction underground = createCaveFunctions(density, noise);
        // 以sloped_cheese的1.5625为分界线，分为地表和地下
        DensityFunction boundary = DensityFunctionTypes.rangeChoice(
                entryHolder(density, of("overworld/sloped_cheese")), -1000000.0, 1.5625, surface, underground);
        // 与旧版区块衔接
        DensityFunction oldChunk = DensityFunctionTypes.interpolated(DensityFunctionTypes.blendDensity(applyCavesSlides(boundary)));
        // 隔水层
        // 使其值更接近0，影响含水层各水域间分隔的生成，final_density的负值越小，分隔越不易生成
        DensityFunction aquiclude = DensityFunctionTypes.mul(DensityFunctionTypes.constant(0.64), oldChunk).squeeze();
        // //面条洞穴
        return DensityFunctionTypes.min(aquiclude, entryHolder(density, of("overworld/caves/noodle")));
    }

    public static DensityFunction initialDensityWithoutJaggedness(RegistryEntryLookup<DensityFunction> density) {
        DensityFunction quarterNegative = DensityFunctionTypes.mul(entryHolder(density, of("overworld/depth")), DensityFunctionTypes.cache2d(entryHolder(density, DensityFunctions.FACTOR_OVERWORLD))).quarterNegative();
        DensityFunction clamp = DensityFunctionTypes.add(DensityFunctionTypes.mul(DensityFunctionTypes.constant(4.0), quarterNegative), DensityFunctionTypes.constant(-0.703125)).clamp(-64.0, 64.0);
        DensityFunction mul = DensityFunctionTypes.mul(DensityFunctionTypes.yClampedGradient(240, 256, 1.0, 0), DensityFunctionTypes.add(DensityFunctionTypes.constant(0.078125), clamp));
        DensityFunction add = DensityFunctionTypes.add(DensityFunctionTypes.constant(-0.1171875), DensityFunctionTypes.add(DensityFunctionTypes.constant(-0.078125), mul));
        DensityFunction argument = DensityFunctionTypes.mul(DensityFunctionTypes.yClampedGradient(-64, -40, 0.0, 1.0), add);
        return DensityFunctionTypes.add(DensityFunctionTypes.constant(0.1171875), argument);
    }

    private static DensityFunction applySurfaceSlides(DensityFunction boundary) {
        // 从Y=240到Y=256，密度函数值逐渐变为固定值-0.078125，以避免生成过高地形
        DensityFunction argument1 = DensityFunctionTypes.yClampedGradient(240, 256, 1, 0);
        DensityFunction argument2 = DensityFunctionTypes.add(DensityFunctionTypes.constant(0.078125), boundary);
        return DensityFunctionTypes.add(DensityFunctionTypes.constant(-0.078125), DensityFunctionTypes.mul(argument1, argument2));
    }

    private static DensityFunction applyCavesSlides(DensityFunction boundary) {
        // 从Y=-40到Y=-64，密度函数值逐渐变为固定值0.1171875，以避免洞穴过深暴露甚至穿透基岩层
        DensityFunction argument1 = DensityFunctionTypes.yClampedGradient(-64, -40, 0, 1);
        DensityFunction argument2 = DensityFunctionTypes.add(DensityFunctionTypes.constant(-0.1171875), applySurfaceSlides(boundary));
        return DensityFunctionTypes.add(DensityFunctionTypes.constant(0.1171875), DensityFunctionTypes.mul(argument1, argument2));
    }

    private static DensityFunction createCaveFunctions(RegistryEntryLookup<DensityFunction> density, RegistryEntryLookup<NoiseParameters> noise) {
        // 使洞穴更小且形状更不规则。该部分永远为正
        DensityFunction caveCheeseLayer = DensityFunctionTypes.mul(DensityFunctionTypes.constant(4.0),
                DensityFunctionTypes.noise(noise.getOrThrow(NoiseParametersKeys.CAVE_LAYER), 1.0, 8.0).square());
        // 靠近地表时增加密度，减少靠近地表的芝士洞穴生成。该部分在sloped_cheese的值大于2.34375时为0，在2.34375和1.5626之间逐渐增加，在1.5625时为0.5芝士洞穴的主体。只有该部分小于零的地方才能生成芝士洞穴
        DensityFunction caveCheeseMain = DensityFunctionTypes.add(DensityFunctionTypes.constant(0.27),
                DensityFunctionTypes.noise(noise.getOrThrow(NoiseParametersKeys.CAVE_CHEESE), 0.6666666666666666)).clamp(-1.0, 1.0);
        DensityFunction caveCheeseReduction = DensityFunctionTypes.add(DensityFunctionTypes.constant(1.5),
                DensityFunctionTypes.mul(DensityFunctionTypes.constant(-0.64),
                        // 决定了地形表面的高度和形状
                        entryHolder(density, of("overworld/sloped_cheese")))).clamp(0.0, 0.5);
        DensityFunction caveCheesePart = DensityFunctionTypes.add(caveCheeseMain, caveCheeseReduction);
        // 芝士洞穴和洞穴入口
        DensityFunction caveCheese = DensityFunctionTypes.min(DensityFunctionTypes.add(caveCheeseLayer, caveCheesePart),
                entryHolder(density, of("overworld/caves/entrances")));
        // 意面洞穴
        DensityFunction cavePasta = DensityFunctionTypes.add(
                entryHolder(density, of("overworld/caves/spaghetti_2d")),
                entryHolder(density, of("overworld/caves/spaghetti_roughness_function")));
        /* 洞穴 */
        // 意面、芝士洞穴与洞穴入口，取两次最小值相对于取三者的最小值。这里有必要再次调用洞穴入口，否则洞穴入口只在地表生成而被切断
        DensityFunction cave = DensityFunctionTypes.min(caveCheese, cavePasta);
        // 噪声洞穴里的噪声柱
        DensityFunction pillars = DensityFunctionTypes.rangeChoice(
                entryHolder(density, of("overworld/caves/pillars")),
                -1000000.0, 0.03,
                DensityFunctionTypes.constant(-1000000.0),
                entryHolder(density, of("overworld/caves/pillars")));
        return DensityFunctionTypes.max(cave, pillars);
    }

    private static DensityFunction entryHolder(RegistryEntryLookup<DensityFunction> density, RegistryKey<DensityFunction> key) {
        return new DensityFunctionTypes.RegistryEntryHolder(density.getOrThrow(key));
    }

    private static RegistryKey<DensityFunction> of(String id) {
        return RegistryKey.of(RegistryKeys.DENSITY_FUNCTION, Identifier.ofVanilla(id));
    }
}
