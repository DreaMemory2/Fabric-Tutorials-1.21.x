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
* Description: <br>
* Datetime: 2025/5/27 19:21<br>
* @author Crystal
* @version 1.0
* @since 1.0
*/
public class EntropyRecipe implements Recipe<RecipeInput> {

    private final Input input;
    private final Output output;
    private final EntropyMode mode;

    public EntropyRecipe(EntropyMode mode, Input input, Output output) {
        this.mode = mode;
        this.input = input;
        this.output = output;
    }

    @Override
    public boolean matches(RecipeInput input, World world) {
        return false;
    }

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

    @Override
    public RecipeSerializer<? extends Recipe<RecipeInput>> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<? extends Recipe<RecipeInput>> getType() {
        return Type.INSTANCE;
    }

    @Override
    public IngredientPlacement getIngredientPlacement() {
        return IngredientPlacement.NONE;
    }

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

    public record Input(Optional<BlockInput> block, Optional<FluidInput> fluid) {
        public static Codec<Input> CODEC = RecordCodecBuilder.create(builder -> builder.group(
                BlockInput.CODEC.optionalFieldOf("block").forGetter(Input::block),
                FluidInput.CODEC.optionalFieldOf("fluid").forGetter(Input::fluid))
                .apply(builder, Input::new));
        public static PacketCodec<RegistryByteBuf, Input> STREAM_CODEC = PacketCodec.tuple(
                BlockInput.STREAM_CODEC.collect(PacketCodecs::optional),
                Input::block,
                FluidInput.STREAM_CODEC.collect(PacketCodecs::optional),
                Input::fluid,
                Input::new
        );

        public boolean matches(BlockState blockState, FluidState fluidState) {

            if (block.isPresent()) {
                var inputBlock = block.get().block();
                return blockState.getBlock() == inputBlock.getBlock();
            }

            if (fluid.isPresent()) {
                var inputFluid = fluid.get().fluid();
                return fluidState.getFluid().getDefaultState() == inputFluid;
            }

            return true;
        }
    }

    public record BlockInput(BlockState block, Map<String, PropertyValueMatcher> properties) {
        public static Codec<BlockInput> CODEC = RecordCodecBuilder.create(builder -> builder.group(
                BlockState.CODEC.fieldOf("id").forGetter(BlockInput::block),
                PropertyValueMatcher.MAP_CODEC.optionalFieldOf("properties",
                        Map.of()).forGetter(BlockInput::properties)
        ).apply(builder, BlockInput::new));

        public static PacketCodec<RegistryByteBuf, BlockInput> STREAM_CODEC = PacketCodec.tuple(
                PacketCodecs.registryCodec(BlockState.CODEC),
                BlockInput::block,
                PacketCodecs.map(Maps::newHashMapWithExpectedSize, PacketCodecs.STRING,
                        PropertyValueMatcher.STREAM_CODEC),
                BlockInput::properties,
                BlockInput::new
        );
    }

    public record FluidInput(FluidState fluid, Map<String, PropertyValueMatcher> properties) {
        public static Codec<FluidInput> CODEC = RecordCodecBuilder.create(builder -> builder.group(
                FluidState.CODEC.fieldOf("id").forGetter(FluidInput::fluid),
                PropertyValueMatcher.MAP_CODEC.optionalFieldOf("properties",
                        Map.of()).forGetter(FluidInput::properties)
        ).apply(builder, FluidInput::new));

        public static PacketCodec<RegistryByteBuf, FluidInput> STREAM_CODEC = PacketCodec.tuple(
                PacketCodecs.registryCodec(FluidState.CODEC),
                FluidInput::fluid,
                PacketCodecs.map(Maps::newHashMapWithExpectedSize, PacketCodecs.STRING,
                        PropertyValueMatcher.STREAM_CODEC),
                FluidInput::properties,
                FluidInput::new);
    }

    public record Output(Optional<BlockOutput> block, Optional<FluidOutput> fluid, List<ItemStack> drops) {
        public static Codec<Output> CODEC = RecordCodecBuilder.create(builder -> builder.group(
                        BlockOutput.CODEC.optionalFieldOf("block").forGetter(Output::block),
                        FluidOutput.CODEC.optionalFieldOf("fluid").forGetter(Output::fluid),
                        ItemStack.CODEC.listOf().optionalFieldOf("drops", List.of()).forGetter(Output::drops))
                .apply(builder, Output::new));

        public static PacketCodec<RegistryByteBuf, Output> STREAM_CODEC = PacketCodec.tuple(
                BlockOutput.STREAM_CODEC.collect(PacketCodecs::optional),
                Output::block,
                FluidOutput.STREAM_CODEC.collect(PacketCodecs::optional),
                Output::fluid,
                ItemStack.OPTIONAL_LIST_PACKET_CODEC,
                Output::drops,
                Output::new);
    }

    public record BlockOutput(BlockState block, boolean keepProperties, Map<String, String> properties) {
        public static Codec<BlockOutput> CODEC = RecordCodecBuilder.create(builder -> builder.group(
                BlockState.CODEC.fieldOf("id").forGetter(BlockOutput::block),
                Codec.BOOL.optionalFieldOf("mode", false).forGetter(BlockOutput::keepProperties),
                Codec.unboundedMap(Codec.STRING, Codec.STRING).optionalFieldOf("properties",
                        Map.of()).forGetter(BlockOutput::properties)
        ).apply(builder, BlockOutput::new));
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

    public record FluidOutput(FluidState fluid, boolean keepProperties, Map<String, String> properties) {
        public static Codec<FluidOutput> CODEC = RecordCodecBuilder.create(builder -> builder.group(
                        FluidState.CODEC.fieldOf("id").forGetter(FluidOutput::fluid),
                        Codec.BOOL.optionalFieldOf("mode", false).forGetter(FluidOutput::keepProperties),
                        Codec.unboundedMap(Codec.STRING, Codec.STRING).optionalFieldOf("properties", Map.of())
                                .forGetter(FluidOutput::properties))
                .apply(builder, FluidOutput::new));

        public static PacketCodec<RegistryByteBuf, FluidOutput> STREAM_CODEC = PacketCodec.tuple(
                PacketCodecs.registryCodec(FluidState.CODEC),
                FluidOutput::fluid,
                PacketCodecs.BOOLEAN,
                FluidOutput::keepProperties,
                PacketCodecs.map(Maps::newHashMapWithExpectedSize, PacketCodecs.STRING,
                        PacketCodecs.STRING),
                FluidOutput::properties,
                FluidOutput::new);

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
        if (to.contains(property)) {
            return to.with(property, from.get(property));
        } else {
            return to;
        }
    }
}
