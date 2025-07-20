package com.crystal.simpletools.recipe.entropy;

import com.crystal.simpletools.api.FabricStreamCodecs;
import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.recipe.IngredientPlacement;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.recipe.input.RecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.state.State;
import net.minecraft.state.property.Property;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
* ClassName: Entropy Recipe<br>
* Description: 熵变机械臂的配方<br>
* Datetime: 2025/5/27 19:21<br>
* @author Crystal
* @version 1.0
* @since 1.0
*/
public class EntropyRecipe implements Recipe<RecipeInput> {
    private final Input input;
    private final Output output;
    private final EntropyMode mode;

    /**
     * @param mode 模式
     * @param input 输入
     * @param output 输出
     */
    public EntropyRecipe(EntropyMode mode, Input input, Output output) {
        this.mode = mode;
        this.input = input;
        this.output = output;
    }

    @Override
    public boolean matches(RecipeInput input, World world) {
        return false;
    }

    /**
     * @param mode 模式
     * @param blockState 方块状态
     * @param fluidState 液体状态，例如水源、流动通水
     * @return 判断输入方块和液体是否一致
     */
    public boolean matches(EntropyMode mode, BlockState blockState, FluidState fluidState) {
        if (this.getMode() != mode) {
            return false;
        }

        return input.matches(blockState, fluidState);
    }

    @Override
    public ItemStack craft(RecipeInput input, RegistryWrapper.WrapperLookup registries) {
        return ItemStack.EMPTY;
    }

    /**
     * {@return 用于解析对应JSON配方文件的编码器}
     */
    @Override
    public RecipeSerializer<? extends Recipe<RecipeInput>> getSerializer() {
        return Serializer.INSTANCE;
    }

    /**
     * {@return 配方类型，例如："type": "simpletools:entropy"}
     */
    @Override
    public RecipeType<? extends Recipe<RecipeInput>> getType() {
        return Type.INSTANCE;
    }

    @Override
    public IngredientPlacement getIngredientPlacement() {
        return IngredientPlacement.NONE;
    }

    /**
     * {@return 获取默认的配方类别}
     */
    @Override
    public RecipeBookCategory getRecipeBookCategory() {
        return new RecipeBookCategory();
    }

    public BlockState getOutputBlockState(BlockState originalBlockState) {
        return output.block().map(blockOutput -> blockOutput.apply(originalBlockState)).orElse(null);
    }

    public FluidState getOutputFluidState(FluidState originalFluidState) {
        return output.fluid().map(fluidOutput -> fluidOutput.apply(originalFluidState)).orElse(null);
    }

    public EntropyMode getMode() {
        return mode;
    }

    public List<ItemStack> getDrops() {
        return this.output.drops();
    }

    public Input getInput() {
        return input;
    }

    public Output getOutput() {
        return output;
    }

    public static class Serializer implements RecipeSerializer<EntropyRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final String ID = "entropy";

        private Serializer() {

        }

        /**
         * 自定义编码器：需要EntropyMode、Input、Output的编码器
         * <pre>
         * {
         *    "type": "simpletools:entropy",
         *    "mode": "cool"
         *    "input": {
         *
         *    },
         *    "output": {
         *
         *    }
         * }
         * </pre>
         */
        @Override
        public MapCodec<EntropyRecipe> codec() {
            return RecordCodecBuilder.mapCodec(builder -> builder.group(
                    EntropyMode.CODEC.fieldOf("mode").forGetter(EntropyRecipe::getMode),
                    Input.CODEC.fieldOf("input").forGetter(EntropyRecipe::getInput),
                    Output.CODEC.fieldOf("output").forGetter(EntropyRecipe::getOutput))
                    .apply(builder, EntropyRecipe::new));
        }

        @Override
        public PacketCodec<RegistryByteBuf, EntropyRecipe> packetCodec() {
            return PacketCodec.tuple(
                    FabricStreamCodecs.enumCodec(EntropyMode.class),
                    EntropyRecipe::getMode,
                    Input.STREAM_CODEC,
                    EntropyRecipe::getInput,
                    Output.STREAM_CODEC,
                    EntropyRecipe::getOutput,
                    EntropyRecipe::new);
        }
    }

    public static class Type implements RecipeType<EntropyRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "entropy";

        private Type() {

        }
    }

    /**
     * @param block 输入方块（可选）
     * @param fluid 输入液体（可选）
     */
    public record Input(Optional<BlockInput> block, Optional<FluidInput> fluid) {
        /**
         * 自定义编码器：需要BlockInput、FluidInput的编码器
         * <pre>
         * "input": {
         *   "block": {
         *
         *   }
         * }
         * </pre>
         * 或者
         * <pre>
         * "input": {
         *   "fluid": {
         *
         *   }
         * }
         * </pre>
         */
        public static Codec<Input> CODEC = RecordCodecBuilder.create(builder -> builder.group(
                BlockInput.CODEC.optionalFieldOf("block").forGetter(Input::block),
                FluidInput.CODEC.optionalFieldOf("fluid").forGetter(Input::fluid))
                .apply(builder, Input::new)
        );

        public static PacketCodec<RegistryByteBuf, Input> STREAM_CODEC = PacketCodec.tuple(
                BlockInput.STREAM_CODEC.collect(PacketCodecs::optional),
                Input::block,
                FluidInput.STREAM_CODEC.collect(PacketCodecs::optional),
                Input::fluid,
                Input::new
        );

        public boolean matches(BlockState blockState, FluidState fluidState) {

            if (block.isPresent()) {
                BlockState inputBlock = block.get().block();
                return blockState == inputBlock;
            }

            if (fluid.isPresent()) {
                FluidState inputFluid = fluid.get().fluid();
                return fluidState == inputFluid;
            }

            return true;
        }
    }

    /**
     * @param block 输入方块
     * @param properties 方块状态（可选）
     */
    public record BlockInput(BlockState block, Map<String, PropertyValueMatcher> properties) {
        /**
         * 自定义编码器：需要BlockState、PropertyValueMatcher的编码器
         * <pre>
         * "id": {
         *   "Name": "minecraft:grass_block",
         *   "Properties": {
         *     "snowy": "false"
         *   }
         * }
         * </pre>
         */
        public static Codec<BlockInput> CODEC = RecordCodecBuilder.create(builder -> builder.group(
                BlockState.CODEC.fieldOf("id").forGetter(BlockInput::block),
                PropertyValueMatcher.MAP_CODEC.optionalFieldOf("properties", Map.of()).forGetter(BlockInput::properties))
                .apply(builder, BlockInput::new));

        public static PacketCodec<RegistryByteBuf, BlockInput> STREAM_CODEC = PacketCodec.tuple(
                PacketCodecs.registryCodec(BlockState.CODEC),
                BlockInput::block,
                PacketCodecs.map(Maps::newHashMapWithExpectedSize, PacketCodecs.STRING, PropertyValueMatcher.STREAM_CODEC),
                BlockInput::properties,
                BlockInput::new
        );
    }

    /**
     * @param fluid 输入的液体
     * @param properties 液体状态（可选）
     */
    public record FluidInput(FluidState fluid, Map<String, PropertyValueMatcher> properties) {
        /**
         * 自定义编码器：需要FluidState、PropertyValueMatcher的编码器
         * <pre>
         * "id": {
         *   "Name": "minecraft:water",
         *   "Properties": {
         *     "falling": "true"
         *   }
         * }
         * </pre>
         */
        public static Codec<FluidInput> CODEC = RecordCodecBuilder.create(builder -> builder.group(
                FluidState.CODEC.fieldOf("id").forGetter(FluidInput::fluid),
                PropertyValueMatcher.MAP_CODEC.optionalFieldOf("properties", Map.of()).forGetter(FluidInput::properties))
                .apply(builder, FluidInput::new));

        public static PacketCodec<RegistryByteBuf, FluidInput> STREAM_CODEC = PacketCodec.tuple(
                PacketCodecs.registryCodec(FluidState.CODEC),
                FluidInput::fluid,
                PacketCodecs.map(Maps::newHashMapWithExpectedSize, PacketCodecs.STRING, PropertyValueMatcher.STREAM_CODEC),
                FluidInput::properties,
                FluidInput::new
        );
    }

    /**
     * @param block 输出方块（可选）
     * @param fluid 输出液体（可选）
     * @param drops 输出物品（可选）
     */
    public record Output(Optional<BlockOutput> block, Optional<FluidOutput> fluid, List<ItemStack> drops) {
        /**
         * 自定义编码器：需要BlockOutput、FluidOutput、ItemStack的编码器
         * <pre>
         * "output": {
         *   "block": {
         *
         *   }
         * }
         * </pre>
         * 或者
         * <pre>
         * "output": {
         *   "fluid": {
         *   }
         * }
         * </pre>
         * 或者
         * <pre>
         * "output": {
         *   "drops": {
         *   }
         * }
         * </pre>
         */
        public static Codec<Output> CODEC = RecordCodecBuilder.create(builder -> builder.group(
                        BlockOutput.CODEC.optionalFieldOf("block").forGetter(Output::block),
                        FluidOutput.CODEC.optionalFieldOf("fluid").forGetter(Output::fluid),
                        ItemStack.CODEC.listOf().optionalFieldOf("drops", List.of()).forGetter(Output::drops))
                .apply(builder, Output::new)
        );

        public static PacketCodec<RegistryByteBuf, Output> STREAM_CODEC = PacketCodec.tuple(
                BlockOutput.STREAM_CODEC.collect(PacketCodecs::optional),
                Output::block,
                FluidOutput.STREAM_CODEC.collect(PacketCodecs::optional),
                Output::fluid,
                ItemStack.OPTIONAL_LIST_PACKET_CODEC,
                Output::drops,
                Output::new
        );
    }

    /**
     * @param block 输出方块
     * @param keepProperties 是否使用方块状态，默认为false（可选）
     * @param properties 方块状态（可选）
     */
    public record BlockOutput(BlockState block, boolean keepProperties, Map<String, String> properties) {
        /**
         * 自定义编码器：需要BlockState、Boolean、Map<String, String>的编码器
         * <pre>
         * "id": {
         *   "Name": "minecraft:dirt"
         * }
         * </pre>
         */
        public static Codec<BlockOutput> CODEC = RecordCodecBuilder.create(builder -> builder.group(
                BlockState.CODEC.fieldOf("id").forGetter(BlockOutput::block),
                Codec.BOOL.optionalFieldOf("", false).forGetter(BlockOutput::keepProperties),
                Codec.unboundedMap(Codec.STRING, Codec.STRING).optionalFieldOf("properties", Map.of()).forGetter(BlockOutput::properties))
                .apply(builder, BlockOutput::new)
        );

        public static PacketCodec<RegistryByteBuf, BlockOutput> STREAM_CODEC = PacketCodec.tuple(
                PacketCodecs.registryCodec(BlockState.CODEC),
                BlockOutput::block,
                PacketCodecs.BOOLEAN,
                BlockOutput::keepProperties,
                PacketCodecs.map(Maps::newHashMapWithExpectedSize, PacketCodecs.STRING, PacketCodecs.STRING),
                BlockOutput::properties,
                BlockOutput::new
        );

        public BlockState apply(BlockState originalBlockState) {
            BlockState state = block.getBlock().getDefaultState();

            if (keepProperties) {
                for (Property<?> property : originalBlockState.getProperties()) {
                    state = copyProperty(originalBlockState, state, property);
                }
            }

            return state;
        }
    }

    /**
     * @param fluid 输出液体
     * @param keepProperties 是否使用液体状态，默认为false（可选）
     * @param properties 液体状态（可选）
     */
    public record FluidOutput(FluidState fluid, boolean keepProperties, Map<String, String> properties) {
        /**
         * 自定义编码器：需要FluidState、Boolean、Map<String, String>的编码器
         * <pre>
         * "id": {
         *   "Name": "minecraft:water",
         *   "Properties": {
         *     "falling": "true"
         *   }
         * }
         * </pre>
         */
        public static Codec<FluidOutput> CODEC = RecordCodecBuilder.create(builder -> builder.group(
                        FluidState.CODEC.fieldOf("id").forGetter(FluidOutput::fluid),
                        Codec.BOOL.optionalFieldOf("mode", false).forGetter(FluidOutput::keepProperties),
                        Codec.unboundedMap(Codec.STRING, Codec.STRING).optionalFieldOf("properties", Map.of()).forGetter(FluidOutput::properties))
                .apply(builder, FluidOutput::new)
        );

        public static PacketCodec<RegistryByteBuf, FluidOutput> STREAM_CODEC = PacketCodec.tuple(
                PacketCodecs.registryCodec(FluidState.CODEC),
                FluidOutput::fluid,
                PacketCodecs.BOOLEAN,
                FluidOutput::keepProperties,
                PacketCodecs.map(Maps::newHashMapWithExpectedSize, PacketCodecs.STRING, PacketCodecs.STRING),
                FluidOutput::properties,
                FluidOutput::new
        );

        public FluidState apply(FluidState originalFluidState) {
            FluidState state = fluid.getFluid().getDefaultState();

            if (keepProperties) {
                for (Property<?> property : originalFluidState.getProperties()) {
                    state = copyProperty(originalFluidState, state, property);
                }
            }

            return state;
        }
    }

    /**
     * Copies a property from one state to another (if that state also has that property).
     */
    private static <T extends Comparable<T>, SH extends State<?, SH>> SH copyProperty(SH from, SH to, Property<T> property) {
        if (to.contains(property)) return to.with(property, from.get(property));
        return to;
    }
}
