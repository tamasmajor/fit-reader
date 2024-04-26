package com.tamasmajor.fitreader.fit.model.data.definition;

import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.tamasmajor.fitreader.fit.model.data.definition.DefinitionMessage.builder;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DefinitionMessageTest {

    @Test
    void shouldCalculateDefinitionRecordSizeInBytesWithoutDevExtField() {
        // given
        val fieldDef = List.of(
                FieldDefinition.builder().size(1).build(),
                FieldDefinition.builder().size(2).build(),
                FieldDefinition.builder().size(8).build()
        );
        val message = builder().numberOfFields(3).fieldDefinitions(fieldDef)
                .numberOfDevFields(0).devFieldDefinitions(List.of()).build();
        // when
        val length = message.length();
        // then
        assertEquals(15, length);
    }

    @Test
    void shouldCalculateDefinitionRecordSizeInByteWithDevExtField() {
        // given
        val fieldDef = List.of(
                FieldDefinition.builder().size(1).build(),
                FieldDefinition.builder().size(2).build(),
                FieldDefinition.builder().size(8).build()
        );
        val devFieldDef = List.of(
                FieldDefinition.builder().size(2).build(),
                FieldDefinition.builder().size(4).build()
        );
        val message = builder().numberOfFields(3).fieldDefinitions(fieldDef)
                .numberOfDevFields(2).devFieldDefinitions(devFieldDef).build();
        // when
        val length = message.length();
        // then
        assertEquals(22, length);
    }

}
