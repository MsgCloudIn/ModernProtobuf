package it.auties.protobuf.api.jackson;

import com.fasterxml.jackson.core.FormatSchema;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.*;
import it.auties.protobuf.api.exception.ProtobufDeserializationException;
import it.auties.protobuf.api.model.ProtobufMessage;
import it.auties.protobuf.api.model.ProtobufSchema;
import it.auties.protobuf.api.util.VersionInfo;

import java.io.IOException;
import java.net.URL;

public class ProtobufMapper extends ObjectMapper {
    public ProtobufMapper() {
        this(new ProtobufFactory());
    }

    private ProtobufMapper(ProtobufFactory factory) {
        super(factory);
    }

    protected ProtobufMapper(ProtobufMapper src) {
        super(src);
    }

    public static ProtobufMapperBuilder builder() {
        return new ProtobufMapperBuilder();
    }

    public <T extends ProtobufMessage> T readMessage(byte[] src, Class<T> valueType) throws IOException {
        return reader(ProtobufSchema.of(valueType))
                .readValue(src, valueType);
    }

    @Override
    protected Object _readMapAndClose(JsonParser parser, JavaType valueType) throws IOException {
        if(!ProtobufMessage.isMessage(valueType.getRawClass())){
            throw new ProtobufDeserializationException("Cannot deserialize message, invalid type: expected ProtobufMessage, got %s"
                    .formatted(valueType.getRawClass().getName()));
        }

        if(parser.getSchema() == null){
            parser.setSchema(ProtobufSchema.of(valueType.getRawClass().asSubclass(ProtobufMessage.class)));
        }

        return super._readMapAndClose(parser, valueType);
    }

    @Override
    public ProtobufMapper copy() {
        _checkInvalidCopy(ProtobufMapper.class);
        return new ProtobufMapper(this);
    }

    @Override
    public DeserializationConfig getDeserializationConfig() { // These options are set to be compliant with the proto spec
        return _deserializationConfig.with(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
                .without(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    @Override
    public Version version() {
        return VersionInfo.current();
    }
}