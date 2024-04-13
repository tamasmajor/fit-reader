package com.tamasmajor.fitreader.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ByteUtil {

    public static boolean isBitSet(byte b, int position) {
        return ((b >> position) & 1) == 1;
    }

    public static int asInt(byte[] bytes, ByteOrder byteOrder) {
        if (bytes.length != 4) {
            throw new IllegalArgumentException("Length has to be 4 bytes");
        }
        return ByteBuffer.wrap(bytes).order(byteOrder).getInt();
    }

}
