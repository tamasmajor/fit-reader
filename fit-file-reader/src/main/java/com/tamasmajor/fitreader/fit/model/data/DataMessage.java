package com.tamasmajor.fitreader.fit.model.data;

import com.tamasmajor.fitreader.fit.model.data.definition.DefinitionMessage;
import com.tamasmajor.fitreader.fit.model.data.definition.FieldDefinition;
import lombok.*;

import java.util.List;
import java.util.stream.Stream;

@Getter
@Builder
@ToString
@AllArgsConstructor
public class DataMessage {
    private int localMessageNumber;
    private List<Value> values;
    private DefinitionMessage definitionMessage;

    public int length() {
        val fieldsLength = Stream.concat(definitionMessage.getFieldDefinitions().stream(), definitionMessage.getDevFieldDefinitions().stream())
                .map(FieldDefinition::getSize).reduce(0, Integer::sum);
        return 1 + fieldsLength;
    }
}
