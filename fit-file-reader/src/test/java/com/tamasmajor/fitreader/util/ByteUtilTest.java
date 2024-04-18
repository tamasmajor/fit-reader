package com.tamasmajor.fitreader.util;

import lombok.val;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static java.nio.ByteOrder.BIG_ENDIAN;
import static java.nio.ByteOrder.LITTLE_ENDIAN;
import static org.junit.jupiter.api.Assertions.*;

class ByteUtilTest {

    @Nested
    class IsBitSetTests {

        @Test
        void shouldReturnTrueWhenBitIsSet() {
            assertTrue(ByteUtil.isBitSet((byte) -128, 7));
            assertTrue(ByteUtil.isBitSet(Byte.parseByte("01000000", 2), 6));
            assertTrue(ByteUtil.isBitSet(Byte.parseByte("00100000", 2), 5));
            assertTrue(ByteUtil.isBitSet(Byte.parseByte("00010000", 2), 4));
            assertTrue(ByteUtil.isBitSet(Byte.parseByte("00001000", 2), 3));
            assertTrue(ByteUtil.isBitSet(Byte.parseByte("00000100", 2), 2));
            assertTrue(ByteUtil.isBitSet(Byte.parseByte("00000010", 2), 1));
            assertTrue(ByteUtil.isBitSet(Byte.parseByte("00000001", 2), 0));
        }

        @Test
        void shouldReturnTrueWhenBitIsUnset() {
            assertFalse(ByteUtil.isBitSet(Byte.parseByte("01111111", 2), 7));
            assertFalse(ByteUtil.isBitSet(Byte.parseByte("00111111", 2), 6));
            assertFalse(ByteUtil.isBitSet(Byte.parseByte("01011111", 2), 5));
            assertFalse(ByteUtil.isBitSet(Byte.parseByte("01101111", 2), 4));
            assertFalse(ByteUtil.isBitSet(Byte.parseByte("01110111", 2), 3));
            assertFalse(ByteUtil.isBitSet(Byte.parseByte("01111011", 2), 2));
            assertFalse(ByteUtil.isBitSet(Byte.parseByte("01111101", 2), 1));
            assertFalse(ByteUtil.isBitSet(Byte.parseByte("01111110", 2), 0));
        }

    }

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