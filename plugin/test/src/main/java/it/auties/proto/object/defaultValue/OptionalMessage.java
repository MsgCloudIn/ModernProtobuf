package it.auties.proto.object.defaultValue;

import it.auties.protobuf.annotation.ProtobufDefaultValue;
import it.auties.protobuf.annotation.ProtobufDeserializer;
import it.auties.protobuf.annotation.ProtobufProperty;
import it.auties.protobuf.model.ProtobufMessage;
import it.auties.protobuf.model.ProtobufType;

import java.util.Objects;

public final class OptionalMessage implements ProtobufMessage {
    private static final OptionalMessage EMPTY = new OptionalMessage(null);

    @ProtobufProperty(index = 1, type = ProtobufType.STRING)
    private final String value;
    private OptionalMessage(String value) {
        this.value = value;
    }

    @ProtobufDefaultValue
    public static OptionalMessage empty() {
        return EMPTY;
    }

    @ProtobufDeserializer
    public static OptionalMessage ofNullable(String value) {
        return value == null ? EMPTY : new OptionalMessage(value);
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (OptionalMessage) obj;
        return Objects.equals(this.value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "WrapperMessage[" +
                "value=" + value + ']';
    }
}
