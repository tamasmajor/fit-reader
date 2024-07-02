package com.tamasmajor.fitreader.model.converter;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ByteValueConverterTest {

    @Nested
    class IntConversions {

        @Test
        void shouldConvertUsingLittleEndianByDefault() {
            assertEquals(0, ByteValueConverter.convert(new byte[] { 0 }, Integer.class));
            assertEquals(1, ByteValueConverter.convert(new byte[] { 1 }, Integer.class));
            assertEquals(200, ByteValueConverter.convert(new byte[] { -56, 0 }, Integer.class));
            assertEquals(2147483647, ByteValueConverter.convert(new byte[] { -1, -1, -1, 127 }, Integer.class));
        }

    }

    @Nested
    class LongConversions {

        @Test
        void shouldConvertUsingLittleEndianByDefault() {
            assertEquals(0, ByteValueConverter.convert(new byte[] { 0 }, Long.class));
            assertEquals(1, ByteValueConverter.convert(new byte[] { 1 }, Long.class));
            assertEquals(200, ByteValueConverter.convert(new byte[] { -56, 0 }, Long.class));
            assertEquals(2147483647, ByteValueConverter.convert(new byte[] { -1, -1, -1, 127 }, Long.class));
            assertEquals(9223372036854775807L, ByteValueConverter.convert(new byte[] { -1, -1, -1, -1, -1, -1, -1, 127 }, Long.class));
        }

    }

    @Nested
    class StringConversions {

        @Test
        void shouldConvertUsingLittleEndianByDefault() {
            assertEquals("Hello!", ByteValueConverter.convert("Hello!".getBytes(), String.class));
        }

    }

}