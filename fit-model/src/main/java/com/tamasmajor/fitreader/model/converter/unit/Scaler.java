package com.tamasmajor.fitreader.model.converter.unit;

public class Scaler {

    public static Integer multiplyBy2(Integer value) {
        return 2 * value;
    }

    public static Double divideBy10(Integer value) {
        return (double) value / 10.0;
    }

    public static Double divideBy100(Integer value) {
        return (double) value / 100.0;
    }

    public static Double divideBy1000(Integer value) {
        return (double) value / 1000.0;
    }

}
