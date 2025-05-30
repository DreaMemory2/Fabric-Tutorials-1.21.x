package com.crystal.simpletools.recipe.entropy;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.state.State;
import net.minecraft.state.property.Property;

import java.util.List;
import java.util.Map;

public sealed interface PropertyValueMatcher
        permits PropertyValueMatcher.SingleValue, PropertyValueMatcher.MultiValue, PropertyValueMatcher.Range {
    Codec<PropertyValueMatcher> CODEC = new Codec<>() {
        @Override
        public <T> DataResult<Pair<PropertyValueMatcher, T>> decode(DynamicOps<T> ops, T input) {
            // Try single string value first
            var singleValueResult = Codec.STRING.decode(ops, input)
                    .map(pair -> pair.mapFirst(value -> (PropertyValueMatcher) new SingleValue(value)));
            if (singleValueResult.error().isEmpty()) {
                return singleValueResult;
            }

            // Then try list value
            var listValueResult = Codec.STRING.listOf().decode(ops, input)
                    .map(pair -> pair.mapFirst(value -> (PropertyValueMatcher) new MultiValue(value)));
            if (listValueResult.error().isEmpty()) {
                return listValueResult;
            }

            // Then try a min/max object
            var rangeValueResult = Range.CODEC.decode(ops, input)
                    .map(pair -> pair.mapFirst(value -> (PropertyValueMatcher) value));
            if (rangeValueResult.error().isEmpty()) {
                return rangeValueResult;
            }

            // If all three fail, combine the errors
            return DataResult.error(
                    () -> "Property values need to be strings, list of strings, or objects with min/max properties");
        }

        @Override
        public <T> DataResult<T> encode(PropertyValueMatcher input, DynamicOps<T> ops, T prefix) {
            if (input instanceof SingleValue singleValue) {
                return Codec.STRING.encode(singleValue.value(), ops, prefix);
            } else if (input instanceof MultiValue multiValue) {
                return Codec.STRING.listOf().encode(multiValue.values(), ops, prefix);
            } else if (input instanceof Range range) {
                return Range.CODEC.encode(range, ops, prefix);
            } else {
                throw new IllegalStateException("This cannot happen");
            }
        }
    };

    PacketCodec<PacketByteBuf, PropertyValueMatcher> STREAM_CODEC = new PacketCodec<>() {
        @Override
        public PropertyValueMatcher decode(PacketByteBuf buffer) {
            var type = buffer.readByte();
            return switch (type) {
                case 0 -> new SingleValue(buffer.readString());
                case 1 -> new MultiValue(buffer.readList(PacketByteBuf::readString));
                case 2 -> new Range(buffer.readString(), buffer.readString());
                default -> throw new IllegalStateException("Invalid property value matcher type: " + type);
            };
        }

        @Override
        public void encode(PacketByteBuf buffer, PropertyValueMatcher matcher) {
            matcher.toNetwork(buffer);
        }
    };

    Codec<Map<String, PropertyValueMatcher>> MAP_CODEC = Codec.unboundedMap(Codec.STRING, CODEC);

    void toNetwork(PacketByteBuf buffer);

    void validate(Property<? extends Comparable<?>> property);

    <T extends Comparable<T>> boolean matches(Property<T> property, State<?, ?> state);

    record SingleValue(String value) implements PropertyValueMatcher {
        @Override
        public void toNetwork(PacketByteBuf buffer) {
            buffer.writeByte(0);
            buffer.writeString(value);
        }

        @Override
        public void validate(Property<? extends Comparable<?>> property) {
            if (property.parse(value).isEmpty()) {
                throw new IllegalStateException(
                        "Property " + property.getName() + " does not have value '" + value + "'");
            }
        }

        @Override
        public <T extends Comparable<T>> boolean matches(Property<T> property, State<?, ?> state) {
            var currentValue = property.name(state.get(property));
            return value.equals(currentValue);
        }
    }

    record MultiValue(List<String> values) implements PropertyValueMatcher {
        @Override
        public void toNetwork(PacketByteBuf buffer) {
            buffer.writeByte(1);
            buffer.writeCollection(values, PacketByteBuf::writeString);
        }

        @Override
        public void validate(Property<? extends Comparable<?>> property) {
            for (String value : values) {
                if (property.parse(value).isEmpty()) {
                    throw new IllegalStateException(
                            "Property " + property.getName() + " does not have value '" + value + "'");
                }
            }
        }

        @Override
        public <T extends Comparable<T>> boolean matches(Property<T> property, State<?, ?> state) {
            var currentValue = property.name(state.get(property));
            for (var value : values) {
                return value.equals(currentValue);
            }
            return true;
        }
    }

    record Range(String min, String max) implements PropertyValueMatcher {
        static Codec<Range> CODEC = RecordCodecBuilder.create(builder -> builder.group(
                Codec.STRING.fieldOf("min").forGetter(Range::min),
                Codec.STRING.fieldOf("max").forGetter(Range::max)).apply(builder, Range::new));

        @Override
        public void toNetwork(PacketByteBuf buffer) {
            buffer.writeByte(2);
            buffer.writeString(min);
            buffer.writeString(max);
        }

        @Override
        public void validate(Property<? extends Comparable<?>> property) {
            if (property.parse(min).isEmpty()) {
                throw new IllegalStateException(
                        "Property " + property.getName() + " does not have value '" + min + "'");
            }
            if (property.parse(max).isEmpty()) {
                throw new IllegalStateException(
                        "Property " + property.getName() + " does not have value '" + max + "'");
            }
        }

        @Override
        public <T extends Comparable<T>> boolean matches(Property<T> property, State<?, ?> state) {
            var minValue = property.parse(min).orElseThrow();
            var maxValue = property.parse(max).orElseThrow();
            var value = state.get(property);
            return value.compareTo(minValue) >= 0 && value.compareTo(maxValue) <= 0;
        }
    }
}
