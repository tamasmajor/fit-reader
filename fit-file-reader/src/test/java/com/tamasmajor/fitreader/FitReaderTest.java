package com.tamasmajor.fitreader;

import lombok.val;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FitReaderTest {

    private final FitReader fitReader = new FitReader();

    @Test
    void shouldParseWholeFile() {
        // given
        val header = new byte[] { 14, 0, 1, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        val definition1 = new byte[] { 64, 0, 0, 0, 0, 1, 0, 1, 1 }; // 64 -> 01000000 = definition, id = 0000, 1 field
        val data1_1 = new byte[] { 0, 8 }; // 0 -> 01000000 = def id 0000, value 8
        val data1_2 = new byte[] { 0, 4 }; // 0 -> 01000000 = def id 0000, value 4
        val definition2 = new byte[] { 65, 0, 0, 0, 0, 1, 0, 1, 1 }; // 64 -> 01000001 = definition, id = 0001, 1 field
        val data2_1 = new byte[] { 1, 5 }; // 0 -> 00000001 = def id 0001, value 5
        val crc_bytes = new byte[] { 0, 0 };
        ByteBuffer buffer = ByteBuffer.allocate(header.length + definition1.length + data1_1.length + data1_2.length +
                definition2.length + data2_1.length + crc_bytes.length);
        buffer.put(header);
        buffer.put(definition1);
        buffer.put(data1_1);
        buffer.put(data1_2);
        buffer.put(definition2);
        buffer.put(data2_1);
        buffer.put(crc_bytes);
        // when
        val dataMessages = fitReader.read(buffer.array());
        // then
        assertEquals(3, dataMessages.size());
        assertEquals(0, dataMessages.get(0).getLocalMessageNumber());
        assertEquals(0, dataMessages.get(0).getDefinitionMessage().getGlobalMessageNumber());
        assertEquals(1, dataMessages.get(0).getValues().size());
        assertArrayEquals(new byte[] { 8 }, dataMessages.get(0).getValues().get(0).getData());
        assertEquals(0, dataMessages.get(1).getLocalMessageNumber());
        assertEquals(0, dataMessages.get(1).getDefinitionMessage().getGlobalMessageNumber());
        assertEquals(1, dataMessages.get(1).getValues().size());
        assertArrayEquals(new byte[] { 4 }, dataMessages.get(1).getValues().get(0).getData());
        assertEquals(1, dataMessages.get(2).getLocalMessageNumber());
        assertEquals(1, dataMessages.get(2).getDefinitionMessage().getLocalMessageNumber());
        assertEquals(1, dataMessages.get(2).getValues().size());
        assertArrayEquals(new byte[] { 5 }, dataMessages.get(2).getValues().get(0).getData());
    }

}