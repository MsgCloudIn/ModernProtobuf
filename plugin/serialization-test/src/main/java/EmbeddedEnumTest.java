import it.auties.protobuf.Protobuf;
import it.auties.protobuf.annotation.ProtobufProperty;
import it.auties.protobuf.model.ProtobufType;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.extern.jackson.Jacksonized;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EmbeddedEnumTest {
    @Test
    @SneakyThrows
    public void testModifiers() {
        var anotherMessage = new AnotherMessage(Type.SECOND);
        var someMessage = new SomeMessage(anotherMessage);
        var encoded = Protobuf.writeMessage(someMessage);
        var decoded = Protobuf.readMessage(encoded, SomeMessage.class);
        Assertions.assertNotNull(decoded.content());
        Assertions.assertEquals(anotherMessage.type(), decoded.content().type());
    }

    @Getter
    @AllArgsConstructor
    @Accessors(fluent = true)
    public enum Type {
        FIRST(0),
        SECOND(1),
        THIRD(10);

        private final int index;
    }

    @Jacksonized
    @Builder
    @Data
    @Accessors(fluent = true)
    public static class SomeMessage {
        @ProtobufProperty(index = 1, type = ProtobufType.ENUM)
        private AnotherMessage content;
    }

    @Jacksonized
    @Builder
    @Data
    @Accessors(fluent = true)
    public static class AnotherMessage {
        @ProtobufProperty(index = 3, type = ProtobufType.ENUM)
        private Type type;
    }
}
