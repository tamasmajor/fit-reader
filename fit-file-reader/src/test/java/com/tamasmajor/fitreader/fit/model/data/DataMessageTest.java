package com.tamasmajor.fitreader.fit.model.data;

import com.tamasmajor.fitreader.fit.model.data.definition.DefinitionMessage;
import com.tamasmajor.fitreader.fit.model.data.definition.FieldDefinition;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DataMessageTest {

    @Test
    void shouldCalculateDataMessageLengthWhenOnlyRecordHeaderWithoutFields() {
        // given
        val definitionMessage = DefinitionMessage.builder().fieldDefinitions(List.of())
                .devFieldDefinitions(List.of()).build();
        // when
        val dataMessage = DataMessage.builder().definitionMessage(definitionMessage).build();
        // then
        assertEquals(1, dataMessage.length());
    }

    @Test
    void shouldCalculateDataMessageLengthWithoutDevExtensionFields() {
        // given
        val definitionMessage = DefinitionMessage.builder().fieldDefinitions(List.of(
            FieldDefinition.builder().size(2).build(),
            FieldDefinition.builder().size(4).build(),
            FieldDefinition.builder().size(22).build()
        )).devFieldDefinitions(List.of()).build();
        // when
        val dataMessage = DataMessage.builder().definitionMessage(definitionMessage).build();
        // then
        assertEquals(29, dataMessage.length());
    }

    @Test
    void shouldCalculateDataMessageLengthWithDevExtensionFields() {
        // given
        val definitionMessage = DefinitionMessage.builder().fieldDefinitions(List.of(
                FieldDefinition.builder().size(2).build(),
                FieldDefinition.builder().size(4).build(),
                FieldDefinition.builder().size(22).build()
        )).devFieldDefinitions(List.of(
                FieldDefinition.builder().size(4).build(),
                FieldDefinition.builder().size(8).build()
        )).build();
        // when
        val dataMessage = DataMessage.builder().definitionMessage(definitionMessage).build();
        // then
        assertEquals(41, dataMessage.length());
    }

}