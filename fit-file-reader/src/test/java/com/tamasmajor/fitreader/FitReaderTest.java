package com.tamasmajor.fitreader;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FitReaderTest {

    private final FitReader fitReader = new FitReader();

    @Test
    void shouldParseWholeFile(@TempDir Path tempDir) throws IOException {
        // given
        val header = new byte[] { 14, 0, 1, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        val definition1 = new byte[] { 64, 0, 0, 0, 0, 1, 0, 1, 1 }; // 64 -> 01000000 = definition, id = 0000, 1 field
        val data1_1 = new byte[] { 0, 8 }; // 0 -> 01000000 = def id 0000, value 8
        val data1_2 = new byte[] { 0, 4 }; // 0 -> 01000000 = def id 0000, value 4
        val definition2 = new byte[] { 65, 0, 0, 20, 0, 2, 3, 2, 2, 7, 2, 2 }; // 64 -> 01000001 = definition, 20 = record, id = 0001, 2 fields
        val data2_1 = new byte[] { 1, 110, 0, -96, 0 }; // 0 -> 00000001 = def id 0001, heartrate 110, power 160
        val data2_2 = new byte[] { 1, 114, 0, -94, 0 }; // 0 -> 00000001 = def id 0001, heartrate 114, power 162
        val crc_bytes = new byte[] { 0, 0 };
        ByteBuffer buffer = ByteBuffer.allocate(header.length + definition1.length + data1_1.length + data1_2.length +
                definition2.length + data2_1.length + data2_2.length + crc_bytes.length);
        buffer.put(header);
        buffer.put(definition1);
        buffer.put(data1_1);
        buffer.put(data1_2);
        buffer.put(definition2);
        buffer.put(data2_1);
        buffer.put(data2_2);
        buffer.put(crc_bytes);

        Path tempFile = Files.createFile(tempDir.resolve("tempFile.fit"));
        Files.write(tempFile, buffer.array());
        // when
        val fitFile = fitReader.read(tempFile);
        // then
        assertEquals(2, fitFile.getRecords().size());
        assertEquals(110, fitFile.getRecords().get(0).getHeartRate());
        assertEquals(160, fitFile.getRecords().get(0).getPower());
        assertEquals(114, fitFile.getRecords().get(1).getHeartRate());
        assertEquals(162, fitFile.getRecords().get(1).getPower());
    }

}