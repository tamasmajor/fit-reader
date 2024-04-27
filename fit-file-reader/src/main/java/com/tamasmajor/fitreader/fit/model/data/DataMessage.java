package com.tamasmajor.fitreader.fit.model.data;

import com.tamasmajor.fitreader.fit.model.data.definition.DefinitionMessage;
import com.tamasmajor.fitreader.fit.model.data.definition.FieldDefinition;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.val;

import java.util.List;
import java.util.stream.Stream;

@Getter
@Builder
@ToString
public class DataMessage {
    private int localMessage;
    private List<Value> values;
    private DefinitionMessage definitionMessage;

    public int length() {
        val fieldsLength = Stream.concat(definitionMessage.getFieldDefinitions().stream(), definitionMessage.getDevFieldDefinitions().stream())
                .map(FieldDefinition::getSize).reduce(0, Integer::sum);
        return 1 + fieldsLength;
    }
}
