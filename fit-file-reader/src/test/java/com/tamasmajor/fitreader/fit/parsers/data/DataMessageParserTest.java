package com.tamasmajor.fitreader.fit.parsers.data;

import com.tamasmajor.fitreader.fit.model.data.DataMessage;
import com.tamasmajor.fitreader.fit.model.data.definition.DefinitionMessage;
import com.tamasmajor.fitreader.fit.model.data.definition.FieldDefinition;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DataMessageParserTest {

    private final DataMessageParser parser = new DataMessageParser();

    @Test
    void shouldParseRecordHeader() {
        // given
        val headerByte = (byte) Integer.parseInt("00000011", 2);
        val message = new byte[] { headerByte, 0, 0, 0, 0, 0 };
        val dataHolder = new DataHolder(message);
        val definitionMessages = Map.of(2, aDefinitionMessage(2).build(), 3, aDefinitionMessage(3).build());
        // when
        DataMessage dataMessage = parser.parse(dataHolder, definitionMessages);
        // then
        assertEquals(3, dataMessage.getLocalMessageNumber());
    }

    @Test
    void shouldSetCorrespondingDefinitionFromDefinitions() {
        // given
        val headerByte = (byte) Integer.parseInt("00000011", 2);
        val message = new byte[] { headerByte, 0, 0, 0, 0, 0 };
        val dataHolder = new DataHolder(message);
        val definitionMessages = Map.of(2, aDefinitionMessage(2).build(), 3, aDefinitionMessage(3).build());
        // when
        DataMessage dataMessage = parser.parse(dataHolder, definitionMessages);
        // then
        assertEquals(3, dataMessage.getDefinitionMessage().getLocalMessageNumber());
    }

    @Test
    void shouldParseValues() {
        // given
        val headerByte = (byte) Integer.parseInt("00000000", 2);
        val message = new byte[] { headerByte, 11, 21, 22, 31, 32, 33, 34, 41, 51, 52 };
        val dataHolder = new DataHolder(message);
        val definitionMessage = aDefinitionMessage(0).fieldDefinitions(List.of(
                FieldDefinition.builder().size(1).build(),
                FieldDefinition.builder().size(2).build(),
                FieldDefinition.builder().size(4).build()
        )).devFieldDefinitions(List.of(
                FieldDefinition.builder().size(1).build(),
                FieldDefinition.builder().size(2).build()
        )).build();
        // when
        DataMessage dataMessage = parser.parse(dataHolder, Map.of(0, definitionMessage));
        // then
        val values = dataMessage.getValues();
        assertArrayEquals(new byte[] { 11 }, values.get(0).getData());
        assertArrayEquals(new byte[] { 21, 22 }, values.get(1).getData());
        assertArrayEquals(new byte[] { 31, 32, 33, 34 }, values.get(2).getData());
        assertArrayEquals(new byte[] { 41 }, values.get(3).getData());
        assertArrayEquals(new byte[] { 51, 52 }, values.get(4).getData());
    }

    private DefinitionMessage.DefinitionMessageBuilder aDefinitionMessage(int localMessageNumber) {
        return DefinitionMessage.builder().localMessageNumber(localMessageNumber)
                .fieldDefinitions(List.of()).devFieldDefinitions(List.of());
    }

}
