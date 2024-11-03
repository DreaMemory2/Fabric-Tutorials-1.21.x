package com.crystal.bluecore.registry;

import com.crystal.bluecore.BlueCore;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.DimensionTypes;

import java.util.OptionalLong;

public class ModDimensions {
    // 雪花晶体维度
    public static final RegistryKey<DimensionOptions> SNOWFLAKE_CRYSTAL = RegistryKey.of(RegistryKeys.DIMENSION,
            Identifier.of(BlueCore.MOD_ID, "snowflake_crystal"));
    // 雪花晶体世界
    public static final RegistryKey<World> SNOWFLAKE_CRYSTAL_WORLD = RegistryKey.of(RegistryKeys.WORLD,
            Identifier.of(BlueCore.MOD_ID, "snowflake_crystal"));
    // 雪花晶体维度类型
    public static final RegistryKey<DimensionType> SNOWFLAKE_CRYSTAL_TYPE = RegistryKey.of(RegistryKeys.DIMENSION_TYPE,
            Identifier.of(BlueCore.MOD_ID, "snowflake_crystal_type"));

    public static void bootstrap(Registerable<DimensionType> context) {
        context.register(SNOWFLAKE_CRYSTAL_TYPE, snowflakeCrystal());
    }

    /**
     * <p>维度类型设计</p>
     * <ol>
     *     <li>fixedTime：如果设置本字段，该维度的昼夜时间将会固定在这个指定值上。</li>
     *     <li>hasSkyLight：该维度是否有天空光照</li>
     *     <li>hasCeiling：该维度是否有天花板</li>
     *     <li>ultrawarm：为true时，水桶无法放出水，湿海绵会变干，熔岩会流动得更快、扩散得更远（地狱维度效果）</li>
     *     <li>natural：为false时，指南针会随机转动，且无法用床睡觉或重置重生点（地狱维度效果）<br>
     *     为true时，下界传送门方块会生成僵尸猪灵（主世界效果）</li>
     *     <li>coordinateScale：传送到该维度时的坐标缩放值</li>
     *     <li>bedWorks：玩家用床时，是否会爆炸</li>
     *     <li>respawnAnchorWorks：玩家使用重生锚时，是否会爆炸</li>
     *     <li>minY：该维度中可以存在方块的最低高度，数值是16的整数倍</li>
     *     <li>height：该维度中可以存在方块的总高度。数值是16的整数倍</li>
     *     <li>logicalHeight：玩家使用紫颂果或下界传送门可以到达的总高度</li>
     *     <li>infiniburn：该维度中火可以在哪些方块上永久燃烧</li>
     *     <li>effects：可选，默认为主世界，是否有该维度的天空效果</li>
     *     <li>ambientLight：该维度的环境光照，该值越高，渲染得越亮。</li>
     *     <li>monsterSettings：生成怪物设定</li>
     * </ol>
     * <p>Monster Settings（怪物设定）</p>
     * <ol>
     *     <li>piglinSafe：猪灵、猪灵蛮兵和疣猪兽是否会僵尸化</li>
     *     <li>hasRaids：带有不祥之兆的玩家是否可以触发袭击</li>
     *     <li>monsterSpawnLightTest: 取值为0到15的闭区间。怪物生成位置的最大光照。</li>
     *     <li>monsterSpawnBlockLightLimit：取值为0到15的闭区间。怪物生成位置的最大方块光照。</li>
     * </ol>
     * <p>详细参考：请参见document/biome/自定义维度.md</p>
     * @return 自定义维度类型
     * @since 1.0
     * @author <a href="https://space.bilibili.com/489671468">北山_Besson</a>
     * @see <a href="https://zh.minecraft.wiki/w/维度类型">维度类型</a><br>
     */
    private static DimensionType snowflakeCrystal() {
        DimensionType.MonsterSettings type = new DimensionType.MonsterSettings(false, false, UniformIntProvider.create(0, 0), 0);
        return new DimensionType(OptionalLong.empty(),
                true, false, false, true, 1.0, true, false, 0, 256, 256,
                BlockTags.INFINIBURN_OVERWORLD, DimensionTypes.OVERWORLD_ID, 1.0f, type);
    }
}
