package com.tamasmajor.fitreader.model.converter;

import lombok.val;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ByteValueConverter {

    // TODO: extend with additional types
    // TODO: BIG_ENDIAN support
    public static <T> T convert(byte[] value, Class<T> clazz) {
        if (clazz.equals(Integer.class)) {
            return clazz.cast(asInt(value));
        } else if (clazz.equals(Long.class)) {
            return clazz.cast(asLong(value));
        } else if (clazz.equals(String.class)) {
            return clazz.cast(new String(value));
        } else {
            throw new IllegalArgumentException("Unsupported class: " + clazz);
        }
    }

    private static int asInt(byte[] bytes) {
        return asInt(bytes, ByteOrder.LITTLE_ENDIAN);
    }

    private static int asInt(byte[] bytes, ByteOrder byteOrder) {
        if (bytes.length > Integer.BYTES) {
            throw new IllegalArgumentException("Length has to be less than or equal to " + Integer.BYTES +" bytes");
        }
        val num = new byte[Integer.BYTES];
        if (byteOrder == ByteOrder.LITTLE_ENDIAN) {
            System.arraycopy(bytes, 0, num, 0, bytes.length);
        } else {
            System.arraycopy(bytes, 0, num, Integer.BYTES - bytes.length, bytes.length);
        }
        return ByteBuffer.wrap(num).order(byteOrder).getInt();
    }

    private static long asLong(byte[] bytes) {
        return asLong(bytes, ByteOrder.LITTLE_ENDIAN);
    }

    private static long asLong(byte[] bytes, ByteOrder byteOrder) {
        if (bytes.length > Long.BYTES) {
            throw new IllegalArgumentException("Length has to be less than or equal to " + Long.BYTES + "bytes");
        }
        val num = new byte[Long.BYTES];
        if (byteOrder == ByteOrder.LITTLE_ENDIAN) {
            System.arraycopy(bytes, 0, num, 0, bytes.length);
        } else {
            System.arraycopy(bytes, 0, num, Long.BYTES - bytes.length, bytes.length);
        }
        return ByteBuffer.wrap(num).order(byteOrder).getLong();
    }

}
