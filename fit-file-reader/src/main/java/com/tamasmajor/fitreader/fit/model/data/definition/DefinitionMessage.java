package com.tamasmajor.fitreader.fit.model.data.definition;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;


@Getter
@Builder
@ToString
public class DefinitionMessage {
    // record header byte + reserved byte + arch byte + global msg 2 bytes + num of garmin fields byte
    private static final int COMMON_BYTES_LENGTH = 6;


    private int reserved;
    private Architecture architecture;
    private int localMessageNumber;
    private int globalMessageNumber;
    private int numberOfFields;
    private int numberOfDevFields;
    private List<FieldDefinition> fieldDefinitions;
    private List<FieldDefinition> devFieldDefinitions;
    private int definitionLength;

    public enum Architecture {
        LITTLE_ENDIAN,
        BIG_ENDIAN
    }

    public int length() {
        int fieldsLength = fieldDefinitions.stream().map(FieldDefinition::getSize).reduce(0, Integer::sum);
        int devFieldsLength = devFieldDefinitions.stream().map(FieldDefinition::getSize).reduce(0, Integer::sum);
        var length = COMMON_BYTES_LENGTH + fieldsLength;
        if (devFieldsLength > 0) {
            length += 1 + devFieldsLength; // 1 -> number of dev fields byte
        }
        return length;
    }

}
