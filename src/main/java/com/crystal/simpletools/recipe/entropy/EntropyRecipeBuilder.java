package com.crystal.simpletools.recipe.entropy;

import com.google.common.base.Preconditions;
import net.minecraft.block.BlockState;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.util.*;

public class EntropyRecipeBuilder {
    private EntropyMode mode;
    private BlockState inputBlock;
    private final Map<String, PropertyValueMatcher> inputBlockMatchers = new HashMap<>();
    private FluidState inputFluid;
    private final Map<String, PropertyValueMatcher> inputFluidMatchers = new HashMap<>();
    private BlockState outputBlock;
    private final Map<String, String> outputBlockStateAppliers = new HashMap<>();
    private boolean outputBlockKeep;
    private FluidState outputFluid;
    private final Map<String, String> outputFluidStateAppliers = new HashMap<>();
    private boolean outputFluidKeep;
    private List<ItemStack> drops = Collections.emptyList();

    public static EntropyRecipeBuilder cool() {
        return new EntropyRecipeBuilder().setMode(EntropyMode.COOL);
    }

    public static EntropyRecipeBuilder heat() {
        return new EntropyRecipeBuilder().setMode(EntropyMode.HEAT);
    }

    public EntropyRecipeBuilder setMode(EntropyMode mode) {
        this.mode = mode;
        return this;
    }

    public EntropyRecipeBuilder setInputBlock(BlockState inputBlock) {
        this.inputBlock = Objects.requireNonNull(inputBlock, "inputBlock must not be null");
        return this;
    }

    public EntropyRecipeBuilder setInputFluid(FluidState inputFluid) {
        this.inputFluid = Objects.requireNonNull(inputFluid, "inputFluid must not be null");
        return this;
    }

    public EntropyRecipeBuilder setOutputBlock(BlockState outputBlock) {
        this.outputBlock = Objects.requireNonNull(outputBlock, "outputBlock must not be null");
        return this;
    }

    public EntropyRecipeBuilder setOutputBlockKeep(boolean outputBlockKeep) {
        this.outputBlockKeep = outputBlockKeep;
        return this;
    }

    public EntropyRecipeBuilder setOutputFluid(FluidState outputFluid) {
        this.outputFluid = Objects.requireNonNull(outputFluid, "outputFluid must not be null");
        return this;
    }

    public EntropyRecipeBuilder setOutputFluidKeep(boolean outputFluidKeep) {
        this.outputFluidKeep = outputFluidKeep;
        return this;
    }

    public EntropyRecipeBuilder setDrops(List<ItemStack> drops) {
        Preconditions.checkArgument(!drops.isEmpty(), "drops needs to be a non empty list when set");

        this.drops = drops;
        return this;
    }

    public EntropyRecipeBuilder setDrops(ItemStack... drops) {
        return setDrops(Arrays.asList(drops));
    }

    public EntropyRecipe build() {
        Preconditions.checkState(inputBlock != null || inputFluid != null,
                "Either inputBlock or inputFluid needs to be not null");

        EntropyRecipe.BlockInput blockInput = null;
        if (inputBlock != null) {
            blockInput = new EntropyRecipe.BlockInput(inputBlock, inputBlockMatchers);
        }
        EntropyRecipe.FluidInput fluidInput = null;
        if (inputFluid != null) {
            fluidInput = new EntropyRecipe.FluidInput(inputFluid, inputFluidMatchers);
        }
        var input = new EntropyRecipe.Input(Optional.ofNullable(blockInput), Optional.ofNullable(fluidInput));

        EntropyRecipe.BlockOutput blockOutput = null;
        if (outputBlock != null) {
            blockOutput = new EntropyRecipe.BlockOutput(outputBlock, outputBlockKeep, outputBlockStateAppliers);
        }
        EntropyRecipe.FluidOutput fluidOutput = null;
        if (outputFluid != null) {
            fluidOutput = new EntropyRecipe.FluidOutput(outputFluid, outputFluidKeep, outputFluidStateAppliers);
        }

        var output = new EntropyRecipe.Output(Optional.ofNullable(blockOutput), Optional.ofNullable(fluidOutput),
                drops);

        return new EntropyRecipe(mode, input, output);
    }

    public void save(RecipeExporter consumer, Identifier id) {
        consumer.accept(RegistryKey.of(RegistryKeys.RECIPE, id), build(), null);
    }

}
