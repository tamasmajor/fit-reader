package com.tamasmajor.fitreader.fit.parsers.data.definition;

import com.tamasmajor.fitreader.fit.model.data.BaseType;
import com.tamasmajor.fitreader.fit.model.data.definition.DefinitionMessage;
import com.tamasmajor.fitreader.fit.parsers.data.DataHolder;
import lombok.val;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DefinitionMessageParserTest {

    private final DefinitionMessageParser parser = new DefinitionMessageParser();

    @Test
    void shouldParseLocalMessageNumber() {
        // given
        val headerByte = (byte) Integer.parseInt("01000011", 2);
        val message = new byte[] { headerByte, 0, 0, 0, 0, 0 };
        val dataHolder = new DataHolder(message);
        // when
        DefinitionMessage definitionMessage = parser.parse(dataHolder);
        // then
        assertEquals(3, definitionMessage.getLocalMessageNumber());
    }

    @Nested
    class ArchitectureParsingTests {

        @Test
        void shouldParseAsLittleEndianWhenArchitectureBitIsZero() {
            // given
            val headerByte = (byte) Integer.parseInt("01000000", 2);
            val message = new byte[] { headerByte, 5, 0, 5, 5, 0 };
            val dataHolder = new DataHolder(message);
            // when
            DefinitionMessage definitionMessage = parser.parse(dataHolder);
            // then
            assertEquals(DefinitionMessage.Architecture.LITTLE_ENDIAN, definitionMessage.getArchitecture());
        }

        @Test
        void shouldParseAsBigEndianWhenArchitectureBitIsOne() {
            // given
            val headerByte = (byte) Integer.parseInt("01000000", 2);
            val message = new byte[] { headerByte, 0, 1, 0, 0, 0 };
            val dataHolder = new DataHolder(message);
            // when
            DefinitionMessage definitionMessage = parser.parse(dataHolder);
            // then
            assertEquals(DefinitionMessage.Architecture.BIG_ENDIAN, definitionMessage.getArchitecture());
        }

    }

    @Nested
    class GlobalMessageNumberTests {

        @Test
        void shouldParseGlobalMessageNumberWithLittleEndianArchitecture() {
            // given
            val headerByte = (byte) Integer.parseInt("01000000", 2);
            val message = new byte[] { headerByte, 5, 0, 100, 32, 0 };
            val dataHolder = new DataHolder(message);
            // when
            DefinitionMessage definitionMessage = parser.parse(dataHolder);
            // then
            assertEquals(8292, definitionMessage.getGlobalMessageNumber());
        }

        @Test
        void shouldParseGlobalMessageNumberWithBigEndianArchitecture() {
            // given
            val headerByte = (byte) Integer.parseInt("01000000", 2);
            val message = new byte[] { headerByte, 5, 1, 32, 100, 0 };
            val dataHolder = new DataHolder(message);
            // when
            DefinitionMessage definitionMessage = parser.parse(dataHolder);
            // then
            assertEquals(8292, definitionMessage.getGlobalMessageNumber());
        }

    }

    @Nested
    class FieldMappingTests {

        @Test
        void shouldMapGarminFields() {
            // given
            val headerByte = (byte) Integer.parseInt("01000000", 2);
            val message = new byte[] { headerByte, 0, 0, 0, 0, 3,
                    11, 1, 2,
                    22, 2, (byte) 132,
                    33, 4, (byte) 134
            };
            val dataHolder = new DataHolder(message);
            // when
            DefinitionMessage definitionMessage = parser.parse(dataHolder);
            // then
            assertEquals(3, definitionMessage.getNumberOfFields());
            assertEquals(11, definitionMessage.getFieldDefinitions().get(0).getFieldDefinitionNumber());
            assertEquals(1, definitionMessage.getFieldDefinitions().get(0).getSize());
            assertEquals(BaseType.UINT8, definitionMessage.getFieldDefinitions().get(0).getBaseType());
            assertEquals(22, definitionMessage.getFieldDefinitions().get(1).getFieldDefinitionNumber());
            assertEquals(2, definitionMessage.getFieldDefinitions().get(1).getSize());
            assertEquals(BaseType.UINT16, definitionMessage.getFieldDefinitions().get(1).getBaseType());
            assertEquals(33, definitionMessage.getFieldDefinitions().get(2).getFieldDefinitionNumber());
            assertEquals(4, definitionMessage.getFieldDefinitions().get(2).getSize());
            assertEquals(BaseType.UINT32, definitionMessage.getFieldDefinitions().get(2).getBaseType());
        }

        @Test
        void shouldMapDevExtendedFields() {
            // given
            val headerByte = (byte) Integer.parseInt("01100000", 2);
            val message = new byte[] { headerByte, 0, 0, 0, 0, 1, 1, 1, 1, 2,
                    11, 1, 2,
                    22, 2, (byte) 132,
            };
            val dataHolder = new DataHolder(message);
            // when
            DefinitionMessage definitionMessage = parser.parse(dataHolder);
            // then
            assertEquals(2, definitionMessage.getNumberOfDevFields());
            assertEquals(11, definitionMessage.getDevFieldDefinitions().get(0).getFieldDefinitionNumber());
            assertEquals(1, definitionMessage.getDevFieldDefinitions().get(0).getSize());
            assertEquals(BaseType.UINT8, definitionMessage.getDevFieldDefinitions().get(0).getBaseType());
            assertEquals(22, definitionMessage.getDevFieldDefinitions().get(1).getFieldDefinitionNumber());
            assertEquals(2, definitionMessage.getDevFieldDefinitions().get(1).getSize());
            assertEquals(BaseType.UINT16, definitionMessage.getDevFieldDefinitions().get(1).getBaseType());
        }

    }

}