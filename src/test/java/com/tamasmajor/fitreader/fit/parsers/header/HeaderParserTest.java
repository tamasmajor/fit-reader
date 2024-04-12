package com.tamasmajor.fitreader.fit.parsers.header;

import lombok.val;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HeaderParserTest {

    private final HeaderParser headerParser = new HeaderParser();

    @Test
    void shouldParseHeaderLength() {
        val header = headerParser.parse(new byte[] { 14, 0, 1, 2, -21, 12, 98, 3, 10, 11 });
        assertEquals(14, header.getHeaderSize());
    }

    @Test
    void shouldParseDataSize() {
        val header = headerParser.parse(new byte[] { 14, 0, 1, 2, -21, 12, 98, 3, 10, 11 });
        assertEquals(56757483, header.getDataSize());
    }

}