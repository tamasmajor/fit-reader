package com.tamasmajor.fitreader.fit.parsers.file;

import lombok.val;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FileStructureParserTest {

    private final FileStructureParser parser = new FileStructureParser();

    @Test
    void shouldSplitBasedOnHeaderLength() {
        // given
        val input = new byte[40];
        input[0] = 14;
        // when
        val fitFile = parser.parse(input);
        // then
        assertEquals(14, fitFile.getHeaderBytes().length);
        assertEquals(24, fitFile.getDataBytes().length);
        assertEquals(2, fitFile.getCrcBytes().length);
    }

    @Test
    void shouldHaveExpectedBytesInSections() {
        // given
        val input = new byte[] { 3, 1, 2, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 1, 2 };
        // when
        val fitFile = parser.parse(input);
        // then
        assertArrayEquals(new byte[] { 3, 1, 2 }, fitFile.getHeaderBytes());
        assertArrayEquals(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }, fitFile.getDataBytes());
        assertArrayEquals(new byte[] { 1, 2 }, fitFile.getCrcBytes());
    }

    @Test
    void shouldThrowExceptionWhenLengthIsSmallerThanMinimum() {
        // given
        val input = new byte[] { 1, 2, 3 };
        // when
        val exception = assertThrows(IllegalArgumentException.class, () -> parser.parse(input));
        // then
        assertEquals("Invalid length: 3. Minimum is 14", exception.getMessage());
    }

}