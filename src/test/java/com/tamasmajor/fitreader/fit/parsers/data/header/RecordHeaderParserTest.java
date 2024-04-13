package com.tamasmajor.fitreader.fit.parsers.data.header;

import com.tamasmajor.fitreader.fit.model.data.header.RecordType;
import com.tamasmajor.fitreader.fit.model.data.header.normal.MessageType;
import lombok.val;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RecordHeaderParserTest {

    private final RecordHeaderParser parser = new RecordHeaderParser();

    @Nested
    class ParseNormalHeaderTests {

        @Test
        void shouldParseAsDefinitionDevExtendedWhenBothRelevantBitsSet() {
            val header = parser.parseNormalHeader(Byte.parseByte("01100000", 2));
            assertEquals(RecordType.NORMAL, header.getRecordType());
            assertEquals(MessageType.DEFINITION_DEV_EXTENDED, header.getMessageType());
        }

        @Test
        void shouldParseAsDefinitionWhenOnlyTheDefinitionBitIsSet() {
            val header = parser.parseNormalHeader(Byte.parseByte("01000000", 2));
            assertEquals(RecordType.NORMAL, header.getRecordType());
            assertEquals(MessageType.DEFINITION, header.getMessageType());
        }

        @Test
        void shouldParseAsDataWhenDefinitionBitIsNotSet() {
            val header = parser.parseNormalHeader(Byte.parseByte("00111111", 2));
            assertEquals(RecordType.NORMAL, header.getRecordType());
            assertEquals(MessageType.DATA, header.getMessageType());
        }

        @Test
        void shouldThrowExceptionWhenCalledWithNonNormalHeaderByte() {
            val exception = assertThrows(IllegalArgumentException.class, () ->parser.parseNormalHeader((byte) -128));
            assertEquals("It must represent a normal header", exception.getMessage());
        }

    }

    @Nested
    class GetRecordTypeTests {

        @Test
        void shouldReturnNormalWhenTypeBitNotSet() {
            assertEquals(RecordType.NORMAL, parser.getRecordType((byte) 0));
        }

        @Test
        void shouldReturnTimestampWhenTypeBitIsSet() {
            assertEquals(RecordType.TIMESTAMP, parser.getRecordType((byte) -128));
        }

    }

}