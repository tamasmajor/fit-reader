package testcases.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Converter {

    public static <T> T convert(byte[] value, Class<T> clazz) {
        if (clazz.equals(Integer.class)) {
            return clazz.cast(ByteBuffer.wrap(value).order(ByteOrder.LITTLE_ENDIAN).getInt());
        } else if (clazz.equals(String.class)) {
            return clazz.cast(new String(value));
        } else {
            throw new IllegalArgumentException("Unsupported class: " + clazz);
        }
    }

}