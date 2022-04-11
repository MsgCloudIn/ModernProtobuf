package it.auties.protobuf.util;

import it.auties.protobuf.exception.ProtobufSerializationException;
import it.auties.protobuf.model.ProtobufMessage;
import it.auties.protobuf.model.ProtobufProperty.Type;

import java.util.Objects;

public record ProtobufField(String name, int index, Type type, Class<? extends ProtobufMessage> messageType, Object value, boolean packed, boolean required, boolean repeated) {
    public ProtobufField(String name, int index, Type type, Object value, boolean packed, boolean required, boolean repeated) {
        this(name, index, type, null, value, packed, required, repeated);
    }

    public ProtobufField withValue(Object value){
        return new ProtobufField(name, index, type, messageType,
                value, packed, required, false);
    }

    @SuppressWarnings("unchecked")
    public <T> T valueAs(){
        try {
            return (T) value;
        }catch (ClassCastException exception){
            throw new RuntimeException("A field misreported its own type in a schema: %s".formatted(this), exception);
        }
    }

    public boolean valid(){
        if(required && value == null){
            throw new ProtobufSerializationException("Cannot encode object: missing mandatory field with index %s and type %s"
                    .formatted(index, type));
        }

        return value != null
                && (!(value instanceof Number number) || number.floatValue() != 0F);
    }
}
