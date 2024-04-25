package com.tamasmajor.fitreader.util;

import lombok.val;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ByteUtil {

    public static boolean isBitSet(byte b, int position) {
        return ((b >> position) & 1) == 1;
    }

    public static int asInt(byte[] bytes, ByteOrder byteOrder) {
        if (bytes.length > 4) {
            throw new IllegalArgumentException("Length has to be less than or equal to 4 bytes");
        }
        val num = new byte[4];
        if (byteOrder == ByteOrder.LITTLE_ENDIAN) {
            System.arraycopy(bytes, 0, num, 0, bytes.length);
        } else {
            System.arraycopy(bytes, 0, num, 4 - bytes.length, bytes.length);
        }
        return ByteBuffer.wrap(num).order(byteOrder).getInt();
    }

}
