package com.tamasmajor.fitreader.util;

import lombok.val;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static java.nio.ByteOrder.BIG_ENDIAN;
import static java.nio.ByteOrder.LITTLE_ENDIAN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ByteUtilTest {

    @Nested
    class AsIntTests {

        @Test
        void shouldConvertLittleEndian() {
            assertEquals(56757483, ByteUtil.asInt(new byte[] { -21, 12, 98, 3 }, LITTLE_ENDIAN));
        }

        @Test
        void shouldConvertBigEndian() {
            assertEquals(56757483, ByteUtil.asInt(new byte[] { 3, 98, 12, -21 }, BIG_ENDIAN));
        }

        @Test
        void shouldThrowExceptionWhenLengthIsIncorrect() {
            val ex = assertThrows(IllegalArgumentException.class, () -> ByteUtil.asInt(new byte[] { 1, 1 }, BIG_ENDIAN));
            assertEquals("Length has to be 4 bytes", ex.getMessage());
        }

    }

}