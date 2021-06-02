package it.auties.protobuf.schema;

import com.google.googlejavaformat.java.Formatter;
import com.google.googlejavaformat.java.FormatterException;
import it.auties.protobuf.ast.EnumStatement;
import it.auties.protobuf.ast.MessageStatement;
import it.auties.protobuf.ast.ProtobufDocument;
import it.auties.protobuf.ast.ProtobufObject;
import lombok.AllArgsConstructor;
import org.simart.writeonce.common.GeneratorException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@AllArgsConstructor
public class ProtobufSchemaCreator {
    private final ProtobufDocument document;
    private final String pack;
    private final File directory;
    private final Formatter formatter;

    public void generateSchema() throws GeneratorException, IOException, FormatterException {
        for (var protobufObject : document.getStatements()) {
            generateSchema(protobufObject);
        }
    }

    private void generateSchema(ProtobufObject<?> object) throws GeneratorException, IOException, FormatterException {
        var schemaCreator = object instanceof MessageStatement msg ? new MessageSchemaCreator(msg, pack, true) : new EnumSchemaCreator((EnumStatement) object, pack, true);
        var formattedSchema = formatter.formatSourceAndFixImports(schemaCreator.createSchema());
        Files.write(Path.of(directory.getPath(), "/%s.java".formatted(object.getName())), formattedSchema.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }
}
