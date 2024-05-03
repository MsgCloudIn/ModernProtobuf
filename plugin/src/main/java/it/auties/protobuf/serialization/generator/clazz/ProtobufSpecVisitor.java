package it.auties.protobuf.serialization.generator.clazz;

import it.auties.protobuf.serialization.generator.method.ProtobufDeserializationMethodGenerator;
import it.auties.protobuf.serialization.generator.method.ProtobufSerializationMethodGenerator;
import it.auties.protobuf.serialization.object.ProtobufObjectElement;
import it.auties.protobuf.serialization.property.ProtobufPropertyElement;
import it.auties.protobuf.serialization.support.CompilationUnitWriter;
import it.auties.protobuf.stream.ProtobufInputStream;
import it.auties.protobuf.stream.ProtobufOutputStream;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.PackageElement;
import java.io.IOException;
import java.util.*;

public class ProtobufSpecVisitor {
    private final ProcessingEnvironment processingEnv;

    public ProtobufSpecVisitor(ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
    }

    public void createClass(ProtobufObjectElement result, PackageElement packageName) throws IOException {
        // Names
        var simpleGeneratedClassName = result.getGeneratedClassNameBySuffix("Spec");
        var qualifiedGeneratedClassName = packageName != null ? packageName + "." + simpleGeneratedClassName : simpleGeneratedClassName;
        var sourceFile = processingEnv.getFiler().createSourceFile(qualifiedGeneratedClassName);

        // Declare a new compilation unit
        try (var compilationUnitWriter = new CompilationUnitWriter(sourceFile.openWriter())) {
            // If a package is available, write it in the compilation unit
            if(packageName != null) {
                compilationUnitWriter.printPackageDeclaration(packageName.getQualifiedName().toString());
            }

            // Declare the imports needed for everything to work
            var imports = getSpecImports(result);
            imports.forEach(compilationUnitWriter::printImportDeclaration);

            // Separate imports from classes
            compilationUnitWriter.printClassSeparator();

            // Declare the spec class
            try(var classWriter = compilationUnitWriter.printClassDeclaration(simpleGeneratedClassName)) {
                // Write the serializer
                var serializationVisitor = new ProtobufSerializationMethodGenerator(result, classWriter);
                serializationVisitor.generate();

                // Write the deserializer
                var deserializationVisitor = new ProtobufDeserializationMethodGenerator(result, classWriter);
                deserializationVisitor.generate();
            }
        }
    }

    // Get the imports to include in the compilation unit
    private List<String> getSpecImports(ProtobufObjectElement message) {
        if(message.isEnum()) {
            return List.of(
                    message.element().getQualifiedName().toString(),
                    Arrays.class.getName(),
                    Optional.class.getName()
            );
        }

        var imports = new ArrayList<String>();
        imports.add(message.element().getQualifiedName().toString());
        imports.add(ProtobufInputStream.class.getName());
        imports.add(ProtobufOutputStream.class.getName());
        if (message.properties().stream().anyMatch(ProtobufPropertyElement::required)) {
            imports.add(Objects.class.getName());
        }

        return Collections.unmodifiableList(imports);
    }
}
