package it.auties.protobuf.serialization.object;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;

public record ProtobufUnknownFieldsElement(TypeMirror type, String defaultValue, ExecutableElement setter) {
}
