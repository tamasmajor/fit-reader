package com.tamasmajor.fitreader.fit.parsers.data;

import lombok.val;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DataHolderTest {

    @Test
    void shouldReturnCurrentPosition() {
        val dataHolder = new DataHolder(new byte[] { 0, 1, 2 });
        dataHolder.movePositionBy(1);
        assertEquals(1, dataHolder.getCurrentPosition());
    }

    @Test
    void shouldReturnByteAtCurrentPosition() {
        val dataHolder = new DataHolder(new byte[] { 0, 1, 2, 3, 4, 5 });
        assertEquals(0, dataHolder.getCurrentByte());
        dataHolder.movePositionBy(2);
        assertEquals(2, dataHolder.getCurrentByte());
    }

    @Nested
    class GetNextBytesTests {

        @Test
        void shouldReturnAllBytes() {
            val dataHolder = new DataHolder(new byte[] { 0, 1, 2, 3 });
            assertArrayEquals(new byte[] { 0, 1, 2, 3 }, dataHolder.getNextBytes(4));
        }

        @Test
        void shouldReturnBytesStartingFromCurrentPosition() {
            val dataHolder = new DataHolder(new byte[] { 0, 1, 2, 3 });
            dataHolder.movePositionBy(1);
            assertArrayEquals(new byte[] { 1, 2 }, dataHolder.getNextBytes(2));
        }

        @Test
        void shouldThrowExceptionIfThereIsNotEnoughDataLeftToReturn() {
            val dataHolder = new DataHolder(new byte[] { 0, 1, 2, 3 });
            dataHolder.movePositionBy(1);
            val ex = assertThrows(IllegalArgumentException.class, () -> dataHolder.getNextBytes(4));
            assertEquals("Not enough data remaining", ex.getMessage());
        }

    }

    @Nested
    class MovePositionByTests {

        @Test
        void shouldMovePositionForward() {
            val dataHolder = new DataHolder(new byte[] { 0, 1, 2, 3 });
            dataHolder.movePositionBy(2);
            assertEquals(2, dataHolder.getCurrentByte());
        }

        @Test
        void shouldMovePositionBackwards() {
            val dataHolder = new DataHolder(new byte[] { 0, 1, 2, 3 });
            dataHolder.movePositionBy(2);
            assertEquals(2, dataHolder.getCurrentByte());
            dataHolder.movePositionBy(-1);
            assertEquals(1, dataHolder.getCurrentByte());
        }

        @Test
        void shouldThrowExceptionIfAfterMovePositionWouldBeBelowZero() {
            val dataHolder = new DataHolder(new byte[] { 0, 1, 2, 3 });
            val ex = assertThrows(IllegalArgumentException.class, () -> dataHolder.movePositionBy(-1));
            assertEquals("Illegal position: -1", ex.getMessage());
        }

        @Test
        void shouldThrowExceptionIfAfterMovePositionWouldBeOutsideOfDataLength() {
            val dataHolder = new DataHolder(new byte[] { 0, 1, 2 });
            val ex = assertThrows(IllegalArgumentException.class, () -> dataHolder.movePositionBy(4));
            assertEquals("Illegal position: 4", ex.getMessage());
        }

    }

}